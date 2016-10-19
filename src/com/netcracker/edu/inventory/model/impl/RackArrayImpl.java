package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.exception.DeviceValidationException;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.service.impl.ServiceImpl;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Created by barmin on 07.10.2016.
 */
public class RackArrayImpl implements Rack {
    int size;
    @Deprecated
    String type;
    Device devices [];
    static protected Logger LOGGER = Logger.getLogger(RackArrayImpl.class.getName());

        @Deprecated
       public RackArrayImpl(int size, String type)  {
            LOGGER.log(Level.WARNING, "This constructor is out of date");
    }

    public int getSize(){
        return size;
    }

    public int getFreeSize(){
        int count = 0;

        for (int i = 0; i < devices.length; i++){
            if (devices[i] == null)
                count++;
        }
        return count;
    }

    public Device getDevAtSlot(int index) throws IndexOutOfBoundsException{
        if (index >= devices.length || index < 0){
            IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index " + index + " is out of bounds of array. Index should be from 0 to " + (devices.length - 1));
            LOGGER.log(Level.SEVERE, "Index " + index + " is out of bounds of array. Index should be from 0 to " + (devices.length - 1));
            throw e;
        }

        if (devices[index] == null)
            return null;
        else
            return devices[index];
    }

    public boolean insertDevToSlot(Device device, int index) throws IndexOutOfBoundsException, DeviceValidationException {

        if (device == null || device.getIn() == 0 || device.getType() == null){
            DeviceValidationException e;

            if (device == null) {
                e = new DeviceValidationException();
                LOGGER.log(Level.SEVERE, "Device is not valid for operation. Rack.insertDevToSlot", e);
            }
                else{
                e = new DeviceValidationException("Device " + device + " is not valid for operation. Rack.insertDevToSlot");
                LOGGER.log(Level.SEVERE, "Device " + device + " is not valid for operation. Rack.insertDevToSlot", e);
            }
            throw e;
        }

        if (index >= devices.length || index < 0){
            IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index " + index + " is out of bounds of array. Index should be from 0 to " + (devices.length - 1));
            LOGGER.log(Level.SEVERE, "Index " + index + " is out of bounds of array. Index should be from 0 to " + (devices.length - 1));
            throw e;
        }

        if (devices[index] == null){
            if (type == null){
                devices[index] = device;
                return true;
            }
            else {
                if (type.equals(device.getType())) {
                    devices[index] = device;
                    return true;
                } else {
                    LOGGER.log(Level.WARNING, "The rack can contain only devices type  " + type);
                    return false;
                }
            }
        }
        else
        {
            LOGGER.log(Level.WARNING, "Slot is full");
            return false;
        }
    }

    public Device removeDevFromSlot(int index) throws IndexOutOfBoundsException{
        Device dt;

        if (index >= devices.length || index < 0){
            IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index " + index + " is out of bounds of array. Index should be from 0 to " + (devices.length - 1));
            LOGGER.log(Level.SEVERE, "Index " + index + " is out of bounds of array. Index should be from 0 to " + (devices.length - 1));
            throw e;
        }

        if (devices[index] == null){
            LOGGER.log(Level.WARNING, "Can not remove from empty slot" + index);
            return null;
        }
            else {
            dt = devices[index];
            devices[index] = null;
            return dt;
        }
    }

    public Device getDevByIN(int in){
        for (int i = 0; i < devices.length; i++){
            if (devices[i] != null) {
                if (in == devices[i].getIn()) {
                    return devices[i];
                }
            }
        }
        return null;
    }

    public Device[] getAllDeviceAsArray(){
        return null;
    }
}

