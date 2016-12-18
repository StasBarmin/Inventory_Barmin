package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Connection;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.FeelableEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by barmin on 11.12.2016.
 */
abstract class AbstractConnection <A extends Device, B extends Device> implements Connection<A,B>, FeelableEntity, java.io.Serializable  {
    String status = Connection.PLANED;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void feelAllFields(Field[] fields) {

        fillAllFields(new ArrayList<Field>(Arrays.asList(fields)));

    }

    public Field[] getAllFields(){
        Field[] fields = new Field[1];

        return getAllFieldsList().toArray(fields);
    }

    public void fillAllFields(List<Field> fields){

        setStatus((String) fields.get(0).getValue());

    }

    public List<Field> getAllFieldsList(){
        List<Field> fields = new ArrayList<Field>();

        fields.add(new Field(String.class, getStatus()));

        return fields;
    }
}
