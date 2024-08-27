package de.revivemc.proxy.modules.tablist;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TablistModule {

    public TablistModule() {

    }

    public void buildTablist() {
        for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
            final String firstcolor = new ReviveMCPlayer(iCloudPlayer).getFirstColor();
            final String secondColor = new ReviveMCPlayer(iCloudPlayer).getSecondColor();
            final String header = "§8 \n " + firstcolor + "ReviveMC.de §8× §7your " + secondColor + "oldschool minecraft §7network \n §8▶ §7Du befindest dich derzeit auf " + secondColor + player.getServer().getInfo().getName() + " §8◀ \n §8";
            final String footer = "§8 \n §7Website §8» " + secondColor + "ReviveMC.de \n §7Twitter §8» " + secondColor + "@ReviveMCde \n §7Powered by §8» " + secondColor + "Venocix.de \n §8";
            player.setTabHeader(new TextComponent(header), new TextComponent(footer));
        }
    }
}
