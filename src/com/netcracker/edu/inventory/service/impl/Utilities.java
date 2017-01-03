package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.FeelableEntity;
import com.netcracker.edu.inventory.model.impl.*;

import static java.util.Arrays.fill;

/**
 * Created by barmin on 11.11.2016.
 */
 class Utilities {
     void sortByIN(Device[] devices)  {
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
                if (devs[i].getIn() > devs[i + 1].getIn() && devs[i + 1].getIn() != 0) {
                    Device temp = devs[i + 1];
                    devs[i + 1] = devs[i];
                    devs[i] = temp;
                    flag = true;
                }
                else {
                    if (devs[i].getIn() == 0 && devs[i + 1].getIn() != 0){
                        Device temp = devs[i + 1];
                        devs[i + 1] = devs[i];
                        devs[i] = temp;
                        flag = true;
                    }
                }
            }
        }


        for (int i = 0; i < devs.length; i++){
            devices[i] = devs[i];
        }
        if (devs.length != devices.length)
            fill(devices, devs.length, devices.length - 1, null);
    }

     void filtrateByType(Device[] devices, String type) {
         if (devices == null)
             return;

        for (int i = 0; i < devices.length; i++) {
            if (devices[i] != null) {
                if (devices[i].getType() != null) {
                    if (!devices[i].getType().equals(type)) {
                        devices[i] = null;
                    }
                }
                else {
                    if (type != null){
                        devices[i] = null;
                    }
                }
            }
        }
    }

    static FeelableEntity createDevice_Connection(Class clazz){

        if (clazz.equals(Router.class))
           return new Router();

        if (clazz.equals(Switch.class))
            return new Switch();

        if (clazz.equals(WifiRouter.class))
            return new WifiRouter();

        if (clazz.equals(Battery.class))
            return new Battery();

        if (clazz.equals(TwistedPair.class))
            return new TwistedPair();

        if (clazz.equals(OpticFiber.class))
            return new OpticFiber();

        if (clazz.equals(Wireless.class))
            return new Wireless();

        if (clazz.equals(ThinCoaxial.class))
            return new ThinCoaxial();

        return null;
    }
}
