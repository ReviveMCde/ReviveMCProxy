package de.revivemc.proxy.listener.player;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import de.revivemc.proxy.modules.punish.PunishModule;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerChatListener implements Listener {
    @EventHandler
    public void onMessage(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        final ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        final ReviveMCPlayer ReviveMCPlayer = new ReviveMCPlayer(iCloudPlayer);
        assert iCloudPlayer != null;

        PunishModule punishModule = new PunishModule();
        if (punishModule.isPunished(player.getUniqueId())) {
            long current = System.currentTimeMillis();
            long end = punishModule.getPunishEnd(player.getUniqueId());
            if (((current < end ? 1 : 0) | (end == -1L ? 1 : 0)) != 0) {
                event.setCancelled(true);
                player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§7Du wurdest gemuted§8. §7Grund§8: " + ReviveMCPlayer.getSecondColor() + punishModule.getPunishReason(player.getUniqueId()) + "\n " + ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§7Verbleibende Zeit: " + ReviveMCPlayer.getSecondColor() + punishModule.getRemainingPunishTime(player.getUniqueId())));
            } else {
                punishModule.pardon(player.getUniqueId());
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    if (all.hasPermission("proxy.mute")) {
                        all.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wurde von der Cloud entmutet."));
                    }
                }
            }
        }

        if (!player.hasPermission("proxy.chatfilter.bypass")) {
            for (String all : ProxySystem.getInstance().getWords()) {
                if (event.getMessage().contains(all) && !event.getMessage().startsWith("/")) {
                    event.setCancelled(true);
                    player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Bitte achte auf deine Wortwahl (ಠ_ಠ)."));
                }
            }
        }

        if (!player.hasPermission("proxy.chatfilter.cmd.bypass")) {
            for (String all : ProxySystem.getInstance().getCmdblock()) {
                if (event.getMessage().startsWith("/") && event.getMessage().contains(all)) {
                    event.setCancelled(true);
                    player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Dieser Befehl ist nicht im System vorhanden!"));
                }
            }
        }
    }
}
