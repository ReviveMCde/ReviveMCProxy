package de.revivemc.proxy.commands;

import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import de.revivemc.proxy.modules.rang.RangModule;
import de.revivemc.proxy.modules.uuid.UUIDFetcher;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import eu.thesimplecloud.module.permission.PermissionPool;
import eu.thesimplecloud.module.permission.player.IPermissionPlayer;
import eu.thesimplecloud.module.permission.player.PlayerPermissionGroupInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import javax.xml.soap.Text;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RangCommand extends Command {


    public RangCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer cloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        final ReviveMCPlayer reviveMCPlayer = new ReviveMCPlayer(cloudPlayer);
        assert cloudPlayer != null;
        final String prefix = ProxySystem.getInstance().getPrefix(cloudPlayer);
        final String secondColor = reviveMCPlayer.getSecondColor();

        if (!player.hasPermission("proxy.rang")) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "&cDu hast keine Berechtigungen diesen Command auszuführen!"));
            return;
        }

        if (args.length != 5) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: 'rang set (Spieler) (Rang) (Grund) (Bewerbungs-ID)'"));
            return;
        }


        if (args[0].equalsIgnoreCase("set")) {
            if (!player.hasPermission("proxy.rang.admin")) {
                if (args[2].equalsIgnoreCase("Moderator") ||
                     args[2].equalsIgnoreCase("Content") ||
                     args[2].equalsIgnoreCase("Supporter") ||
                     args[2].equalsIgnoreCase("Builder") ||
                     args[2].equalsIgnoreCase("Spieler")) {

                    if (args[3].equalsIgnoreCase("Uprank") ||
                            args[3].equalsIgnoreCase("Teamkick") ||
                            args[3].equalsIgnoreCase("Bewerbung") ||
                            args[3].equalsIgnoreCase("Downrank")) {
                        if (!Objects.equals(args[4], "-")) {
                            player.sendMessage(TextComponent.fromLegacyText(prefix + "Gib noch eine gültige Bewerbungs-ID an! Bei Uprank/Downrank/Teamkick gib '-' ein."));
                            player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: 'rang set (Spieler) (Rang) Bewerbung (Bewerbungs-ID)'"));
                        } else {
                            if (args[1].equalsIgnoreCase("leTobiCW") || args[1].equalsIgnoreCase("leTobiBW")) {
                                player.sendMessage(TextComponent.fromLegacyText(prefix + "Du darfst den Rang von diesem Spieler nicht verändern!"));
                                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                                    if (players.hasPermission("proxy.rang.admin")) {
                                        players.sendMessage(TextComponent.fromLegacyText(prefix + "Das Teammitglied " + secondColor + player.getName() + "§7 hat versucht den Rang von dem Spieler " + secondColor + args[1] + "§7 zu verändern!"));
                                    }
                                }
                                return;
                            }
                            final UUID target = UUIDFetcher.getUUID(args[1]);
                            assert target != null;
                            RangModule.updateRangArchiv(player.getUniqueId(), target, args[2], args[3], args[4]);
                            final IPermissionPlayer permissionPlayer = PermissionPool.getInstance().getPermissionPlayerManager().getCachedPermissionPlayer(target);
                            assert permissionPlayer != null;
                            permissionPlayer.clearGroups();
                            permissionPlayer.addPermissionGroup(new PlayerPermissionGroupInfo(args[2], -1));
                            permissionPlayer.update().sync();
                            player.sendMessage(TextComponent.fromLegacyText(prefix + "Der Spieler " + secondColor + args[1] + "§7 hat den Rang " + secondColor + args[2] + " §aerfolgreich §7erhalten!"));
                            try {
                                ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(args[1]);
                                if (targetPlayer != null) {
                                    targetPlayer.disconnect(TextComponent.fromLegacyText("§8§l» §cReviveMC.de §8§l« \n\n §7Dein Rang hat sich geändert."));
                                }
                                return;
                            }catch (Exception ignored) {}
                            return;
                        }
                    } else {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Gib einen gültigen Grund an: Uprank, Teamkick, Bewerbung, Downrank"));
                        return;
                    }
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du kannst folgende Ränge vergeben: Moderator, Content, Supporter, Builder, Spieler"));
                    return;
                }
            }

            final UUID target = UUIDFetcher.getUUID(args[1]);
            assert target != null;
            RangModule.updateRangArchiv(player.getUniqueId(), target, args[2], args[3], args[4]);
            final IPermissionPlayer permissionPlayer = PermissionPool.getInstance().getPermissionPlayerManager().getCachedPermissionPlayer(target);
            assert permissionPlayer != null;
            permissionPlayer.clearGroups();
            permissionPlayer.addPermissionGroup(new PlayerPermissionGroupInfo(args[2], -1));
            permissionPlayer.update().sync();
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Der Spieler " + secondColor + args[1] + "§7 hat den Rang " + secondColor + args[2] + " §aerfolgreich §7erhalten!"));
            try {
                ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(args[1]);
                if (targetPlayer != null) {
                    targetPlayer.disconnect(TextComponent.fromLegacyText("§8§l» §cReviveMC.de §8§l« \n\n §7Dein Rang hat sich geändert."));
                }
            }catch (Exception ignored) {}
        }
    }
}
