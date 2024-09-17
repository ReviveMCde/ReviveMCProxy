package de.revivemc.proxy.commands;

import de.revivemc.proxy.modules.uuid.UUIDFetcher;
import eu.thesimplecloud.module.permission.PermissionPool;
import eu.thesimplecloud.module.permission.player.IPermissionPlayer;
import eu.thesimplecloud.module.permission.player.PlayerPermissionGroupInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ShopRangCommand extends Command {


    public ShopRangCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            final ProxiedPlayer player = (ProxiedPlayer) commandSender;
            player.sendMessage(TextComponent.fromLegacyText("§cDieser Befehl ist nur möglich in der Konsole auszuführen!"));
            return;
        }

        final UUID target = UUIDFetcher.getUUID(args[1]);
        assert target != null;

        try {
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length == 3) {
                    final IPermissionPlayer permissionPlayer = PermissionPool.getInstance().getPermissionPlayerManager().getCachedPermissionPlayer(target);
                    assert permissionPlayer != null;
                    permissionPlayer.clearGroups();
                    permissionPlayer.addPermissionGroup(new PlayerPermissionGroupInfo(args[2], -1));
                    permissionPlayer.update().sync();
                    commandSender.sendMessage(TextComponent.fromLegacyText("Group Updated!"));
                    for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                        if (players.hasPermission("proxy.shopupdates")) {
                            players.sendMessage(TextComponent.fromLegacyText("§aShop §8» §7Der Spieler " + args[1] + " §7hat sich den Rang §b" + args[2] + " §7Lifetime gekauft."));
                        }

                        try {
                            ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(target);

                            targetPlayer.disconnect(TextComponent.fromLegacyText("§8» §aShop §8« \n\n §7Danke für deinen Einkauf in unserem Shop :) \n\n §7Du erhälst folgenden Rang: §a" + args[2] + " \n\n §7Wir wünschen dir Viel Spaß mit den Vorteilen deines Ranges. \n\n §bReviveMC.de"));
                        }catch (Exception exception) {
                            return;
                        }
                    }
                    return;
                }

                if (args.length == 4) {
                    final IPermissionPlayer permissionPlayer = PermissionPool.getInstance().getPermissionPlayerManager().getCachedPermissionPlayer(target);
                    assert permissionPlayer != null;
                    final long timeout = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Long.parseLong(args[3]));
                    permissionPlayer.clearGroups();
                    permissionPlayer.addPermissionGroup(new PlayerPermissionGroupInfo(args[2], timeout));
                    permissionPlayer.update().sync();
                    commandSender.sendMessage(TextComponent.fromLegacyText("Group Updated!"));
                    for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                        if (players.hasPermission("proxy.shopupdates")) {
                            players.sendMessage(TextComponent.fromLegacyText("§aShop §8» §7Der Spieler " + args[1] + " §7hat sich den Rang §b" + args[2] + " §7für §b" + args[3] + " §7Tage gekauft."));
                        }

                        try {
                            ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(target);

                            targetPlayer.disconnect(TextComponent.fromLegacyText("§8» §aShop §8« \n\n §7Danke für deinen Einkauf in unserem Shop :) \n\n §7Du erhälst folgenden Rang: §a" + args[2] + " \n §7Laufzeit deines Ranges: §a" + args[3] + " §7Tage \n\n §7Wir wünschen dir Viel Spaß mit den Vorteilen deines Ranges. \n\n §bReviveMC.de"));
                        }catch (Exception exception) {
                            return;
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            commandSender.sendMessage(TextComponent.fromLegacyText("Falsche Command Parameter!"));
        }

    }
}
