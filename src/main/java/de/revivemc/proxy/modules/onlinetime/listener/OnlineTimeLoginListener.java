package de.revivemc.proxy.modules.onlinetime.listener;

import de.revivemc.proxy.modules.onlinetime.OnlinePlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnlineTimeLoginListener implements Listener {

    @EventHandler
    public void onLogin(final LoginEvent event) {
        final Thread thread = new Thread(() -> new OnlinePlayer(event.getConnection().getUniqueId()));
        thread.start();
    }
}
