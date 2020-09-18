package network.atria.Database;

import com.zaxxer.hikari.HikariDataSource;
import network.atria.Main;

import java.sql.*;

public class MySQL {

    public static HikariDataSource hikari;

    public static void connect() {
        hikari = new HikariDataSource();

        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", getHost());
        hikari.addDataSourceProperty("port", getPort());
        hikari.addDataSourceProperty("databaseName", getDatabase());
        hikari.addDataSourceProperty("user", getUser());
        hikari.addDataSourceProperty("password", getPassword());

        hikari.setMaximumPoolSize(500);
        hikari.setConnectionTimeout(20000);
        hikari.setMinimumIdle(50);
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
        return hikari;
    }

    public static Connection getConnection() {
        try {
            return hikari.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
