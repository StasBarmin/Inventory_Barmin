package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by barmin on 07.12.2016.
 */
public class Wireless<A extends Device, B extends Device>  extends AbstractConnection<A,B> implements OneToManyConnection<A,B> {
    int version;
    String securityProtocol;
    String technology;
    ConnectorType APointConnectorType = ConnectorType.Wireless;
    ConnectorType BPointConnectorType = ConnectorType.Wireless;
    A APoint;
    B[]  BPoints;

    public Wireless() {
    }

    public Wireless(int capacity, String technology) {
        BPoints =  (B[]) new Device[capacity];
        this.technology = technology;
    }

    @Override
    public void fillAllFields(List<Field> fields) {
        int size = super.getAllFieldsList().size();

        super.fillAllFields(fields);
        if (fields.get(size).getValue() != null)
            setVersion((Integer) fields.get(size).getValue());
        setProtocol((String) fields.get(size + 1).getValue());
        if (technology == null)
            this.technology = (String) fields.get(size + 2).getValue();
        setAPoint((A) fields.get(size + 3).getValue());
        setBPoints((List<B>) fields.get(size + 4).getValue());
    }

    @Override
    public List<Field> getAllFieldsList() {
        List<Field> fields = super.getAllFieldsList();

        fields.add(new Field(Integer.class, getVersion()));

        fields.add(new Field(String.class, getProtocol()));

        fields.add(new Field(String.class, getTechnology()));

        fields.add(new Field(Device.class, getAPoint()));

        fields.add(new Field(List.class,getBPoints()));

        return fields;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getProtocol() {
        return securityProtocol;
    }

    public void setProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public String getTechnology() {
        return technology;
    }

    @Override
    public ConnectorType getAPointConnectorType() {
        return APointConnectorType;
    }

    @Override
    public ConnectorType getBPointConnectorType() {
        return BPointConnectorType;
    }

    @Override
    public A getAPoint() {
        return APoint;
    }

    @Override
    public void setAPoint(A device) {
        this.APoint = device;
    }

    @Override
    public List<B> getBPoints() {
        if (BPoints == null)
            return new ArrayList<B>();
        else
            return Arrays.asList(BPoints);
    }

    @Override
    public void setBPoints(List<B> devices) {
        if (devices.size() == 0)
            this.BPoints = (B[]) new Device[0];
        else
            if (BPoints == null)
                this.BPoints = devices.toArray((B[]) new Device[0]);
            else
                this.BPoints = devices.toArray(BPoints);
    }

    @Override
    public int getBCapacity() {
        if (BPoints == null)
            return 0;
        else
            return BPoints.length;
    }

    @Override
    public B getBPoint(int deviceNumber) {
        return BPoints[deviceNumber];
    }

    @Override
    public void setBPoint(B device, int deviceNumber) {
        this.BPoints[deviceNumber] = device;
    }
}
