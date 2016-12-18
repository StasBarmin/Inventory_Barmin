package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Connection;
import com.netcracker.edu.inventory.model.ConnectorType;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.FeelableEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by barmin on 12.12.2016.
 */
abstract class AbstractOneToOneConnection<A extends Device, B extends Device> extends AbstractConnection<A,B> implements Connection<A,B>, FeelableEntity, java.io.Serializable {
    ConnectorType APointConnectorType;
    ConnectorType BPointConnectorType;
    A APoint;
    B BPoint;

    public ConnectorType getAPointConnectorType() {
        return APointConnectorType;
    }

    public ConnectorType getBPointConnectorType() {
        return BPointConnectorType;
    }

    public A getAPoint() {
        return APoint;
    }

    public void setAPoint(A device) {
        this.APoint = device;
    }

    public B getBPoint() {
        return BPoint;
    }

    public void setBPoint(B device) {
        this.BPoint = device;
    }

    public void feelAllFields(Field[] fields) {
        fillAllFields(new ArrayList<Field>(Arrays.asList(fields)));
    }

    public Field[] getAllFields(){
        Field[] fields = new Field[3];

        return getAllFieldsList().toArray(fields);
    }

    public void fillAllFields(List<Field> fields){
        int size = super.getAllFieldsList().size();

        super.fillAllFields(fields);
        setAPoint((A) fields.get(size).getValue());
        setBPoint((B) fields.get(size + 1).getValue());
    }

    public List<Field> getAllFieldsList(){
        List<Field> fields = super.getAllFieldsList();

        fields.add(new Field(Device.class, getAPoint()));

        fields.add(new Field(Device.class,getBPoint()));

        return fields;
    }
}
