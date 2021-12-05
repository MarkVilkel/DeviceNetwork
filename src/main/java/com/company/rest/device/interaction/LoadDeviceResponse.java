package com.company.rest.device.interaction;

import com.company.device.data.Device;
import com.company.device.data.DeviceType;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record LoadDeviceResponse(DeviceType type, String macAddress) {

    public static LoadDeviceResponse of(Device device) {
        return new LoadDeviceResponse(device.getType(), device.getMacAddress());
    }
}
