package de.revivemc.proxy.commands;

import de.revivemc.proxy.modules.uuid.NameFetcher;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.onlinetime.OnlinePlayer;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import de.revivemc.proxy.modules.punish.PunishModule;
import de.revivemc.proxy.modules.uuid.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class InfoCommand extends Command {
    public InfoCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        final ReviveMCPlayer ReviveMCPlayer = new ReviveMCPlayer(iCloudPlayer);
        assert iCloudPlayer != null;
        if (!player.hasPermission("proxy.info")) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§cDu hast keine Rechte auf diesen Befehl."));
            return;
        }

        if (args.length != 1) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Verwende: 'info (name)'"));
            return;
        }

        String target = args[0];
        UUID uuid = UUIDFetcher.getUUID(target);
        ICloudPlayer target2 = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(uuid);
        assert target2 != null;
        PunishModule punishModule = new PunishModule();
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " "));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Spielerinformationen über " + ReviveMCPlayer.getSecondColor() + target));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " "));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Spielername: " + ReviveMCPlayer.getSecondColor() + target));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " "));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "PunishPoints: " + ReviveMCPlayer.getSecondColor() + getPlayerPunishPoints(uuid)));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Spielzeit: " + ReviveMCPlayer.getSecondColor() + getPlayerfinalOnlineTime(uuid)));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " "));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Aktive Bestrafungen: "));

        if (punishModule.isPunished(uuid)) {
            try {
                ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_punish_active WHERE UUID='" + uuid + "'");
                while (resultSet.next()) {
                    player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " "));
                    player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Grund: " + ReviveMCPlayer.getSecondColor() + resultSet.getString("PunishReason") + " §7(" + ReviveMCPlayer.getSecondColor() + resultSet.getString("PunishType") + "§7)"));
                    player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Gebannt von: " + ReviveMCPlayer.getSecondColor() + NameFetcher.getName(punishModule.getPunisher(uuid))));
                    player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Verbleibende Zeit: " + ReviveMCPlayer.getSecondColor() + punishModule.getRemainingPunishTime(uuid)));
                }
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " §8- §cKeine Vorhanden §8/ §c✘"));
        }
    }

    public int getPlayerPunishPoints(UUID uuid) {
        Integer i = 0;
        try {
            ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_player WHERE UUID='" + uuid + "'");
            if (resultSet.next()) {
                i = resultSet.getInt("PunishPoints");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return i;
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
}
