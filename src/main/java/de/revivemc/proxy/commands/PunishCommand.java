package de.revivemc.proxy.commands;

import de.revivemc.proxy.modules.notify.NotifyModule;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import de.revivemc.proxy.modules.punish.PunishModule;
import de.revivemc.proxy.modules.uuid.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.lang.reflect.Proxy;

public class PunishCommand extends Command {

    public PunishCommand(String name) {
        super(name);
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        final ReviveMCPlayer ReviveMCPlayer = new ReviveMCPlayer(iCloudPlayer);
        assert iCloudPlayer != null;
        if (!player.hasPermission("proxy.punish")) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§cDu hast keine Rechte auf diesen Befehl."));
            return;
        }

        if (!(args.length <= 2) || args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Verwende: 'punish (name) (punish-id)'"));
            return;
        }

        if (args.length == 1) {
            displayAllReasons(player, iCloudPlayer, ReviveMCPlayer);
            return;
        }

        if (args[0].equals("leTobiCW") || args[0].equals("leTobiBW")) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Du darfst diesen Spieler nicht bestrafen!"));
            for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                if(players.hasPermission("proxy.admin")) {
                    players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Das Teammitglied " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7hat versucht " + ReviveMCPlayer.getSecondColor() + args[0] + " §7zu bestrafen."));
                }
            }
            return;
        }

        if (args[0].equals(player.getName())) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Du darfst dich nicht selbst bestrafen."));
            return;
        }

        String target = args[0];
        int punish_id = Integer.parseInt(args[1]);
        PunishModule punishModule = new PunishModule();
        switch (punish_id) {
            case 1:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.PUNISH, 20, "Hacking", player.getUniqueId(), 2592000);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() + "Hacking §7bestraft."));
                        }
                    }
                }
                break;

            case 2:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.PUNISH, 5, "Trolling", player.getUniqueId(), 259200);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Trolling §7bestraft."));
                        }
                    }
                }
                break;

            case 3:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.PUNISH, 5, "Report-Ausnutzung", player.getUniqueId(), 864000);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Report-Ausnutzung §7bestraft."));
                        }
                    }
                }
                break;

            case 4:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.PUNISH, 5, "Skin/Name", player.getUniqueId(), 432000);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Skin/Name §7bestraft."));
                        }
                    }
                }
                break;

            case 5:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.PUNISH, 5, "Teaming", player.getUniqueId(), 432000);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Teaming §7bestraft."));
                        }
                    }
                }
                break;

            case 6:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.PUNISH, 10, "Bugusing", player.getUniqueId(), 1209600);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Bugusing §7bestraft."));
                        }
                    }
                }
                break;

            case 7:
                if (player.hasPermission("proxy.punish.crashing")) {
                    punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.PUNISH, 30, "Server-Crashing", player.getUniqueId(), -1);
                    for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                        if (players.hasPermission("proxy.punish")) {
                            NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                            if(notifyModule.getNotifyModulePlayer() != 0) {
                                players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Server-Crashing §7bestraft."));
                            }
                        }
                    }
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Du darfst keine Spieler mit dem Grund " + ReviveMCPlayer.getSecondColor() +"Server-Crashing §7bestrafen"));
                }
                break;

            case 8:
                if (player.hasPermission("proxy.punish.hausverbot")) {
                    punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.PUNISH, 30, "Hausverbot", player.getUniqueId(), -1);
                    for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                        if (players.hasPermission("proxy.punish")) {
                            NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                            if(notifyModule.getNotifyModulePlayer() != 0) {
                                players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Hausverbot §7bestraft."));
                            }
                        }
                    }
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Du darfst keine Spieler mit dem Grund " + ReviveMCPlayer.getSecondColor() +"Hausverbot §7bestrafen"));
                }
                break;

            case 9:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.PUNISH, 30, "Bannumgehung", player.getUniqueId(), -1);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Bannumgehung §7bestraft."));
                        }
                    }
                }
                break;

            case 10:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.MUTE, 10, "Werbung", player.getUniqueId(), 604800);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Werbung §7bestraft."));
                        }
                    }
                }
                break;

            case 11:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.MUTE, 20, "Rassismus", player.getUniqueId(), -1);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Rassismus §7bestraft."));
                        }
                    }
                }
                break;

            case 12:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.MUTE, 20, "Religionsfeindlichkeit", player.getUniqueId(), -1);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Religionsfeindlichkeit §7bestraft."));
                        }
                    }
                }
                break;

            case 13:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.MUTE, 20, "Nazismus", player.getUniqueId(), -1);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Nazismus §7bestraft."));
                        }
                    }
                }
                break;

            case 14:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.MUTE, 5, "Spam", player.getUniqueId(), 259200);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Spam §7bestraft."));
                        }
                    }
                }
                break;

            case 15:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.MUTE, 10, "Beleidigung", player.getUniqueId(), 432000);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Beleidigung §7bestraft."));
                        }
                    }
                }
                break;

            case 16:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.MUTE, 10, "Hassrede", player.getUniqueId(), 1209600);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Hassrede §7bestraft."));
                        }
                    }
                }
                break;

            case 17:
                punishModule.punish(UUIDFetcher.getUUID(target), PunishModule.PunishType.MUTE, 20, "Satanismus", player.getUniqueId(), -1);
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    if (players.hasPermission("proxy.punish")) {
                        NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                        if(notifyModule.getNotifyModulePlayer() != 0) {
                            players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7wurde von " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wegen " + ReviveMCPlayer.getSecondColor() +"Satanismus §7bestraft."));
                        }
                    }
                }
                break;
            default:
                displayAllReasons(player, iCloudPlayer, ReviveMCPlayer);
        }
    }




    public void displayAllReasons(ProxiedPlayer player, ICloudPlayer iCloudPlayer, ReviveMCPlayer ReviveMCPlayer) {
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§8§m----------------------"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " "));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(1) §8| §7Hacking §8| " + ReviveMCPlayer.getSecondColor() +"Punish"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(2) §8| §7Trolling §8| " + ReviveMCPlayer.getSecondColor() +"Punish"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(3) §8| §7Report-Ausnutzung §8| " + ReviveMCPlayer.getSecondColor() +"Punish"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(4) §8| §7Skin/Name §8| " + ReviveMCPlayer.getSecondColor() +"Punish"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(5) §8| §7Teaming §8| " + ReviveMCPlayer.getSecondColor() +"Punish"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(6) §8| §7Bugusing §8| " + ReviveMCPlayer.getSecondColor() +"Punish"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(7) §8| §7Server-Crashing §8| " + ReviveMCPlayer.getSecondColor() +"Punish"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(8) §8| §7Hausverbot §8| " + ReviveMCPlayer.getSecondColor() +"Punish"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(9) §8| §7Bannumgehung §8| " + ReviveMCPlayer.getSecondColor() +"Punish"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " "));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(10) §8| §7Werbung §8| " + ReviveMCPlayer.getSecondColor() +"Mute"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(11) §8| §7Rassismus §8| " + ReviveMCPlayer.getSecondColor() +"Mute"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(12) §8| §7Religionsfeindlichkeit §8| " + ReviveMCPlayer.getSecondColor() +"Mute"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(13) §8| §7Nazismus §8| " + ReviveMCPlayer.getSecondColor() +"Mute"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(14) §8| §7Spam §8| " + ReviveMCPlayer.getSecondColor() +"Mute"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(15) §8| §7Beleidung §8| " + ReviveMCPlayer.getSecondColor() +"Mute"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(16) §8| §7Hassrede §8| " + ReviveMCPlayer.getSecondColor() +"Mute"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "(17) §8| §7Satanismus §8| " + ReviveMCPlayer.getSecondColor() +"Mute"));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + " "));
        player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§8§m----------------------"));
    }
}
