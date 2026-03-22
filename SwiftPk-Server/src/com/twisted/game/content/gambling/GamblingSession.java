package com.twisted.game.content.gambling;

import com.twisted.GameServer;
import com.twisted.game.GameConstants;
import com.twisted.game.content.gambling.impl.Flower;
import com.twisted.game.content.gambling.impl.FlowerPoker;
import com.twisted.game.world.InterfaceConstants;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.PlayerStatus;
import com.twisted.game.world.entity.mob.player.commands.impl.owner.ToggleGambleCommand;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.ItemContainer;
import com.twisted.game.world.items.container.inventory.Inventory;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.util.Color;
import com.twisted.util.SecondsTimer;
import com.twisted.util.Utils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.twisted.game.world.InterfaceConstants.REMOVE_OFFERED_GAMBLE_ITEMS;

public class GamblingSession {

    public static boolean ENABLED = true;
    public static boolean DISABLE_FLOWER_POKER = false;

    private static final Logger logger = LogManager.getLogger(GamblingSession.class);
    private static final Logger gambleLogs = LogManager.getLogger("GambleLogs");
    private static final Level GAMBLE;

    static {
        GAMBLE = Level.getLevel("GAMBLE");
    }

    private static final int INTERFACE_ID = 16200;
    public static final int MY_ITEMS_OFFERED = 16230;
    public static final int OPPONENT_ITEMS_OFFERED = 16233;
    private static final int FLOWER_POKER_CONFIG_ID = 330;
    public static final int FLOWER_POKER_ID = 1;

    private final SecondsTimer request_delay = new SecondsTimer();
    private final SecondsTimer button_delay = new SecondsTimer();
    private final SecondsTimer item_delay = new SecondsTimer();

    public SecondsTimer getButtonDelay() {
        return button_delay;
    }

    private final Player player;

    /**
     * The gambling stage
     */
    public GambleState gambleState = GambleState.NONE;

    /**
     * No game type selected
     */
    private GameType type = GameType.NONE;

    /**
     * What game is playing
     */
    public Gamble game;

    /**
     * The player we requested to gamble
     */
    private Player opponent;

    /**
     * The player has confirmed the gamble
     */
    private boolean confirmed;

    /**
     * Checks what the player selected
     */
    public int selection;

    /**
     * The flowers
     */
    public ArrayList<Flower> flowers = new ArrayList<>();

    /**
     * The game flowers
     */
    public ArrayList<GameObject> gameFlowers = new ArrayList<>();

    private final ItemContainer container;

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void setGambleState(GambleState gambleState) {
        this.gambleState = gambleState;
    }

    public GambleState getGambleState() {
        return gambleState;
    }

    public GamblingSession(Player player) {
        this.player = player;
        //The container which will hold all our offered items.
        this.container = new ItemContainer(28, ItemContainer.StackPolicy.STANDARD) {
            public void onRefresh() {
                player.getInterfaceManager().openInventory(INTERFACE_ID, InterfaceConstants.REMOVE_INVENTORY_ITEM - 1);
                player.getPacketSender().sendItemOnInterface(InterfaceConstants.REMOVE_INVENTORY_ITEM, player.inventory().toArray());
                player.getPacketSender().sendItemOnInterface(MY_ITEMS_OFFERED, player.getGamblingSession().getContainer().toArray());
                player.getPacketSender().sendItemOnInterface(OPPONENT_ITEMS_OFFERED, opponent.getGamblingSession().getContainer().toArray());
                opponent.getPacketSender().sendItemOnInterface(MY_ITEMS_OFFERED, opponent.getGamblingSession().getContainer().toArray());
                opponent.getPacketSender().sendItemOnInterface(OPPONENT_ITEMS_OFFERED, player.getGamblingSession().getContainer().toArray());
                player.getPacketSender().sendString(16229, "You (" + Utils.formatRunescapeStyle(player.getGamblingSession().getContainer().containerValue()) + " BM)");
                opponent.getPacketSender().sendString(16232, opponent.getGamblingSession().opponent.getUsername() + " (" + Utils.formatRunescapeStyle(player.getGamblingSession().getContainer().containerValue()) + " BM))");
            }
        };
    }

    private void resetConfigs() {
        player.getPacketSender().sendConfig(FLOWER_POKER_CONFIG_ID, 0);
    }

