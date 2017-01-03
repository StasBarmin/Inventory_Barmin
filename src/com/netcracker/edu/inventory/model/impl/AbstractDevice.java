package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.DevicePrimaryKey;
import com.netcracker.edu.inventory.model.FeelableEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmin on 07.10.2016.
 */
 abstract class AbstractDevice implements Device, FeelableEntity, java.io.Serializable {
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

    @Deprecated
    public void feelAllFields(Field[] fields) {
        fillAllFields(new ArrayList<Field>(Arrays.asList(fields)));
    }

    @Deprecated
    public Field[] getAllFields(){

        Field[] fields = new Field[0];
        return getAllFieldsList().toArray(fields);
    }

    public void fillAllFields(List<Field> fields){
        if (fields.get(0).getValue() != null)
            if ((Integer)fields.get(0).getValue() > 0)
                setIn((Integer) fields.get(0).getValue());

        setManufacturer((String) fields.get(2).getValue());
        setModel((String) fields.get(3).getValue());
        setProductionDate((Date) fields.get(4).getValue());
    }

    public List<Field> getAllFieldsList(){
        List<Field> fields = new ArrayList<Field>();

        fields.add(new Field(Integer.class, getIn()));

        fields.add(new Field(Class.class, getType()));

        fields.add(new Field(String.class, getManufacturer()));

        fields.add(new Field(String.class, getModel()));

        fields.add(new Field(Date.class,getProductionDate()));

        return fields;
    }

    @Override
    public boolean isPrimaryKey() {
        return false;
    }

    @Override
    public DevicePrimaryKey getPrimaryKey() {
        return null;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null){
            NullPointerException e = new NullPointerException();
            LOGGER.log(Level.SEVERE, "Argument is null. Method compareTo", e);
            throw e;
        }
        if (!Device.class.isInstance(o)){
            ClassCastException e = new ClassCastException();
            LOGGER.log(Level.SEVERE, "Argument is not an instance of Device. Method compareTo", e);
            throw e;
        }

        if (((Device)o).getIn() < in)
            return 1;
        else if (((Device)o).getIn() > in)
            return -1;
        else
            return 0;
    }
}
