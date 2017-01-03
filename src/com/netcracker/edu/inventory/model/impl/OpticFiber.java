package com.netcracker.edu.inventory.model.impl;

import com.netcracker.edu.inventory.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by barmin on 07.12.2016.
 */
public class OpticFiber<A extends Device, B extends Device> extends AbstractOneToOneConnection<A,B> implements OneToOneConnection<A,B> {
    int length;
    Mode mode = Mode.need_init;

    public OpticFiber() {
        this.APointConnectorType = ConnectorType.FiberConnector_FC;
        this.BPointConnectorType = ConnectorType.FiberConnector_FC;
    }

    public OpticFiber( Mode mode, int length) {
        this.length = length;
        this.mode = mode;
        this.APointConnectorType = ConnectorType.FiberConnector_FC;
        this.BPointConnectorType = ConnectorType.FiberConnector_FC;
    }


    @Override
    public void fillAllFields(List<Field> fields) {
        int size = super.getAllFieldsList().size();

        super.fillAllFields(fields);
        if (fields.get(size).getValue() != null)
            setLength((Integer) fields.get(size).getValue());
        if (mode == Mode.need_init)
            this.mode = Mode.valueOf(fields.get(size + 1).getValue().toString());

    }

    @Override
    public List<Field> getAllFieldsList() {
        List<Field> fields = super.getAllFieldsList();

        fields.add(new Field(Integer.class, getLength()));

        fields.add(new Field(Mode.class, getMode()));

        return fields;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Mode getMode() {
        return mode;
    }

   public enum Mode {
        need_init,
        single,
        multi;
    }
}