    /**
     * Resets the gamble
     */
    public void resetAttributes() {
        // Remove any lock states
        opponent.unlock();
        player.unlock();

        //Reset gamble attributes
        setOpponent(null);
        setGambleState(GambleState.NONE);
        confirmed = false;
        game = null;
        type = null;
        selection = -1;

        //Reset player status if it's gambling.
        if (player.getStatus() == PlayerStatus.GAMBLING) {
            player.setStatus(PlayerStatus.NONE);
        }

        //Reset container..
        container.clear(true);

        //Clear items on interface
        player.getPacketSender().clearItemOnInterface(MY_ITEMS_OFFERED).clearItemOnInterface(OPPONENT_ITEMS_OFFERED);

        //Clear configs
        resetConfigs();

        //Reset any active flowers
        removeFlowers();
    }

    /**
     * Removes the flowers of a gambling session
     */
    public void removeFlowers() {
        for (GameObject gameFlower : player.getGamblingSession().gameFlowers) {
            GameObject remove = new GameObject(gameFlower.getId(), gameFlower.tile().copy());
            ObjectManager.removeObj(remove);

            Player otherPlayer = player.getGamblingSession().opponent;
            if (otherPlayer != null)
                ObjectManager.removeObj(remove);
        }
    }

    /**
     * Aborts a gamble, giving the other player a message that the gamble has cancelled by this user.
     */
    public void abortGambling() {
        if (gambleState != GambleState.NONE) {
            //Cache the current interact
            final Player opponent = this.opponent;

            //Return all items...
            for (Item t : container.toNonNullArray()) {
                player.inventory().addAll(t);
            }

            //Refresh inventory
            player.inventory().refresh();

            //Reset all attributes...
            resetAttributes();

            //Send decline message
            player.message("Gamble declined.");
            player.getInterfaceManager().close();

            //Reset/close gamble for other player as well (the cached interact)
            if (opponent != null) {
                if (opponent.getStatus() == PlayerStatus.GAMBLING) {
                    if (opponent.getGamblingSession().opponent() != null && opponent.getGamblingSession().opponent() == player) {
                        opponent.getGamblingSession().abortGambling();
                    }
                }
            }
        }
    }

    /**
     * Handles any button interactions for the interfaces concerning this gambling session.
     */
    public boolean handleButton(int buttonId) {
        if (player.getGamblingSession().state() == GambleState.NONE) {
            return false;
        }

        switch (buttonId) {
            case 16205 -> {
                if(DISABLE_FLOWER_POKER) {
                    player.message("Flower poker is disabled at this time.");
                    return true;
                }
                handleModeSelection(GameType.FLOWER_POKER);
                return true;
            }
            case 16212 -> accept();
            case 16202, 16215 -> {
                abortGambling();
                return true;
            }
        }
        return false;
    }

    private Gamble getGame(Player otherPlayer, Player player, GameType type) {
        if (type == GameType.FLOWER_POKER) {
            return new FlowerPoker(player, otherPlayer);
        }
        return null;
    }

    /**
     * Changes the game mode of the gamble session
     */
    public void handleModeSelection(GameType type) {
        if (opponent == null)
            return;

        GameType lastGame = this.type;
        player.getGamblingSession().confirmed = false;
        player.getGamblingSession().type = type;
        opponent.getGamblingSession().confirmed = false;
        opponent.getGamblingSession().type = type;

        //Update strings on interface
        List<Player.TextData> dataList = new ArrayList<>();
        dataList.add(new Player.TextData(type.description, 16222));
        dataList.add(new Player.TextData(type.formalName, 16218));

        player.getPacketSender().sendMultipleStrings(dataList);
        opponent.getPacketSender().sendMultipleStrings(dataList);

        if (lastGame != null) {
            opponent.getPacketSender().sendConfig(lastGame.configId, 0);
            player.getPacketSender().sendConfig(lastGame.configId, 0);
        }

        opponent.getPacketSender().sendConfig(type.configId, 1);
        player.getPacketSender().sendConfig(type.configId, 1);
    }

    public void onLogout() {
        if (gambleState != GambleState.IN_PROGRESS) {
            abortGambling();
        }
    }

