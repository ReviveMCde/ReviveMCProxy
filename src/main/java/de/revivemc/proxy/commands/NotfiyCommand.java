package de.revivemc.proxy.commands;

import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.notify.NotifyModule;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import de.revivemc.proxy.modules.uuid.NameFetcher;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class NotfiyCommand extends Command {

    public NotfiyCommand(String name) { super(name); }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        final ReviveMCPlayer reviveMCPlayer = new ReviveMCPlayer(iCloudPlayer);
        final String prefix = ProxySystem.getInstance().getPrefix(iCloudPlayer);
        assert iCloudPlayer != null;
        if(!player.hasPermission("proxy.notify")) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDu hast keine Rechte auf diesen Befehl."));
            return;
        }

        NotifyModule notifyModule = new NotifyModule(player.getUniqueId());


        if (args.length != 1) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: 'notify on/off/status'"));
            return;
        }

        String command = args[0];

        switch (command) {
            case "on":
                if(notifyModule.getNotifyModulePlayer() != 0) {
                    player.sendMessage(prefix + "§cDu befindest dich bereits im Notify System!");
                    break;
                }

                notifyModule.updateNotifyModulePlayer(1);
                player.sendMessage(prefix + "§aDu hast dich in das Notify System eingeloggt.");
                break;
            case "off":
                if(notifyModule.getNotifyModulePlayer() != 1) {
                    player.sendMessage(prefix + "§cDu befindest dich bereits nicht im Notify System!");
                    break;
                }

                //Teammitglieder überprüfung

                notifyModule.updateNotifyModulePlayer(0);
                player.sendMessage(prefix + "§4Du hast dich aus dem Notify System ausgeloggt.");
                break;

            case "status":
                if (notifyModule.getNotifyModulePlayer() == 1) {
                    player.sendMessage(prefix + "§aDu befindest dich im Notify System.");
                    break;
                } else if (notifyModule.getNotifyModulePlayer() == 0) {
                    player.sendMessage(prefix + "§4Du befindest dich nicht im Notify System.");
                    break;
                } else {
                    player.sendMessage(prefix + "§c§lEs ist ein Fehler aufgetreten. Bitte kontaktiere die Developer!");
                    break;
                }
            case "list":
                if(!player.hasPermission("proxy.notify.list")) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: 'notify on/off/status'"));
                    break;
                }

                player.sendMessage(prefix + "§7§m-------------------------------------");
                player.sendMessage(prefix + "§aEingeloggte Teammitglieder: ");
                player.sendMessage(prefix + " ");
                try {
                    final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_notify_active WHERE status='1'");

                    while (resultSet.next()) {
                        player.sendMessage(prefix + NameFetcher.getName(UUID.fromString(resultSet.getString("UUID"))));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                player.sendMessage(prefix + " ");
                player.sendMessage(prefix + "§4Ausgeloggte Teammitglieder: ");
                player.sendMessage(prefix + " ");

                try {
                    final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_notify_active WHERE status='0'");

                    while (resultSet.next()) {
                        player.sendMessage(prefix + NameFetcher.getName(UUID.fromString(resultSet.getString("UUID"))));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                player.sendMessage(prefix + " ");
                player.sendMessage(prefix + "§7§m-------------------------------------");
                break;

            default:
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: 'notify on/off/status'"));
                break;
        }
    }
}
