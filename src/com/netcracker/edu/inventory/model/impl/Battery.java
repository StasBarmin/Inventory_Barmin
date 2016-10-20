package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

/**
 * Created by barmin on 07.10.2016.
 */
 class Battery extends AbstractDevice implements Device {
    int chargeVolume;

    public int getChargeVolume() {
        return chargeVolume;
    }

    public void setChargeVolume(int chargeVolume) {
        this.chargeVolume = chargeVolume;
    }
}
