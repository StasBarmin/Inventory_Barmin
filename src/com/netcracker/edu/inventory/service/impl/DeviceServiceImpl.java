package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.service.DeviceService;

/**
 * Created by barmin on 20.10.2016.
 */
 class DeviceServiceImpl implements DeviceService{
    public boolean isCastableTo(Device device, Class clazz){
        if (clazz != null) {
            if (clazz.isInstance(device))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public boolean isValidDeviceForInsertToRack(Device device){
        if (device == null || device.getIn() == 0 || device.getType() == null)
            return false;
        else
            return true;
    }
}
