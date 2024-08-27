package de.revivemc.proxy.modules.team;

import de.revivemc.proxy.modules.notify.NotifyModule;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TeamModule {

    public void sendMessage(ProxiedPlayer player, ICloudPlayer iCloudPlayer, String[] message) {
        StringBuilder msg = new StringBuilder();
        for (String i : message) {
            msg.append(i).append(" ");
        }
        for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
            if (players.hasPermission("proxy.team")) {
                NotifyModule notifyModule = new NotifyModule(players.getUniqueId());
                if (notifyModule.getNotifyModulePlayer() != 0) {
                    players.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + player.getName() + " §8» §7" + msg));
                }
            }
        }
    }
}
