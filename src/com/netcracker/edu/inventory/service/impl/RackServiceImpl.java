package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.service.RackService;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmin on 29.10.2016.
 */
class RackServiceImpl implements RackService{
    static protected Logger LOGGER = Logger.getLogger(RackServiceImpl.class.getName());
    private InputOutputOperations inputOutputOperations = new InputOutputOperations();

    public void writeRack(Rack rack, Writer writer) throws IOException{
        inputOutputOperations.writeRack(rack, writer);
    }

    public Rack readRack(Reader reader) throws IOException, ClassNotFoundException{
        return inputOutputOperations.readRack(reader);
    }

    public void outputRack(Rack rack, OutputStream outputStream) throws IOException{
        inputOutputOperations.outputRack(rack, outputStream);
    }

    public Rack inputRack(InputStream inputStream) throws IOException, ClassNotFoundException {
        return inputOutputOperations.inputRack(inputStream);
    }

    public void serializeRack(Rack rack, OutputStream outputStream) throws IOException{
        inputOutputOperations.serializeRack(rack, outputStream);
    }

    public Rack deserializeRack(InputStream inputStream) throws IOException, ClassCastException, ClassNotFoundException{
        return inputOutputOperations.deserializeRack(inputStream);
    }
}
