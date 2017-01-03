package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.*;

import java.util.*;

/**
 * Created by barmin on 07.12.2016.
 */
public class ThinCoaxial<T extends Device> extends AbstractConnection<T, T> implements AllToAllConnection<T> {
    ConnectorType connectorType = ConnectorType.TConnector;
    int maxSize;
    Set <T> devices;

    public ThinCoaxial() {
    }

    public ThinCoaxial(int maxSize) {
        this.maxSize = maxSize;
        devices = new HashSet<T>(maxSize);
    }

    @Override
    public void fillAllFields(List<Field> fields) {
        int size = super.getAllFieldsList().size();

        super.fillAllFields(fields);
        if (fields.get(size).getValue() != null)
            setMaxSize((Integer) fields.get(size).getValue());
        this.devices =  (Set<T>)fields.get(size + 1).getValue();
    }

    @Override
    public List<Field> getAllFieldsList() {
        List<Field> fields = super.getAllFieldsList();

        fields.add(new Field(Integer.class, getMaxSize()));

        fields.add(new Field(Set.class, getAllDevices()));

        return fields;
    }

    @Override
    public ConnectorType getConnectorType() {
        return connectorType;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public boolean addDevice(T device) {
        return devices.add(device);
    }

    @Override
    public boolean removeDevice(T device) {
        return devices.remove(device);
    }

    @Override
    public boolean containDevice(T device) {
        return devices.contains(device);
    }

    @Override
    public Set getAllDevices() {
        if (devices == null)
            return new HashSet<T>();
        else
            return new HashSet<T>(devices) ;
    }

    @Override
    public int getCurSize() {
        if (devices == null)
            return 0;
        else
            return devices.size();
    }

}
