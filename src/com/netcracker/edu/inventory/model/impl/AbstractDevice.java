package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmin on 07.10.2016.
 */
 abstract class AbstractDevice implements Device, java.io.Serializable {
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
        if (in > 0)
        this.in = in;
        else {
            IllegalArgumentException e = new IllegalArgumentException("Inventory number should be greater than 0");
            LOGGER.log(Level.SEVERE, "Inventory number should be greater than 0", e);
            throw e;
        }
    }

    @Override
    public String getType() {
        return type;
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

    public void feelAllFields(Field[] fields) {
        setIn((Integer) fields[0].getValue());
        setManufacturer((String) fields[2].getValue());
        setModel((String) fields[3].getValue());
        setProductionDate((Date) fields[4].getValue());
    }

    public Field[] getAllFields(){
        Field[] fields = new Field[5];

        fields[0].setType(Integer.class);
        fields[0].setValue(getIn());

        fields[1].setType(Class.class);
        fields[1].setValue(getType());

        fields[2].setType(String.class);
        fields[2].setValue(getManufacturer());

        fields[3].setType(String.class);
        fields[3].setValue(getModel());

        fields[4].setType(Date.class);
        fields[4].setValue(getProductionDate());

        return fields;
    }
}
