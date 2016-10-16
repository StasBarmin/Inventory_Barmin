package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.service.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by barmin on 09.10.2016.
 */
public class ServiceImpl implements Service   {
    static protected Logger LOGGER = Logger.getLogger(ServiceImpl.class.getName());

    public void sortByIN(Device[] devices) throws NotImplementedException {
        NotImplementedException  e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "Operation sortByIN not supported yet", e);
        throw e;
    }

    public void filtrateByType(Device[] devices, String type) throws NotImplementedException{
        NotImplementedException  e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "Operation filtrateByType not supported yet", e);
        throw e;

    }
}
