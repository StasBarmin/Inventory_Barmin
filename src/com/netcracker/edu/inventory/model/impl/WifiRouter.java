package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Connection;
import com.netcracker.edu.inventory.model.ConnectorType;
import com.netcracker.edu.inventory.model.Device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by barmin on 07.10.2016.
 */
public class WifiRouter extends Router implements Device {
    String securityProtocol;
    protected String technologyVersion;
    protected ConnectorType wirePortType= ConnectorType.need_init;
    protected Connection wireConnection;
    protected Connection wirelessConnection;

    public WifiRouter() {
    }

    public WifiRouter(String technologyVersion, ConnectorType wirePortType) {
        this.technologyVersion = technologyVersion;
        this.wirePortType = wirePortType;
    }

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public String getTechnologyVersion() {
        return technologyVersion;
    }

    public Connection getWirelessConnection() {
        return wirelessConnection;
    }

    public void setWirelessConnection(Connection wirelessConnection) {
        this.wirelessConnection = wirelessConnection;
    }

    public ConnectorType getWirePortType() {
        return wirePortType;
    }

    public Connection getWireConnection() {
        return wireConnection;
    }

    public void setWireConnection(Connection wireConnection) {
        this.wireConnection = wireConnection;
    }

//    public void feelAllFields(Field[] fields) {
//        fillAllFields(new ArrayList<Field>(Arrays.asList(fields)));
//    }
//
//    public Field[] getAllFields(){
//        Field[] fields = new Field[7];
//
//        return getAllFieldsList().toArray(fields);
//    }

    public void fillAllFields(List<Field> fields){
        int size = super.getAllFieldsList().size();

        super.fillAllFields(fields);
        setSecurityProtocol((String) fields.get(size).getValue());
        this.technologyVersion = (String) fields.get(size + 1).getValue();
        if (wirePortType == ConnectorType.need_init)
            this.wirePortType = ConnectorType.valueOf(fields.get(size + 2).getValue().toString());
        setWireConnection((Connection)fields.get(size + 3).getValue());
        setWirelessConnection((Connection)fields.get(size + 4).getValue());
    }

    public List<Field> getAllFieldsList(){
        List<Field> fields = super.getAllFieldsList();

        fields.add(new Field(String.class, getSecurityProtocol()));
        fields.add(new Field(String.class, getTechnologyVersion()));
        fields.add(new Field(ConnectorType.class, getWirePortType()));
        fields.add(new Field(Connection.class, getWireConnection()));
        fields.add(new Field(Connection.class, getWirelessConnection()));
        return fields;
    }
}
