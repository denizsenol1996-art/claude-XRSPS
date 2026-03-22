package com.twisted.game.content.gambling.impl;

import com.twisted.game.content.gambling.Gamble;
import com.twisted.game.content.gambling.GambleState;
import com.twisted.game.content.gambling.GamblingSession;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FlowerPoker extends Gamble {

    private final List<Tile> spawnTiles = Arrays.asList(
        new Tile(3096, 3465, 0),
        new Tile(3091, 3465, 0),
        new Tile(3086, 3465, 0),
        new Tile(3096, 3473, 0),
        new Tile(3086, 3473, 0),
        new Tile(3091, 3473, 0));

    @Override
    public String toString() {
        return "Flower Poker";
    }

    public FlowerPoker(Player host, Player opponent) {
        super(host, opponent);
    }

    public final Set<Tile> occupiedSpace = new HashSet<>();
    Tile section = null;
    boolean isDraw = false;

    @Override
    public void gamble() {
        host.lock();
        opponent.lock();

        host.getGamblingSession().flowers.clear();
        opponent.getGamblingSession().flowers.clear();
        host.getGamblingSession().gameFlowers.clear();
        opponent.getGamblingSession().gameFlowers.clear();

        for (int index = 0; index < 5; index++) {
            Flower hostFlower = Flower.flower();
            Flower opponentFlower = Flower.flower();
            while (hostFlower == Flower.BLACK || hostFlower == Flower.WHITE) {
                hostFlower = Flower.flower();
            }
            while (opponentFlower == Flower.BLACK || opponentFlower == Flower.WHITE) {
                opponentFlower = Flower.flower();
            }
            host.getGamblingSession().flowers.add(hostFlower);
            opponent.getGamblingSession().flowers.add(opponentFlower);
        }

        Ranking hostResult = getRank(host);
        Ranking opponentResult = getRank(opponent);

        List<Tile> temp = new ArrayList<>();
        for (final Tile tile : spawnTiles) {
            if (occupiedSpace.contains(tile)) {
                continue;
            }
            temp.add(tile);
        }

        final Tile spawn;
        if (section == null) {
            spawn = Utils.randomElement(temp);
        } else {
            spawn = section;
        }

        int[] time = {0};
        Chain.noCtx().runFn(1, () -> {
            occupiedSpace.add(spawn);
            host.teleport(spawn);
            opponent.teleport(spawn.copy().add(-1, 0));
        }).then(1, () -> {
            walk(host);
            walk(opponent);
        }).then(2, () -> {
            plantFlower(host);
            plantFlower(opponent);
        });

        Chain.noCtxRepeat().repeatingTask(4, plant -> {
            if (host.getGamblingSession().state() != GambleState.IN_PROGRESS) {
                plant.stop();
                return;
            }

            switch (time[0]) {
                case 1, 2, 3, 4 -> {
                    plantFlower(host);
                    plantFlower(opponent);
                }
            }

            if (time[0] >= 5) {
                if (hostResult.equals(opponentResult)) {
                    this.section = spawn;
                    this.isDraw = true;
                    host.forceChat("It's a draw!");
                    opponent.forceChat("It's a draw!");
                    host.getGamblingSession().removeFlowers();
                    opponent.getGamblingSession().removeFlowers();
                    this.gamble();
                    plant.stop();
                    return;
                }
                this.section = null;
                this.isDraw = false;
                occupiedSpace.remove(spawn);
                host.getGamblingSession().finish(GamblingSession.FLOWER_POKER_ID, host, opponent, hostResult.ordinal(), opponentResult.ordinal());
                plant.stop();
                return;
            }

            time[0]++;
        });
    }

    private enum Ranking {
        /**
         * Nothing
         */
        BUST,
        /**
         * Got 1 pair of the same flower
         */
        ONE_PAIR,
        /**
         * Got 2 pairs of the same flower
         */
        TWO_PAIR,
        /**
         * Got 3 of the same flower
         */
        THREE_OAK,
        /**
         * Got 3 of the same flower plus 2 other of the same flower
         */
        FULL_HOUSE,
        /**
         * Got 4 of the same flower
         */
        FOUR_OAK,
        /**
         * Got 5 of the same flower
         */
        FIVE_OAK,
    }

    private static Ranking getRank(Player player) {
        ArrayList<Flower> flowers = new ArrayList<>(player.getGamblingSession().flowers);
        Collections.sort(flowers);
        Map<Integer, Integer> pairs = getPairs(flowers);
        for (int i = 0; i < pairs.size(); i++) {
            if (pairs.get(i) == null) {
                continue;
            }
            if (pairs.get(i) == 5) {
                return Ranking.FIVE_OAK;
            }
        }
        if (pairs.size() == 2) {
            if ((pairs.get(0) == 3 && pairs.get(1) == 2)
                || (pairs.get(1) == 3 && pairs.get(0) == 2)) {
                return Ranking.FULL_HOUSE;
            }
        }
        int totalPairs = 0;
        for (int i = 0; i < pairs.size(); i++) {
            if (pairs.get(i) == null) {
                continue;
            }
            if (pairs.get(i) == 4) {
                return Ranking.FOUR_OAK;
            }
            if (pairs.get(i) == 3) {
                return Ranking.THREE_OAK;
            }
            if (pairs.get(i) == 2) {
                totalPairs++;
            }
        }
        if (totalPairs == 2) {
            return Ranking.TWO_PAIR;
        }
        if (totalPairs == 1) {
            return Ranking.ONE_PAIR;
        }
        return Ranking.BUST;
    }

    private static Map<Integer, Integer> getPairs(ArrayList<Flower> list) {
        Map<Integer, Integer> finalPairs = new HashMap<>();
        int[] pairs = new int[14];
        for (Flower flower : list) {
            pairs[flower.ordinal()]++;
        }
        int slot = 0;
        for (int pair : pairs) {
            if (pair >= 2) {
                finalPairs.put(slot, pair);
                slot++;
            }
        }
        return finalPairs;
    }

    private void walk(Player player) {
        Tile targTile = player.tile().transform(0, +1, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }

    public void plantFlower(final Player player) {
        if (player.getGamblingSession().state() != GambleState.IN_PROGRESS) {
            return;
        }
        if (player.getGamblingSession().gameFlowers.size() >= 5) {
            return;
        }
        Flower flower = player.getGamblingSession().flowers.get(player.getGamblingSession().gameFlowers.size());
        if (flower == null) {
            return;
        }
        if (flower.name().equalsIgnoreCase("WHITE") || flower.name().equalsIgnoreCase("BLACK")) {
            host.getGamblingSession().finish(GamblingSession.FLOWER_POKER_ID, player, opponent, 0, 0);
            return;
        }
        Chain.noCtx().runFn(1, () -> {
            player.animate(827);
            GameObject gameFlower = spawnFlower(player, flower);
            player.faceObj(gameFlower);
            walk(player);
        });
    }

    private static @NotNull GameObject spawnFlower(Player player, Flower flower) {
        GameObject gameFlower = new GameObject(flower.getId(), player.tile().copy());
        ObjectManager.addObj(gameFlower);
        player.getGamblingSession().gameFlowers.add(gameFlower);
        return gameFlower;
    }

    private Player getOtherPlayer(Player player) {
        /**
         * No game
         */
        if (player.getGamblingSession().game == null) {
            return null;
        }
        return player.getGamblingSession().game.host == player ? player.getGamblingSession().game.opponent : player.getGamblingSession().game.host;
    }

}
