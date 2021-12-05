package com.company.rest.device.error;

public class DeviceNotFoundException extends RuntimeException {

    private DeviceNotFoundException(String message) {
        super(message);
    }

    public static DeviceNotFoundException of(String message) {
        return new DeviceNotFoundException(message);
    }

    public static DeviceNotFoundException ofId(String id) {
        return of("Could not find device " + id);
    }

}