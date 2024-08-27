package de.revivemc.proxy.commands;

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

public class PardonCommand extends Command {
    public PardonCommand(String name) {
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
        if(!player.hasPermission("proxy.pardon")) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§cDu hast keine Rechte auf diesen Befehl."));
            return;
        }

        if(args.length != 1) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Verwende: 'pardon (name)'"));
            return;
        }

        String target = args[0];
        PunishModule punishModule = new PunishModule();
        if(!punishModule.isPunished(UUIDFetcher.getUUID(target))) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§cDer Spieler hat keine aktive Bestrafung."));
            return;
        }

        punishModule.pardon(UUIDFetcher.getUUID(target));
        for(ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
            if(players.hasPermission("proxy.pardon")) {
                players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Das Teammitglied " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7hat alle Bestrafungen von dem Spieler " + ReviveMCPlayer.getSecondColor() + args[0] + " §7entfernt."));
            }
        }
    }
}
