package de.revivemc.proxy.modules.report;

import de.revivemc.proxy.ProxySystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ReportModule {

    private final UUID uuid;

    public ReportModule(UUID uuid) {
        this.uuid = uuid;
    }

    //UUID varchar(64), ReporterUUID varchar(64), ReportId varchar(32), Reason varchar(32), Server varchar(16), Active BIT, AcceptorUUID varchar(64)
    public void addUUIDToList(UUID reporterUUID, String reason, String server) {
        final String finalKey = keyCheck();
        ProxySystem.getInstance().getDatabaseDriver().update("INSERT INTO `revivemc_proxy_reportarchiv`(`UUID`, `ReporterUUID`, `ReportId`, `Reason`, `Server`, `Active`, `AcceptorUUID`) VALUES ('" + this.uuid + "', '" + reporterUUID + "', '" + finalKey + "', '" + reason + "', '" + server + "', '1', 'null');");
    }

    public String keyCheck() {
        String key = generateVaildKey();
        if (keyAlreadyExists(key)) {
            keyCheck();
            return null;
        }

        return key;
    }

    public int getReportStatus() {        
        try {
            final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_reportarchiv` WHERE UUID='" + this.uuid + "'");
            while (resultSet.next()) {
                return resultSet.getInt("Active");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public String getReportReason() {
        try {
            final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_reportarchiv` WHERE UUID='" + this.uuid + "'");
            while (resultSet.next()) {
                return resultSet.getString("Reason");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getReportServer() {
        try {
            final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_reportarchiv` WHERE UUID='" + this.uuid + "'");
            while (resultSet.next()) {
                return resultSet.getString("Server");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public UUID getReporterUUID() {
        try {
            final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_reportarchiv` WHERE UUID='" + this.uuid + "'");
            while (resultSet.next()) {
                return UUID.fromString(resultSet.getString("ReporterUUID"));
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getActiveReports() {
        int i = 0;
        try {
            final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_reportarchiv`");
            while (resultSet.next()) {
                if (resultSet.getBoolean("Active")) {
                    i++;
                }
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return i;
    }

    private String generateVaildKey() {
        final int length = 5;
        final String vaildKeys = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";
        final StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            final int index = (int) (vaildKeys.length() * Math.random());
            stringBuilder.append(vaildKeys.charAt(index));
        }
        return stringBuilder.toString();
    }

    public String getReportId() {
        try {
            final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_reportarchiv` WHERE UUID='" + this.uuid + "'");
            while (resultSet.next()) {
                if (resultSet.getInt("Active") == 1) {
                    return resultSet.getString("ReportId");
                }
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private boolean keyAlreadyExists(String key) {
        try {
            final ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_reportarchiv` WHERE  `ReportId`='" + key + "'");
            if (resultSet.next()) {
                return resultSet.getString("ReportId") != null;
            }
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
