package de.revivemc.proxy.commands;

import de.revivemc.proxy.modules.notify.NotifyModule;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.team.TeamModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import sun.security.krb5.internal.APRep;

public class TeamChatCommand extends Command {
    public TeamChatCommand(String name) {
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
        if (!player.hasPermission("proxy.team")) {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§cDu hast keine Rechte auf diesen Befehl."));
            return;
        }

        final NotifyModule notifyModule = new NotifyModule(player.getUniqueId());

        if (notifyModule.getNotifyModulePlayer() != 0) {
            if(args.length == 0) {
                player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "Verwende: 'tc (message)'"));
                return;
            }

            TeamModule team_chat = new TeamModule();
            team_chat.sendMessage(player, iCloudPlayer, args);
        } else {
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(iCloudPlayer) + "§cDu befindest dich nicht im Notify System!"));
        }
    }
}