    /**
     * Sends the other player a gamble request
     */
    public void requestGamble(Player requestee, boolean acceptRequest) {
        if (!GameServer.properties().enableGambling) {
            player.message("Gambling is currently disabled.");
            return;
        }
        if (ToggleGambleCommand.gambleDisabled) {//updateox
            player.message("@red@Gambling is currently disabled.");
            return;
        }

        boolean isGambler2 = requestee.getAttribOr(AttributeKey.GAMBLER, false);

        boolean isGambler = player.getAttribOr(AttributeKey.GAMBLER, false);
        if (!isGambler) {//ok you already have an scroll in game i've coded it to display gamble if player
            //dont claim it and it will let him know also will let another player know too that other player
            //not gambler
            player.message("You need to be a gambler, become one by using a gambler scroll.");//this is not a rank
            //player who dont have use this scroll will not be able to gamble anymore just like that // yeah true
            return;
        }//ok you can update now where do you will add scroll too// i want to add it to donator store for $30 and in donator store in game for 3k donator tickets
        if (!isGambler2) {
            player.message("Other player needs to be a gambler, to gamble. Become one by using a gambler scroll.");
            return;
        }

        if (!ENABLED) {
            player.message("Gambling is currently disabled.");
            return;
        }

        if (requestee.ironMode() != IronMode.NONE) {
            player.message("That player is an ironman and can not gamble.");
            return;
        }

        if (requestee.mode().isDarklord()) {
            player.message("That player is an darklord and can not gamble.");
            return;
        }

        if (player.getUsername().equalsIgnoreCase("Box test")) {
            player.message("This account can't gamble other players.");
            return;
        }

        if (gambleState == GambleState.NONE || gambleState == GambleState.REQUESTED_GAMBLE) {
            //Make sure to not allow flooding!
            if (request_delay.active()) {
                int seconds = request_delay.secondsRemaining();
                player.message("You must wait another " + (seconds == 1 ? "second" : "" + seconds + " seconds") + " before sending more gamble challenges.");
                return;
            }

            if (!requestee.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
                player.message("That player is currently busy.");
                return;
            }

            //The other players' current gamble state.
            final GambleState requestee_gamble_state = requestee.getGamblingSession().state();

            //Should we open the gamble or simply send a request?
            boolean initiateGamble = false;

            //Update this instance...
            this.setOpponent(requestee);
            this.setGambleState(GambleState.REQUESTED_GAMBLE);

            //Check if target requested a gamble with us...
            if (requestee_gamble_state == GambleState.REQUESTED_GAMBLE) {
                if (requestee.getGamblingSession().opponent() != null &&
                    requestee.getGamblingSession().opponent() == player) {
                    initiateGamble = true;
                }
            }

            //Initiate gamble for both players with eachother?
            if (initiateGamble) {
                requestee.getGamblingSession().initiateGamble();
                player.getGamblingSession().initiateGamble();
                // how do you set the host the host is always "player"
            } else {
                player.message("Sending gamble offer....");
                requestee.message(Utils.capitalizeJustFirst(player.getUsername()) + ":gamblereq:");
            }

            //Set the request delay to 2 seconds at least.
            request_delay.start(2);
        } else {
            player.message("You cannot request a gamble right now.");
        }
    }

    public void initiateGamble() {
        //Safety!
        if (player.locked() || player.hp() < 1 || opponent.locked() || opponent.hp() < 1 || player.dead()
            || opponent.dead() || player == opponent || player.getIndex() == opponent.getIndex()
            || player.getGamblingSession().matchActive() || opponent.getGamblingSession().matchActive()) {
            player.message("You can't start a gamble right now.");
            return;
        }

        player.healPlayer();
        opponent.healPlayer();

        //Set our gamble state
        setGambleState(GambleState.PLACING_BET);
        //Set our player status
        player.setStatus(PlayerStatus.GAMBLING);
        //Refresh container
        container.onRefresh();
        //Update strings on interface
        List<Player.TextData> dataList = new ArrayList<>();
        dataList.add(new Player.TextData("Select game mode...", 16218));
        dataList.add(new Player.TextData("Select game mode...", 16222));
        dataList.add(new Player.TextData("", 16223));
        dataList.add(new Player.TextData("Gambling with " + player.getGamblingSession().opponent.getUsername(), 16203));
        dataList.add(new Player.TextData("You (0 BM)", 16229));
        dataList.add(new Player.TextData(player.getGamblingSession().opponent.getUsername() + " (0 BM))", 16232));
        player.getPacketSender().sendMultipleStrings(dataList);
        //Update configs
        resetConfigs();
    }

