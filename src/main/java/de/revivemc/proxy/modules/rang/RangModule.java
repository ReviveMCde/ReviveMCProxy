package de.revivemc.proxy.modules.rang;

import de.revivemc.proxy.ProxySystem;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class RangModule {

    public static void updateRangArchiv(UUID player, UUID target, String rang, String reason, String id) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateTime = dateFormat.format(currentDate);
        ProxySystem.getInstance().getDatabaseDriver().update("INSERT INTO `revivemc_proxy_rang_archiv`(`UUIDPlayer1`, `UUIDPlayer2`, `Rang`, `Reason`, `ID`, `Date`) VALUES ('" + player + "','" + target + "','" + rang + "','" + reason + "','" + id +"','" + currentDateTime + "')");
    }
}
