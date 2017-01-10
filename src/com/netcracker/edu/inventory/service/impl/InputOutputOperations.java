package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.exception.DeviceValidationException;
import com.netcracker.edu.inventory.model.*;
import com.netcracker.edu.inventory.model.impl.*;
import com.netcracker.edu.inventory.service.Service;
import com.netcracker.edu.location.Location;
import com.netcracker.edu.location.Trunk;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.netcracker.edu.inventory.model.FeelableEntity.*;

/**
 * Created by barmin on 11.11.2016.
 */
 class InputOutputOperations {
    com.netcracker.edu.location.impl.ServiceImpl locServImpl = new com.netcracker.edu.location.impl.ServiceImpl();
    Service service = new ServiceImpl();
    static protected Logger LOGGER = Logger.getLogger(DeviceServiceImpl.class.getName());

    void writeDevice(Device device, Writer writer) throws IOException {
        if (device == null)
            return;

        Validator validator = new Validator();
        if (!validator.isValidDeviceForWriteToStream(device)) {
            DeviceValidationException e = new DeviceValidationException("Device is not valid for operation. DeviceService.writeDevice()");
            LOGGER.log(Level.SEVERE, "Device is not valid for operation. DeviceService.writeDevice()", e);
            throw e;
        }

        writeFillableEntity(device, writer);

    }

    void writeConnection(Connection connection, Writer writer) throws IOException {
        if (connection == null)
            return;

        Validator validator = new Validator();
        if (!validator.isValidConnectionForWriteToStream(connection)) {
            DeviceValidationException e = new DeviceValidationException("Connection is not valid for operation. ConnectionService.writeConnection()");
            LOGGER.log(Level.SEVERE, "Connection is not valid for operation. ConnectionService.writeConnection()", e);
            throw e;
        }

        writeFillableEntity(connection, writer);

    }

    Device readDevice(Reader reader) throws IOException, ClassNotFoundException {

        return (Device)readFillableEntity(reader);
    }

    Connection readConnection(Reader reader) throws IOException, ClassNotFoundException {

        return (Connection) readFillableEntity(reader);
    }

    void outputDevice(Device device, OutputStream outputStream) throws IOException {

        outputFillableEntity(device, outputStream);
    }

    void outputConnection(Connection connection, OutputStream outputStream) throws IOException {

        outputFillableEntity(connection, outputStream);
    }

    Device inputDevice(InputStream inputStream) throws IOException, ClassNotFoundException {

        return (Device)inputFillableEntity(inputStream);
    }

    Connection inputConnection(InputStream inputStream) throws IOException, ClassNotFoundException {

        return (Connection) inputFillableEntity(inputStream);
    }
    @Deprecated
    void serializeDevice(Device device, OutputStream outputStream) throws IOException {
        if (device == null)
            return;
        if (outputStream == null) {
            missingOutputStream();
        }

        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(device);
    }
    @Deprecated
    Device deserializeDevice(InputStream inputStream) throws IOException, ClassCastException, ClassNotFoundException {
        if (inputStream == null) {
            missingInputStream();
        }

        ObjectInputStream ois = new ObjectInputStream(inputStream);
        Device device = (Device) ois.readObject();

        return device;
    }

    public void writeRack(Rack rack, Writer writer) throws IOException {
        if (rack == null)
            return;
        if (writer == null) {
            missingOutputStream();
        }

        DeviceServiceImpl dsi = new DeviceServiceImpl();
        StringBuilder resStr = new StringBuilder();

        resStr.append(rack.getSize());
        resStr.append(" ");
        resStr.append(rack.getTypeOfDevices().getCanonicalName());
        resStr.append("\n\n\n");

        locServImpl.writeLocation(rack.getLocation(), writer);
        writer.write(resStr.toString());

        for (int i = 0; i < rack.getSize(); i++) {
            Device d = rack.getDevAtSlot(i);
            if (d != null) {
                dsi.writeDevice(d, writer);
                writer.write("\n");
            } else
                writer.write("\n\n");
        }
    }

    public Rack readRack(Reader reader) throws IOException, ClassNotFoundException {
        if (reader == null) {
            missingInputStream();
        }

        Location location = locServImpl.readLocation(reader);
        String firstStr = readString(reader);
        StringTokenizer sT = new StringTokenizer(firstStr);

        int size = Integer.parseInt(sT.nextToken());
        Class rackClazz;
        String s = sT.nextToken();

        rackClazz = classFromStream(s);

        Rack rack = new RackArrayImpl(size, rackClazz);
        rack.setLocation(location);
        DeviceServiceImpl dsi = new DeviceServiceImpl();

//        Skip 2 lines
        s = readString(reader);
        s = readString(reader);

        for (int i = 0; i < size; i++) {
            Device d = dsi.readDevice(reader);
//            Skip empty line
            s = readString(reader);
            if (d != null)
                rack.insertDevToSlot(d, i);
        }

        return rack;
    }

    public void outputRack(Rack rack, OutputStream outputStream) throws IOException {
        if (rack == null)
            return;
        if (outputStream == null) {
            missingOutputStream();
        }

        DeviceServiceImpl dsi = new DeviceServiceImpl();
        DataOutput dataOutput = new DataOutputStream(outputStream);

        locServImpl.outputLocation(rack.getLocation(), outputStream);
        dataOutput.writeInt(rack.getSize());
        dataOutput.writeUTF(rack.getTypeOfDevices().getCanonicalName());

        for (int i = 0; i < rack.getSize(); i++) {
            Device d = rack.getDevAtSlot(i);
            if (d != null)
                dsi.outputDevice(d, outputStream);
            else
                dataOutput.writeUTF("\n");
        }
    }

    public Rack inputRack(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputStream == null) {
            missingInputStream();
        }
        DataInput dataInput = new DataInputStream(inputStream);

        Location location = locServImpl.inputLocation(inputStream);
        int size = dataInput.readInt();
        String s = dataInput.readUTF();

        Class c = classFromStream(s);

        Rack rack = new RackArrayImpl(size, c);
        rack.setLocation(location);
        DeviceServiceImpl dsi = new DeviceServiceImpl();

        for (int i = 0; i < size; i++) {
            Device d = dsi.inputDevice(inputStream);
            if (d != null)
                rack.insertDevToSlot(d, i);
        }

        return rack;
    }
    @Deprecated
    public void serializeRack(Rack rack, OutputStream outputStream) throws IOException {
        if (rack == null)
            return;
        if (outputStream == null) {
            missingOutputStream();
        }

        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(rack);
    }
    @Deprecated
    public Rack deserializeRack(InputStream inputStream) throws IOException, ClassCastException, ClassNotFoundException {
        if (inputStream == null) {
            missingInputStream();
        }

        ObjectInputStream ois = new ObjectInputStream(inputStream);
        Rack rack = (Rack) ois.readObject();

        return rack;
    }


    StringTokenizer initDeviceChar(String s, List<Field> fields) throws IOException {

        int position = s.indexOf("]");
        int in = Integer.parseInt(s.substring(1, position));
        if (in > 0)
            fields.get(0).setValue(in);

        String sWithoutIn = s.substring(position + 1);
        StringTokenizer sT = new StringTokenizer(sWithoutIn, "|");

        return sT;
    }

    static String readString(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        do {
            int charV = reader.read();
            if (charV == -1 || charV == ((int) ('\n')))
                break;
            else
                sb.append(Character.toChars(charV));

        } while (true);

        return sb.toString();
    }

    Unique.PrimaryKey readPK(StringTokenizer stringTokenizer) throws IOException {
        String temp;

        if (!stringTokenizer.hasMoreTokens())
            return null;
        else
            temp = stringTokenizer.nextToken();
        if ("DPK:".equals(temp))
            return new DevicePK(Integer.parseInt(stringTokenizer.nextToken()));
        else {
            temp = stringTokenizer.nextToken();
            return new ConnectionPK(locServImpl.readTrunk(stringTokenizer), Integer.parseInt(temp));
        }
    }

    void writePK(Writer writer, Unique.PrimaryKey primaryKey) throws IOException {
        StringBuilder resString = new StringBuilder();
        if (DevicePrimaryKey.class.isAssignableFrom(primaryKey.getClass())){
            resString.append("DPK: ");
            resString.append(((DevicePK)primaryKey).getIn());
            resString.append(" | ");
            writer.write(resString.toString());
        }
        else {
            resString.append("CPK: ");
            resString.append(((ConnectionPK)primaryKey).getSerialNumber());
            writer.write(resString.toString());
            locServImpl.writeTrunk(((ConnectionPK)primaryKey).getTrunk(), writer);
            writer.write(" ");
        }
    }

    void outputPK(OutputStream outputStream, Unique.PrimaryKey primaryKey) throws IOException {
        DataOutput dataOutput = new DataOutputStream(outputStream);
        if (DevicePrimaryKey.class.isAssignableFrom(primaryKey.getClass())){
            dataOutput.writeUTF("DPK");
            dataOutput.writeInt(((DevicePK)primaryKey).getIn());
        }
        else {
            dataOutput.writeUTF("CPK");
            dataOutput.writeInt(((ConnectionPK)primaryKey).getSerialNumber());
            locServImpl.outputTrunk(((ConnectionPK)primaryKey).getTrunk(), outputStream);
        }
    }

    Unique.PrimaryKey inputPK(InputStream inputStream) throws IOException {
        DataInput dataInput = new DataInputStream(inputStream);
        String s = dataInput.readUTF();

        if (s.equals("\n"))
            return null;

        if ("CPK".equals(s)){
            int num = dataInput.readInt();
            return new ConnectionPK(locServImpl.inputTrunk(inputStream), num );
        }
        else
            return new DevicePK(dataInput.readInt());
    }

    void missingOutputStream() {
        IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
        LOGGER.log(Level.SEVERE, "Missing output stream", e);
        throw e;
    }

    void missingInputStream() {
        IllegalArgumentException e = new IllegalArgumentException("Missing input stream");
        LOGGER.log(Level.SEVERE, "Missing input stream", e);
        throw e;
    }

    Class classFromStream(String s) throws ClassNotFoundException {
        Class c;

        try {
            c = Class.forName(s);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Class " + s + " was not found", e);
            throw e;
        }

        return c;
    }

    void writeFillableEntity( FeelableEntity entity, Writer writer) throws IOException {
        if (writer == null) {
            missingOutputStream();
        }

        FeelableEntity copyPK = (FeelableEntity) service.getIndependentCopy((Unique) entity);
        List<Field> fields = copyPK.getAllFieldsList();
        StringBuilder resStr = new StringBuilder();
        resStr.append(entity.getClass().getCanonicalName());
        if (Device.class.isAssignableFrom(copyPK.getClass())) {
            resStr.append("\n[");
            resStr.append(fields.get(0).getValue());
            resStr.append("] ");
        }
        else{
            resStr.append("\n");
            resStr.append(fields.get(0).getValue());
            resStr.append(" | ");
        }
        for (int i = 1; i < fields.size(); i++) {
            if (fields.get(i).getValue() == null)
                if (fields.get(i).getType() == Date.class)
                    resStr.append("-1 | ");
                else
                    resStr.append("| ");
            else {
                if (fields.get(i).getType() == Date.class)
                    resStr.append(((Date) fields.get(i).getValue()).getTime());
                else if (FeelableEntity.class.isAssignableFrom(fields.get(i).getType())) {
                    writer.write(resStr.toString());
                    writePK(writer, (Unique.PrimaryKey)fields.get(i).getValue());
                    resStr = new StringBuilder();
                    if (i == fields.size() - 1)
                        resStr.append(" |\n");
                } else if (Collection.class.isAssignableFrom(fields.get(i).getType())) {
                    writer.write(resStr.toString());
                    resStr = new StringBuilder();
                    if (Switch.class.isAssignableFrom(copyPK.getClass())) {
                        Connection[] connections = (Connection[]) fields.get(i).getValue();
                        resStr.append(connections.length);
                        writer.write(resStr.toString());
                        writer.write(" |\n");
                        for (int j = 0; j < connections.length; j++) {
                            Connection connection1 = connections[j];
                            if (connection1 != null)
                                writePK(writer, (Unique.PrimaryKey)connections[j]);
                            else
                                writer.write(" |");
                            if (j == connections.length - 1)
                                writer.write(" ");
                        }
                    }
                    else {
                        resStr.append(((Collection) fields.get(i).getValue()).size());
                        writer.write(resStr.toString());
                        writer.write(" |\n");
                        Iterator iterator = ((Collection) fields.get(i).getValue()).iterator();
                        Unique.PrimaryKey collectionPK;
                        for (int j = 0; j < ((Collection) fields.get(i).getValue()).size(); j++) {
                            collectionPK = (Unique.PrimaryKey) iterator.next();
                            if (collectionPK != null)
                                writePK(writer, collectionPK);
                            else
                                writer.write(" |");
                            if (j == ((Collection) fields.get(i).getValue()).size() - 1)
                                writer.write(" ");
                        }
                    }
                    writer.write("\n");
                    resStr = new StringBuilder();
                } else if (Trunk.class.equals(fields.get(i).getType())){
                    writer.write(resStr.toString());
                    locServImpl.writeTrunk((Trunk) fields.get(i).getValue(), writer);
                    resStr = new StringBuilder(" ");
                }
                else
                    resStr.append(fields.get(i).getValue());
                if (fields.get(i).getType() != List.class && fields.get(i).getType() != Trunk.class && !FeelableEntity.class.isAssignableFrom(fields.get(i).getType()))
                    if (i == fields.size() - 1)
                        resStr.append(" |\n");
                    else
                        resStr.append(" | ");
            }
        }
        writer.write(resStr.toString());
    }

    FeelableEntity readFillableEntity(Reader reader) throws IOException, ClassNotFoundException {
        if (reader == null) {
            missingInputStream();
        }

        String scan = readString(reader);

        if (scan.equals(""))
            return null;

        Class classFromStream = classFromStream(scan);

        FeelableEntity entity = Utilities.createDevice_Connection(classFromStream);
        scan = readString(reader);
        String temp;

        List<Field> fields = entity.getAllFieldsList();
        StringTokenizer strTok = new StringTokenizer(scan, "|");
        if (Device.class.isAssignableFrom(entity.getClass())) {
            strTok = initDeviceChar(scan, fields);
        }
        else{
            temp = strTok.nextToken();
            fields.get(0).setValue(temp.substring(0, temp.length() - 1));
        }

        for (int i = 1; i < fields.size(); i++) {
            temp = strTok.nextToken();

            if (fields.get(i).getType() == Integer.class) {
                if (!temp.equals(" "))
                    fields.get(i).setValue(Integer.parseInt(temp.substring(1, temp.length() - 1)));
            } else if (fields.get(i).getType() == Date.class) {
                if (!temp.trim().equals("-1")) {
                    Date b = new Date(Long.parseLong(temp.trim()));
                    fields.get(i).setValue(b);
                }
            } else if (FeelableEntity.class.isAssignableFrom(fields.get(i).getType())) {
                StringTokenizer stringTokenizer = new StringTokenizer(temp);
                fields.get(i).setValue(readPK(stringTokenizer));
            } else if (Collection.class.isAssignableFrom(fields.get(i).getType())) {
                if (!temp.equals(" ")) {
                    int size = Integer.parseInt(temp.substring(1, temp.length() - 1));
                    temp = readString(reader);
                    strTok = new StringTokenizer(temp, "|");
                    StringTokenizer stringTokenizer = new StringTokenizer(strTok.nextToken());
                    if (Switch.class.isAssignableFrom(entity.getClass())) {
                        Connection[] connections = new Connection[size];
                        for (int j = 0; j < size; j++) {
                            Connection connection1 = (ConnectionPK) readPK(stringTokenizer);
                            connections[j] = connection1;
                            stringTokenizer = new StringTokenizer(strTok.nextToken());
                        }
                        fields.get(i).setValue(connections);
                    }
                    else {
                        Collection<Device> collection;
                        if (List.class.isAssignableFrom(fields.get(i).getType()))
                            collection = new ArrayList<Device>(size);
                        else
                            collection = new HashSet<Device>(size);
                        for (int j = 0; j < size; j++) {
                            collection.add((DevicePK)readPK(stringTokenizer));
                            stringTokenizer = new StringTokenizer(strTok.nextToken());
                        }
                        fields.get(i).setValue(collection);
                    }
                }
            } else if (Trunk.class.equals(fields.get(i).getType())){
                if (!temp.equals(" ")){
                    StringTokenizer stringTokenizer = new StringTokenizer(temp);
                    fields.get(i).setValue(locServImpl.readTrunk(stringTokenizer));
                }
            }
            else if (!temp.equals(" "))
                fields.get(i).setValue(temp.substring(1, temp.length() - 1));
        }

        entity.fillAllFields(fields);

        return entity;
    }

    void outputFillableEntity(FeelableEntity entity, OutputStream outputStream) throws IOException {
        if (entity == null)
            return;
        if (outputStream == null) {
            missingOutputStream();
        }

        FeelableEntity copyPK = (FeelableEntity) service.getIndependentCopy((Unique)entity);
        List<Field> fields = copyPK.getAllFieldsList();
        DataOutput dataOutput = new DataOutputStream(outputStream);
        dataOutput.writeUTF(entity.getClass().getCanonicalName());

        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).getValue() == null)
                if (fields.get(i).getType() == Date.class)
                    dataOutput.writeLong(-1);
                else
                    dataOutput.writeUTF("\n");
            else if (fields.get(i).getType() == Date.class)
                dataOutput.writeLong(((Date) fields.get(i).getValue()).getTime());
            else if (fields.get(i).getType() == Integer.class)
                dataOutput.writeInt((Integer) fields.get(i).getValue());
            else if (FeelableEntity.class.isAssignableFrom(fields.get(i).getType()))
                outputPK (outputStream, (Unique.PrimaryKey)fields.get(i).getValue());
            else if (Collection.class.isAssignableFrom(fields.get(i).getType())) {
                if (Switch.class.isAssignableFrom(copyPK.getClass())) {
                    Connection[] connections = (Connection[]) fields.get(i).getValue();
                    dataOutput.writeInt(connections.length);
                    for (int j = 0; j < connections.length; j++) {
                        Connection connection1 = connections[j];
                        if (connection1 != null)
                            outputPK(outputStream,(Unique.PrimaryKey) connection1);
                        else
                            dataOutput.writeUTF("\n");
                    }
                }
                else {
                    dataOutput.writeInt(((Collection) fields.get(i).getValue()).size());
                    Iterator iterator = ((Collection) fields.get(i).getValue()).iterator();
                    Unique.PrimaryKey collectionPK;
                    for (int j = 0; j < ((Collection) fields.get(i).getValue()).size(); j++) {
                        collectionPK = (Unique.PrimaryKey) iterator.next();
                        if (collectionPK != null)
                            outputPK(outputStream, collectionPK);
                        else
                            dataOutput.writeUTF("\n");
                    }
                }
            }
            else if (Trunk.class.equals(fields.get(i).getType()))
                locServImpl.outputTrunk((Trunk) fields.get(i).getValue(), outputStream);
            else
                dataOutput.writeUTF(fields.get(i).getValue().toString());
        }

    }

    FeelableEntity inputFillableEntity(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputStream == null) {
            missingInputStream();
        }

        DataInput dataInput = new DataInputStream(inputStream);
        Class classFromStream;
        String s = dataInput.readUTF();

        if (s.equals("\n"))
            return null;

        classFromStream = classFromStream(s);

        FeelableEntity entity =  Utilities.createDevice_Connection(classFromStream);
        int v_int;
        String v_string;
        Long v_long;


        List<Field> fields = entity.getAllFieldsList();

        for (int i = 0; i < fields.size(); i++) {

            if (fields.get(i).getType() == Integer.class) {
                v_int = dataInput.readInt();
                fields.get(i).setValue(v_int);
            } else if (fields.get(i).getType() == Date.class) {
                v_long = dataInput.readLong();
                if (v_long != -1) {
                    Date b = new Date(v_long);
                    fields.get(i).setValue(b);
                }
            } else if (FeelableEntity.class.isAssignableFrom(fields.get(i).getType())) {
                fields.get(i).setValue(inputPK(inputStream));
            } else if (Collection.class.isAssignableFrom(fields.get(i).getType())) {
                int size = dataInput.readInt();
                if (Switch.class.isAssignableFrom(entity.getClass())) {
                    Connection[] connections = new Connection[size];
                    for (int j = 0; j < size; j++) {
                        Connection connection1 = (ConnectionPK)inputPK(inputStream);
                        connections[j] = connection1;
                    }
                    fields.get(i).setValue(connections);
                }
                else {
                    Collection collection;
                    if (List.class.isAssignableFrom(fields.get(i).getType()))
                        collection = new ArrayList<Device>(size);
                    else
                        collection = new HashSet<Device>(size);
                    for (int j = 0; j < size; j++) {
                        collection.add(inputPK(inputStream));
                    }
                    fields.get(i).setValue(collection);
                }
            }else if (Trunk.class.equals(fields.get(i).getType()))
                fields.get(i).setValue(locServImpl.inputTrunk(inputStream));
            else {
                v_string = dataInput.readUTF();
                if (!v_string.equals("\n"))
                    fields.get(i).setValue(v_string);
            }
        }

        entity.fillAllFields(fields);

        return entity;
    }
}
