package org.example.model;

public enum CampaignType {
    MESSAGE("message"),
    DELAY_STEP("delay-step");

    private final String value;

    CampaignType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
