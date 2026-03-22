//package com.twisted; // dont forget to change packaging ^-^
//
//import com.twisted.game.world.entity.mob.player.Player;
//import com.twisted.game.world.entity.mob.player.skills.Skills;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//
//
//public class Highscores implements Runnable {
//
//
//
//    public static final String HOST = "72.167.57.128"; // website ip address
//    public static final String USER = "hasnew";
//    public static final String PASS = "R_n,+n[e,x6B";
//    public static final String DATABASE = "yastahs";
//    public static final String TABLE = "hs_users";
//
//    private Player player;
//    private Connection conn;
//    private Statement stmt;
//
//    public Highscores(Player player, boolean isDeath) {
//        this.player = player;
//        this.isDeath = isDeath;
//    }
//
//    public boolean connect(String host, String database, String user, String pass) {
//        try {
//            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public void run() {
//        try {
//            /*if (!connect(HOST, DATABASE, USER, PASS)) {
//                return;
//            }*/
//            if (player.getSkills().getTotalLevel() <= 36) {
//                return;
//            }
//
//            String name = player.getUsername();
//
//            PreparedStatement stmt1 = prepare("DELETE FROM "+TABLE+" WHERE username=?");
//            stmt1.setString(1, player.getUsername());
//            stmt1.execute();
//
//            PreparedStatement stmt2 = prepare(generateQuery());
//            stmt2.setString(1, player.getUsername());
//            if (player.isDonator() && !player.isModerator() && !player.isSupporter()) {
//                stmt2.setInt(2, 4);
//            } else if (player.isSupporter()) {
//                stmt2.setInt(2, 3);
//            } else if (player.isOwner) {
//                stmt2.setInt(2, 2);
//            } else if (player.isModerator()) {
//                stmt2.setInt(2, 1);
//            } else if (player.getRights() == 0) {
//                stmt2.setInt(2, 0);
//            } else if (player.isIronmanMode() && isDeath) {
//                stmt2.setInt(2, 5);
//            }
//
//            stmt2.setInt(3, player.getGameMode()); // game mode number
//            stmt2.setInt(4, player.skills().totalLevel()); // total level
//
//            stmt2.setLong(5, getTotalXp(player));
//
//            for (int i = 0; i < 25; i++)
//                stmt2.setInt(6 + i, (int)player.skills().getXp()[i]);
//            stmt2.execute();
//
//            destroy();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public PreparedStatement prepare(String query) throws SQLException {
//        return conn.prepareStatement(query);
//    }
//
//    public void destroy() {
//        try {
//            conn.close();
//            conn = null;
//            if (stmt != null) {
//                stmt.close();
//                stmt = null;
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static long getTotalXp(Player player) {
//        long totalxp = 0;
//        for (double xp : player.skills().xp()) {
//            totalxp += xp;
//        }
//        return  totalxp;
//    }
//
//    public static String generateQuery() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("INSERT INTO "+TABLE+" (");
//        sb.append("username, ");
//        sb.append("rights, ");
//        sb.append("mode, ");
//        sb.append("total_level, ");
//        sb.append("overall_xp, ");
//        sb.append("attack_xp, ");
//        sb.append("defence_xp, ");
//        sb.append("strength_xp, ");
//        sb.append("constitution_xp, ");
//        sb.append("ranged_xp, ");
//        sb.append("prayer_xp, ");
//        sb.append("magic_xp, ");
//        sb.append("cooking_xp, ");
//        sb.append("woodcutting_xp, ");
//        sb.append("fletching_xp, ");
//        sb.append("fishing_xp, ");
//        sb.append("firemaking_xp, ");
//        sb.append("crafting_xp, ");
//        sb.append("smithing_xp, ");
//        sb.append("mining_xp, ");
//        sb.append("herblore_xp, ");
//        sb.append("agility_xp, ");
//        sb.append("thieving_xp, ");
//        sb.append("slayer_xp, ");
//        sb.append("farming_xp, ");
//        sb.append("runecrafting_xp, ");
//        sb.append("hunter_xp, ");
//        sb.append("construction_xp, ");
//        sb.append("summoning_xp, ");
//        sb.append("dungeoneering_xp) ");
//        sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//        return sb.toString();
//    }
//
//}
