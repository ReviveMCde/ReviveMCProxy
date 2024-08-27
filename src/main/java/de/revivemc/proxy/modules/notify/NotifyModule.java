package de.revivemc.proxy.modules.notify;

import de.revivemc.proxy.ProxySystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class NotifyModule {

    private final UUID uuid;

    public NotifyModule(UUID uuid) {
        this.uuid = uuid;
    }

    public void updateNotifyModulePlayer(int status) {
        if (!playerExists()) { return; }
        ProxySystem.getInstance().getDatabaseDriver().update("UPDATE `revivemc_proxy_notify_active` SET `status`='" + status + "'  WHERE UUID='" + this.uuid + "'");
    }

    public int getNotifyModulePlayer() {
        try {
            ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_notify_active` WHERE  `UUID` = '" + this.uuid.toString() + "'");
            if (resultSet.next()) {
                return resultSet.getInt("status");
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 1;
    }

    public void insertNotifyDatabase() {
        if(!playerExists()) {
            ProxySystem.getInstance().getDatabaseDriver().update("INSERT INTO `revivemc_proxy_notify_active`(`UUID`, `status`) VALUES ('" + this.uuid + "', '1');");
        }
    }

    private boolean playerExists() {
        try {
            ResultSet resultSet = ProxySystem.getInstance().getDatabaseDriver().query("SELECT * FROM `revivemc_proxy_notify_active` WHERE  `UUID` = '" + this.uuid + "'");
            if (resultSet.next()) {
                return resultSet.getString("UUID") != null;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
