package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.exception.WrongPKMethodException;
import com.netcracker.edu.inventory.model.DevicePrimaryKey;
import com.netcracker.edu.inventory.model.FeelableEntity;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmin on 04.01.2017.
 */
public class DevicePK implements DevicePrimaryKey {
    final int in;
    static protected Logger LOGGER = Logger.getLogger(DevicePK.class.getName());

    public DevicePK(int in) {
        this.in = in;
    }

    public boolean isPrimaryKey() {
        return true;
    }

    public DevicePrimaryKey getPrimaryKey() {
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

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        wrongPKMethod();
    }

    public String getType() {
        wrongPKMethod();
        return null;
    }

    public String getManufacturer() {
        wrongPKMethod();
        return null;
    }

    public void setManufacturer(String manufacturer) {
        wrongPKMethod();
    }

    public String getModel() {
        wrongPKMethod();
        return null;
    }

    public void setModel(String model) {
        wrongPKMethod();
    }

    public Date getProductionDate() {
        wrongPKMethod();
        return null;
    }

    public void setProductionDate(Date productionDate) {
        wrongPKMethod();
    }

    public int compareTo(Object o) {
        if (o == null){
            NullPointerException e = new NullPointerException();
            LOGGER.log(Level.SEVERE, "Argument is null. Method compareTo", e);
            throw e;
        }
        if (!DevicePK.class.isInstance(o)){
            return -1;
        }

        if (((DevicePK)o).getIn() < in)
            return 1;
        else if (((DevicePK)o).getIn() > in)
            return -1;
        else
            return 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof DevicePrimaryKey)
            return (in == ((DevicePK)obj).getIn());
        else return false;
    }

    void wrongPKMethod(){
        WrongPKMethodException e = new WrongPKMethodException("Wrong PK method");
        LOGGER.log(Level.SEVERE, "Wrong PK method", e);
        throw e;
    }
}
