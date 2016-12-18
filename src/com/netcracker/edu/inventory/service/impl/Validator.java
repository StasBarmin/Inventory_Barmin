package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Connection;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.impl.WifiRouter;
import com.netcracker.edu.inventory.model.impl.Wireless;

/**
 * Created by barmin on 11.11.2016.
 */
 class Validator {
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

    boolean isValidConnectionForWriteToStream(Connection connection){
        if (connection == null)
            return false;

        boolean flag = true;
        String s = connection.getStatus();

        if (s != null)
            if (s.contains("|"))
                flag = false;
        if (Wireless.class.isInstance(connection)){
            s = ((Wireless)connection).getProtocol();
            if (s != null)
                if (s.contains("|"))
                    flag = false;

            s = ((Wireless)connection).getTechnology();
            if (s != null)
                if (s.contains("|"))
                    flag = false;
        }
        return flag;
    }
}
