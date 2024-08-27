package de.revivemc.proxy.commands;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClearChatCommand extends Command {
    public ClearChatCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        assert iCloudPlayer != null;
        String prefix = ProxySystem.getInstance().getPrefix(iCloudPlayer);
        if(!player.hasPermission("proxy.clearchat")) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDu hast keine Rechte auf diesen Befehl."));
            return;
        }

        for (ProxiedPlayer players1 : ProxyServer.getInstance().getPlayers()) {
            if (!players1.hasPermission("proxy.team")) {
                for(int i = 0; i < 100; i++) {
                    ProxyServer.getInstance().getPlayers().forEach(players -> {
                        players.sendMessage(TextComponent.fromLegacyText(" "));
                    });
                }
                ProxyServer.getInstance().getPlayers().forEach(players -> {
                    players.sendMessage(TextComponent.fromLegacyText(prefix + "Der Chat wurde geleert."));
                });
            }
        }
        player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast den Chat geleert."));
    }
}
