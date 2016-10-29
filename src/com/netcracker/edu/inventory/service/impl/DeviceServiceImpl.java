package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.impl.Battery;
import com.netcracker.edu.inventory.model.impl.Router;
import com.netcracker.edu.inventory.model.impl.Switch;
import com.netcracker.edu.inventory.model.impl.WifiRouter;
import com.netcracker.edu.inventory.service.DeviceService;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Class.forName;

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

    public void writeDevice(Device device, Writer writer) throws IOException{

    }

    public Device readDevice(Reader reader) throws IOException, ClassNotFoundException{
        return null;
    }

    public void outputDevice(Device device, OutputStream outputStream) throws IOException, IllegalArgumentException{
        if (device == null)
           return ;
        if (outputStream == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing output stream");
            LOGGER.log(Level.SEVERE, "Missing output stream", e);
            throw e;
        }

        DataOutput dataOutput = new DataOutputStream(outputStream);

        dataOutput.writeChars(device.getClass().getCanonicalName());
        dataOutput.writeInt(device.getIn());
        if (device.getType() == null)
            dataOutput.writeChars("\n");
        else
            dataOutput.writeChars(device.getType());
        if (device.getModel() == null)
            dataOutput.writeChars("\n");
        else
            dataOutput.writeChars(device.getModel());
        if (device.getManufacturer() == null)
            dataOutput.writeChars("\n");
        else
            dataOutput.writeChars(device.getManufacturer());
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
                    dataOutput.writeChars("\n");
                else
                    dataOutput.writeChars(((WifiRouter)device).getSecurityProtocol());
            }

        }
        if (Battery.class.isInstance(device)){
            dataOutput.writeInt(((Battery)device).getChargeVolume());
        }
    }

    public Device inputDevice(InputStream inputStream) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        DataInput dataInput = new DataInputStream(inputStream);
        Class c =  Class.forName(dataInput.readLine());
        Device d = c.newInstance();
        return d;
    }

    public void serializeDevice(Device device, OutputStream outputStream) throws IOException{

    }

    public Device deserializeDevice(InputStream inputStream) throws IOException, ClassCastException{
        return null;
    }
}
