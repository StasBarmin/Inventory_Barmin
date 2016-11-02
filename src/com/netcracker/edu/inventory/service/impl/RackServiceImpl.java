package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.model.impl.RackArrayImpl;
import com.netcracker.edu.inventory.service.RackService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmin on 29.10.2016.
 */
class RackServiceImpl implements RackService{
    static protected Logger LOGGER = Logger.getLogger(RackServiceImpl.class.getName());

    public void writeRack(Rack rack, Writer writer) throws IOException{
        NotImplementedException e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "Method is not implemented", e);
        throw e;
    }

    public Rack readRack(Reader reader) throws IOException{
        NotImplementedException e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "Method is not implemented", e);
        throw e;
    }

    public void outputRack(Rack rack, OutputStream outputStream) throws IOException{
        if (rack == null)
            return ;
        if (outputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
            LOGGER.log(Level.SEVERE, "Missing output stream", e);
            throw e;
        }

        DeviceServiceImpl dsi = new DeviceServiceImpl();
        DataOutput dout = new DataOutputStream(outputStream);

        DataOutput dataOutput = new DataOutputStream(outputStream);
        dataOutput.writeInt(rack.getSize());
        dataOutput.writeUTF(rack.getTypeOfDevices().getCanonicalName());

        for (int i = 0; i < rack.getSize(); i++ ){
            Device d = rack.getDevAtSlot(i);
            if (d != null)
            dsi.outputDevice(d, outputStream);
            else
                dout.writeUTF("\n");
        }
    }

    public Rack inputRack(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing input stream");
            LOGGER.log(Level.SEVERE, "Missing input stream", e);
            throw e;
        }
        DataInput dataInput = new DataInputStream(inputStream);

        int size = dataInput.readInt();
        Class c;
        String s = dataInput.readUTF();
        try {
            c =  Class.forName(s);
        } catch (ClassNotFoundException e){
            LOGGER.log(Level.SEVERE, "Class " + s + " was not found", e);
            throw e;
        }

        Rack rack = new RackArrayImpl(size, c);
        DeviceServiceImpl dsi = new DeviceServiceImpl();

        for (int i = 0; i < size; i++ ){
            Device d = dsi.inputDevice(inputStream);
            if (d != null)
                rack.insertDevToSlot(d,i);
        }

        return rack;
    }

    public void serializeRack(Rack rack, OutputStream outputStream) throws IOException{
        NotImplementedException e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "Method is not implemented", e);
        throw e;
    }

    public Rack deserializeRack(InputStream inputStream) throws IOException, ClassCastException{
        NotImplementedException e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "Method is not implemented", e);
        throw e;
    }
}
