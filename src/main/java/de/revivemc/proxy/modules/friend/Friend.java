package de.revivemc.proxy.modules.friend;

public class Friend {

    private String uuid;
    private String since;

    public String getSince() {
        return since;
    }

    public String getUuid() {
        return uuid;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
