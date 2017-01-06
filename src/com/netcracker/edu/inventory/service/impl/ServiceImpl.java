package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.*;
import com.netcracker.edu.inventory.model.impl.Switch;
import com.netcracker.edu.inventory.model.impl.Wireless;
import com.netcracker.edu.inventory.service.ConnectionService;
import com.netcracker.edu.inventory.service.DeviceService;
import com.netcracker.edu.inventory.service.RackService;
import com.netcracker.edu.inventory.service.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.fill;


/**
 * Created by barmin on 09.10.2016.
 */
 public class ServiceImpl implements Service   {
    static protected Logger LOGGER = Logger.getLogger(ServiceImpl.class.getName());
    private Utilities util = new Utilities();

    public DeviceService getDeviceService(){
        DeviceService ds = new DeviceServiceImpl();
        return ds;
    }

    public ConnectionService getConnectionService(){
        ConnectionService cs = new ConnectionServiceImpl();
        return cs;
    }

    public RackService getRackService(){
        RackService rs = new RackServiceImpl();
        return rs;
    }

    public void sortByIN(Device[] devices)  {
        util.sortByIN(devices);
    }

    public void filtrateByType(Device[] devices, String type) {
        util.filtrateByType(devices, type);
    }

    public <T extends Unique.PrimaryKey> Unique<T> getIndependentCopy(Unique<T> element) {
        if (FeelableEntity.class.isAssignableFrom(element.getClass())){
            List<FeelableEntity.Field> fields = ((FeelableEntity)element).getAllFieldsList();
            Collection<Device> devices;
            Connection [] connections;
            Iterator iterator;
            Device device;

            for (int i = 0; i < fields.size(); i++) {
                if (Unique.class.isAssignableFrom(fields.get(i).getType())){
                    fields.get(i).setValue(((Unique)fields.get(i).getValue()).getPrimaryKey());
                }
                if (Collection.class.isAssignableFrom(fields.get(i).getType())) {
                    int size;
                    if (Switch.class.isAssignableFrom(element.getClass())) {
                        size = ((Connection[]) fields.get(i).getValue()).length;
                        connections = new Connection[size];
                        for (int j = 0; j < size; j++) {
                            if(((Connection[]) fields.get(i).getValue())[j] != null)
                            connections[j] = (Connection) ((Connection[]) fields.get(i).getValue())[j].getPrimaryKey();
                        }
                        fields.get(i).setValue(connections);
                    } else {
                        size = ((Collection) fields.get(i).getValue()).size();
                        iterator = ((Collection) fields.get(i).getValue()).iterator();
                        if (Wireless.class.isAssignableFrom(element.getClass()))
                            devices = new LinkedList<Device>();
                        else
                            devices = new HashSet<Device>();
                        for (int j = 0; j < size; j++) {
                            device = (Device) iterator.next();
                            if (device != null)
                                 devices.add(device.getPrimaryKey());
                            else
                                devices.add(null);
                        }
                        fields.get(i).setValue(devices);
                    }
                }
            }
            ((FeelableEntity) element).fillAllFields(fields);
        }

        if (Rack.class.isAssignableFrom(element.getClass())){
            for (int i = 0; i < ((Rack)element).getSize(); i++) {
                Device d = ((Rack)element).getDevAtSlot(i);
                if (d != null)
                    ((Rack)element).insertDevToSlot((Device)getIndependentCopy(d), i);
            }

        }
        return element;
    }
}
