package org.example.model;

public enum ChannelType {
    EMAIL("EMAIL"),
    LINKEDIN("LINKEDIN_MESSAGE"),
    GMAIL("GMAIL");

    private final String value;

    ChannelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}