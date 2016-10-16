package com.netcracker.edu.inventory.exception;

import com.netcracker.edu.inventory.model.Device;

/**
 * Created by barmin on 15.10.2016.
 */
public class DeviceValidationException extends RuntimeException {
    Device device;

    public DeviceValidationException(){
        super("Device is not valid for operation");
    }

    public DeviceValidationException(String s){
        super(s);
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
