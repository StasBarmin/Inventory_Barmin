package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.impl.Battery;
import com.netcracker.edu.inventory.model.impl.Router;
import com.netcracker.edu.inventory.model.impl.Switch;
import com.netcracker.edu.inventory.model.impl.WifiRouter;
import com.netcracker.edu.inventory.service.DeviceService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Created by barmin on 20.10.2016.
 */
 class DeviceServiceImpl implements DeviceService{
    static protected Logger LOGGER = Logger.getLogger(DeviceServiceImpl.class.getName());

    public boolean isCastableTo(Device device, Class clazz){
        if (clazz != null) {
            if (clazz.isInstance(device))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public boolean isValidDeviceForInsertToRack(Device device){
        if (device == null || device.getIn() == 0 || device.getType() == null)
            return false;
        else
            return true;
    }

    public boolean isValidDeviceForWriteToStream(Device device){
        boolean flag = true;
        if (device.getType().contains("|"))
            flag = false;
        if (device.getManufacturer().contains("|"))
            flag = false;
        if (device.getModel().contains("|"))
            flag = false;
        if (WifiRouter.class.isInstance(device)){
            if (((WifiRouter)device).getSecurityProtocol().contains("|"))
                flag = false;
        }
        return flag;
    }

    public void writeDevice(Device device, Writer writer) throws IOException{
        NotImplementedException e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "Method is not implemented", e);
        throw e;

    }

    public Device readDevice(Reader reader) throws IOException, ClassNotFoundException{
        NotImplementedException e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "Method is not implemented", e);
        throw e;
    }

    public void outputDevice(Device device, OutputStream outputStream) throws IOException{
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

    public Device inputDevice(InputStream inputStream) throws IOException, ClassNotFoundException{
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

    public void serializeDevice(Device device, OutputStream outputStream) throws IOException{
        NotImplementedException e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "Method is not implemented", e);
        throw e;
    }

    public Device deserializeDevice(InputStream inputStream) throws IOException, ClassCastException{
        NotImplementedException e = new NotImplementedException();
        LOGGER.log(Level.SEVERE, "Method is not implemented", e);
        throw e;
    }

    void initDevice (DataInput dataInput, Device device) throws IOException{
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
}
