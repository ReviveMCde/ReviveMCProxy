package de.revivemc.proxy.commands;

import de.revivemc.proxy.modules.uuid.NameFetcher;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import de.revivemc.proxy.modules.uuid.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CheckReportCommand extends Command {

    public CheckReportCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ICloudPlayer cloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        assert cloudPlayer != null;
        final String prefix = ProxySystem.getInstance().getPrefix(cloudPlayer);
        final String secondColor = new ReviveMCPlayer(cloudPlayer).getSecondColor();
        if (!player.hasPermission("proxy.report")) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDu hast keine Rechte auf diesen Befehl."));
            return;
        }

        if (args.length != 1) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende 'checkReport (ReportId)'"));
            return;
        }

        try {
            final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_reportarchiv WHERE ReportId='" + args[0] + "'");

            if (resultSet.next()) {
                if (resultSet.getInt("Active") == 0) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "§8§m---------------------------"));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Report-Informationen für die Id " + secondColor + resultSet.getString("ReportId")));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Gemeldeter Spieler §8» " + secondColor + NameFetcher.getName(UUID.fromString(resultSet.getString("UUID")))));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Gemeldet von §8» " + secondColor + NameFetcher.getName(UUID.fromString(resultSet.getString("ReporterUUID")))));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Abgeschlossen von §8» " + secondColor + NameFetcher.getName(UUID.fromString(resultSet.getString("AcceptorUUID")))));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Grund §8» " + secondColor + resultSet.getString("Reason")));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Server §8» " + secondColor + resultSet.getString("Server")));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "ReportId §8» " + secondColor + resultSet.getString("ReportId")));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "§8§m---------------------------"));
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Report ist noch aktiv."));
                }
            } else {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Die ReportId " + secondColor + args[0] + " §7existiert nicht."));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
