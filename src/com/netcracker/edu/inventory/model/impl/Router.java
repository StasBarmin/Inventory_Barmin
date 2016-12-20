package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    public void fillAllFields(List<Field> fields){
        int size = super.getAllFieldsList().size();

        super.fillAllFields(fields);
        if (fields.get(size).getValue() != null)
            setDataRate((Integer) fields.get(size).getValue());
    }

    public List<Field> getAllFieldsList(){
        List<Field> fields = super.getAllFieldsList();

        fields.add(new Field(Integer.class, getDataRate()));
        return fields;
    }
}
