package de.revivemc.proxy.modules.onlinetime;

import de.revivemc.proxy.ProxySystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class OnlinePlayer {
    private static HashMap<UUID, OnlinePlayer> onlineplayers;
    private UUID uuid;
    private long joined;
    private long last;

    public OnlinePlayer(final UUID uuid) {
        this.uuid = uuid;
        this.joined = System.currentTimeMillis();
        this.last = Long.parseLong(getlong(uuid));
        if (this.last < 0L) {
            ProxySystem.getInstance().getDatabaseDriver().update("UPDATE revivemc_proxy_player SET onlinetime='0' WHERE UUID='" + uuid + "'");
            this.last = 0L;
        }
        OnlinePlayer.onlineplayers.put(uuid, this);
    }

    public void removeCache() {
        ProxySystem.getInstance().getDatabaseDriver().update("UPDATE revivemc_proxy_player SET onlinetime='" + this.getCurrentTime() + "' WHERE UUID='" + uuid + "'");
        OnlinePlayer.onlineplayers.remove(this.uuid);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis() - this.joined + this.last;
    }

    public static OnlinePlayer getOnlinePlayer(final UUID uuid) {
        return OnlinePlayer.onlineplayers.get(uuid);
    }

    public String getlong(UUID uuid) {
        String string = "0";
        try {
            ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_player WHERE UUID='" + uuid + "'");
            if (resultSet.next()) {
                string = resultSet.getString("onlinetime");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return string;
    }

    public String getPlayerfinalOnlineTime(UUID uuid) {
        long time = getPlayerOnlineTime(uuid);
        long hours = time / 3600000L;
        long minT = time - hours * 3600000L;
        long min = minT / 60000L;
        return hours + "h " + min + "min";
    }

    public long getPlayerOnlineTime(UUID uuid) {
        long l = 0L;
        try {
            ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_player WHERE UUID='" + uuid + "'");
            if(resultSet.next()) {
                l = resultSet.getLong("onlinetime");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return l;
    }

    static {
        OnlinePlayer.onlineplayers = new HashMap<UUID, OnlinePlayer>();
    }
}
