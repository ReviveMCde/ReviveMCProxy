package de.revivemc.proxy.modules.punish;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class PunishModule {
    public void punish(UUID uuid, PunishType punishType, int punishpoints, String punishreason, UUID punisher, long punishend) {
        long end = 0;
        if (punishend == -1) {
            end = -1;
        } else {
            long current = System.currentTimeMillis();
            long millis = punishend * 1000;
            end = current + millis;
        }
        ProxySystem.getInstance().getDatabaseDriver().update("INSERT INTO revivemc_punish_active(UUID, PunishType, PunishReason, Punisher, PunishEnd) VALUES ('" + uuid + "','" + punishType + "','" + punishreason + "','" + punisher + "','" + end + "')");
        ProxySystem.getInstance().getDatabaseDriver().update("INSERT INTO revivemc_punish_archiv(UUID, PunishType, PunishReason, Punisher) VALUES ('" + uuid + "','" + punishType + "','" + punishreason + "','" + punisher + "')");
        addPunishPoints(uuid, punishpoints);
        if(punishType == PunishType.PUNISH) {
            if (ProxyServer.getInstance().getPlayer(uuid) != null) {
                ProxyServer.getInstance().getPlayer(uuid).disconnect( TextComponent.fromLegacyText("§8§l» §cCytura.net §8§l« \n\n §7Du wurdest vom Netzwerk gebannt. \n\n §7Grund: §c§l" + punishreason + " \n §7Dauer: §c" + getRemainingPunishTime(uuid) + " \n\n §7Solltest du dies als Ungerecht finden, \n §7dann schreibe einen Entbannungsantrag in unserem Forum. \n\n §7Forum: §chttps://Cytura.net"));
            }
        } else {
            if (ProxyServer.getInstance().getPlayer(uuid) != null) {
                final ICloudPlayer iCloudPlayer = (ICloudPlayer) CloudAPI.getInstance().getCloudPlayerManager().getCloudPlayer(uuid);
                ProxyServer.getInstance().getPlayer(uuid).sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Du wurdest gemutet. \n §7Grund: §c" + punishreason + " \n §7Dauer: §c" + getRemainingPunishTime(uuid)));
            }
        }
    }

    public void setPunishPoints(UUID uuid, Integer value) {
        if (value < 0) {
            value = 0;
        }
        ProxySystem.getInstance().getDatabaseDriver().update("UPDATE revivemc_proxy_player SET PunishPoints = '" + value + "' WHERE UUID= '" + uuid + "';");
    }

    public void addPunishPoints(UUID uuid, Integer value) {
        setPunishPoints(uuid, (getPunishPoints(uuid) + value));
    }

    public void removePunishPoints(UUID uuid, Integer value) {
        setPunishPoints(uuid, (getPunishPoints(uuid) - value));
    }

    public int getPunishPoints(UUID uuid) {
        Integer integer = 0;
        try {
            ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_player WHERE UUID='" + uuid + "'");
            if(resultSet.next()) {
                integer = resultSet.getInt("PunishPoints");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return integer;
    }

    public void pardon(UUID uuid) {
        ProxySystem.getInstance().getDatabaseDriver().update("DELETE FROM revivemc_punish_active WHERE UUID='" + uuid + "'");
    }

    public boolean isPunished(UUID uuid) {
        ResultSet rs = ProxySystem.getInstance().getDatabaseDriver().query("SELECT PunishEnd FROM revivemc_punish_active WHERE UUID='" + uuid + "'");
        try {
            return rs.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
    }

    public String getPunishReason(UUID uuid) {
        ResultSet rs = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_punish_active WHERE UUID='" + uuid + "'");
        try {
            if (rs.next()) {
                return rs.getString("PunishReason");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public UUID getPunisher(UUID uuid) {
        ResultSet rs = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_punish_active WHERE UUID='" + uuid + "'");
        try {
            if (rs.next()) {
                return UUID.fromString(rs.getString("Punisher"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Long getPunishEnd(UUID uuid) {
        ResultSet rs = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_punish_active WHERE UUID='" + uuid + "'");
        try {
            if (rs.next()) {
                return rs.getLong("PunishEnd");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Integer getAllPunishedPlayersAsInteger() {
        Integer punishedplayers = 0;
        ResultSet rs = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_punish_active`");
        try {
            while (rs.next()) {
                punishedplayers++;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return punishedplayers;
    }


    public String getRemainingPunishTime(UUID uuid) {
        long current = System.currentTimeMillis();
        long end = Objects.requireNonNull(getPunishEnd(uuid));
        if (end == -1) {
            return "§c§lPERMANENT";
        }
        long millis = end - current;
        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        long weeks = 0;
        while (millis > 1000) {
            millis -= 1000;
            ++seconds;
        }
        while (seconds > 60) {
            seconds -= 60;
            ++minutes;
        }
        while (minutes > 60) {
            minutes -= 60;
            ++hours;
        }
        while (hours > 24) {
            hours -= 24;
            ++days;
        }
        while (days > 7) {
            days -= 7;
            ++weeks;
        }
        return "§e" + weeks + " Woche(n) " + days + " Tag(e) " + hours + " Stunde(n) " + minutes + " Minute(n) " + seconds + " Sekunde(n) ";
    }

    public enum PunishType {
        MUTE, PUNISH;
    }
}
