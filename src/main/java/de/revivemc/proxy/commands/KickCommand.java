package de.revivemc.proxy.commands;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {
    public KickCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        assert iCloudPlayer != null;
        String prefix = ProxySystem.getInstance().getPrefix(iCloudPlayer);
        if (!player.hasPermission("proxy.kick")) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDu hast keine Rechte auf diesen Befehl."));
            return;
        }

        if (!(args.length <= 2)) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: 'kick (name) (kick-id)'"));
            return;
        }

        if (args.length == 1) {
            displayAllReasons(player, iCloudPlayer);
            return;
        }

        if (args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: 'kick (name) (kick-id)'"));
            return;
        }

        if (args[0].equalsIgnoreCase("leTobiCW") || args[0].equalsIgnoreCase("leTobiBW")) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Du kannst diesen Spieler nicht bestrafen."));
            return;
        }

        if (args[0].equalsIgnoreCase(player.getName())) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Du kannst dich nicht Spieler bestrafen."));
            return;
        }

        final int kickid = Integer.parseInt(args[1]);
        final ReviveMCPlayer ReviveMCPlayer = new ReviveMCPlayer(iCloudPlayer);
        final String secondprefix = ReviveMCPlayer.getSecondColor();
        switch (kickid) {
            case 1:
                try {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    target.disconnect(TextComponent.fromLegacyText("§8§l» §cCytura.net §8§l« \n\n §7Du wurdest gekickt. \n §7Grund: §cSpielverhalten"));
                    for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                        if(players.hasPermission("proxy.kick")) {
                            players.sendMessage(TextComponent.fromLegacyText(prefix + "Das Teammitglied " + secondprefix + player.getName() + " §7hat den Spieler " + secondprefix + args[1] + " §7wegen §cSpielverhalten §7von dem Netzwerk geworfen."));
                        }
                    }
                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Der Spieler " + secondprefix + args[0] + " §7befinet sich nicht auf dem Netzwerk."));
                    return;
                }
                break;
            case 2:
                try {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    target.disconnect(TextComponent.fromLegacyText("§8§l» §cCytura.net §8§l« \n\n §7Du wurdest gekickt. \n §7Grund: §cChatverhalten"));
                    for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                        if(players.hasPermission("proxy.kick")) {
                            players.sendMessage(TextComponent.fromLegacyText(prefix + "Das Teammitglied " + secondprefix + player.getName() + " §7hat den Spieler " + secondprefix + args[1] + " §7wegen §cChatverhalten §7von dem Netzwerk geworfen."));
                        }
                    }
                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Der Spieler " + secondprefix + args[0] + " §7befinet sich nicht auf dem Netzwerk."));
                    return;
                }
                break;
            default:
                displayAllReasons(player, iCloudPlayer);
        }
    }


    public void displayAllReasons(ProxiedPlayer player, ICloudPlayer iCloudPlayer) {
        String prefix = ProxySystem.getInstance().getPrefix(iCloudPlayer);
        player.sendMessage(TextComponent.fromLegacyText(prefix + "§8§m----------------------"));
        player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
        player.sendMessage(TextComponent.fromLegacyText(prefix + "(1) Spielverhalten"));
        player.sendMessage(TextComponent.fromLegacyText(prefix + "(2) Chatverhalten"));
        player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
        player.sendMessage(TextComponent.fromLegacyText(prefix + "§8§m----------------------"));
    }
}
