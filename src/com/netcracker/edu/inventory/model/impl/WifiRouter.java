package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

import java.util.Arrays;

/**
 * Created by barmin on 07.10.2016.
 */
public class WifiRouter extends Router implements Device {
    String securityProtocol;

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public void feelAllFields(Field[] fields) {
        super.feelAllFields(fields);
        setSecurityProtocol((String) fields[6].getValue());
    }

    public Field[] getAllFields(){
        Field[] fields;

        fields = Arrays.copyOf(super.getAllFields(), 7);
        fields[6].setType(String.class);
        fields[6].setValue(getSecurityProtocol());

        return fields;
    }
}
