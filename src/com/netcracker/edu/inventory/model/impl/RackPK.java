package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.exception.WrongPKMethodException;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.RackPrimaryKey;
import com.netcracker.edu.inventory.model.Unique;
import com.netcracker.edu.location.Location;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmin on 04.01.2017.
 */
public class RackPK implements RackPrimaryKey {
    final Location location;
    static protected Logger LOGGER = Logger.getLogger(RackPK.class.getName());

    public RackPK(Location location) {
        this.location = location;
    }

    public boolean isPrimaryKey() {
        return true;
    }

    public PrimaryKey getPrimaryKey() {
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        wrongPKMethod();
    }

    public int getSize() {
        wrongPKMethod();
        return 0;
    }

    public int getFreeSize() {
        wrongPKMethod();
        return 0;
    }

    public Class getTypeOfDevices() {
        wrongPKMethod();
        return null;
    }

    public Device getDevAtSlot(int index) {
        wrongPKMethod();
        return null;
    }

    public boolean insertDevToSlot(Device device, int index) {
        wrongPKMethod();
        return false;
    }

    public Device removeDevFromSlot(int index) {
        wrongPKMethod();
        return null;
    }

    public Device getDevByIN(int in) {
        wrongPKMethod();
        return null;
    }

    public Device[] getAllDeviceAsArray() {
        wrongPKMethod();
        return new Device[0];
    }

    public boolean equals(Object obj) {
        return (location.equals(((RackPK)obj).getLocation()));
    }

    void wrongPKMethod(){
        WrongPKMethodException e = new WrongPKMethodException("Wrong PK method");
        LOGGER.log(Level.SEVERE, "Wrong PK method", e);
        throw e;
    }

}
