package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.service.DeviceService;
import com.netcracker.edu.inventory.service.RackService;
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

    public DeviceService getDeviceService(){
        DeviceService ds = new DeviceServiceImpl();
        return ds;
    }

    public RackService getRackService(){
        RackService rs = new RackServiceImpl();
        return rs;
    }

    public void sortByIN(Device[] devices)  {
        Utilities util = new Utilities();
        util.sortByIN(devices);
    }

    public void filtrateByType(Device[] devices, String type) {
       Utilities util = new Utilities();
        util.filtrateByType(devices, type);
    }
}
