package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.service.DeviceService;
import com.netcracker.edu.inventory.service.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.fill;


/**
 * Created by barmin on 09.10.2016.
 */
public class ServiceImpl implements Service   {
    static protected Logger LOGGER = Logger.getLogger(ServiceImpl.class.getName());

    public void sortByIN(Device[] devices)  {
        boolean flag = true;
        int counter = 0;

        for (int i = 0; i < devices.length; i++){
            if (devices[i] != null) {
                counter++;
            }
        }

        Device[] devs = new Device[counter];
        counter = 0;

        for (int i = 0; i < devices.length; i++){
            if (devices[i] != null) {
                devs[counter] = devices[i];
                counter++;
            }
        }

        while (flag){
            flag = false;
            for (int i = 0; i < devs.length - 1; i++){
                if (devs[i].getIn() < devs[i+1].getIn()) {
                    Device temp = devs[i + 1];
                    devs[i + 1] = devs[i];
                    devs[i] = temp;
                    flag = true;
                }
            }
        }

        for (int i = 0; i < devs.length; i++){
           devices[i] = devs[i];
        }

        fill(devices, devs.length, devices.length - 1, null);
    }

    public void filtrateByType(Device[] devices, String type) {
        for (int i = 0; i < devices.length; i++) {
            if (!devices[i].getType().equals(type)) {
                devices[i] = null;
            }
        }
    }

   public DeviceService getDeviceService(){
        return null;
    }
}
