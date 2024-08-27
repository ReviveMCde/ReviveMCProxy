package de.revivemc.proxy;

import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.commands.*;
import de.revivemc.proxy.listener.player.PlayerChatListener;
import de.revivemc.proxy.listener.player.PlayerConnectListener;
import de.revivemc.proxy.listener.player.PlayerDisconnectListener;
import de.revivemc.proxy.listener.player.PlayerServerSwitchListener;
import de.revivemc.proxy.modules.database.DatabaseDriver;
import de.revivemc.proxy.modules.onlinetime.listener.OnlineTimeDisconnectListener;
import de.revivemc.proxy.modules.onlinetime.listener.OnlineTimeLoginListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class ProxySystem extends Plugin {

    private static ProxySystem instance;
    private DatabaseDriver databaseDriver;
    private String[] words = new String[] {"hurensohn", "hure", "hur3ns0hn", "hur3", "hur3sohn", "hurens0hn", "bastard", "bastardshon", "b4stard", "b4st4rd", "ficker", "fick dich", "nuttensohn", "nutte", "hilter", "adolf hitler", "Schutzstaffel", "gaskammer", "sieg heil", "heil hitler", "vergasung" +
            "Deutsche Reich", "judenvergasung", "drecks jude", "verreck", "easy", "eazy", "ez", "pleb", "clap", "ten", "snitch", "bitch", "fresse", "halts maul", "kahba" +
            "wixxer", "wichser", "satan", "lappen", "lpn", "nigga", "nigger", "n1bba", "nibba", "negger", "neger", "niger", "bhd", "behindert", "behinderter", "behinderte" +
            "arschloch", "narzi", "nazi","lutscher", "Fortnite", "bastart", "missgeburt", "missit", "mistgeburt", "satanist", "satansbraten", "satansfreund", "schwanzlutscher", "yarak", "yarrak", "son of a bitch", "hooker", "prostitutka", "проститутка" +
            "шлюха", "shlyukha", "Sukin syn", "Сукин сын", "sukinsyn", "kurwa", "drań", "prostytutka", "prostituată", "curvă", "Fiu de curvă", "nenorocitule", "mutterficker", "fahişe" +
            "orospu çocuğu", "Orospu çocuğu", "Piç", "whore", "motherfucker", "cocksucker", "idiot", "spast", "milf", "milfhunter", "zurückgeblieben", "Schlecher Server", "MoonshitMC.eu", "MoonfuckMC", "MoonbugMC", "MoonhureMC", "Drecks Server", "geh sterben", "vergrab dich", "erhäng dich", "stirb an deinen deppressionen", "depri kid", "kid", "fortnite", "fortnite bestes game", "vbucks", "Minecraft ist scheiße", "kiddi", "kiddie", "kiddy", "kidy", "kidi", "kidie"};

    private String[] cmdblock = new String[] {"plugins", "pl", "bukkit:plugins", "bukkit:pl", "minecraft:me", "/calc", "/calculate", "bungee", "bungeecord", "proxy", "bukkit:help"};

    @Override
    public void onEnable() {
        instance = this;
        databaseDriver = new DatabaseDriver("localhost", "ReviveMC_Cloud", "root", "~aO_8QPm|5S!LNp{?PZt(+Ez%ldr$iY%6My[kjEaYy*D`(4A0FmM1ajku{z402]0");
        databaseDriver.update("CREATE TABLE IF NOT EXISTS revivemc_proxy_player(UUID varchar(64), PunishPoints integer(16), StrikePoints integer(16), onlinetime varchar(64))");
        databaseDriver.update("CREATE TABLE IF NOT EXISTS revivemc_punish_active(UUID varchar(64), PunishType varchar(6), PunishReason varchar(32), Punisher varchar(64), PunishEnd varchar(100))");
        databaseDriver.update("CREATE TABLE IF NOT EXISTS revivemc_punish_archiv(UUID varchar(64), PunishType varchar(6), PunishReason varchar(32), Punisher varchar(64))");
        databaseDriver.update("CREATE TABLE IF NOT EXISTS revivemc_proxy_reportarchiv(UUID varchar(64), ReporterUUID varchar(64), ReportId varchar(32), Reason varchar(32), Server varchar(16), Active integer(2), AcceptorUUID varchar(64))");
        databaseDriver.update("CREATE TABLE IF NOT EXISTS revivemc_proxy_adminbans(ipaddress varchar(32))");
        databaseDriver.update("CREATE TABLE IF NOT EXISTS revivemc_proxy_notify_active(UUID varchar(64), status integer(1))");
        databaseDriver.update("CREATE TABLE IF NOT EXISTS revivemc_proxy_rang_archiv(UUIDPlayer1 varchar(64), UUIDPlayer2 varchar(64), Rang varchar(64), Reason varchar(64), ID varchar(64), Date varchar(64))");
        databaseDriver.update("CREATE TABLE IF NOT EXISTS revivemc_friends_data(UUID varchar(64), Friends JSON, Requests JSON, Online integer(2), LastConnect BIGINT, Server varchar(32))");
        initListener();
        initCommands();
    }

    @Override
    public void onDisable() {

    }

    public void initListener() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerConnectListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerDisconnectListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerChatListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerServerSwitchListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new OnlineTimeLoginListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new OnlineTimeDisconnectListener());
    }

    public void initCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new AdminCommand("admin"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PunishCommand("punish"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PardonCommand("pardon"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PunishInfoCommand("punishinfo"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new InfoCommand("info"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickCommand("kick"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TeamChatCommand("tc"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TeamChatCommand("teamchat"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ClearChatCommand("cc"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ClearChatCommand("clearchat"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReportCommand("report"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReportsCommand("reports"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CheckReportCommand("checkreport"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CoinsCommand("coins"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new OnlinetimeCommand("ot"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new NotfiyCommand("notify"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new RangCommand("rang"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new FriendCommand("friend"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new RulesCommand("rules"));
    }

    public String getPrefix(ICloudPlayer iCloudPlayer) {
        return "§8» " + Objects.requireNonNull(iCloudPlayer.getProperty("color")).getValue() + "Proxy §8× §7";
    }

    public static ProxySystem getInstance() {
        return instance;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    private boolean playerExists(UUID uuid) {
        try {
            ResultSet rs = getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_player` WHERE  `UUID` = '" + uuid.toString() + "'");
            if (rs.next()) {
                return rs.getString("UUID") != null;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isPlayerIPBanned(String ip) {
        try {
            ResultSet rs = getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_adminbans` WHERE  `ipaddress` = '" + ip + "'");
            if (rs.next()) {
                return rs.getString("ipaddress") != null;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void createProxyPlayer(UUID uuid) {
        if(!playerExists(uuid)) {
            getInstance().getDatabaseDriver().update("INSERT INTO `revivemc_proxy_player`(`UUID`, `PunishPoints`, `StrikePoints`, `onlinetime`) VALUES ('" + uuid + "', '0', '0', '0');");
            getInstance().getDatabaseDriver().update("INSERT INTO `revivemc_friends_data`(`UUID`, `Friends`, `Requests`, `Online`, `LastConnect`, `Server`) VALUES ('" + uuid + "', '', '', '1', '1', 'Lobby-1');");
        }
    }

    public String[] getCmdblock() {
        return cmdblock;
    }

    public String[] getWords() {
        return words;
    }
}
