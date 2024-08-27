package de.revivemc.proxy.commands;

import de.revivemc.proxy.modules.notify.NotifyModule;
import de.revivemc.proxy.modules.uuid.NameFetcher;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import de.revivemc.proxy.modules.report.ReportModule;
import de.revivemc.proxy.modules.uuid.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ReportsCommand extends Command {

    public ReportsCommand(String name) {
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
        if (!player.hasPermission("proxy.report")) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDu hast keine Rechte auf diesen Befehl."));
            return;
        }

        final NotifyModule notifyModule =  new NotifyModule(player.getUniqueId());

        if (notifyModule.getNotifyModulePlayer() != 0) {
            if (args.length == 0) {
                player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
                player.sendMessage(TextComponent.fromLegacyText(prefix + "/reports list §8| §7Bekomme eine Liste aller aktiven Reports."));
                player.sendMessage(TextComponent.fromLegacyText(prefix + "/reports accept <Report-Id> §8| §7Nehme einen Report an."));
                player.sendMessage(TextComponent.fromLegacyText(prefix + "/reports finish <Report-Id> §8| §7Beende deinen Report."));
                player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
                return;
            }

            if (args[0].equals("list")) {
                try {
                    final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_reportarchiv WHERE Active='1'");

                    while (resultSet.next()) {
                        boolean bool = resultSet.wasNull();
                        if (bool) {
                            player.sendMessage(TextComponent.fromLegacyText(prefix + "Es ist derzeit kein Report aktiv."));
                        } else {
                            if (resultSet.getString("AcceptorUUID").equals("null")) {
                                TextComponent textComponent = new TextComponent(prefix + "§7" + NameFetcher.getName(UUID.fromString(resultSet.getString("UUID"))) + " §8| §a" + resultSet.getString("ReportId"));
                                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aReport annehmen").create()));
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports accept " + resultSet.getString("ReportId")));
                                player.sendMessage(textComponent);
                            } else {
                                player.sendMessage(TextComponent.fromLegacyText(prefix + "§7" + NameFetcher.getName(UUID.fromString(resultSet.getString("UUID"))) + " §8| §6" + resultSet.getString("ReportId") + " §7» §a" + NameFetcher.getName(UUID.fromString(resultSet.getString("AcceptorUUID")))));
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return;
            }

            if (args[0].equalsIgnoreCase("accept")) {
                if (args.length == 2) {
                    try {
                        final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_reportarchiv WHERE ReportId='" + args[1] + "'");
                        if (resultSet.next()) {
                            if (resultSet.getInt("Active") == 1) {
                                if (resultSet.getString("AcceptorUUID").equalsIgnoreCase("null")) {
                                    ProxySystem.getInstance().getDatabaseDriver().update("UPDATE revivemc_proxy_reportarchiv SET AcceptorUUID='" + player.getUniqueId() + "' WHERE ReportId='" + resultSet.getString("ReportId") + "'");
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + "§8§m---------------------------"));
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Gemeldeter Spieler §8» " + secondColor + NameFetcher.getName(UUID.fromString(resultSet.getString("UUID")))));
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Gemeldet von §8» " + secondColor + NameFetcher.getName(UUID.fromString(resultSet.getString("ReporterUUID")))));
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Grund §8» " + secondColor + resultSet.getString("Reason")));
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Server §8» " + secondColor + resultSet.getString("Server")));
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + "ReportId §8» " + secondColor + resultSet.getString("ReportId")));
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + " "));
                                    TextComponent textComponent = new TextComponent(prefix + "§c§lReport Abschließen.");
                                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c§lReport abschließen").create()));
                                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports finish " + resultSet.getString("ReportId")));
                                    player.sendMessage(textComponent);
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + "§8§m---------------------------"));
                                    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(resultSet.getString("Server"));
                                    player.connect(serverInfo);
                                } else {
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Report ist derzeit schon in Bearbeitung."));
                                }
                            } else {
                                player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Report ist nicht mehr aktiv."));
                            }
                        } else {
                            player.sendMessage(TextComponent.fromLegacyText(prefix + "Die ReportId " + secondColor + args[1] + " §7existiert nicht."));
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "/reports accept <Report-Id> §8| §7Nehme einen Report an."));
                }
            }

            if (args[0].equalsIgnoreCase("finish")) {
                if (args.length == 2) {
                    try {
                        final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM revivemc_proxy_reportarchiv WHERE ReportId='" + args[1] + "'");
                        if (resultSet.next()) {
                            if (resultSet.getInt("Active") == 1) {
                                if (resultSet.getString("AcceptorUUID").equalsIgnoreCase(player.getUniqueId().toString())) {
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast den Report mit der Id " + secondColor + resultSet.getString("ReportId") + " §7abgeschlossen."));
                                    ProxySystem.getInstance().getDatabaseDriver().update("UPDATE revivemc_proxy_reportarchiv SET Active='0' WHERE ReportId='" + resultSet.getString("ReportId") + "'");
                                } else {
                                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du kannst einen Report von einem anderem Teammitglied nicht schließen."));
                                }
                            } else {
                                player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Report ist nicht mehr aktiv."));
                            }
                        } else {
                            player.sendMessage(TextComponent.fromLegacyText(prefix + "Die ReportId " + secondColor + args[1] + " §7existiert nicht."));
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "/reports finish <Report-Id> §8| §7Beende deinen Report."));
                }
            }
        } else {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "§cDu befindest dich nicht im Notify System!"));
        }
    }
}
