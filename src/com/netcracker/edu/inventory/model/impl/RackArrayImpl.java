package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.exception.DeviceValidationException;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.model.RackPrimaryKey;
import com.netcracker.edu.location.Location;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Created by barmin on 07.10.2016.
 */
 public class RackArrayImpl<D extends Device> implements Rack<D>, java.io.Serializable {
    int size;
    final Class clazz;
    Location location;
    D devices [];
    static protected Logger LOGGER = Logger.getLogger(RackArrayImpl.class.getName());

    public RackArrayImpl(int size, Class clazz) throws IllegalArgumentException {
        if (size < 0) {
            IllegalArgumentException e = new IllegalArgumentException("Rack size should not be negative");
            LOGGER.log(Level.SEVERE, "Rack size should not be negative", e);
            throw e;
        }
        if (clazz == null) {
            IllegalArgumentException e = new IllegalArgumentException("Device type for the rack set as null");
            LOGGER.log(Level.SEVERE, "Device type for the rack set as null");
            throw e;
        }
        this.size = size;
        this.clazz = clazz;
        devices = (D[]) new Device[size];
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
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

    public Class getTypeOfDevices(){
        return clazz;
    }

    public D getDevAtSlot(int index) throws IndexOutOfBoundsException{
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


    public boolean insertDevToSlot(D device, int index) throws IndexOutOfBoundsException, DeviceValidationException {

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

        if (devices[index] == null) {
            if (clazz.isInstance(device)) {
                devices[index] =(D) device;
                return true;
            } else {
                LOGGER.log(Level.WARNING, "The rack can contain only devices type  " + clazz.getSimpleName());
                return false;
            }
            }
        else
        {
            LOGGER.log(Level.WARNING, "Slot is full");
            return false;
        }
    }

    public D removeDevFromSlot(int index) throws IndexOutOfBoundsException{
        D dt;

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

    public D getDevByIN(int in){
        for (int i = 0; i < devices.length; i++){
            if (devices[i] != null) {
                if (in == devices[i].getIn()) {
                    return devices[i];
                }
            }
        }
        return null;
    }

    public D[] getAllDeviceAsArray(){
        int counter = 0;

        for (int i = 0; i < devices.length; i++){
            if (devices[i] != null) {
                counter++;
            }
        }

        D[] devs = (D[])new Device[counter];
        counter = 0;

        for (int i = 0; i < devices.length; i++){
            if (devices[i] != null) {
                devs[counter] = devices[i];
                counter++;
            }
        }

        return devs;
    }

    @Override
    public boolean isPrimaryKey() {
        return false;
    }

    @Override
    public RackPrimaryKey getPrimaryKey() {
        if (location != null)
            return new RackPK(location);
        else
            return null;
    }
}

