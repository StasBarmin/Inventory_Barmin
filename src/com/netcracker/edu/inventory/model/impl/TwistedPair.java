package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by barmin on 28.11.2016.
 */
public class TwistedPair<A extends Device, B extends Device> extends AbstractOneToOneConnection<A,B> implements OneToOneConnection<A,B> {
    int length;
    Type type = Type.need_init;


    public TwistedPair() {
        this.APointConnectorType = ConnectorType.RJ45;
        this.BPointConnectorType = ConnectorType.RJ45;
    }

    public TwistedPair(Type type, int length) {
        this.length = length;
        this.type = type;
        this.APointConnectorType = ConnectorType.RJ45;
        this.BPointConnectorType = ConnectorType.RJ45;
    }

    public void feelAllFields(Field[] fields) {
        fillAllFields(new ArrayList<Field>(Arrays.asList(fields)));
    }

    public Field[] getAllFields() {
        Field[] fields = new Field[5];

        return getAllFieldsList().toArray(fields);
    }

    public void fillAllFields(List<Field> fields) {
        int size = super.getAllFieldsList().size();

        super.fillAllFields(fields);
        if (fields.get(size).getValue() != null)
            setLength((Integer) fields.get(size).getValue());
        if (type == Type.need_init)
            this.type = Type.valueOf(fields.get(size + 1).getValue().toString());

    }

    public List<Field> getAllFieldsList() {
        List<Field> fields = super.getAllFieldsList();

        fields.add(new Field(Integer.class, getLength()));

        fields.add(new Field(Type.class, getType()));

        return fields;

    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Type getType() {
        return type;
    }

     public enum Type {
        need_init,
        UTP,
        FTP,
        STP,
        S_FTP,
        SFTP;
    }

}
