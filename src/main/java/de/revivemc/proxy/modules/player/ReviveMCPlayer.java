package de.revivemc.proxy.modules.player;

import eu.thesimplecloud.api.player.ICloudPlayer;

import java.util.Objects;

public class ReviveMCPlayer {

    private final ICloudPlayer iCloudPlayer;

    public ReviveMCPlayer(final ICloudPlayer iCloudPlayer) {
        this.iCloudPlayer = iCloudPlayer;
    }

    public String getFirstColor() {
        return String.valueOf(Objects.requireNonNull(this.iCloudPlayer.getProperty("color")).getValue());
    }

    public String getSecondColor() {
        return String.valueOf(Objects.requireNonNull(this.iCloudPlayer.getProperty("colorsub")).getValue());
    }
}
