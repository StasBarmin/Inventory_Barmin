package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

import java.util.Arrays;

/**
 * Created by barmin on 07.10.2016.
 */
public class Switch extends Router implements Device {
    int numberOfPorts;

    public int getNumberOfPorts() {
        return numberOfPorts;
    }

    public void setNumberOfPorts(int numberOfPorts) {
        this.numberOfPorts = numberOfPorts;
    }

    public void feelAllFields(Field[] fields) {
        super.feelAllFields(fields);
        setNumberOfPorts((Integer) fields[6].getValue());
    }

    public Field[] getAllFields(){
        Field[] fields;

        fields = Arrays.copyOf(super.getAllFields(), 7);
        fields[6].setType(Integer.class);
        fields[6].setValue(getNumberOfPorts());

        return fields;
    }
}
