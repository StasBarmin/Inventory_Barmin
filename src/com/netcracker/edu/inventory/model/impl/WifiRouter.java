package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

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
}
