package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

/**
 * Created by barmin on 07.10.2016.
 */
 class Switch extends Router implements Device {
    int numberOfPorts;

    public int getNumberOfPorts() {
        return numberOfPorts;
    }

    public void setNumberOfPorts(int numberOfPorts) {
        this.numberOfPorts = numberOfPorts;
    }
}
