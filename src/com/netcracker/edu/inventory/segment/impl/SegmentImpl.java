package com.netcracker.edu.inventory.segment.impl;

import com.netcracker.edu.inventory.model.*;
import com.netcracker.edu.inventory.model.impl.Switch;
import com.netcracker.edu.inventory.model.impl.Wireless;
import com.netcracker.edu.inventory.segment.Segment;
import com.netcracker.edu.inventory.service.Service;
import com.netcracker.edu.inventory.service.impl.ServiceImpl;

import java.util.*;

/**
 * Created by barmin on 08.01.2017.
 */
public class SegmentImpl implements Segment {

    List<Unique> segment = new ArrayList<Unique>();
    Service service = new ServiceImpl();

    public boolean add(Unique element) {
        if (element == null)
            return false;
        Unique.PrimaryKey pK = element.getPrimaryKey();
        if (contain(pK) || pK == null || element instanceof Unique.PrimaryKey || element instanceof Rack)
            return false;
        Unique entity = service.getIndependentCopy(element);
        segment.add(entity);
        return true;
    }

    public boolean set(Unique element) {
        if (element == null)
            return false;
        if (!contain(element.getPrimaryKey()) || element instanceof Unique.PrimaryKey)
         return false;
        for (Unique el : segment) {
            if (element.getPrimaryKey().equals(el.getPrimaryKey()))
                ((FeelableEntity)el).fillAllFields(((FeelableEntity)element).getAllFieldsList());
        }
        return true;
    }

    public Unique get(Unique.PrimaryKey pk) {
        if (!contain(pk))
            return null;
        else {
            for (Unique element : segment) {
                if (element.getPrimaryKey().equals(pk))
                    return service.getIndependentCopy(element);
            }
        }
        return null;
    }

    public boolean put(Unique element) {
        if (element == null)
            return false;
        Unique.PrimaryKey pK = element.getPrimaryKey();
        if (pK == null || element instanceof Unique.PrimaryKey || element instanceof Rack)
            return false;
        if (!contain(pK)){
            Unique entity = service.getIndependentCopy(element);
            segment.add(entity);
        }
        else {
            for (Unique el : segment) {
                if (element.getPrimaryKey().equals(el.getPrimaryKey())){
                    ((FeelableEntity)el).fillAllFields(((FeelableEntity)element).getAllFieldsList());
                }
            }
        }
        return true;
    }

    public boolean contain(Unique.PrimaryKey pk) {
        for (Unique element : segment) {
            if (element.getPrimaryKey().equals(pk))
                return true;
        }
        return false;
    }

    public Collection<Unique> getGraph() {
        List graph = new ArrayList();
        Unique entity;
        for (Unique element : segment) {
            entity = get(element.getPrimaryKey());
            List<FeelableEntity.Field> fields = ((FeelableEntity)entity).getAllFieldsList();
            Collection<Device> devices;
            Connection[] connections;
            Iterator iterator;
            Device device;

            for (int i = 0; i < fields.size(); i++) {
                if (Unique.class.isAssignableFrom(fields.get(i).getType())){
                    if (fields.get(i).getValue() != null)
                        if (fields.get(i).getValue() instanceof Unique.PrimaryKey)
                            if (contain((Unique.PrimaryKey)fields.get(i).getValue()))
                            fields.get(i).setValue(get(((Unique)fields.get(i).getValue()).getPrimaryKey()));
                }
                if (Collection.class.isAssignableFrom(fields.get(i).getType())) {
                    int size;
                    if (Switch.class.isAssignableFrom(entity.getClass())) {
                        size = ((Connection[]) fields.get(i).getValue()).length;
                        connections = new Connection[size];
                        for (int j = 0; j < size; j++) {
                            if(((Connection[]) fields.get(i).getValue())[j] != null)
                                if (fields.get(i).getValue() instanceof Unique.PrimaryKey)
                                    if (contain((Unique.PrimaryKey)fields.get(i).getValue()))
                                        connections[j] = (Connection)get(((Unique)fields.get(i).getValue()).getPrimaryKey());
                        }
                        fields.get(i).setValue(connections);
                    } else {
                        size = ((Collection) fields.get(i).getValue()).size();
                        iterator = ((Collection) fields.get(i).getValue()).iterator();
                        if (Wireless.class.isAssignableFrom(entity.getClass()))
                            devices = new LinkedList<Device>();
                        else
                            devices = new HashSet<Device>();
                        for (int j = 0; j < size; j++) {
                            device = (Device) iterator.next();
                            if (device != null)
                                if (fields.get(i).getValue() instanceof Unique.PrimaryKey)
                                    if (contain((Unique.PrimaryKey)fields.get(i).getValue()))
                                    devices.add((Device) get(((Unique)fields.get(i).getValue()).getPrimaryKey()));
                            else
                                devices.add(null);
                        }
                        fields.get(i).setValue(devices);
                    }
                }
            }
            graph.add(entity);
        }
        return graph;
    }
}
