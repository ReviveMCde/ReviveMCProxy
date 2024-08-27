package de.revivemc.proxy.commands;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Objects;

public class CoinsCommand extends Command {
    public CoinsCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ICloudPlayer cloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        assert cloudPlayer != null;
        final String prefix = ProxySystem.getInstance().getPrefix(cloudPlayer);
        final String secondColor = new ReviveMCPlayer(cloudPlayer).getSecondColor();
        final int coins = (int) Objects.requireNonNull(cloudPlayer.getProperty("coins")).getValue();
        player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast " + secondColor + coins + " §7Coins."));
    }
}
