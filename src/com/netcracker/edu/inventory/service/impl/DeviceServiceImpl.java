package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.service.DeviceService;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Created by barmin on 20.10.2016.
 */
 class DeviceServiceImpl implements DeviceService{
    static protected Logger LOGGER = Logger.getLogger(DeviceServiceImpl.class.getName());
    private DeviceValidator deviceValidator = new DeviceValidator();
    private InputOutputOperations inputOutputOperations = new InputOutputOperations();

    public boolean isCastableTo(Device device, Class clazz) {
        if (clazz != null)
            return clazz.isInstance(device);
        else
            return false;
    }

    public boolean isValidDeviceForInsertToRack(Device device){
        return deviceValidator.isValidDeviceForInsertToRack(device);
    }

    public boolean isValidDeviceForWriteToStream(Device device){
        return deviceValidator.isValidDeviceForWriteToStream(device);
    }

    public void writeDevice(Device device, Writer writer) throws IOException{
        inputOutputOperations.writeDevice(device, writer);
    }

    public Device readDevice(Reader reader) throws IOException, ClassNotFoundException{
        return inputOutputOperations.readDevice(reader);
    }

    public void outputDevice(Device device, OutputStream outputStream) throws IOException{
        inputOutputOperations.outputDevice(device, outputStream);
    }

    public Device inputDevice(InputStream inputStream) throws IOException, ClassNotFoundException{
        return inputOutputOperations.inputDevice(inputStream);
    }

    public void serializeDevice(Device device, OutputStream outputStream) throws IOException{
        inputOutputOperations.serializeDevice(device, outputStream);
    }

    public Device deserializeDevice(InputStream inputStream) throws IOException, ClassCastException, ClassNotFoundException{
        return inputOutputOperations.deserializeDevice(inputStream);
    }


}
