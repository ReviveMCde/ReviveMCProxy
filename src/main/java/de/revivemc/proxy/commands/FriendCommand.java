package de.revivemc.proxy.commands;

import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.friend.FriendModule;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import de.revivemc.proxy.modules.uuid.NameFetcher;
import de.revivemc.proxy.modules.uuid.UUIDFetcher;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class FriendCommand extends Command {

    public FriendCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        assert iCloudPlayer != null;
        final ReviveMCPlayer reviveMCPlayer = new ReviveMCPlayer(iCloudPlayer);
        final String firstcolor = reviveMCPlayer.getFirstColor();
        final String secondcolor = reviveMCPlayer.getSecondColor();
        final String prefix = ProxySystem.getInstance().getPrefix(iCloudPlayer);
        final FriendModule friendModule = new FriendModule(player.getUniqueId());

        if (args.length == 0) {
            displayAllOptions(player, prefix, secondcolor);
        }

        if (args[0].equalsIgnoreCase("get")) {

            String[] friendsUUID = friendModule.getFriendsArray();
            for (String friendUUID : friendsUUID) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Name: " + NameFetcher.getName(friendUUID)));
            }
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 2) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: '/friend add (Name)'"));
                return;
            }

            final UUID targetUUID = UUIDFetcher.getUUID(args[1]);
            if (targetUUID == null) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDieser Spieler existiert nicht in unserer Datenbank."));
                return;
            }


            if (!friendModule.playerExists(targetUUID)) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDieser Spieler existiert nicht in unserer Datenbank."));
                return;
            }

            if (friendModule.isFriend(targetUUID)) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Du bist bereits mit diesem Spieler befreundet."));
                return;
            }


            friendModule.addFriend(targetUUID);
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length != 2) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: '/friend remove (Name)'"));
                return;
            }

            final UUID targetUUID = UUIDFetcher.getUUID(args[1]);
            if (targetUUID == null) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDieser Spieler existiert nicht in unserer Datenbank."));
                return;
            }


            if (!friendModule.playerExists(targetUUID)) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDieser Spieler existiert nicht in unserer Datenbank."));
                return;
            }

            if (!friendModule.isFriend(targetUUID)) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Du bist nicht mit diesem Spieler befreundet."));
                return;
            }


            friendModule.removeFriend(targetUUID);
        }

        if (args[0].equalsIgnoreCase("requests")) {
            if (args.length > 1) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: '/friend requests'"));
                return;
            }

            //Liste aller Freundschaftsanfragen
        }

        if (args[0].equalsIgnoreCase("accept")) {
            if (args.length != 2) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: '/friend accept (Name)'"));
                return;
            }


            final UUID targetUUID = UUIDFetcher.getUUID(args[1]);
            if (targetUUID == null) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDieser Spieler existiert nicht in unserer Datenbank."));
                return;
            }


            if (!friendModule.playerExists(targetUUID)) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDieser Spieler existiert nicht in unserer Datenbank."));
                return;
            }

            if (!friendModule.isInRequest(targetUUID)) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Spieler hat dir keine Freundschaftsanfrage geschickt."));
                return;
            }

            friendModule.acceptFriend(targetUUID);
        }

        if (args[0].equalsIgnoreCase("deny")) {
            if (args.length != 2) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: '/friend deny (Name)'"));
                return;
            }

            final UUID targetUUID = UUIDFetcher.getUUID(args[1]);
            if (targetUUID == null) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDieser Spieler existiert nicht in unserer Datenbank."));
                return;
            }


            if (!friendModule.playerExists(targetUUID)) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDieser Spieler existiert nicht in unserer Datenbank."));
                return;
            }

            if (!friendModule.isInRequest(targetUUID)) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Spieler hat dir keine Freundschaftsanfrage geschickt."));
                return;
            }

            friendModule.denyFriend(targetUUID);
        }

        if (args[0].equalsIgnoreCase("tp")) {
            if (args.length != 2) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende: '/friend tp (Name)'"));
                return;
            }

            final UUID targetUUID = UUIDFetcher.getUUID(args[1]);
            if (targetUUID == null) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDieser Spieler existiert nicht in unserer Datenbank."));
                return;
            }


            if (!friendModule.playerExists(targetUUID)) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDieser Spieler existiert nicht in unserer Datenbank."));
                return;
            }

            if (!friendModule.isFriend(targetUUID)) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDu bist nicht mit diesem Spieler befreundet."));
                return;
            }

            for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                if (!players.getUniqueId().equals(targetUUID)) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Spieler befindet sich nicht auf dem Netzwerk."));
                    return;
                }

                player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast dich zu " + secondcolor + args[1] + " §7teleportiert."));
                final ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(players.getServer().getInfo().toString());
                player.connect(serverInfo);
                return;
            }
        }
    }

    private void displayAllOptions(ProxiedPlayer player, String prefix, String secondcolor) {
        player.sendMessage(TextComponent.fromLegacyText(prefix + "§8§m--------------------------"));
        player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
        player.sendMessage(TextComponent.fromLegacyText(prefix + "/friend add (Name) §8|" + secondcolor + "Versende eine Freundschaftsanfrage."));
        player.sendMessage(TextComponent.fromLegacyText(prefix + "/friend remove (Name) §8| " + secondcolor + "Entferne einen vorhandenen Freund."));
        player.sendMessage(TextComponent.fromLegacyText(prefix + "/friend requests §8| " + secondcolor + "Siehe alle Freundschaftsanfragen."));
        player.sendMessage(TextComponent.fromLegacyText(prefix + "/friend accept/deny (Name) §8| " + secondcolor + "Akzeptiere/Lehne eine Freundschaftanfrage an/ab."));
        player.sendMessage(TextComponent.fromLegacyText(prefix + "/friend tp (Name) §8| " + secondcolor + "Teleportiere dich zu einem Freund."));
        player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
        player.sendMessage(TextComponent.fromLegacyText(prefix + "§8§m--------------------------"));
    }
}
