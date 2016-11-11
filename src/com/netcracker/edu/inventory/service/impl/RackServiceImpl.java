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

    public void writeRack(Rack rack, Writer writer) throws IOException{
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        inputOutputOperations.writeRack(rack, writer);
    }

    public Rack readRack(Reader reader) throws IOException, ClassNotFoundException{
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        return inputOutputOperations.readRack(reader);
    }

    public void outputRack(Rack rack, OutputStream outputStream) throws IOException{
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        inputOutputOperations.outputRack(rack, outputStream);
    }

    public Rack inputRack(InputStream inputStream) throws IOException, ClassNotFoundException {
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        return inputOutputOperations.inputRack(inputStream);
    }

    public void serializeRack(Rack rack, OutputStream outputStream) throws IOException{
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        inputOutputOperations.serializeRack(rack, outputStream);
    }

    public Rack deserializeRack(InputStream inputStream) throws IOException, ClassCastException, ClassNotFoundException{
        InputOutputOperations inputOutputOperations = new InputOutputOperations();
        return inputOutputOperations.deserializeRack(inputStream);
    }
}
