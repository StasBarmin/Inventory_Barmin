package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.impl.WifiRouter;

/**
 * Created by barmin on 11.11.2016.
 */
 class DeviceValidator {
     boolean isValidDeviceForInsertToRack(Device device){
        if (device == null || device.getIn() == 0 || device.getType() == null)
            return false;
        else
            return true;
    }

     boolean isValidDeviceForWriteToStream(Device device){
        if (device == null)
            return false;

        boolean flag = true;
        String s = device.getManufacturer();

        if (device.getType().contains("|"))
            flag = false;
        if (s != null)
            if (s.contains("|"))
                flag = false;
        s = device.getModel();
        if (s != null)
            if (s.contains("|"))
                flag = false;
        if (WifiRouter.class.isInstance(device)){
            s = ((WifiRouter)device).getSecurityProtocol();
            if (s != null)
                if (s.contains("|"))
                    flag = false;
        }
        return flag;
    }
}
