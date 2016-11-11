package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.exception.DeviceValidationException;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.model.impl.*;
import com.netcracker.edu.location.Location;
import com.netcracker.edu.location.impl.*;

import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        StringBuilder resStr = new StringBuilder();
        resStr.append(device.getClass().getCanonicalName());
        resStr.append("\n[");
        resStr.append(device.getIn());
        resStr.append("] ");
        resStr.append(device.getType());
        resStr.append(" | ");
        if (device.getModel() == null)
            resStr.append("| ");
        else{
            resStr.append(device.getModel());
            resStr.append(" | ");
        }
        if (device.getManufacturer() == null)
            resStr.append("| ");
        else{
            resStr.append(device.getManufacturer());
            resStr.append(" | ");
        }
        if (device.getProductionDate() == null){
            resStr.append("-1 | ");
        }
        else {
            resStr.append(device.getProductionDate().getTime());
            resStr.append(" | ");
        }
        if (Router.class.isInstance(device)){
            resStr.append(((Router)device).getDataRate());
            if (device.getClass().equals(Router.class))
                resStr.append(" |\n");
            else
                resStr.append(" | ");
            if (Switch.class.isInstance(device)){
                resStr.append(((Switch)device).getNumberOfPorts());
                resStr.append(" |\n");
            }
            if (WifiRouter.class.isInstance(device)){
                if (((WifiRouter)device).getSecurityProtocol() == null)
                    resStr.append("|\n");
                else{
                    resStr.append(((WifiRouter)device).getSecurityProtocol());
                    resStr.append(" |\n");
                }
            }
        }
        if (Battery.class.isInstance(device)){
            resStr.append(((Battery)device).getChargeVolume());
            resStr.append(" |\n");
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
            StringTokenizer strTok = initDeviceChar(scan, device);
            temp = strTok.nextToken();
            if (!temp.equals(" "))
                ((Router)device).setDataRate(Integer.parseInt(temp.substring(1,temp.length()-1)));
        }
        if (classFromStream.equals(Switch.class)) {
            device = new Switch();
            StringTokenizer strTok = initDeviceChar(scan, device);
            temp = strTok.nextToken();
            if (!temp.equals(" "))
                ((Router)device).setDataRate(Integer.parseInt(temp.substring(1,temp.length()-1)));
            temp = strTok.nextToken();
            if (!temp.equals("  "))
                ((Switch) device).setNumberOfPorts(Integer.parseInt(temp.substring(1,temp.length()-1)));
        }
        if (classFromStream.equals(WifiRouter.class)){
            device = new WifiRouter();
            StringTokenizer strTok = initDeviceChar(scan, device);
            temp = strTok.nextToken();
            if (!temp.equals(" "))
                ((Router)device).setDataRate(Integer.parseInt(temp.substring(1,temp.length()-1)));
            temp = strTok.nextToken();
            if (!temp.equals(" "))
                ((WifiRouter)device).setSecurityProtocol(temp.substring(1,temp.length()-1));
        }
        if (classFromStream.equals(Battery.class)){
            device = new Battery();
            StringTokenizer strTok = initDeviceChar(scan, device);
            temp = strTok.nextToken();
            if (!temp.equals(" "))
                ((Battery)device).setChargeVolume(Integer.parseInt(temp.substring(1,temp.length()-1)));
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

        DataOutput dataOutput = new DataOutputStream(outputStream);
        dataOutput.writeUTF(device.getClass().getCanonicalName());
        dataOutput.writeInt(device.getIn());
        dataOutput.writeUTF(device.getType());
        if (device.getModel() == null)
            dataOutput.writeUTF("\n");
        else
            dataOutput.writeUTF(device.getModel());
        if (device.getManufacturer() == null)
            dataOutput.writeUTF("\n");
        else
            dataOutput.writeUTF(device.getManufacturer());
        if (device.getProductionDate() == null)
            dataOutput.writeLong(-1);
        else
            dataOutput.writeLong(device.getProductionDate().getTime());
        if (Router.class.isInstance(device)){
            dataOutput.writeInt(((Router)device).getDataRate());
            if (Switch.class.isInstance(device))
                dataOutput.writeInt(((Switch)device).getNumberOfPorts());
            if (WifiRouter.class.isInstance(device)){
                if (((WifiRouter)device).getSecurityProtocol() == null)
                    dataOutput.writeUTF("\n");
                else
                    dataOutput.writeUTF(((WifiRouter)device).getSecurityProtocol());
            }

        }
        if (Battery.class.isInstance(device)){
            dataOutput.writeInt(((Battery)device).getChargeVolume());
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
            initDevice(dataInput, device);
            ((Router)device).setDataRate(dataInput.readInt());
        }
        if (classFromStream.equals(Switch.class)) {
            device = new Switch();
            initDevice(dataInput, device);
            ((Router)device).setDataRate(dataInput.readInt());
            ((Switch) device).setNumberOfPorts(dataInput.readInt());
        }
        if (classFromStream.equals(WifiRouter.class)){
            device = new WifiRouter();
            initDevice(dataInput, device);
            ((Router)device).setDataRate(dataInput.readInt());
            s = dataInput.readUTF();
            if (!s.equals("\n"))
                ((WifiRouter)device).setSecurityProtocol(s);
        }
        if (classFromStream.equals(Battery.class)){
            device = new Battery();
            initDevice(dataInput, device);
            ((Battery)device).setChargeVolume(dataInput.readInt());
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

    void initDevice(DataInput dataInput, Device device) throws IOException{
        String s;

        int i = dataInput.readInt();
        if (i > 0)
            device.setIn(i);
        dataInput.readUTF();
        s = dataInput.readUTF();
        if (!s.equals("\n"))
            device.setModel(s);
        s = dataInput.readUTF();
        if (!s.equals("\n"))
            device.setManufacturer(s);
        Long l = dataInput.readLong();
        if (l != -1) {
            Date b = new Date();
            b.setTime(l);
            device.setProductionDate(b);
        }
    }

    StringTokenizer initDeviceChar(String s, Device device) throws IOException{
        int position = s.indexOf("]");
        int i = Integer.parseInt(s.substring(1,position));
        if (i > 0)
            device.setIn(i);
        String sWithoutIn = s.substring(position + 1);
        StringTokenizer sT = new StringTokenizer(sWithoutIn, "|");
        String temp;
        temp = sT.nextToken();
        temp = sT.nextToken();
        if (!temp.equals(" "))
            device.setModel(temp.substring(1,temp.length()-1));
        temp = sT.nextToken();
        if (!temp.equals(" "))
            device.setManufacturer(temp.substring(1,temp.length()-1));
        temp = sT.nextToken();
        if (!temp.trim().equals("-1")) {
            Date b = new Date(Long.parseLong(temp.trim()));
            device.setProductionDate(b);
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
