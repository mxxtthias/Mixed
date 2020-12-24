package network.atria.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;
import network.atria.Mixed;

public class MySQL {

  private static HikariDataSource ds;

  public void connect() {
    HikariConfig hikari = new HikariConfig();
    hikari.setJdbcUrl(
        "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase() + "?useSSL=false");
    hikari.addDataSourceProperty("user", getUser());
    hikari.addDataSourceProperty("password", getPassword());
    hikari.addDataSourceProperty("useServerPrepStmts", true);
    hikari.addDataSourceProperty("cachePrepStmts", true);
    hikari.addDataSourceProperty("cacheResultSetMetadata", true);
    hikari.addDataSourceProperty("useLocalSessionState", true);
    hikari.addDataSourceProperty("rewriteBatchedStatements", true);
    hikari.addDataSourceProperty("cacheResultSetMetadata", true);
    hikari.addDataSourceProperty("cacheServerConfiguration", true);
    hikari.addDataSourceProperty("elideSetAutoCommits", true);
    hikari.addDataSourceProperty("maintainTimeStats", false);
    hikari.addDataSourceProperty("prepStmtCacheSize", 250);
    hikari.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    hikari.addDataSourceProperty("characterEncoding", "utf8");

    ds = new HikariDataSource(hikari);
  }

  public void createTables() {
    Connection connection = null;
    try {
      connection = MySQL.getHikari().getConnection();
      Statement statement = connection.createStatement();

      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS STATS(UUID varchar(36) NOT NULL UNIQUE KEY, NAME varchar(20), KILLS int, DEATHS int, FLAGS int, CORES int, WOOLS int, MONUMENTS int, PLAYTIME int, POINTS int);");
      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS WEEK_STATS(UUID varchar(36) NOT NULL UNIQUE KEY, NAME varchar(20), KILLS int, DEATHS int, FLAGS int, CORES int, WOOLS int, MONUMENTS int, PLAYTIME int, POINTS int);");
      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS RANKS(UUID varchar(36) NOT NULL UNIQUE KEY, NAME varchar(20), GAMERANK varchar(20), EFFECT varchar(20), SOUND varchar(20), PROJECTILE varchar(20));");

      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private String getHost() {
    return Mixed.get().getConfig().getString("MySQL.Host");
  }

  private String getDatabase() {
    return Mixed.get().getConfig().getString("MySQL.Database");
  }

  private String getUser() {
    return Mixed.get().getConfig().getString("MySQL.User");
  }

  private String getPassword() {
    return Mixed.get().getConfig().getString("MySQL.Password");
  }

  private Integer getPort() {
    return Mixed.get().getConfig().getInt("MySQL.Port");
  }

  public static HikariDataSource getHikari() {
    return ds;
  }
}
