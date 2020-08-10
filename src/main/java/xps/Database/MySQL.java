package xps.Database;

import xps.Main;

import java.sql.*;

public class MySQL {

    public final String database;
    public final String user;
    public final String password;
    public final Integer port;

    private Connection connection;

    public MySQL(String database, String user, String password, int port) {
        this.database = database;
        this.user = user;
        this.password = password;
        this.port = port;

        connect();
    }

    public void connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://45.32.51.223:" +
                            getPort() + "/" + getDatabase() + "?autoReconnect=true",
                    getUser(), getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String qry) {
        try {
            Statement st = this.connection.createStatement();
            st.executeUpdate(qry);
            st.close();
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
        }
    }

    public ResultSet query(String qry) {
        ResultSet rs = null;
        try {
            Statement st = this.connection.createStatement();
            rs = st.executeQuery(qry);
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
        }
        return rs;
    }

    public static Integer getHost() {
        return Main.getInstance().getConfig().getInt("MySQL.Host");
    }

    public static String getDatabase() {
        return Main.getInstance().getConfig().getString("MySQL.Database");
    }

    public static String getUser() {
        return Main.getInstance().getConfig().getString("MySQL.User");
    }

    public static String getPassword() {
        return Main.getInstance().getConfig().getString("MySQL.Password");
    }

    public static Integer getPort() {
        return Main.getInstance().getConfig().getInt("MySQL.Port");
    }
}
