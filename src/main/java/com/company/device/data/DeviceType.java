package com.company.device.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum DeviceType {

    GATEWAY("Gateway", "Serves as access point to another network"),
    SWITCH("Switch", "Connects devices on a computer network"),
    ACCESS_POINT("Access Point", "Connects devices on a computer network via Wi-Fi"),
    ;

    private final String title;
    private final String description;

    DeviceType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static DeviceType fromTitle(String title) {
        if (title == null) {
            return null;
        }

        return Stream.of(DeviceType.values())
                .filter(d -> d.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

}
