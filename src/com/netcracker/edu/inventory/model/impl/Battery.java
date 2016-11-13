package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

import java.util.Arrays;

/**
 * Created by barmin on 07.10.2016.
 */
 public class Battery extends AbstractDevice implements Device {
    int chargeVolume;

    public int getChargeVolume() {
        return chargeVolume;
    }

    public void setChargeVolume(int chargeVolume) {
        this.chargeVolume = chargeVolume;
    }

    public void feelAllFields(Field[] fields) {
        super.feelAllFields(fields);
        if (fields[5].getValue() != null)
        setChargeVolume((Integer) fields[5].getValue());
    }

    public Field[] getAllFields(){
        Field[] fields;

        fields = Arrays.copyOf(super.getAllFields(), 6);
        fields[5] = new Field(Integer.class,getChargeVolume());

        return fields;
    }
}
