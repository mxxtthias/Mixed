package network.atria.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;
import network.atria.Main;

public class MySQL {

  private static HikariDataSource ds;

  public static void connect() {
    HikariConfig hikari = new HikariConfig();

    hikari.setJdbcUrl("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase());
    hikari.addDataSourceProperty("user", getUser());
    hikari.addDataSourceProperty("password", getPassword());
    hikari.addDataSourceProperty("autoReconnect", true);
    hikari.addDataSourceProperty("cachePrepStmts", true);
    hikari.addDataSourceProperty("prepStmtCacheSize", 250);
    hikari.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    hikari.addDataSourceProperty("useServerPrepStmts", true);
    hikari.addDataSourceProperty("cacheResultSetMetadata", true);

    ds = new HikariDataSource(hikari);
  }

  private static String getHost() {
    return Main.getInstance().getConfig().getString("MySQL.Host");
  }

  private static String getDatabase() {
    return Main.getInstance().getConfig().getString("MySQL.Database");
  }

  private static String getUser() {
    return Main.getInstance().getConfig().getString("MySQL.User");
  }

  private static String getPassword() {
    return Main.getInstance().getConfig().getString("MySQL.Password");
  }

  private static Integer getPort() {
    return Main.getInstance().getConfig().getInt("MySQL.Port");
  }

  public static HikariDataSource getHikari() {
    return ds;
  }
}
