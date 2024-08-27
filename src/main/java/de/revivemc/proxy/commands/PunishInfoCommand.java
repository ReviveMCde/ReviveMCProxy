package de.revivemc.proxy.commands;

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

public class PunishInfoCommand extends Command {

    public PunishInfoCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        final ReviveMCPlayer ReviveMCPlayer = new ReviveMCPlayer(iCloudPlayer);
        assert iCloudPlayer != null;
        if (!player.hasPermission("proxy.punish")) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§cDu hast keine Rechte auf diesen Befehl."));
            return;
        }

        if (args.length != 1) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Verwende: 'punishinfo (name)'"));
            return;
        }

        String target = args[0];
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Alle Bestrafungen des Spielers " + ReviveMCPlayer.getSecondColor() + target));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " "));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§8» §7Alle Bestrafungen "));
        ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_punish_archiv` WHERE UUID='" + UUIDFetcher.getUUID(target) + "'");
        try {
            if (!resultSet.next()) {
                player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " §8- §cKeine Vorhanden §8/ §c✘"));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (resultSet.next()) {
                player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " §8- " + ReviveMCPlayer.getSecondColor() + resultSet.getString("PunishReason") + " §7(" + ReviveMCPlayer.getSecondColor() + resultSet.getString("PunishType") + "§7)"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
