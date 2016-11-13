package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

import java.util.Arrays;

/**
 * Created by barmin on 07.10.2016.
 */
public class Router extends AbstractDevice implements Device {
    int dataRate;

    public int getDataRate() {
        return dataRate;
    }

    public void setDataRate(int dataRate) {
        this.dataRate = dataRate;
    }

    public void feelAllFields(Field[] fields) {
        super.feelAllFields(fields);
        setDataRate((Integer) fields[5].getValue());
    }

    public Field[] getAllFields(){
        Field[] fields;

        fields = Arrays.copyOf(super.getAllFields(), 6);
        fields[5].setType(Integer.class);
        fields[5].setValue(getDataRate());

        return fields;
    }
}
