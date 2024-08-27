package de.revivemc.proxy.commands;

import de.revivemc.proxy.modules.punish.PunishModule;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Objects;

public class AdminCommand extends Command {

    public AdminCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;
        if (!player.hasPermission("bester.server.eu.west.proxy.admin")) {
            player.sendMessage(TextComponent.fromLegacyText("Unknown command. Type \"/help\" for help."));
            return;
        }

        final ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        assert iCloudPlayer != null;
        final String prefix = ProxySystem.getInstance().getPrefix(iCloudPlayer);
        final String secondColor = new ReviveMCPlayer(iCloudPlayer).getSecondColor();

        if (args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende 'admin (parameter)'"));
            return;
        }

        if (args[0].equalsIgnoreCase("punishPoints")) {
            if (args.length != 4) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende 'admin punishPoints (player) (add/remove/set) (anzahl)'"));
                return;
            }

            try {
                final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                final ICloudPlayer targetCloud = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(target.getUniqueId());
                assert targetCloud != null;

                final PunishModule punishModule = new PunishModule();
                if (args[2].equalsIgnoreCase("add")) {
                    punishModule.addPunishPoints(target.getUniqueId(), Integer.parseInt(args[3]));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast " + secondColor + args[3] + " §7PunishPoints dem Spieler " + secondColor + args[1] + " §7hinzugefügt."));
                }

                if (args[2].equalsIgnoreCase("remove")) {
                    punishModule.removePunishPoints(target.getUniqueId(), Integer.parseInt(args[3]));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast " + secondColor + args[3] + " §7PunishPoints dem Spieler " + secondColor + args[1] + " §7entfernt."));
                }

                if (args[2].equalsIgnoreCase("set")) {
                    punishModule.setPunishPoints(target.getUniqueId(), Integer.parseInt(args[3]));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast die PunishPoints von dem Spieler " + secondColor + args[1] + " §7auf " + secondColor + args[3] + " §7gesetzt."));
                }

            }catch (Exception ex) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Es ist ein Fehler aufgetreten."));
            }
        }

        if (args[0].equalsIgnoreCase("coins")) {
            if (args.length != 4) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende 'admin coins (player) (add/remove/set) (anzahl)'"));
                return;
            }

            try {
                final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                final ICloudPlayer targetCloud = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(target.getUniqueId());
                assert targetCloud != null;

                if (args[2].equalsIgnoreCase("add")) {
                    targetCloud.setProperty("coins", Integer.parseInt(Objects.requireNonNull(targetCloud.getProperty("coins")).getValueAsString()) + Integer.parseInt(args[3]));
                    targetCloud.update().sync();
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast " + secondColor + args[3] + " §7Coins dem Spieler " + secondColor + args[1] + " §7hinzugefügt."));
                }

                if (args[2].equalsIgnoreCase("remove")) {
                    targetCloud.setProperty("coins", Integer.parseInt(Objects.requireNonNull(targetCloud.getProperty("coins")).getValueAsString()) - Integer.parseInt(args[3]));
                    targetCloud.update().sync();
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast " + secondColor + args[3] + " §7Coins dem Spieler " + secondColor + args[1] + " §7entfernt."));
                }

                if (args[2].equalsIgnoreCase("set")) {
                    targetCloud.setProperty("coins", Integer.parseInt(args[3]));
                    targetCloud.update().sync();
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast die Coins von dem Spieler " + secondColor + args[1] + " §7auf " + secondColor + args[3] + " §7gesetzt."));
                }

            }catch (Exception ex) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Es ist ein Fehler aufgetreten."));
            }

        }

        if (args[0].equalsIgnoreCase("lobby")) {
            if (args[1].equalsIgnoreCase("gadgets")) {
                if (args.length != 5) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende 'admin lobby gadgets (player) (gadgetName) (true/false)'"));
                    return;
                }

                try {
                    final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[2]);
                    final ICloudPlayer targetCloud = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(target.getUniqueId());
                    assert targetCloud != null;

                    if (args[4].equalsIgnoreCase("true")) {
                        targetCloud.setProperty("game." + args[3], args[4]);
                        targetCloud.update().sync();
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast dem Spieler " + secondColor + args[2] + " §7das Gadget " + secondColor + args[3] + " §7hinzugefügt."));
                    }

                    if (args[4].equalsIgnoreCase("false")) {
                        targetCloud.setProperty("game." + args[3], args[4]);
                        targetCloud.update().sync();
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast dem Spieler " + secondColor + args[2] + " §7das Gadget " + secondColor + args[3] + " §7entfernt."));
                    }

                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Es ist ein Fehler aufgetreten."));
                }
            } else {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende 'admin lobby (parameter)'"));
            }
        }

        if (args[0].equalsIgnoreCase("ip")) {
            if (args[1].equalsIgnoreCase("get")) {
                try {
                    final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[2]);
                    if (args.length != 3) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende 'admin ip get (player)'"));
                        return;
                    }

                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Die IPv4-Adresse von dem Spieler " + secondColor + args[2] + " §7lautet " + secondColor + target.getAddress().getHostString()));

                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Es ist ein Fehler aufgetreten."));
                }
            }else if (args[1].equalsIgnoreCase("ban")) {
                try {
                    if (args.length != 3) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende 'admin ip ban (ip)'"));
                        return;
                    }

                    ProxySystem.getInstance().getDatabaseDriver().update("INSERT INTO `revivemc_proxy_adminbans`(`ipaddress`) VALUES ('" + args[2] + "');");
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast die IPv4-Adresse " + secondColor + args[2] + " §7gebannt."));

                    for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                        if (players.getAddress().getHostString().equalsIgnoreCase(args[2])) {
                            players.disconnect(TextComponent.fromLegacyText("Your IP has been permanently banned."));
                        } else {
                            return;
                        }
                    }
                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Es ist ein Fehler aufgetreten."));
                }
            } else {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende 'admin ip (parameter)'"));
            }
        }
    }
}
