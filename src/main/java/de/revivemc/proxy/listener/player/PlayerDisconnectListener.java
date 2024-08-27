package de.revivemc.proxy.listener.player;

import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.notify.NotifyModule;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.ICloudAPI;
import eu.thesimplecloud.api.event.player.CloudPlayerDisconnectEvent;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.modules.tablist.TablistModule;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void onPlayerDisconnect(ServerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ICloudPlayer cloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        final NotifyModule notifyModule = new NotifyModule(player.getUniqueId());
        if(ProxyServer.getInstance().getPlayer(player.getUniqueId()) == null) {
            notifyModule.updateNotifyModulePlayer(3);
        }
        new TablistModule().buildTablist();
    }
}
