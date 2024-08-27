package de.revivemc.proxy.commands;

import de.revivemc.proxy.modules.notify.NotifyModule;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import de.revivemc.proxy.ProxySystem;
import de.revivemc.proxy.modules.player.ReviveMCPlayer;
import de.revivemc.proxy.modules.report.ReportModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;

public class ReportCommand extends Command {

    public ReportCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;
        final ICloudPlayer cloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId());
        assert cloudPlayer != null;
        final String prefix = ProxySystem.getInstance().getPrefix(cloudPlayer);
        final String secondColor = new ReviveMCPlayer(cloudPlayer).getSecondColor();

        if (args.length != 2) {
            player.sendMessage(TextComponent.fromLegacyText(prefix + "Verwende 'report (Name) (Grund)'"));
            return;
        }

        switch (args[1]) {
            case "Hacking":
                try {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    ReportModule reportModule = new ReportModule(target.getUniqueId());

                    if (target.getName().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Du kannst dich nicht selber Reporten."));
                        return;
                    }

                    if (reportModule.getReportStatus() == 1) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Spieler wurde bereits gemeldet."));
                        return;
                    }

                    reportModule.addUUIDToList(player.getUniqueId(), "Hacking", target.getServer().getInfo().getName());
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast den Spieler " + secondColor + args[0] + " §7erfolgreich gemeldet."));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Deine Report-Id: " + secondColor + reportModule.getReportId()));
                    for (ProxiedPlayer team : ProxyServer.getInstance().getPlayers()) {
                        if (team.hasPermission("proxy.report")) {
                            NotifyModule notifyModule = new NotifyModule(team.getUniqueId());
                            if (notifyModule.getNotifyModulePlayer() != 0) {
                                TextComponent textComponent = new TextComponent(prefix + "Es wurde ein Spieler gemeldet (" + reportModule.getActiveReports() + ").");
                                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reports anschauen").create()));
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports list "));
                                team.sendMessage(textComponent);
                            }
                        }
                    }
                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Der Spieler " + secondColor + args[0] + " §7befindet sich nicht auf dem Netzwerk."));
                }
                break;

            case "Teaming":
                try {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    ReportModule reportModule = new ReportModule(target.getUniqueId());

                    if (target.getName().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Du kannst dich nicht selber Reporten."));
                        return;
                    }

                    if (reportModule.getReportStatus() == 1) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Spieler wurde bereits gemeldet."));
                        return;
                    }

                    reportModule.addUUIDToList(player.getUniqueId(), "Teaming", target.getServer().getInfo().getName());
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast den Spieler " + secondColor + args[0] + " §7erfolgreich gemeldet."));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Deine Report-Id: " + secondColor + reportModule.getReportId()));
                    for (ProxiedPlayer team : ProxyServer.getInstance().getPlayers()) {
                        if (team.hasPermission("proxy.report")) {
                            NotifyModule notifyModule = new NotifyModule(team.getUniqueId());
                            if (notifyModule.getNotifyModulePlayer() != 0) {
                                TextComponent textComponent = new TextComponent(prefix + "Es wurde ein Spieler gemeldet (" + reportModule.getActiveReports() + ").");
                                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reports anschauen").create()));
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports list "));
                                team.sendMessage(textComponent);
                            }
                        }
                    }
                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Der Spieler " + secondColor + args[0] + " §7befindet sich nicht auf dem Netzwerk."));
                }
                break;

            case "Bugusing":
                try {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    ReportModule reportModule = new ReportModule(target.getUniqueId());

                    if (target.getName().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Du kannst dich nicht selber Reporten."));
                        return;
                    }

                    if (reportModule.getReportStatus() == 1) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Spieler wurde bereits gemeldet."));
                        return;
                    }

                    reportModule.addUUIDToList(player.getUniqueId(), "Bugusing", target.getServer().getInfo().getName());
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast den Spieler " + secondColor + args[0] + " §7erfolgreich gemeldet."));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Deine Report-Id: " + secondColor + reportModule.getReportId()));
                    for (ProxiedPlayer team : ProxyServer.getInstance().getPlayers()) {
                        if (team.hasPermission("proxy.report")) {
                            NotifyModule notifyModule = new NotifyModule(team.getUniqueId());
                            if (notifyModule.getNotifyModulePlayer() != 0) {
                                TextComponent textComponent = new TextComponent(prefix + "Es wurde ein Spieler gemeldet (" + reportModule.getActiveReports() + ").");
                                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reports anschauen").create()));
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports list "));
                                team.sendMessage(textComponent);
                            }
                        }
                    }
                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Der Spieler " + secondColor + args[0] + " §7befindet sich nicht auf dem Netzwerk."));
                }

                break;

            case "Skin/Name":
                try {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    ReportModule reportModule = new ReportModule(target.getUniqueId());

                    if (target.getName().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Du kannst dich nicht selber Reporten."));
                        return;
                    }

                    if (reportModule.getReportStatus() == 1) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Spieler wurde bereits gemeldet."));
                        return;
                    }

                    reportModule.addUUIDToList(player.getUniqueId(), "Skin/Name", target.getServer().getInfo().getName());
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast den Spieler " + secondColor + args[0] + " §7erfolgreich gemeldet."));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Deine Report-Id: " + secondColor + reportModule.getReportId()));
                    for (ProxiedPlayer team : ProxyServer.getInstance().getPlayers()) {
                        if (team.hasPermission("proxy.report")) {
                            NotifyModule notifyModule = new NotifyModule(team.getUniqueId());
                            if (notifyModule.getNotifyModulePlayer() != 0) {
                                TextComponent textComponent = new TextComponent(prefix + "Es wurde ein Spieler gemeldet (" + reportModule.getActiveReports() + ").");
                                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reports anschauen").create()));
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports list "));
                                team.sendMessage(textComponent);
                            }
                        }
                    }
                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Der Spieler " + secondColor + args[0] + " §7befindet sich nicht auf dem Netzwerk."));
                }
                break;

            case "Werbung":
                try {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    ReportModule reportModule = new ReportModule(target.getUniqueId());

                    if (target.getName().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Du kannst dich nicht selber Reporten."));
                        return;
                    }

                    if (reportModule.getReportStatus() == 1) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Spieler wurde bereits gemeldet."));
                        return;
                    }

                    reportModule.addUUIDToList(player.getUniqueId(), "Werbung", target.getServer().getInfo().getName());
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast den Spieler " + secondColor + args[0] + " §7erfolgreich gemeldet."));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Deine Report-Id: " + secondColor + reportModule.getReportId()));
                    for (ProxiedPlayer team : ProxyServer.getInstance().getPlayers()) {
                        if (team.hasPermission("proxy.report")) {
                            NotifyModule notifyModule = new NotifyModule(team.getUniqueId());
                            if (notifyModule.getNotifyModulePlayer() != 0) {
                                TextComponent textComponent = new TextComponent(prefix + "Es wurde ein Spieler gemeldet (" + reportModule.getActiveReports() + ").");
                                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reports anschauen").create()));
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports list "));
                                team.sendMessage(textComponent);
                            }
                        }
                    }
                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Der Spieler " + secondColor + args[0] + " §7befindet sich nicht auf dem Netzwerk."));
                }

                break;

            case "Chatverhalten":
                try {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    ReportModule reportModule = new ReportModule(target.getUniqueId());

                    if (target.getName().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Du kannst dich nicht selber Reporten."));
                        return;
                    }

                    if (reportModule.getReportStatus() == 1) {
                        player.sendMessage(TextComponent.fromLegacyText(prefix + "Dieser Spieler wurde bereits gemeldet."));
                        return;
                    }

                    reportModule.addUUIDToList(player.getUniqueId(), "Chatverhalten", target.getServer().getInfo().getName());
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Du hast den Spieler " + secondColor + args[0] + " §7erfolgreich gemeldet."));
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Deine Report-Id: " + secondColor + reportModule.getReportId()));
                    for (ProxiedPlayer team : ProxyServer.getInstance().getPlayers()) {
                        if (team.hasPermission("proxy.report")) {
                            NotifyModule notifyModule = new NotifyModule(team.getUniqueId());
                            if (notifyModule.getNotifyModulePlayer() != 0) {
                                TextComponent textComponent = new TextComponent(prefix + "Es wurde ein Spieler gemeldet (" + reportModule.getActiveReports() + ").");
                                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reports anschauen").create()));
                                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports list "));
                                team.sendMessage(textComponent);
                            }
                        }
                    }
                }catch (Exception ex) {
                    player.sendMessage(TextComponent.fromLegacyText(prefix + "Der Spieler " + secondColor + args[0] + " §7befindet sich nicht auf dem Netzwerk."));
                }

                break;

            default:
                player.sendMessage(TextComponent.fromLegacyText(prefix + "Folgende Gründe existieren: Hacking, Teaming, Bugusing, Skin/Name, Werbung, Chatverhalten"));
        }
    }
}
