package de.revivemc.proxy.modules.onlinetime.listener;

import de.revivemc.proxy.modules.onlinetime.OnlinePlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnlineTimeDisconnectListener implements Listener {

    @EventHandler
    public void onDisconnect(final PlayerDisconnectEvent event) {
        final Thread thread = new Thread(() -> OnlinePlayer.getOnlinePlayer(event.getPlayer().getUniqueId()).removeCache());
        thread.start();
    }
}
