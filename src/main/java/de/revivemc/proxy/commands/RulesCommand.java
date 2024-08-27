package de.revivemc.proxy.commands;

import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class RulesCommand extends Command {


    public RulesCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer cloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        final ReviveMCPlayer reviveMCPlayer = new ReviveMCPlayer(cloudPlayer);
        assert cloudPlayer != null;
        final String prefix = ProxySystem.getInstance().getPrefix(cloudPlayer);
        if (args.length == 0) {
            player.sendMessage(TextComponent.fromLegacyText("Unknown command. Type \"/help\" for help."));
            return;
        }


        if (args[0].equalsIgnoreCase("rulesAcceptID")) {
            if (Objects.equals(hasPlayerRulesAccepted(player.getUniqueId()), "false")) {
                updatePlayerRules(player.getUniqueId());
                player.disconnect(TextComponent.fromLegacyText("§aDu hast das Regelwerk aktzeptiert."));
                return;
            }
            return;
        }

        if (args[0].equalsIgnoreCase("rulesDenyID")) {
            if (Objects.equals(hasPlayerRulesAccepted(player.getUniqueId()), "false")) {
                player.disconnect(TextComponent.fromLegacyText("§cDu musst das Regelwerk akzeptieren um auf dem Server spielen zu können."));
            }
        }
    }

    public void updatePlayerRules(UUID uuid) {
        ProxySystem.getInstance().getDatabaseDriver().update("UPDATE revivemc_lobby_player SET rules = 'true' WHERE UUID= '" + uuid + "';");
    }

    public static String hasPlayerRulesAccepted(UUID uuid) {
        try {
            ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_lobby_player` WHERE  `UUID` = '" + uuid.toString() + "'");
            if (resultSet.next()) {
                return resultSet.getString("rules");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
