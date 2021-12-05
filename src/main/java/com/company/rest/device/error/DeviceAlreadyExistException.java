package com.company.rest.device.error;

public class DeviceAlreadyExistException extends RuntimeException {

    private DeviceAlreadyExistException(String message) {
        super(message);
    }

    public static DeviceAlreadyExistException of(String message) {
        return new DeviceAlreadyExistException(message);
    }

    public static DeviceAlreadyExistException ofId(String id) {
        return of("Device already exist " + id);
    }

}