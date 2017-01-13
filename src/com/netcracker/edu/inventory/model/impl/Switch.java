package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Connection;
import com.netcracker.edu.inventory.model.ConnectorType;
import com.netcracker.edu.inventory.model.Device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by barmin on 07.10.2016.
 */
public class Switch extends Router implements Device {
    int numberOfPorts;
    protected ConnectorType portsType = ConnectorType.need_init;
    protected Connection[] portsConnections;

    public Switch() {
    }

    public Switch(ConnectorType portsType) {
        this.portsType = portsType;
    }

    public int getNumberOfPorts() {
        return numberOfPorts;
    }

    public void setNumberOfPorts(int numberOfPorts) {
        this.numberOfPorts = numberOfPorts;
        this.portsConnections = new Connection[numberOfPorts];
    }

    public ConnectorType getPortsType() {
        return portsType;
    }

    public Connection getPortConnection(int portNumber) throws IndexOutOfBoundsException{
        if (portNumber >= numberOfPorts || portNumber < 0){
            IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index " + portNumber + " is out of bounds of array. Index should be from 0 to " + numberOfPorts);
            LOGGER.log(Level.SEVERE, "Index " + portNumber + " is out of bounds of array. Index should be from 0 to " + numberOfPorts);
            throw e;
        }

        return portsConnections[portNumber];
    }

    public void setPortConnection(Connection connection, int portNumber) throws IndexOutOfBoundsException{
        if (portNumber >= numberOfPorts || portNumber < 0){
            IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index " + portNumber + " is out of bounds of array. Index should be from 0 to " + numberOfPorts);
            LOGGER.log(Level.SEVERE, "Index " + portNumber + " is out of bounds of array. Index should be from 0 to " + numberOfPorts);
            throw e;
        }

        portsConnections[portNumber] = connection;
    }

    public List<Connection> getAllPortConnections() {
        if (portsConnections == null)
            return new ArrayList<Connection>();
        else
        return Arrays.asList(portsConnections);
    }

    public void fillAllFields(List<Field> fields){
        int size = super.getAllFieldsList().size();

        super.fillAllFields(fields);
        if (fields.get(size).getValue() != null)
            setNumberOfPorts((Integer) fields.get(size).getValue());
        if (portsType == ConnectorType.need_init)
            this.portsType = ConnectorType.valueOf(fields.get(size + 1).getValue().toString());
        this.portsConnections = (Connection[]) ((Collection)fields.get(size + 2).getValue()).toArray(portsConnections);
    }

    public List<Field> getAllFieldsList(){
        List<Field> fields = super.getAllFieldsList();

        fields.add(new Field(Integer.class, getNumberOfPorts()));
        fields.add(new Field(ConnectorType.class, getPortsType()));
        fields.add(new Field(List.class, getAllPortConnections()));

        return fields;
    }
}
