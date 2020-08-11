package xps.Database;

import xps.Main;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLSetterGetter {

    public static boolean playerExists(String uuid) {
        try {
            ResultSet rs = Main.mysql.query("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
            ResultSet week_rs = Main.mysql.query("SELECT * FROM WEEK_STATS WHERE UUID= '" + uuid + "'");
            ResultSet rank_rs = Main.mysql.query("SELECT * FROM RANK WHERE UUID= '" + uuid + "'");
            return (rs.next() && rs.getString("UUID") != null || week_rs.next() && week_rs.getString("UUID") != null || rank_rs.next() && rank_rs.getString("UUID") != null);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void createPlayer(String uuid) {
        if (!playerExists(uuid)) {
            Main.mysql.update("INSERT INTO STATS(UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, MONUMENTS, NAME) VALUES ('" + uuid + "', '0', '0', '0', '0', '0', '0', 'Null');");
            Main.mysql.update("INSERT INTO WEEK_STATS(UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, MONUMENTS, NAME, DATE) VALUES ('" + uuid + "', '0', '0', '0', '0', '0', '0', 'Null', 'Null');");
            Main.mysql.update("INSERT INTO RANK(UUID, NAME, POINTS, RANK) VALUES ('" + uuid + "', 'Null', '0', 'Null');");
        }
    }

    public static String getName(String uuid) {
        String i = "";
        if (playerExists(uuid)) {
            try {
                ResultSet rs = Main.mysql.query("SELECT * FROM WEEK_STATS WHERE UUID= '" + uuid + "'");
                if (rs.next())
                    rs.getString("NAME");
                i = rs.getString("NAME");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(uuid);
            getName(uuid);
        }
        return i;
    }

    public static void setName(String uuid, String name) {
        if (playerExists(uuid)) {
            Main.mysql.update("UPDATE WEEK_STATS SET NAME= '" + name + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setName(uuid, name);
        }
    }

    public static Integer getMonuments(String uuid) {
        int i = 0;
        if(playerExists(uuid)) {
            try {
                ResultSet rs = Main.mysql.query("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
                if(rs.next())
                    rs.getInt("MONUMENTS");
                i = rs.getInt("MONUMENTS");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(uuid);
            getMonuments(uuid);
        }
        return i;
    }

    public static Integer getFlags(String uuid) {
        int i = 0;
        if(playerExists(uuid)) {
            try {
                ResultSet rs = Main.mysql.query("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
                if(rs.next())
                    rs.getInt("FLAGS");
                i = rs.getInt("FLAGS");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(uuid);
            getFlags(uuid);
        }
        return i;
    }

    public static Integer getCores(String uuid) {
        int i = 0;
        if (playerExists(uuid)) {
            try {
                ResultSet rs = Main.mysql.query("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
                if (rs.next())
                    rs.getInt("CORES");
                i = rs.getInt("CORES");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(uuid);
            getCores(uuid);
        }
        return i;
    }

    public static Integer getWools(String uuid) {
        int i = 0;
        if(playerExists(uuid)) {
            try {
                ResultSet rs = Main.mysql.query("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
                if(rs.next())
                    rs.getInt("WOOLS");
                i = rs.getInt("WOOLS");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(uuid);
            getWools(uuid);
        }
        return i;
    }

    public static Integer getKills(String uuid) {
        int i = 0;
        if(playerExists(uuid)) {
            try {
                ResultSet rs = Main.mysql.query("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
                if(rs.next())
                    rs.getInt("KILLS");
                i = rs.getInt("KILLS");
                } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(uuid);
            getKills(uuid);
        }
        return i;
    }

    public static Integer getDeaths(String uuid) {
        int i = 0;
        if (playerExists(uuid)) {
            try {
                ResultSet rs = Main.mysql.query("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
                if (rs.next())
                    rs.getInt("DEATHS");
                i = rs.getInt("DEATHS");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(uuid);
            getDeaths(uuid);
        }
        return i;
    }

    public static Integer getPoints(String uuid) {
        int i = 0;
        if (playerExists(uuid)) {
            try {
                ResultSet rs = Main.mysql.query("SELECT * FROM RANK WHERE UUID= '" + uuid + "'");
                if(rs.next())
                    rs.getInt("POINTS");
                i = rs.getInt("POINTS");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(uuid);
            getPoints(uuid);
        }
        return i;
    }

    public static String getRank(String uuid) {
        String i = "";
        if (playerExists(uuid)) {
            try {
                ResultSet rs = Main.mysql.query("SELECT * FROM RANK WHERE UUID= '" + uuid + "'");
                if(rs.next())
                    rs.getString("RANK");
                i = rs.getString("RANK");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(uuid);
            getRank(uuid);
        }
        return i;
    }

    public static String getDate() {
        String i = "";
        try {
            ResultSet rs = Main.mysql.query("SELECT * FROM WEEK_STATS");
            if (rs.next())
                rs.getString("DATE");
            i = rs.getString("DATE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    /* Monuments */

    public static void setMonuments(String uuid, int monuments) {
        if(playerExists(uuid)) {
            Main.mysql.update("UPDATE WEEK_STATS SET MONUMENTS= '" + monuments + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setMonuments(uuid, monuments);
        }
    }

    public static void addMonuments(String uuid, int monuments) {
        if(playerExists(uuid)) {
            setMonuments(uuid, getMonuments(uuid) + monuments);
        } else {
            createPlayer(uuid);
            addMonuments(uuid, monuments);
        }
    }

    /* Kills */

    public static void setKills(String uuid, Integer kills) {
        if (playerExists(uuid)) {
            Main.mysql.update("UPDATE WEEK_STATS SET KILLS= '" + kills + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setKills(uuid, kills);
        }
    }

    public static void addKills(String uuid, Integer kills) {
        if (playerExists(uuid)) {
            setKills(uuid, getKills(uuid) + kills);
        } else {
            createPlayer(uuid);
            addKills(uuid, kills);
        }
    }

    /* Deaths */

    public static void setDeaths(String uuid, Integer deaths) {
        if (playerExists(uuid)) {
            Main.mysql.update("UPDATE WEEK_STATS SET DEATHS= '" + deaths + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setDeaths(uuid, deaths);
        }
    }

    public static void addDeaths(String uuid, Integer deaths) {
        if (playerExists(uuid)) {
            setDeaths(uuid, getDeaths(uuid) + deaths);
        } else {
            createPlayer(uuid);
            addDeaths(uuid, deaths);
        }
    }

    /* Wools */

    public static void setWools(String uuid, Integer wools) {
        if (playerExists(uuid)) {
            Main.mysql.update("UPDATE WEEK_STATS SET WOOLS= '" + wools + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setWools(uuid, wools);
        }
    }

    public static void addWools(String uuid, Integer wools) {
        if (playerExists(uuid)) {
            setWools(uuid, getWools(uuid) + wools);
        } else {
            createPlayer(uuid);
            addWools(uuid, wools);
        }
    }

    /* Cores */

    public static void setCores(String uuid, Integer cores) {
        if(playerExists(uuid)) {
            Main.mysql.update("UPDATE WEEK_STATS SET CORES= '" + cores + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setCores(uuid, cores);
        }
    }

    public static void addCores(String uuid, Integer cores) {
        if(playerExists(uuid)) {
            setCores(uuid, getCores(uuid) + cores);
        } else {
            createPlayer(uuid);
            addCores(uuid, cores);
        }
    }

    /* Flags */

    public static void setFlags(String uuid, Integer flags) {
        if(playerExists(uuid)) {
            Main.mysql.update("UPDATE WEEK_STATS SET FLAGS= '" + flags + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setFlags(uuid, flags);
        }
    }

    public static void addFlags(String uuid, Integer flags) {
        if(playerExists(uuid)) {
            setFlags(uuid, getFlags(uuid) + flags);
        } else {
            createPlayer(uuid);
            addFlags(uuid, flags);
        }
    }

    /* Points */

    public static void setPoints(String uuid, Integer points) {
        if(playerExists(uuid)) {
            Main.mysql.update("UPDATE RANK SET POINTS= '" + points + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setPoints(uuid, points);
        }
    }

    public static void addPoints(String uuid, Integer points) {
        if(playerExists(uuid)) {
            setPoints(uuid, getPoints(uuid) + points);
        } else {
            createPlayer(uuid);
            addPoints(uuid, points);
        }
    }

    /* Rank */

    public static void setRank(String uuid, String rank) {
        if(playerExists(uuid)) {
            Main.mysql.update("UPDATE RANK SET RANK= '" + rank + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setRank(uuid, rank);
        }
    }

    public static void addRank(String uuid, String rank) {
        if(playerExists(uuid)) {
            setRank(uuid, rank);
        } else {
            createPlayer(uuid);
            addRank(uuid, rank);
        }
    }

    /* Date */

    public static void setDate(String date) {
        Main.mysql.update("UPDATE WEEK_STATS SET DATE= '" + date + "';");
    }
}
