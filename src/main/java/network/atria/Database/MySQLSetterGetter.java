package network.atria.Database;

import java.sql.*;
import java.util.UUID;

public class MySQLSetterGetter {

  public static boolean playerExists(String uuid) {

    ResultSet rs = null;
    PreparedStatement statement = null;
    Connection connection = null;
    StringBuilder builder = new StringBuilder();
    builder
        .append("SELECT UUID FROM STATS WHERE UUID = '")
        .append(uuid)
        .append("' union SELECT UUID FROM RANKS WHERE UUID ='")
        .append(uuid)
        .append("' union SELECT UUID FROM WEEK_STATS WHERE UUID = '")
        .append("';");
    try {
      connection = MySQL.getHikari().getConnection();
      statement = connection.prepareStatement(builder.toString());
      rs = statement.executeQuery();

      return (rs.next() && rs.getString("UUID") != null);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      closeStatement(statement);
      closeResultSet(rs);
      close(connection);
    }
  }

  public static void createPlayer(String uuid) {
    Statement statement = null;
    Connection connection = null;
    final String query =
        "INSERT INTO STATS(UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, MONUMENTS, NAME) VALUES ('"
            + uuid
            + "', '0', '0', '0', '0', '0', '0', 'Null');";
    final String query2 =
        "INSERT INTO STATS(UUID, KILLS, DEATHS, FLAGS, CORES, WOOLS, MONUMENTS, NAME) VALUES ('"
            + uuid
            + "', '0', '0', '0', '0', '0', '0', 'Null');";
    final String query3 =
        "INSERT INTO RANKS(UUID, NAME, POINTS, GAMERANK, EFFECT, SOUND, PROJECTILE) VALUES ('"
            + uuid
            + "', 'Null', '0', 'wood_iii', 'NONE', 'DEFAULT', 'NONE');";
    if (!playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement = connection.createStatement();
        statement.addBatch(query);
        statement.addBatch(query2);
        statement.addBatch(query3);
        statement.executeBatch();

      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        try {
          if (statement != null) {
            statement.close();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
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
      statement = connection.prepareStatement("SELECT NAME FROM STATS WHERE UUID= '" + uuid + "';");
      rs = statement.executeQuery();
      if (rs.next()) i = rs.getString("NAME");
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
    Statement statement = null;
    Connection connection = null;
    final String query = "UPDATE STATS SET NAME= '" + name + "' WHERE UUID= '" + uuid + "';";
    final String query2 = "UPDATE WEEK_STATS SET NAME= '" + name + "' WHERE UUID= '" + uuid + "'";
    final String query3 = "UPDATE RANKS SET NAME= '" + name + "' WHERE UUID= '" + uuid + "';";
    if (playerExists(uuid)) {
      try {
        connection = MySQL.getHikari().getConnection();
        statement = connection.createStatement();
        statement.addBatch(query);
        statement.addBatch(query2);
        statement.addBatch(query3);
        statement.executeBatch();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        try {
          if (statement != null) {
            statement.close();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        close(connection);
      }
    } else {
      createPlayer(uuid);
    }
  }

  public static Integer getPoints(UUID uuid) {
    int i = 0;
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement =
          connection.prepareStatement("SELECT POINTS FROM RANKS WHERE UUID= '" + uuid + "'");
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

  public static String getRank(UUID uuid) {
    String i = "";
    PreparedStatement statement = null;
    ResultSet rs = null;
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      statement =
          connection.prepareStatement("SELECT GAMERANK FROM RANKS WHERE UUID= '" + uuid + "'");
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
      statement =
          connection.prepareStatement("SELECT EFFECT FROM RANKS WHERE UUID= '" + uuid + "'");
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
      statement = connection.prepareStatement("SELECT SOUND FROM RANKS WHERE UUID= '" + uuid + "'");
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
      statement =
          connection.prepareStatement("SELECT PROJECTILE FROM RANKS WHERE UUID= '" + uuid + "'");
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

  /* Points */

  public static void setPoints(String uuid, Integer points) {
    PreparedStatement statement = null;
    Connection connection = null;
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
  }

  public static void addPoints(String uuid, Integer points) {
    setPoints(uuid, getPoints(UUID.fromString(uuid)) + points);
  }

  /* RankSystemManager */

  public static void setRank(String uuid, String rank) {
    PreparedStatement statement = null;
    Connection connection = null;
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
  }

  /* KillEffect */

  public static void setKillEffect(String uuid, String effect) {
    PreparedStatement statement = null;
    Connection connection = null;
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
  }

  /* KillSound */
  public static void setKillSound(String uuid, String sound) {
    PreparedStatement statement = null;
    Connection connection = null;
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
  }

  /* Projectile Trails */
  public static void setProjectileTrails(String uuid, String projectile) {
    PreparedStatement statement = null;
    Connection connection = null;
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
