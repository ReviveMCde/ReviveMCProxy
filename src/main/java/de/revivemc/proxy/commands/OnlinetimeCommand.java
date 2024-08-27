package de.revivemc.proxy.commands;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.onlinetime.OnlinePlayer;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnlinetimeCommand extends Command {
    final ExecutorService executorService;

    public OnlinetimeCommand(String name) {
        super(name);
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer cloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        final long[] time = new long[1];
        final Long[] hours = new Long[1];
        final Long[] minT = new Long[1];
        final Long[] min = new Long[1];
        this.executorService.execute(() -> {
            time[0] = OnlinePlayer.getOnlinePlayer(player.getUniqueId()).getCurrentTime();
            hours[0] = time[0] / 3600000L;
            minT[0] = time[0] - hours[0] * 3600000L;
            min[0] = minT[0] / 60000L;
            player.sendMessage(TextComponent.fromLegacyText(ProxySystem.getInstance().getPrefix(cloudPlayer) + "Deine Spielzeit beträgt " + new ReviveMCPlayer(cloudPlayer).getSecondColor() + hours[0] + "h " + min[0] + "min"));
        });
    }
}
