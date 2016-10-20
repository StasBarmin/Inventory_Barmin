package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

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
}
