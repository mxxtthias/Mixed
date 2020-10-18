package network.atria.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLSetterGetter {

  public static boolean playerExists(String uuid) {

    ResultSet rs = null;
    ResultSet week_rs = null;
    ResultSet rank_rs = null;
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    PreparedStatement statement3 = null;
    Connection connection = null;

    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
      statement2 =
          connection.prepareStatement("SELECT * FROM WEEK_STATS WHERE UUID= '" + uuid + "'");
      statement3 = connection.prepareStatement("SELECT * FROM RANKS WHERE UUID= '" + uuid + "'");

      rs = statement.executeQuery();
      week_rs = statement2.executeQuery();
      rank_rs = statement3.executeQuery();

      return (rs.next()
          && rs.getString("UUID") != null
          && week_rs.next()
          && week_rs.getString("UUID") != null
          && rank_rs.next()
          && rank_rs.getString("UUID") != null);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      closeStatement(statement);
      closeStatement(statement2);
      closeStatement(statement3);
      closeResultSet(rs);
      closeResultSet(week_rs);
      closeResultSet(rank_rs);
      close(connection);
    }
  }

  public static void createPlayer(String uuid) {
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    PreparedStatement statement3 = null;
    Connection connection = null;
    if (!playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "INSERT INTO STATS(UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, MONUMENTS, NAME) VALUES ('"
                    + uuid
                    + "', '0', '0', '0', '0', '0', '0', 'Null');");
        statement2 =
            connection.prepareStatement(
                "INSERT INTO WEEK_STATS(UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, MONUMENTS, NAME) VALUES ('"
                    + uuid
                    + "', '0', '0', '0', '0', '0', '0', 'Null');");
        statement3 =
            connection.prepareStatement(
                "INSERT INTO RANKS(UUID, NAME, POINTS, GAMERANK, EFFECT, SOUND, PROJECTILE) VALUES ('"
                    + uuid
                    + "', 'Null', '0', 'wood_iii', 'NONE', 'DEFAULT', 'NONE');");
        statement.execute();
        statement2.execute();
        statement3.execute();

      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        closeStatement(statement2);
        closeStatement(statement3);
        close(connection);
      }
    }
  }

  public static String getName(String uuid) {
    String i = "";
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getString("UUID");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeStatement(statement);
      closeResultSet(rs);
      close(connection);
    }
    return i;
  }

  public static void setName(String uuid, String name) {
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    PreparedStatement statement3 = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                ("UPDATE WEEK_STATS SET NAME= '" + name + "' WHERE UUID= '" + uuid + "';"));
        statement2 =
            connection.prepareStatement(
                ("UPDATE STATS SET NAME= '" + name + "' WHERE UUID= '" + uuid + "';"));
        statement3 =
            connection.prepareStatement(
                ("UPDATE RANKS SET NAME= '" + name + "' WHERE UUID= '" + uuid + "';"));
        statement.executeUpdate();
        statement2.executeUpdate();
        statement3.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        closeStatement(statement2);
        closeStatement(statement3);
        close(connection);
      }
    } else {
      createPlayer(uuid);
      setName(uuid, name);
    }
  }

  public static Integer getMonuments(String uuid) {
    int i = 0;
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getInt("MONUMENTS");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeResultSet(rs);
      closeStatement(statement);
      close(connection);
    }
    return i;
  }

  public static Integer getFlags(String uuid) {
    int i = 0;
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getInt("FLAGS");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeStatement(statement);
      closeResultSet(rs);
      close(connection);
    }
    return i;
  }

  public static Integer getCores(String uuid) {
    int i = 0;
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getInt("CORES");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeResultSet(rs);
      closeStatement(statement);
      close(connection);
    }
    return i;
  }

  public static Integer getWools(String uuid) {
    int i = 0;
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getInt("WOOLS");
      closeStatement(statement);
      closeResultSet(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeStatement(statement);
      closeResultSet(rs);
      close(connection);
    }
    return i;
  }

  public static Integer getKills(String uuid) {
    int i = 0;
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getInt("KILLS");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeResultSet(rs);
      closeStatement(statement);
      close(connection);
    }
    return i;
  }

  public static Integer getDeaths(String uuid) {
    int i = 0;
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM STATS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getInt("DEATHS");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeStatement(statement);
      closeResultSet(rs);
      close(connection);
    }
    return i;
  }

  public static Integer getPoints(String uuid) {
    int i = 0;
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM RANKS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getInt("POINTS");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeResultSet(rs);
      closeStatement(statement);
      close(connection);
    }
    return i;
  }

  public static String getRank(String uuid) {
    String i = "";
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM RANKS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getString("GAMERANK");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeStatement(statement);
      closeResultSet(rs);
      close(connection);
    }
    return i;
  }

  public static String getKillEffect(String uuid) {
    String i = "";
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM RANKS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getString("EFFECT");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeResultSet(rs);
      closeStatement(statement);
      close(connection);
    }
    return i;
  }

  public static String getKillSound(String uuid) {
    String i = "";
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM RANKS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getString("SOUND");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeStatement(statement);
      closeResultSet(rs);
      close(connection);
    }
    return i;
  }

  public static String getProjectileTrails(String uuid) {
    String i = "";
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement("SELECT * FROM RANKS WHERE UUID= '" + uuid + "'");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getString("PROJECTILE");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeStatement(statement);
      closeResultSet(rs);
      close(connection);
    }
    return i;
  }

  /* Monuments */

  public static void setMonuments(String uuid, int monuments) {
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE WEEK_STATS SET MONUMENTS= '" + monuments + "' WHERE UUID= '" + uuid + "';");
        statement2 =
            connection.prepareStatement(
                "UPDATE STATS SET MONUMENTS= '" + monuments + "' WHERE UUID= '" + uuid + "';");
        statement.executeUpdate();
        statement2.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        closeStatement(statement2);
        close(connection);
      }
    } else {
      createPlayer(uuid);
      setMonuments(uuid, monuments);
    }
  }

  public static void addMonuments(String uuid, int monuments) {
    if (playerExists(uuid)) {
      setMonuments(uuid, getMonuments(uuid) + monuments);
    } else {
      createPlayer(uuid);
      addMonuments(uuid, monuments);
    }
  }

  /* Kills */

  public static void setKills(String uuid, Integer kills) {
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE WEEK_STATS SET KILLS= '" + kills + "' WHERE UUID= '" + uuid + "';");
        statement2 =
            connection.prepareStatement(
                "UPDATE STATS SET KILLS= '" + kills + "' WHERE UUID= '" + uuid + "';");
        statement.executeUpdate();
        statement2.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        closeStatement(statement2);
        close(connection);
      }
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
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE WEEK_STATS SET DEATHS= '" + deaths + "' WHERE UUID= '" + uuid + "';");
        statement2 =
            connection.prepareStatement(
                "UPDATE STATS SET DEATHS= '" + deaths + "' WHERE UUID= '" + uuid + "';");
        statement.executeUpdate();
        statement2.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        closeStatement(statement2);
        close(connection);
      }
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
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE WEEK_STATS SET WOOLS= '" + wools + "' WHERE UUID= '" + uuid + "';");
        statement2 =
            connection.prepareStatement(
                "UPDATE STATS SET WOOLS= '" + wools + "' WHERE UUID= '" + uuid + "';");
        statement.executeUpdate();
        statement2.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        closeStatement(statement2);
        close(connection);
      }
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
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE WEEK_STATS SET CORES= '" + cores + "' WHERE UUID= '" + uuid + "';");
        statement2 =
            connection.prepareStatement(
                "UPDATE STATS SET CORES= '" + cores + "' WHERE UUID= '" + uuid + "';");
        statement.executeUpdate();
        statement2.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        closeStatement(statement2);
        close(connection);
      }
    } else {
      createPlayer(uuid);
      setCores(uuid, cores);
    }
  }

  public static void addCores(String uuid, Integer cores) {
    if (playerExists(uuid)) {
      setCores(uuid, getCores(uuid) + cores);
    } else {
      createPlayer(uuid);
      addCores(uuid, cores);
    }
  }

  /* Flags */

  public static void setFlags(String uuid, Integer flags) {
    PreparedStatement statement = null;
    PreparedStatement statement2 = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE WEEK_STATS SET FLAGS= '" + flags + "' WHERE UUID= '" + uuid + "';");
        statement2 =
            connection.prepareStatement(
                "UPDATE STATS SET FLAGS= '" + flags + "' WHERE UUID= '" + uuid + "';");

        statement.executeUpdate();
        statement2.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        closeStatement(statement2);
        close(connection);
      }
    } else {
      createPlayer(uuid);
      setFlags(uuid, flags);
    }
  }

  public static void addFlags(String uuid, Integer flags) {
    if (playerExists(uuid)) {
      setFlags(uuid, getFlags(uuid) + flags);
    } else {
      createPlayer(uuid);
      addFlags(uuid, flags);
    }
  }

  /* Points */

  public static void setPoints(String uuid, Integer points) {
    PreparedStatement statement = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE RANKS SET POINTS= '" + points + "' WHERE UUID= '" + uuid + "';");
        statement.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        close(connection);
      }
    } else {
      createPlayer(uuid);
      setPoints(uuid, points);
    }
  }

  public static void addPoints(String uuid, Integer points) {
    if (playerExists(uuid)) {
      setPoints(uuid, getPoints(uuid) + points);
    } else {
      createPlayer(uuid);
      addPoints(uuid, points);
    }
  }

  /* RankSystemManager */

  public static void setRank(String uuid, String rank) {
    PreparedStatement statement = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE RANKS SET GAMERANK= '" + rank + "' WHERE UUID= '" + uuid + "';");
        statement.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        close(connection);
      }
    } else {
      createPlayer(uuid);
      setRank(uuid, rank);
    }
  }

  /* KillEffect */

  public static void setKillEffect(String uuid, String effect) {
    PreparedStatement statement = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE RANKS SET EFFECT= '" + effect + "' WHERE UUID= '" + uuid + "';");
        statement.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        close(connection);
      }
    } else {
      createPlayer(uuid);
      setKillEffect(uuid, effect);
    }
  }

  /* KillSound */
  public static void setKillSound(String uuid, String sound) {
    PreparedStatement statement = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE RANKS SET SOUND= '" + sound + "' WHERE UUID= '" + uuid + "';");

        statement.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        close(connection);
      }
    } else {
      createPlayer(uuid);
      setKillSound(uuid, sound);
    }
  }

  /* Projectile Trails */
  public static void setProjectileTrails(String uuid, String projectile) {
    PreparedStatement statement = null;
    Connection connection = null;
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement =
            connection.prepareStatement(
                "UPDATE RANKS SET PROJECTILE= '" + projectile + "' WHERE UUID= '" + uuid + "';");

        statement.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        closeStatement(statement);
        close(connection);
      }
    } else {
      createPlayer(uuid);
      setProjectileTrails(uuid, projectile);
    }
  }

  private static void close(Connection connection) {
      if (connection != null) {
          try {
              connection.close();
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  }

  private static void closeStatement(PreparedStatement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private static void closeResultSet(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
