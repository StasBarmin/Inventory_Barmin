package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.model.impl.RackArrayImpl;
import com.netcracker.edu.inventory.service.RackService;
import com.netcracker.edu.inventory.service.impl.DeviceServiceImpl;
import com.netcracker.edu.location.Location;
import com.netcracker.edu.location.impl.*;
import com.netcracker.edu.location.impl.ServiceImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmin on 29.10.2016.
 */
class RackServiceImpl implements RackService{
    static protected Logger LOGGER = Logger.getLogger(RackServiceImpl.class.getName());

    public void writeRack(Rack rack, Writer writer) throws IOException{
        if (rack == null)
            return ;
        if (writer == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
            LOGGER.log(Level.SEVERE, "Missing output stream", e);
            throw e;
        }

        DeviceServiceImpl dsi = new DeviceServiceImpl();
        StringBuilder resStr = new StringBuilder();
        com.netcracker.edu.location.impl.ServiceImpl locServImpl = new ServiceImpl();

        resStr.append(rack.getSize());
        resStr.append(" ");
        resStr.append(rack.getTypeOfDevices().getCanonicalName());
        resStr.append("\n\n\n");

        locServImpl.writeLocation(rack.getLocation(), writer);
        writer.write(resStr.toString());

        for (int i = 0; i < rack.getSize(); i++ ){
            Device d = rack.getDevAtSlot(i);
            if (d != null) {
                dsi.writeDevice(d, writer);
                writer.write("\n");
            }
            else
                writer.write("\n\n");
        }
    }

    public Rack readRack(Reader reader) throws IOException, ClassNotFoundException{
        if (reader == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing input stream");
            LOGGER.log(Level.SEVERE, "Missing input stream", e);
            throw e;
        }

        String firstStr = DeviceServiceImpl.readString(reader);
        StringTokenizer sT = new StringTokenizer(firstStr);
        com.netcracker.edu.location.impl.ServiceImpl locServImpl = new ServiceImpl();

        Location location = locServImpl.readLocation(reader);
        int size = Integer.parseInt(sT.nextToken());
        Class rackClazz;
        String s = sT.nextToken();
        try {
            rackClazz =  Class.forName(s);
        } catch (ClassNotFoundException e){
            LOGGER.log(Level.SEVERE, "Class " + s + " was not found", e);
            throw e;
        }

        Rack rack = new RackArrayImpl(size, rackClazz);
        rack.setLocation(location);
        DeviceServiceImpl dsi = new DeviceServiceImpl();

//        Skip 2 lines
        s = DeviceServiceImpl.readString(reader);
        s = DeviceServiceImpl.readString(reader);

        for (int i = 0; i < size; i++ ){
            Device d = dsi.readDevice(reader);
//            Skip empty line
            s = DeviceServiceImpl.readString(reader);
            if (d != null)
                rack.insertDevToSlot(d,i);
        }

        return rack;
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
        com.netcracker.edu.location.impl.ServiceImpl locServImpl = new ServiceImpl();
//        DataOutput dout = new DataOutputStream(outputStream);

        DataOutput dataOutput = new DataOutputStream(outputStream);

        locServImpl.outputLocation(rack.getLocation(), outputStream);
        dataOutput.writeInt(rack.getSize());
        dataOutput.writeUTF(rack.getTypeOfDevices().getCanonicalName());

        for (int i = 0; i < rack.getSize(); i++ ){
            Device d = rack.getDevAtSlot(i);
            if (d != null)
            dsi.outputDevice(d, outputStream);
            else
                dataOutput.writeUTF("\n");
        }
    }

    public Rack inputRack(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing input stream");
            LOGGER.log(Level.SEVERE, "Missing input stream", e);
            throw e;
        }
        DataInput dataInput = new DataInputStream(inputStream);

        com.netcracker.edu.location.impl.ServiceImpl locServImpl = new ServiceImpl();
        Location location = locServImpl.inputLocation(inputStream);
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
        rack.setLocation(location);
        DeviceServiceImpl dsi = new DeviceServiceImpl();

        for (int i = 0; i < size; i++ ){
            Device d = dsi.inputDevice(inputStream);
            if (d != null)
                rack.insertDevToSlot(d,i);
        }

        return rack;
    }

    public void serializeRack(Rack rack, OutputStream outputStream) throws IOException{
        if (rack == null)
            return ;
        if (outputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
            LOGGER.log(Level.SEVERE, "Missing output stream", e);
            throw e;
        }

        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(rack);
    }

    public Rack deserializeRack(InputStream inputStream) throws IOException, ClassCastException, ClassNotFoundException{
        if (inputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing input stream");
            LOGGER.log(Level.SEVERE, "Missing input stream", e);
            throw e;
        }

        ObjectInputStream ois = new ObjectInputStream(inputStream);
        Rack rack = (Rack) ois.readObject();

        return rack;
    }
}
