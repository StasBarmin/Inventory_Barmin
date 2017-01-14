package com.netcracker.edu.inventory.segment.impl;

import com.netcracker.edu.inventory.model.*;
import com.netcracker.edu.inventory.segment.Segment;
import com.netcracker.edu.inventory.service.Service;
import com.netcracker.edu.inventory.service.impl.ServiceImpl;

import java.util.*;

/**
 * Created by barmin on 08.01.2017.
 */
public class SegmentImpl implements Segment {

    Map<Unique.PrimaryKey, Unique> segment = new TreeMap<Unique.PrimaryKey, Unique>();
    Service service = new ServiceImpl();

    public boolean add(Unique element) {
        if (element == null)
            return false;
        Unique.PrimaryKey pK = element.getPrimaryKey();
        if (contain(pK) || pK == null || element instanceof Unique.PrimaryKey || element instanceof Rack)
            return false;
        Unique entity = service.getIndependentCopy(element);
        segment.put(entity.getPrimaryKey(), entity);
        return true;
    }

    public boolean set(Unique element) {
        if (element == null)
            return false;
        if (!contain(element.getPrimaryKey()) || element instanceof Unique.PrimaryKey)
            return false;
        ((FeelableEntity)segment.get(element.getPrimaryKey())).fillAllFields(((FeelableEntity)element).getAllFieldsList());
        return true;
    }

    public Unique get(Unique.PrimaryKey pk) {
        return service.getIndependentCopy(segment.get(pk));
    }

    public boolean put(Unique element) {
        if (element == null)
            return false;
        Unique.PrimaryKey pK = element.getPrimaryKey();
        if (pK == null || element instanceof Unique.PrimaryKey || element instanceof Rack)
            return false;
        if (!contain(pK)){
            Unique entity = service.getIndependentCopy(element);
            segment.put(entity.getPrimaryKey(), entity);
        }
        else
            ((FeelableEntity)segment.get(element.getPrimaryKey())).fillAllFields(((FeelableEntity)element).getAllFieldsList());
        return true;
    }

    public boolean contain(Unique.PrimaryKey pk) {
        if (pk == null)
            return false;
        else
            return segment.containsKey(pk);
    }

    public Collection<Unique> getGraph() {
        List graph = new ArrayList();
        Unique entity;
        for (Unique element : segment.values()) {
            entity = get(element.getPrimaryKey());
            List<FeelableEntity.Field> fields = ((FeelableEntity)entity).getAllFieldsList();
            Collection<FeelableEntity> entities;
            Iterator iterator;
            Unique.PrimaryKey entity2;

            for (int i = 0; i < fields.size(); i++) {
                if (Unique.class.isAssignableFrom(fields.get(i).getType())){
                    if (fields.get(i).getValue() != null)
                        if (contain((Unique.PrimaryKey)fields.get(i).getValue()))
                            fields.get(i).setValue(get(((Unique)fields.get(i).getValue()).getPrimaryKey()));
                }
                if (Collection.class.isAssignableFrom(fields.get(i).getType())) {
                    int size;
                        size = ((Collection) fields.get(i).getValue()).size();
                        iterator = ((Collection) fields.get(i).getValue()).iterator();
                        if (List.class.isAssignableFrom(fields.get(i).getType()))
                            entities = new LinkedList<FeelableEntity>();
                        else
                            entities = new HashSet<FeelableEntity>();
                        for (int j = 0; j < size; j++) {
                            entity2 = (Unique.PrimaryKey) iterator.next();
                            if (entity2 != null) {
                                if (contain(entity2))
                                    entities.add((FeelableEntity) get((entity2)));
                                else
                                    entities.add((FeelableEntity) entity2);
                            }
                            else
                                entities.add(null);
                        }
                        fields.get(i).setValue(entities);
                    }
            }
            ((FeelableEntity) entity).fillAllFields(fields);
            graph.add(entity);
        }
        return graph;
    }
}
