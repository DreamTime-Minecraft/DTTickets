package ru.buseso.dreamtime.dttickets.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.*;

public class MySQLConnect {
    public static String HOST;
    public static String DATABASE;
    public static String USER;
    public static String PASSWORD;
    private Connection con;

    public MySQLConnect(String host, String database, String user, String password) {
        this.HOST = host;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;

        connect();
    }

    public void connect() {
        try {
            this.con = DriverManager
                    .getConnection("jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?autoReconnect=true", USER, PASSWORD);
            ProxyServer.getInstance().getConsole()
                    .sendMessage(new TextComponent("§aMySQL was successfully connected"));
        } catch (SQLException e) {
            ProxyServer.getInstance().getConsole()
                    .sendMessage(new TextComponent("§cMySQL connection failed: " + e.getMessage()));
        }
    }

    public void reconnect() {
        if(con == null) {
            connect();
        }
    }

    public void close() {
        try {
            if (this.con != null) {
                this.con.close();
            }
        } catch (SQLException sQLException) {}
    }


    public void update(String qry) {
        try {
            reconnect();
            Statement st = this.con.createStatement();
            st.executeUpdate(qry);
            st.close();
        } catch (SQLException e) {
            connect();
            System.err.println(e);
        }
    }

    public ResultSet query(String qry) {
        ResultSet rs = null;
        try {
            reconnect();
            Statement st = this.con.createStatement();
            rs = st.executeQuery(qry);
        } catch (SQLException e) {
            connect();
            System.err.println(e);
        }
        return rs;
    }
}
