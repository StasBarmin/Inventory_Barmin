package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmin on 07.10.2016.
 */
 abstract class AbstractDevice implements Device {
    int in;
    final String type = this.getClass().getSimpleName();
    String manufacturer;
    String model;
    Date productionDate;
    static protected Logger LOGGER = Logger.getLogger(RackArrayImpl.class.getName());

    @Override
    public int getIn() {
        return in;
    }

    @Override
    public void setIn(int in) {
        this.in = in;
    }

    @Override
    public String getType() {
        return type;
    }

    @Deprecated
    public void setType(String type) {
        LOGGER.log(Level.WARNING, "Method setType is out of date");
           }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public Date getProductionDate() {
        return productionDate;
    }

    @Override
    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }
}
