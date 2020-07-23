package xps.Database;

import xps.Main;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLSetterGetter {

    public static boolean playerExists(String uuid) {
        try {
            ResultSet rs = Main.mysql.query("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
            return (rs.next() && rs.getString("UUID") != null);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void createPlayer(String uuid) {
        if (!playerExists(uuid)) {
            Main.mysql.update("INSERT INTO STATS(UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, NAME) VALUES ('" + uuid + "', '0', '0', '0', '0', '0', 'Null');"); // uuid, kills, deaths, flags, cores, wools, name
        }
    }

    public static String getName(String uuid) {
        String i = "";
        if (playerExists(uuid)) {
            try {
                ResultSet rs = Main.mysql.query("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
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
            Main.mysql.update("UPDATE STATS SET NAME= '" + name + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setName(uuid, name);
        }
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

    public static void setWools(String uuid, Integer wools) {
        if (playerExists(uuid)) {
            Main.mysql.update("UPDATE STATS SET WOOLS= '" + wools + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setWools(uuid, wools);
        }
    }

    public static void setCores(String uuid, Integer cores) {
        if(playerExists(uuid)) {
            Main.mysql.update("UPDATE STATS SET CORES= '" + cores + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setCores(uuid, cores);
        }
    }

    public static void setFlags(String uuid, Integer flags) {
        if(playerExists(uuid)) {
            Main.mysql.update("UPDATE STATS SET FLAGS= '" + flags + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setFlags(uuid, flags);
        }
    }


    public static void setKills(String uuid, Integer kills) {
        if (playerExists(uuid)) {
            Main.mysql.update("UPDATE STATS SET KILLS= '" + kills + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setKills(uuid, kills);
        }
    }

    public static void setDeaths(String uuid, Integer deaths) {
        if (playerExists(uuid)) {
            Main.mysql.update("UPDATE STATS SET DEATHS= '" + deaths + "' WHERE UUID= '" + uuid + "';");
        } else {
            createPlayer(uuid);
            setDeaths(uuid, deaths);
        }
    }

    public static void addName(String uuid, String name) {
        if(playerExists(uuid)) {
            setName(uuid, name);
        } else {
            createPlayer(uuid);
            addName(uuid, name);
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

    public static void addDeaths(String uuid, Integer deaths) {
        if (playerExists(uuid)) {
            setDeaths(uuid, getDeaths(uuid) + deaths);
        } else {
            createPlayer(uuid);
            addDeaths(uuid, deaths);
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

    public static void addCores(String uuid, Integer cores) {
        if(playerExists(uuid)) {
            setCores(uuid, getCores(uuid) + cores);
        } else {
            createPlayer(uuid);
            addCores(uuid, cores);
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
}
