package de.revivemc.proxy.listener.player;

import de.revivemc.proxy.commands.RulesCommand;
import de.revivemc.proxy.modules.friend.FriendModule;
import de.revivemc.proxy.modules.notify.NotifyModule;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import de.revivemc.proxy.modules.punish.PunishModule;
import de.revivemc.proxy.modules.tablist.TablistModule;
import io.netty.handler.proxy.ProxyConnectException;
import io.netty.handler.proxy.ProxyConnectionEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class PlayerConnectListener implements Listener {

    @EventHandler
    public void onPlayerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());

        if (ProxySystem.getInstance().isPlayerIPBanned(player.getAddress().getHostString())) {
            player.disconnect(TextComponent.fromLegacyText("Your IP has been permanently banned."));
            return;
        }


        ReviveMCPlayer ReviveMCPlayer = new ReviveMCPlayer(iCloudPlayer);
        assert iCloudPlayer != null;
        final String prefix = ProxySystem.getInstance().getPrefix(iCloudPlayer);
        final String secondcolor = ReviveMCPlayer.getSecondColor();
        ProxySystem.getInstance().createProxyPlayer(player.getUniqueId());
        final NotifyModule notifyModule = new NotifyModule(player.getUniqueId());
        if (Objects.equals(RulesCommand.hasPlayerRulesAccepted(player.getUniqueId()), "false")) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Akzeptiere das Regelwerk um auf diesen Server spielen zu können."));
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Regelwerk: https://ReviveMC.de/regelwerk"));
            final TextComponent accept = new TextComponent(prefix + "§aAktzeptieren");
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAktzeptieren").create()));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rules rulesAcceptId"));
            player.sendMessage(accept);
            final TextComponent deny = new TextComponent(prefix + "§cAblehnen");
            deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cAblehnen").create()));
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rules rulesDenyId"));
            player.sendMessage(deny);
            return;
        }

        if(player.hasPermission("proxy.notify")) {
            notifyModule.insertNotifyDatabase();
            if (notifyModule.getNotifyModulePlayer() == 3) {
                notifyModule.updateNotifyModulePlayer(1);
                player.sendMessage(TextComponent.fromLegacyText(prefix + "§aDu wurdest ins Notify System eingeloggt."));
            }
        }
        if (player.hasPermission("proxy.report")) {
            if (notifyModule.getNotifyModulePlayer() != 0) {
                if (getReportsAsInteger() > 1) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Es sind derzeit " + secondcolor + getReportsAsInteger() + " §7Reports offen."));
                } else if (getReportsAsInteger() == 1) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Es ist derzeit " + secondcolor + getReportsAsInteger() + " §7Report offen."));
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Es sind derzeit " + secondcolor + getReportsAsInteger() + " §7Reports offen."));
                }
            }
        }

        PunishModule punishModule = new PunishModule();
        if (punishModule.isPunished(player.getUniqueId())) {
            long current = System.currentTimeMillis();
            long end = Objects.requireNonNull(punishModule.getPunishEnd(player.getUniqueId()));
            if (((current < end ? 1 : 0) | (end == -1L ? 1 : 0)) != 0) {
                event.setCancelled(true);
                player.disconnect(TextComponent.fromLegacyText("§8§l» §cCytura.net §8§l« \n\n §7Du wurdest vom Netzwerk gebannt. \n\n §7Grund: §c§l" + punishModule.getPunishReason(player.getUniqueId()) + " \n §7Verbleibende Zeit: §c" + punishModule.getRemainingPunishTime(player.getUniqueId()) + " \n\n §7Solltest du dies als Ungerecht finden, \n §7dann schreibe einen Entbannungsantrag in unserem Forum. \n\n §7Forum: §chttps://Cytura.net"));
            } else {
                punishModule.pardon(player.getUniqueId());
                for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    if (all.hasPermission("proxy.punish")) {
                        all.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Der Spieler " + ReviveMCPlayer.getSecondColor() + player.getName() + " §7wurde von der Cloud entbannt."));
                    }
                }
            }
        }
        new TablistModule().buildTablist();
    }

    private int getReportsAsInteger() {
        int i = 0;
        try {
            final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_reportarchiv WHERE Active='1'");
            while (resultSet.next()) {
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return i;
    }

    public void clearChat(ProxiedPlayer player) {
        for (int i = 0; i < 200; i++) {
            player.sendMessage(TextComponent.fromLegacyText(" "));
        }
    }
}
