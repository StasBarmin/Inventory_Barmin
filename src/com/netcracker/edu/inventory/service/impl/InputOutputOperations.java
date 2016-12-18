package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.exception.DeviceValidationException;
import com.netcracker.edu.inventory.model.Connection;
import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.model.impl.*;
import com.netcracker.edu.location.Location;

import java.io.*;
import java.util.*;
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
        Validator validator = new Validator();
        if (!validator.isValidDeviceForWriteToStream(device)){
            DeviceValidationException e = new DeviceValidationException("Device is not valid for operation. DeviceService.writeDevice()");
            LOGGER.log(Level.SEVERE, "Device is not valid for operation. DeviceService.writeDevice()", e);
            throw e;
        }

        List<Field> fields = device.getAllFieldsList();
        StringBuilder resStr = new StringBuilder();
         boolean flag2connection = false;
        resStr.append(device.getClass().getCanonicalName());
        resStr.append("\n[");
        resStr.append(fields.get(0).getValue());
        resStr.append("] ");
         for (int i = 1; i < fields.size(); i++){
             if (fields.get(i).getValue() == null)
                 if (fields.get(i).getType() == Date.class)
                     resStr.append("-1 | ");
                 else
                 if (fields.get(i).getType() == Connection.class) {
                     if (flag2connection)
                         resStr.append("\n");
                     resStr.append("\n");
                     flag2connection = true;
                 }
                 else
                     resStr.append("| ");
            else{
                 if (fields.get(i).getType() == Date.class)
                     resStr.append(((Date)fields.get(i).getValue()).getTime());
                 else
                     if (fields.get(i).getType() == Connection.class){
                         if (!flag2connection)
                         resStr.append(" |\n");
                         writer.write(resStr.toString());
                         Connection connection = (Connection) fields.get(i).getValue();
                         writeConnection(connection, writer);
                         }
                     else
                         if (fields.get(i).getType() == List.class){
                             Connection[] connections = (Connection[]) fields.get(i).getValue();
                             writer.write(resStr.toString());
                             resStr = new StringBuilder();
                             resStr.append(connections.length);
                             resStr.append(" |\n");
                             writer.write(resStr.toString());
                             for (int j = 0; j < connections.length; j++) {
                                 Connection connection = connections[j];
                                 if (connection != null)
                                 writeConnection(connection, writer);
                                 else {
                                     String str = "\n";
                                     writer.write(str);
                                 }
                             }
                             resStr = new StringBuilder();
                         }
                         else
                            resStr.append(fields.get(i).getValue());
                 if (fields.get(i).getType() != List.class && fields.get(i).getType() != Connection.class)
                 if (i == fields.size() - 1 )
                     resStr.append(" |\n");
                 else
                     resStr.append(" | ");
             }
         }
         writer.write(resStr.toString());
     }

    void writeConnection(Connection connection, Writer writer) throws IOException{
        if (connection == null)
            return ;
        if (writer == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
            LOGGER.log(Level.SEVERE, "Missing output stream", e);
            throw e;
        }
        Validator validator = new Validator();
        if (!validator.isValidConnectionForWriteToStream(connection)){
            DeviceValidationException e = new DeviceValidationException("Connection is not valid for operation. ConnectionService.writeConnection()");
            LOGGER.log(Level.SEVERE, "Connection is not valid for operation. ConnectionService.writeConnection()", e);
            throw e;
        }

        List<Field> fields = connection.getAllFieldsList();
        StringBuilder resStr = new StringBuilder();
        resStr.append(connection.getClass().getCanonicalName());
        resStr.append("\n ");

        for (int i = 0; i < fields.size(); i++){
            if (fields.get(i).getValue() == null || fields.get(i).getType() == Device.class)
                resStr.append("| ");
            else{
                if (Collection.class.isAssignableFrom(fields.get(i).getType())) {
                            Collection collection = (Collection) fields.get(i).getValue();
                            writeDeviceCollection(resStr, collection);
                        }
                else
                    resStr.append(fields.get(i).getValue());
                if (i == fields.size() - 1)
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

         if (classFromStream.equals(Router.class))
             device = new Router();

         if (classFromStream.equals(Switch.class))
             device = new Switch();

         if (classFromStream.equals(WifiRouter.class))
             device = new WifiRouter();

         if (classFromStream.equals(Battery.class))
             device = new Battery();

         List<Field> fields = device.getAllFieldsList();
         StringTokenizer strTok = initDeviceChar(scan, fields);

         for (int i = 1; i < fields.size(); i++) {
             temp = strTok.nextToken();

             if (fields.get(i).getType() == Integer.class){
                 if (!temp.equals(" "))
                     fields.get(i).setValue(Integer.parseInt(temp.substring(1,temp.length()-1)));}
             else
             if (fields.get(i).getType() == Date.class){
                 if (!temp.trim().equals("-1")){
                     Date b = new Date(Long.parseLong(temp.trim()));
                     fields.get(i).setValue(b);
                 }}
             else
             if (fields.get(i).getType() == Connection.class){
                 fields.get(i).setValue(readConnection(reader));
                 scan = " a";
                 strTok = new StringTokenizer(scan);
             }
             else
                if (fields.get(i).getType() == List.class){
                         if (!temp.equals(" ")){
                            int size =  Integer.parseInt(temp.substring(1,temp.length()-1));
                             Connection[] connections = new Connection[size];
                             for (int j = 0; j < size; j++){
                                 Connection connection = readConnection(reader);
                                 connections[j] = connection;
                             }
                             fields.get(i).setValue(connections);
                     }}
                else
                  if (!temp.equals(" "))
                      fields.get(i).setValue(temp.substring(1, temp.length() - 1));

             }

         device.fillAllFields(fields);

        return device;
    }

    Connection readConnection(Reader reader) throws IOException, ClassNotFoundException{
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

        Connection connection = null;
        scan = readString(reader);
        String temp;

        if (classFromStream.equals(TwistedPair.class))
            connection = new TwistedPair();

        if (classFromStream.equals(OpticFiber.class))
            connection = new OpticFiber();

        if (classFromStream.equals(Wireless.class))
            connection = new Wireless();

        if (classFromStream.equals(ThinCoaxial.class))
            connection = new ThinCoaxial();

        List<Field> fields = connection.getAllFieldsList();
        StringTokenizer strTok = new StringTokenizer(scan, "|");

        for (int i = 0; i < fields.size(); i++) {
            temp = strTok.nextToken();

            if (fields.get(i).getType() == Integer.class){
                if (!temp.equals(" "))
                    fields.get(i).setValue(Integer.parseInt(temp.substring(1,temp.length()-1)));}
            else
                if (Collection.class.isAssignableFrom(fields.get(i).getType())){
                    int size = Integer.parseInt(temp.substring(1,temp.length()-1));
                    if (List.class.isAssignableFrom(fields.get(i).getType()))
                        fields.get(i).setValue(new ArrayList<Device>(size));
                    else
                        fields.get(i).setValue(new HashSet<Device>(size));
                }
                else
                    if (!temp.equals(" "))
                        fields.get(i).setValue(temp.substring(1, temp.length() - 1));
        }

        connection.fillAllFields(fields);

        return connection;
    }

     void outputDevice(Device device, OutputStream outputStream) throws IOException{
        if (device == null)
            return ;
        if (outputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
            LOGGER.log(Level.SEVERE, "Missing output stream", e);
            throw e;
        }

         List<Field> fields = device.getAllFieldsList();
        DataOutput dataOutput = new DataOutputStream(outputStream);
        dataOutput.writeUTF(device.getClass().getCanonicalName());

         for (int i = 0; i < fields.size(); i++) {
             if (fields.get(i).getValue() == null)
                 if (fields.get(i).getType() == Date.class)
                     dataOutput.writeLong(-1);
             else
                     dataOutput.writeUTF("\n");
             else
                 if (fields.get(i).getType() == Date.class)
                     dataOutput.writeLong(((Date)fields.get(i).getValue()).getTime());
                 else
                     if (fields.get(i).getType() == Integer.class)
                         dataOutput.writeInt((Integer) fields.get(i).getValue());
                     else
                         if (fields.get(i).getType() == Connection.class)
                             outputConnection((Connection)fields.get(i).getValue(),outputStream);
             else
                 if (fields.get(i).getType() == List.class){
                     Connection[] connections = (Connection[]) fields.get(i).getValue();
                     dataOutput.writeInt(connections.length);
                     for (int j = 0; j < connections.length; j++) {
                         Connection connection = connections[j];
                         if (connection != null)
                         outputConnection(connection, outputStream);
                         else
                             dataOutput.writeUTF("\n");
                     }
                 }
                 else
                     dataOutput.writeUTF(fields.get(i).getValue().toString());
         }

     }

    void outputConnection(Connection connection, OutputStream outputStream) throws IOException{
        if (connection == null)
            return ;
        if (outputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
            LOGGER.log(Level.SEVERE, "Missing output stream", e);
            throw e;
        }

        List<Field> fields = connection.getAllFieldsList();
        DataOutput dataOutput = new DataOutputStream(outputStream);
        dataOutput.writeUTF(connection.getClass().getCanonicalName());

        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).getValue() == null)
                dataOutput.writeUTF("\n");
            else
                if (fields.get(i).getType() == Integer.class)
                dataOutput.writeInt((Integer) fields.get(i).getValue());
            else
                dataOutput.writeUTF(fields.get(i).getValue().toString());
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
        int v_int;
        String v_string;
        Long v_long;

        if (classFromStream.equals(Router.class))
            device = new Router();

         if (classFromStream.equals(Switch.class))
             device = new Switch();

         if (classFromStream.equals(WifiRouter.class))
             device = new WifiRouter();

         if (classFromStream.equals(Battery.class))
             device = new Battery();

         List<Field> fields = device.getAllFieldsList();

         for (int i = 0; i < fields.size(); i++) {

             if (fields.get(i).getType() == Integer.class){
                 v_int = dataInput.readInt();
                 fields.get(i).setValue(v_int);}
             else
             if (fields.get(i).getType() == Date.class) {
                         v_long = dataInput.readLong();
                         if (v_long != -1) {
                             Date b = new Date(v_long);
                             fields.get(i).setValue(b);
                         }
             }
             else
             if (fields.get(i).getType() == Connection.class){
                 fields.get(i).setValue(inputConnection(inputStream));}
             else
             if (fields.get(i).getType() == List.class)
                 {
                     int size =  dataInput.readInt();
                     Connection[] connections = new Connection[size];
                     for (int j = 0; j < size; j++){
                         Connection connection = inputConnection(inputStream);
                         connections[j] = connection;
                     }
                     fields.get(i).setValue(connections);
                 }
                 else{
                 v_string = dataInput.readUTF();
                 if (!v_string.equals("\n"))
                 fields.get(i).setValue(v_string);
             }
         }

         device.fillAllFields(fields);

        return device;
    }

    Connection inputConnection(InputStream inputStream) throws IOException, ClassNotFoundException{
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

        Connection connection = null;
        int v_int;
        String v_string;

        if (classFromStream.equals(TwistedPair.class))
            connection = new TwistedPair();

        if (classFromStream.equals(OpticFiber.class))
            connection = new OpticFiber();

        if (classFromStream.equals(Wireless.class))
            connection = new Wireless();

        if (classFromStream.equals(ThinCoaxial.class))
            connection = new ThinCoaxial();

        List<Field> fields = connection.getAllFieldsList();

        for (int i = 0; i < fields.size(); i++) {

            if (fields.get(i).getType() == Integer.class){
                v_int = dataInput.readInt();
                fields.get(i).setValue(v_int);}
            else
            if (Collection.class.isAssignableFrom(fields.get(i).getType())){
                int size = dataInput.readInt();
                if (List.class.isAssignableFrom(fields.get(i).getType()))
                    fields.get(i).setValue(new ArrayList<Device>(size));
                else
                    fields.get(i).setValue(new HashSet<Device>(size));
            }
            else
            {
                v_string = dataInput.readUTF();
                if (!v_string.equals("\n"))
                    fields.get(i).setValue(v_string);
            }
        }

        connection.fillAllFields(fields);

        return connection;
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

        com.netcracker.edu.location.impl.ServiceImpl locServImpl = new com.netcracker.edu.location.impl.ServiceImpl();

        Location location = locServImpl.readLocation(reader);
        String firstStr = readString(reader);
        StringTokenizer sT = new StringTokenizer(firstStr);

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


    StringTokenizer initDeviceChar(String s, List<Field> fields) throws IOException{

        int position = s.indexOf("]");
        int in = Integer.parseInt(s.substring(1,position));
        if (in > 0)
        fields.get(0).setValue(in);

        String sWithoutIn = s.substring(position + 1);
        StringTokenizer sT = new StringTokenizer(sWithoutIn, "|");

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

    void writeDeviceCollection(StringBuilder resString, Collection collection){

        resString.append(collection.size());
        resString.append(" | ");
        for (int i = 0; i < collection.size(); i++){
            resString.append("| ");
        }

    }
}
