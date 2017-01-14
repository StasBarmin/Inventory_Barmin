package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.exception.WrongPKMethodException;
import com.netcracker.edu.inventory.model.Connection;
import com.netcracker.edu.inventory.model.ConnectionPrimaryKey;
import com.netcracker.edu.inventory.model.FeelableEntity;
import com.netcracker.edu.inventory.model.Unique;
import com.netcracker.edu.location.Trunk;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmin on 04.01.2017.
 */
public class ConnectionPK implements ConnectionPrimaryKey {
    final int serialNumber;
    final Trunk trunk;
    static protected Logger LOGGER = Logger.getLogger(ConnectionPK.class.getName());

    public ConnectionPK(Trunk trunk, int serialNumber) {
        this.serialNumber = serialNumber;
        this.trunk = trunk;
    }

    public boolean isPrimaryKey() {
        return true;
    }

    public PrimaryKey getPrimaryKey() {
        return this;
    }

    public void feelAllFields(Field[] fields) {
        wrongPKMethod();
    }

    public Field[] getAllFields() {
        wrongPKMethod();
        return null;
    }

    public void fillAllFields(List<Field> fields) {
        wrongPKMethod();
    }

    public List<Field> getAllFieldsList() {
        wrongPKMethod();
        return null;
    }

    public Trunk getTrunk() {
        return trunk;
    }

    public void setTrunk(Trunk trunk) {
        wrongPKMethod();
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        wrongPKMethod();
    }

    public String getStatus() {
        wrongPKMethod();
        return null;
    }

    public void setStatus(String status) {
        wrongPKMethod();
    }

    public int compareTo(Object o) {
        if (o == null){
            NullPointerException e = new NullPointerException();
            LOGGER.log(Level.SEVERE, "Argument is null. Method compareTo", e);
            throw e;
        }
        if (!Connection.class.isInstance(o)){
            return 1;
        }

        if (((Connection)o).getSerialNumber() < serialNumber)
            return 1;
        else if (((Connection)o).getSerialNumber() > serialNumber)
            return -1;
        else
            return 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ConnectionPrimaryKey)
            return (trunk.equals(((ConnectionPK)obj).getTrunk()) && serialNumber == ((ConnectionPK)obj).getSerialNumber());
        else
            return false;
    }

    void wrongPKMethod(){
        WrongPKMethodException e = new WrongPKMethodException("Wrong PK method");
        LOGGER.log(Level.SEVERE, "Wrong PK method", e);
        throw e;
    }
}
