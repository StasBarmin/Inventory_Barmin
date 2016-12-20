package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    public void fillAllFields(List<Field> fields){
        int size = super.getAllFieldsList().size();

        super.fillAllFields(fields);
        if (fields.get(size).getValue() != null)
            setChargeVolume((Integer) fields.get(size).getValue());
    }

    public List<Field> getAllFieldsList(){
        List<Field> fields = super.getAllFieldsList();

        fields.add(new Field(Integer.class, getChargeVolume()));
        return fields;
    }
}