    /**
     * Validates a player. Basically checks that all specified params add up.
     *
     * @param player       The player
     * @param interact     The opponent
     * @param gambleStates Our current gambling status
     * @return true if we can validate false otherwise
     */
    private static boolean validate(Player player, Player interact, GambleState... gambleStates) {
        //Verify player...
        if (player == null || interact == null) {
            return false;
        }

        //Make sure we have proper status
        if (player.getStatus() != null) {
            if (player.getStatus() != PlayerStatus.GAMBLING) {
                return false;
            }

            //Make sure we're interacting with eachother
            if (interact.getStatus() != PlayerStatus.GAMBLING) {
                return false;
            }
        }

        if (player.getGamblingSession().opponent() == null
            || player.getGamblingSession().opponent() != interact) {
            return false;
        }
        if (interact.getGamblingSession().opponent() == null
            || interact.getGamblingSession().opponent() != player) {
            return false;
        }

        //Make sure we have proper gamble state.
        boolean found = false;
        for (GambleState gambleState : gambleStates) {
            if (player.getGamblingSession().getGambleState() == gambleState) {
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }

        //Do the same for our interact
        found = false;
        for (GambleState gambleState : gambleStates) {
            if (interact.getGamblingSession().getGambleState() == gambleState) {
                found = true;
                break;
            }
        }
        return found;
    }

    //Deposit or withdraw an item....
    public void handleItem(int id, int amount, int slot, ItemContainer from, ItemContainer to) {
        if (player.getInterfaceManager().isInterfaceOpen(INTERFACE_ID)) {
            //System.out.println("test 1 Item " + id + " amount " + amount);
            //Validate this gamble action..
            if (!validate(player, opponent, GambleState.PLACING_BET)) {
                return;
            }
            Item gambleItem = new Item(id, amount);
            if (!gambleItem.rawtradable()) {
                player.message("You cannot gamble that item.");
                return;
            }

            if (Arrays.stream(GameConstants.DONATOR_ITEMS).anyMatch(donator_item -> donator_item == gambleItem.getId())) {
                player.message("You cannot gamble that item.");
                return;
            }

            for (Item bankItem : GameConstants.BANK_ITEMS) {
                if (bankItem.unnote().getId() == gambleItem.getId()) {
                    player.message("You can't gamble spawnable items.");
                    return;
                }
            }

            if (gambleItem.unnote().definition(World.getWorld()).pvpAllowed) {
                player.message("You can't gamble spawnable items.");
                return;
            }

            if (gambleItem.getValue() <= 0) {
                player.message("You can't gamble spawnable items.");
                return;
            }

            //Set the item delay to 5 seconds at least.
            item_delay.start(5);
            int interfaceId = REMOVE_OFFERED_GAMBLE_ITEMS;
            if (interfaceId == REMOVE_OFFERED_GAMBLE_ITEMS && player.getGamblingSession() != null) {
                opponent.getGamblingSession().confirmed = false; //update4.22

                player.getPacketSender().sendString(16223, "<col=ca0d0d>GAMBLE MODIFIED!");
                opponent.getPacketSender().sendString(16223, "<col=ca0d0d>GAMBLE MODIFIED!");

            }
            // player.getPacketSender().sendString(16223, "<col=ca0d0d>GAMBLE MODIFIED!");
            //  opponent.getPacketSender().sendString(16223, "<col=ca0d0d>GAMBLE MODIFIED!");

            //Handle the item switch..
            if (gambleState == GambleState.PLACING_BET && opponent.getGamblingSession().getGambleState() == GambleState.PLACING_BET) {

                //Check if the item is in the right place
                if (from.getItems()[slot] != null && from.getItems()[slot].getId() == id) {
                    if (from instanceof Inventory) {
                        if (!gambleItem.stackable()) {
                            if (amount > container.getFreeSlots()) {
                                //System.out.println("test 3 Item " + id + " amount " + amount);
                                amount = container.getFreeSlots();
                            }
                        }
                    }

                    if (amount <= 0) {
                        return;
                    }

                    if (amount > from.count(id)) {
                        amount = from.count(id);
                    }

                    final Item item = new Item(id, amount);

                    //Do the switch!
                    if (item.getAmount() == 1) {
                        from.remove(item, slot, true, false);
                    } else {
                        from.remove(item, true);
                    }
                    to.add(item, true);
                    container.onRefresh();
                }
            } else {
                player.getInterfaceManager().close();
            }
        }
        //System.out.println("test 4 Item " + id + " amount " + amount);
    }

    /**
     * Accept the gamble session
     */
    public void accept() {
        Player otherPlayer = player.getGamblingSession().opponent;

        if (otherPlayer == null)
            return;

        // Security
        if(gambleState != null && !player.getInterfaceManager().isInterfaceOpen(INTERFACE_ID) && !otherPlayer.getInterfaceManager().isInterfaceOpen(INTERFACE_ID) && gambleState == GambleState.PLACING_BET) {
            return;
        }

        //Validate this gamble action..
        if (!validate(player, otherPlayer, GambleState.PLACING_BET)) {
            return;
        }

        //Check button delay...
        if (button_delay.active()) {
            return;
        }

        if (player.getGamblingSession().type == null || player.getGamblingSession().type == GameType.NONE) {
            player.message("You need to select a game first.");
            return;
        }

        if (player.getGamblingSession().type != otherPlayer.getGamblingSession().type)
            return;

        if (player.getGamblingSession().confirmed)
            return;

        if (item_delay.active()) {
            int seconds = item_delay.secondsRemaining();
            player.message("You must wait another " + (seconds == 1 ? "second" : "" + seconds + " seconds") + " before being able to accept the gamble.");
            return;
        }

        player.getGamblingSession().confirmed = true;

        player.getPacketSender().sendString(16223, "Waiting for other player<br>to accept.");
        otherPlayer.getPacketSender().sendString(16223, Utils.capitalizeJustFirst(player.getUsername()) + " has accepted.");

        if (otherPlayer.getGamblingSession().confirmed) {
            Gamble game = getGame(otherPlayer, player, otherPlayer.getGamblingSession().type);
            if (game instanceof FlowerPoker poker) {
                System.out.println("true");
                if (poker.occupiedSpace.size() >= 6) {
                    poker.host.message(Color.RED.wrap("All flower poker sections are currently in use."));
                    poker.opponent.message(Color.RED.wrap("All flower poker sections are currently in use."));
                    return;
                }
            }

            player.getGamblingSession().game = game;
            otherPlayer.getGamblingSession().game = game;
            player.getGamblingSession().gambleState = GambleState.REQUESTED_GAMBLE;
            player.getGamblingSession().opponent = otherPlayer;
            start();
        }
        button_delay.start(1);
    }

    /**
     * Starts the gamble
     */
    private void start() {
        Player otherPlayer = player.getGamblingSession().opponent;
        if (otherPlayer == null)
            return;

        if (otherPlayer.getGamblingSession().confirmed) {

            if (player.getGamblingSession().game == null)
                return;

            if (player.getGamblingSession().game.host == null)
                return;

            if (player.getGamblingSession().game.host.getGamblingSession().game == null)
                return;

            player.getInterfaceManager().close();
            otherPlayer.getInterfaceManager().close();

            player.getGamblingSession().gambleState = GambleState.IN_PROGRESS;
            otherPlayer.getGamblingSession().gambleState = GambleState.IN_PROGRESS;

            player.getGamblingSession().game.gameId = type.ordinal();
            otherPlayer.getGamblingSession().game.gameId = type.ordinal();

            player.getGamblingSession().game.gamble();
        }
    }

    public void end(int gameId) {

        if (player.getGamblingSession().game == null)
            return;

        if (player.getGamblingSession().game.host == null)
            return;

        if (player.getGamblingSession().game.host.getGamblingSession().game == null)
            return;

        if (player.getGamblingSession().game.host == player) {
            System.out.println("you are the host so opponent wins");
            finish(gameId, game.host, game.opponent, 0, 1);
        } else if (player.getGamblingSession().game.opponent == player) {
            System.out.println("you are the opponent so host wins");
            finish(gameId, game.host, game.opponent, 1, 0);
        }
    }

    public void finish(int gameId, Player host, Player opponent, int hostScore, int opponentScore) {

        if (host.getGamblingSession().game == null)
            return;

        if (host.getGamblingSession().game.host == null)
            return;

        if (host.getGamblingSession().game.host.getGamblingSession().game == null)
            return;

        if (!World.getWorld().getPlayers().contains(opponent) && World.getWorld().getPlayers().contains(host)) {
            //System.out.println("1");
            give(gameId, host, opponent, false);
        } else if (World.getWorld().getPlayers().contains(opponent) && !World.getWorld().getPlayers().contains(host)) {
            //System.out.println("2");
            give(gameId, opponent, host, false);
        } else {
            //System.out.println("hostScore: " + hostScore);
            //System.out.println("opponentScore: " + opponentScore);
            boolean draw = hostScore == opponentScore;
            boolean hostWon = hostScore > opponentScore;
            if (draw) {
                //System.out.println("draw");
                give(gameId, host, opponent, true);
            } else {
                //System.out.println("someone won");
                give(gameId, hostWon ? host : opponent, hostWon ? opponent : host, false);
            }
        }

        host.getGamblingSession().resetAttributes();

        if (opponent != null)
            opponent.getGamblingSession().resetAttributes();
    }

    public void give(int gameId, Player winner, Player loser, boolean draw) {
        if (draw) {
            if (winner != null) {
                for (Item item : winner.getGamblingSession().getContainer().toNonNullArray()) {
                    winner.inventory().addOrBank(item);
                }
                winner.message("It's a draw!");
                winner.forceChat("It's a draw!");
            }

            if (loser != null) {
                for (Item item : loser.getGamblingSession().getContainer().toNonNullArray()) {
                    loser.inventory().addOrBank(item);
                }
                loser.message("It's a draw!");
                loser.forceChat("It's a draw!");
            }
        } else {
            if (winner != null) {
                for (Item item : winner.getGamblingSession().getContainer().toArray()) {
                    if (item == null)
                        continue;
                    //If inv full send to bank!
                    winner.inventory().addOrBank(item);
                }

                for (Item item : loser.getGamblingSession().getContainer().toArray()) {
                    if (item == null)
                        continue;
                    //If inv full send to bank!
                    winner.inventory().addOrBank(item);
                }
                try {
                    long plr_value = player.getGamblingSession().getContainer().containerValue();
                    long other_plr_value = opponent.getGamblingSession().getContainer().containerValue();
                    long difference;
                    difference = (plr_value > other_plr_value) ? plr_value - other_plr_value : other_plr_value - plr_value;
                    gambleLogs.log(GAMBLE, "Player " + player.getUsername() + " gamble value difference of " + Utils.insertCommasToNumber(String.valueOf(difference)) + " BM");
                    Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " gamble value difference of " + Utils.insertCommasToNumber(String.valueOf(difference)) + " BM", "gamble");
                    if (difference > 1_000_000) {
                        gambleLogs.warn("Player " + opponent.getUsername() + " won a gamble against Player " + player.getUsername() + " with a value difference of greater than 1,000,000 BM, this was possibly RWT.");
                        Utils.sendDiscordInfoLog(player,GameServer.properties().discordNotifyId + " Player " + opponent.getUsername() + " won a gamble against Player " + player.getUsername() + " with a value difference of greater than 1,000,000 BM, this was possibly RWT.", "gamble");
                    }
                } catch (Exception e) {
                    //The value shouldn't ever really be a string, but just in case, let's catch the exception.
                    logger.catching(e);
                    logger.error("Somehow there was an exception from logging gambling between player " + player.getUsername() + " and " + opponent.getUsername());
                }
                send(gameId, winner, loser);
            }
        }
    }

    private void send(int gameId, Player winner, Player loser) {
        winner.getInterfaceManager().closeDialogue();
        loser.getInterfaceManager().closeDialogue();
        if (gameId == -1) {
            winner.forceChat("I have won!");
        } else {
            winner.forceChat("I have won!");
            loser.forceChat("I have lost!");
        }
    }

    public boolean currentState(GambleState check) {
        return gambleState == check;
    }

    public boolean matchActive() {
        return gambleState == GambleState.IN_PROGRESS;
    }

    public Player opponent() {
        return opponent;
    }

    public GambleState state() {
        return gambleState;
    }

    public ItemContainer getContainer() {
        return container;
    }
}
