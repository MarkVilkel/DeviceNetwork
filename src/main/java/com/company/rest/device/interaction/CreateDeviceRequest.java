package com.company.rest.device.interaction;

import com.company.device.data.DeviceType;
import com.company.rest.device.validation.MacAddress;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateDeviceRequest(
        @NotBlank(message = "Mac address is absent") @MacAddress(message = "Invalid mac address format") String macAddress,
        @NotNull(message = "Device type is absent or unsupported value")DeviceType type,
        @MacAddress(message = "Invalid uplink mac address format") String uplinkMacAddress
) {

}
