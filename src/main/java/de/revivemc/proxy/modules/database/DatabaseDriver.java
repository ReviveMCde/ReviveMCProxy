package de.revivemc.proxy.modules.database;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.*;

public class DatabaseDriver {

    private String HOST = "127.0.0.1";
    private String DATABASE = "mcserver";
    private String USER = "root";
    private String PASSWORD = "password";
    private Connection connection;

    public DatabaseDriver(String host, String database, String user, String password)
    {
        this.HOST = host;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;

        connect();
    }

    public void connect()
    {
        try
        {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":3306/" + this.DATABASE + "?autoReconnect=true", this.USER, this.PASSWORD);
            ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("Connected to MySQL!"));
        }
        catch (SQLException e)
        {
            ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("Errow while connecting to MySQL! Error: " + e.getMessage()));
        }
    }

    public void close()
    {
        try
        {
            if (this.connection != null)
            {
                this.connection.close();
                ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("Connection to MySQL was stopped!"));
            }
        }
        catch (SQLException e)
        {
            ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("Error while stopping MySQL! Error: " + e.getMessage()));
        }
    }

    public void update(String sql)
    {
        try
        {
            this.connection.createStatement().executeUpdate(sql);
        }
        catch (SQLException ignored) {
            connect();
            update(sql);
        }
    }

    public ResultSet query(String qry) {
        ResultSet resultSet = null;
        try
        {
            Statement statement = this.connection.createStatement();
            resultSet = statement.executeQuery(qry);
        }
        catch (SQLException exception)
        {
            connect();
            query(qry);
            System.err.println(exception);
        }
        return resultSet;
    }
}
