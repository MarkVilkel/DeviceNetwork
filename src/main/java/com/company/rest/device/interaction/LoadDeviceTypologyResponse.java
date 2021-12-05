package com.company.rest.device.interaction;

import com.company.device.data.Device;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record LoadDeviceTypologyResponse(String macAddress, List<LoadDeviceTypologyResponse> children) {

    public static LoadDeviceTypologyResponse of(Device device) {
        var children = device.getChildren();
        List<LoadDeviceTypologyResponse> childrenResponse = null;
        if (children != null) {
            childrenResponse = children.stream().map(LoadDeviceTypologyResponse::of).toList();
        }
        return new LoadDeviceTypologyResponse(device.getMacAddress(), childrenResponse);
    }
}
