package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.service.impl.ServiceImpl;

/**
 * Created by barmin on 07.10.2016.
 */
public class RackArrayImpl implements Rack {
    int size;
    String type;
    Device devices [];

       public RackArrayImpl(int size, String type) {
        this.size = size;
        this.type = type;
        devices = new Device [size];
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

    public Device getDevAtSlot(int index){
        if (index >= devices.length || index < 0){
            System.err.println("Index is out of scope");
            return null;
        }

        if (devices[index] == null)
            return null;
        else
            return devices[index];
    }

    public boolean insertDevToSlot(Device device, int index){

        if (index >= devices.length || index < 0){
            System.err.println("Index is out of scope");
            return false;
        }

        if (devices[index] == null){
                if (type.equals(device.getType())){
                    devices[index] = device;
                    return true;
                }
                else {
                    System.err.println("The rack can contain only devices type  " + type);
                    return false;
                }
            }
        else
        {
            System.err.println("Slot is full");
            return false;
        }
    }

    public Device removeDevFromSlot(int index){
        Device dt;

        if (index >= devices.length || index < 0){
            System.err.println("Index is out of scope");
            return null;
        }

        if (devices[index] == null)
            return null;
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
}

