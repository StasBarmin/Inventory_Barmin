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

    public boolean isCastableTo(Device device, Class clazz) {
        if (clazz != null)
            return clazz.isInstance(device);
        else
            return false;
    }

    public boolean isValidDeviceForInsertToRack(Device device){
        DeviceValidator deviceValidator = new DeviceValidator();
        return deviceValidator.isValidDeviceForInsertToRack(device);
    }

    public boolean isValidDeviceForWriteToStream(Device device){
        DeviceValidator deviceValidator = new DeviceValidator();
        return deviceValidator.isValidDeviceForWriteToStream(device);
    }

    public void writeDevice(Device device, Writer writer) throws IOException{
       InputOutputOperations inputOutputOperations = new InputOutputOperations();
        inputOutputOperations.writeDevice(device, writer);
    }

    public Device readDevice(Reader reader) throws IOException, ClassNotFoundException{
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        return inputOutputOperations.readDevice(reader);
    }

    public void outputDevice(Device device, OutputStream outputStream) throws IOException{
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        inputOutputOperations.outputDevice(device, outputStream);
    }

    public Device inputDevice(InputStream inputStream) throws IOException, ClassNotFoundException{
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        return inputOutputOperations.inputDevice(inputStream);
    }

    public void serializeDevice(Device device, OutputStream outputStream) throws IOException{
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        inputOutputOperations.serializeDevice(device, outputStream);
    }

    public Device deserializeDevice(InputStream inputStream) throws IOException, ClassCastException, ClassNotFoundException{
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        return inputOutputOperations.deserializeDevice(inputStream);
    }


}
