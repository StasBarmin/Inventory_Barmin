package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.exception.DeviceValidationException;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.FeelableEntity;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.model.impl.*;
import com.netcracker.edu.location.Location;
import com.netcracker.edu.location.impl.*;

import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.netcracker.edu.inventory.model.FeelableEntity.*;

/**
 * Created by barmin on 11.11.2016.
 */
 class InputOutputOperations {
    static protected Logger LOGGER = Logger.getLogger(DeviceServiceImpl.class.getName());

     void writeDevice(Device device, Writer writer) throws IOException {
        if (device == null)
            return ;
        if (writer == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
            LOGGER.log(Level.SEVERE, "Missing output stream", e);
            throw e;
        }
        DeviceValidator deviceValidator = new DeviceValidator();
        if (!deviceValidator.isValidDeviceForWriteToStream(device)){
            DeviceValidationException e = new DeviceValidationException("Device is not valid for operation. DeviceService.writeDevice()");
            LOGGER.log(Level.SEVERE, "Device is not valid for operation. DeviceService.writeDevice()", e);
            throw e;
        }

         Field[] fields = device.getAllFields();
        StringBuilder resStr = new StringBuilder();
        resStr.append(device.getClass().getCanonicalName());
        resStr.append("\n[");
        resStr.append(fields[0].getValue());
        resStr.append("] ");
         for (int i = 1; i < fields.length; i++){
             if (fields[i].getValue() == null)
                 if (i == 4)
                     resStr.append("-1 | ");
                 else
                     resStr.append("| ");
            else{
                 if (i == 4)
                     resStr.append(((Date)fields[i].getValue()).getTime());
                 else
                     resStr.append(fields[i].getValue());
                 if (i == fields.length - 1)
                     resStr.append(" |\n");
                 else
                     resStr.append(" | ");
             }
         }

         writer.write(resStr.toString());
    }

     Device readDevice(Reader reader) throws IOException, ClassNotFoundException{
        if (reader == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing input stream");
            LOGGER.log(Level.SEVERE, "Missing input stream", e);
            throw e;
        }

        String scan = readString(reader);

        if (scan.equals(""))
            return null;

        Class classFromStream;
        try {
            classFromStream =  Class.forName(scan);
        } catch (ClassNotFoundException e){
            LOGGER.log(Level.SEVERE, "Class " + scan + " was not found", e);
            throw e;
        }


        Device device = null;
        scan = readString(reader);
        String temp;

        if (classFromStream.equals(Router.class)){
            device = new Router();
            Field[] fields = new Field[6];
            StringTokenizer strTok = initDeviceChar(scan, fields);
            temp = strTok.nextToken();
            if (!temp.equals(" "))
                fields[5].setValue(temp.substring(1,temp.length()-1));
            device.feelAllFields(fields);
        }
        if (classFromStream.equals(Switch.class)) {
            device = new Switch();
            Field[] fields = new Field[7];
            StringTokenizer strTok = initDeviceChar(scan, fields);
            temp = strTok.nextToken();
            if (!temp.equals(" "))
                fields[5].setValue(temp.substring(1,temp.length()-1));
            temp = strTok.nextToken();
            if (!temp.equals("  "))
                fields[6].setValue(temp.substring(1,temp.length()-1));
            device.feelAllFields(fields);
        }
        if (classFromStream.equals(WifiRouter.class)){
            device = new WifiRouter();
            Field[] fields = new Field[7];
            StringTokenizer strTok = initDeviceChar(scan, fields);
            temp = strTok.nextToken();
            if (!temp.equals(" "))
                fields[5].setValue(temp.substring(1,temp.length()-1));
            temp = strTok.nextToken();
            if (!temp.equals(" "))
                fields[6].setValue(temp.substring(1,temp.length()-1));
            device.feelAllFields(fields);
        }
        if (classFromStream.equals(Battery.class)){
            device = new Battery();
            Field[] fields = new Field[6];
            StringTokenizer strTok = initDeviceChar(scan, fields);
            temp = strTok.nextToken();
            if (!temp.equals(" "))
                fields[5].setValue(temp.substring(1,temp.length()-1));
            device.feelAllFields(fields);
        }

        return device;
    }

     void outputDevice(Device device, OutputStream outputStream) throws IOException{
        if (device == null)
            return ;
        if (outputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
            LOGGER.log(Level.SEVERE, "Missing output stream", e);
            throw e;
        }

        Field[] fields = device.getAllFields();
        DataOutput dataOutput = new DataOutputStream(outputStream);
        dataOutput.writeUTF(device.getClass().getCanonicalName());
        dataOutput.writeInt((Integer) fields[0].getValue());

         for (int i = 1; i < fields.length; i++) {
             if (fields[i].getValue() == null)
                 if (i == 4)
                     dataOutput.writeLong(-1);
             else
                     dataOutput.writeUTF("\n");
             else
                 if (i == 4)
                     dataOutput.writeLong((Long)fields[i].getValue());
                 else
                     dataOutput.writeUTF((String) fields[i].getValue());
         }

        if (Router.class.isInstance(device)){
            dataOutput.writeInt((Integer) fields[5].getValue());
            if (Switch.class.isInstance(device))
                dataOutput.writeInt((Integer) fields[6].getValue());
            if (WifiRouter.class.isInstance(device)){
                if (fields[6].getValue() == null)
                    dataOutput.writeUTF("\n");
                else
                    dataOutput.writeUTF((String) fields[6].getValue());
            }
        }
        if (Battery.class.isInstance(device)){
            dataOutput.writeInt((Integer) fields[5].getValue());
        }
    }

     Device inputDevice(InputStream inputStream) throws IOException, ClassNotFoundException{
        if (inputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing input stream");
            LOGGER.log(Level.SEVERE, "Missing input stream", e);
            throw e;
        }

        DataInput dataInput = new DataInputStream(inputStream);
        Class classFromStream;
        String s = dataInput.readUTF();

        if (s.equals("\n"))
            return null;

        try {
            classFromStream =  Class.forName(s);
        } catch (ClassNotFoundException e){
            LOGGER.log(Level.SEVERE, "Class " + s + " was not found", e);
            throw e;
        }

        Device device = null;

        if (classFromStream.equals(Router.class)){
            device = new Router();
            Field[] fields = new Field[6];
            initDevice(dataInput, fields);
            fields[5].setValue(dataInput.readInt());
        }
        if (classFromStream.equals(Switch.class)) {
            device = new Switch();
            Field[] fields = new Field[7];
            initDevice(dataInput, fields);
            fields[5].setValue(dataInput.readInt());
            fields[6].setValue(dataInput.readInt());
        }
        if (classFromStream.equals(WifiRouter.class)){
            device = new WifiRouter();
            Field[] fields = new Field[7];
            initDevice(dataInput, fields);
            fields[5].setValue(dataInput.readInt());
            s = dataInput.readUTF();
            if (!s.equals("\n"))
                fields[6].setValue(s);
        }
        if (classFromStream.equals(Battery.class)){
            device = new Battery();
            Field[] fields = new Field[6];
            initDevice(dataInput, fields);
            fields[5].setValue(dataInput.readInt());
        }

        return device;
    }

     void serializeDevice(Device device, OutputStream outputStream) throws IOException{
        if (device == null)
            return ;
        if (outputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
            LOGGER.log(Level.SEVERE, "Missing output stream", e);
            throw e;
        }

        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(device);
    }

     Device deserializeDevice(InputStream inputStream) throws IOException, ClassCastException, ClassNotFoundException{
        if (inputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing input stream");
            LOGGER.log(Level.SEVERE, "Missing input stream", e);
            throw e;
        }

        ObjectInputStream ois = new ObjectInputStream(inputStream);
        Device device = (Device)ois.readObject();

        return device;
    }

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
        com.netcracker.edu.location.impl.ServiceImpl locServImpl = new com.netcracker.edu.location.impl.ServiceImpl();

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

        String firstStr = readString(reader);
        StringTokenizer sT = new StringTokenizer(firstStr);
        com.netcracker.edu.location.impl.ServiceImpl locServImpl = new com.netcracker.edu.location.impl.ServiceImpl();

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
        s = readString(reader);
        s = readString(reader);

        for (int i = 0; i < size; i++ ){
            Device d = dsi.readDevice(reader);
//            Skip empty line
            s = readString(reader);
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
        com.netcracker.edu.location.impl.ServiceImpl locServImpl = new com.netcracker.edu.location.impl.ServiceImpl();
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

        com.netcracker.edu.location.impl.ServiceImpl locServImpl = new com.netcracker.edu.location.impl.ServiceImpl();
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

    void initDevice(DataInput dataInput, Field[] fields) throws IOException{
        String s;

        int in = dataInput.readInt();
        if (in > 0)
            fields[0].setValue(in);
        for (int i = 1; i < 4; i++) {
            s = dataInput.readUTF();
            if (!s.equals("\n"))
                fields[i].setValue(s);
        }

        Long l = dataInput.readLong();
        if (l != -1) {
            fields[4].setValue(l);
        }
    }

    StringTokenizer initDeviceChar(String s, Field[] fields) throws IOException{

        int position = s.indexOf("]");
        int in = Integer.parseInt(s.substring(1,position));
        if (in > 0)
            fields[0].setValue(in);
        String sWithoutIn = s.substring(position + 1);
        StringTokenizer sT = new StringTokenizer(sWithoutIn, "|");
        String temp;

        for (int i = 1; i < 5; i++){
            temp = sT.nextToken();
            if (!temp.equals(" ") && i != 4)
                fields[i].setValue(temp.substring(1,temp.length()-1));
            else
                if (!temp.trim().equals("-1") && i == 4)
                    fields[i].setValue(temp.substring(1,temp.length()-1));
        }

        return sT;
    }

    static String readString(Reader reader) throws IOException{
        StringBuilder sb = new StringBuilder();

        do {
            int charV = reader.read();
            if (charV == -1 || charV == ((int)('\n')) )
                break;
            else
                sb.append(Character.toChars(charV));

        } while (true);

        return sb.toString();
    }
}
