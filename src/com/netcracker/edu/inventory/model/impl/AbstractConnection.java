package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Connection;
import com.netcracker.edu.inventory.model.ConnectionPrimaryKey;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.FeelableEntity;
import com.netcracker.edu.location.Trunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmin on 11.12.2016.
 */
abstract class AbstractConnection <A extends Device, B extends Device> implements Connection<A,B>, FeelableEntity, java.io.Serializable  {
    String status = Connection.PLANED;
    int serialNumber;
    Trunk trunk;
    static protected Logger LOGGER = Logger.getLogger(RackArrayImpl.class.getName());

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {

        if (serialNumber > 0)
            this.serialNumber = serialNumber;
        else {
            IllegalArgumentException e = new IllegalArgumentException("Serial number should be greater than 0");
            LOGGER.log(Level.SEVERE, "Serial number should be greater than 0", e);
            throw e;
        }
    }

    @Override
    public boolean isPrimaryKey() {
        return false;
    }

    @Override
    public ConnectionPrimaryKey getPrimaryKey() {
        if (serialNumber != 0 && trunk != null)
            return new ConnectionPK(trunk, serialNumber);
        else
            return null;
    }

    @Override
    public Trunk getTrunk() {
        return trunk;
    }

    @Override
    public void setTrunk(Trunk trunk) {
        this.trunk = trunk;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null){
            NullPointerException e = new NullPointerException();
            LOGGER.log(Level.SEVERE, "Argument is null. Method compareTo", e);
            throw e;
        }
        if (!Connection.class.isInstance(o)){
            ClassCastException e = new ClassCastException();
            LOGGER.log(Level.SEVERE, "Argument is not an instance of Connection. Method compareTo", e);
            throw e;
        }

        if (((Connection)o).getSerialNumber() < serialNumber)
            return 1;
        else if (((Connection)o).getSerialNumber() > serialNumber)
            return -1;
        else
            return 0;
    }

    @Deprecated
    public void feelAllFields(Field[] fields) {
        fillAllFields(new ArrayList<Field>(Arrays.asList(fields)));
    }

    @Deprecated
    public Field[] getAllFields(){

        Field[] fields = new Field[0];

        return getAllFieldsList().toArray(fields);
    }

    public void fillAllFields(List<Field> fields){

        setStatus((String) fields.get(0).getValue());
        setSerialNumber((Integer)fields.get(1).getValue());
    }

    public List<Field> getAllFieldsList(){
        List<Field> fields = new ArrayList<Field>();

        fields.add(new Field(String.class, getStatus()));
        fields.add(new Field(Integer.class, getSerialNumber()));

        return fields;
    }
}
