package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.FeelableEntity;
import com.netcracker.edu.inventory.model.Rack;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by barmin on 24.01.2017.
 */
public class ConcurrentInputOtputOperations {

    private InputOutputOperations inputOutputOperations = new InputOutputOperations();

   class OutputElements implements Callable{
       Collection<FeelableEntity> elements;
       OutputStream outputStream;

       public OutputElements(Collection<FeelableEntity> elements, OutputStream outputStream) {
           this.elements = elements;
           this.outputStream = outputStream;
       }

       public Object call() throws Exception {
           DataOutput dataOutput = new DataOutputStream(outputStream);

           for (FeelableEntity entity : elements)
               if (entity != null)
                   inputOutputOperations.outputFillableEntity(entity, outputStream);
                else
                   dataOutput.writeUTF("\n");
           return null;
       }
   }

    class InputElements implements Callable{
        int number;
        Collection<FeelableEntity> elements;
        InputStream inputStream;

        public InputElements(int number, InputStream inputStream) {
            this.number = number;
            elements = new ArrayList<FeelableEntity>();
            this.inputStream = inputStream;
        }

        public Object call() throws Exception {
            FeelableEntity entity;

            for (int i = 0; i < number; i++) {
                try {
                entity = inputOutputOperations.inputFillableEntity(inputStream);
                if (entity != null)
                    elements.add(entity);
                else {
                    while (entity == null)
                        entity = inputOutputOperations.inputFillableEntity(inputStream);
                    elements.add(entity);
                }
                }
                catch (EOFException e){ break;}
            }
            return elements;
        }
    }

    class WriteElements implements Callable{
        Collection<FeelableEntity> elements;
        Writer writer;

        public WriteElements(Collection<FeelableEntity> elements, Writer writer) {
            this.elements = elements;
            this.writer = writer;
        }

        public Object call() throws Exception {

            for (FeelableEntity entity : elements)
                if (entity != null) {
                    inputOutputOperations.writeFillableEntity(entity, writer);
                    writer.write("\n");
                }
                else
                    writer.write("\n\n");
            return null;
        }
    }

    class ReadElements implements Callable{
        int number;
        Collection<FeelableEntity> elements;
        Reader reader;

        public ReadElements(int number, Reader reader) {
            this.number = number;
            elements = new ArrayList<FeelableEntity>();
            this.reader = reader;
        }

        public Object call() throws Exception {
            FeelableEntity entity;

            for (int i = 0; i < number; i++) {
                try {
                    entity = inputOutputOperations.readFillableEntity(reader);
                    if (entity != null)
                        elements.add(entity);
                    else {
                        while (entity == null && reader.ready())
                            entity = inputOutputOperations.readFillableEntity(reader);
                        if (entity != null)
                        elements.add(entity);
                    }
                }
                catch (EOFException e){ break;}
            }
            return elements;
        }
    }

    class OutputRacks implements Callable{
        Collection<Rack> racks;
        OutputStream outputStream;

        public OutputRacks(Collection<Rack> racks, OutputStream outputStream) {
            this.racks = racks;
            this.outputStream = outputStream;
        }

        public Object call() throws Exception {
            DataOutput dataOutput = new DataOutputStream(outputStream);

            for (Rack rack : racks)
                if (rack != null)
                    inputOutputOperations.outputRack(rack, outputStream);
                else
                    dataOutput.writeInt(-1);
            return null;
        }
    }

    class InputRacks implements Callable{
        int number;
        Collection<Rack> racks;
        InputStream inputStream;

        public InputRacks(int number, InputStream inputStream) {
            this.number = number;
            racks = new ArrayList<Rack>();
            this.inputStream = inputStream;
        }

        public Object call() throws Exception {
            Rack rack;

            for (int i = 0; i < number; i++) {
                try {
                    rack = inputOutputOperations.inputRack(inputStream);
                    if (rack != null)
                        racks.add(rack);
                    else {
                        while (rack == null)
                            rack = inputOutputOperations.inputRack(inputStream);
                        racks.add(rack);
                    }
                }
                catch (EOFException e){ break;}
            }
            return racks;
        }
    }

    class WriteRacks implements Callable{
        Collection<Rack> racks;
        Writer writer;

        public WriteRacks(Collection<Rack> racks, Writer writer) {
            this.racks = racks;
            this.writer = writer;
        }

        public Object call() throws Exception {

            for (Rack rack : racks)
                if (rack != null) {
                    inputOutputOperations.writeRack(rack, writer);
                    writer.write("\n");
                }
                else
                    writer.write("\n\n");
            return null;
        }
    }

    class ReadRacks implements Callable{
        int number;
        Collection<Rack> racks;
        Reader reader;

        public ReadRacks(int number, Reader reader) {
            this.number = number;
            racks = new ArrayList<Rack>();
            this.reader = reader;
        }

        public Object call() throws Exception {
            Rack rack;

            for (int i = 0; i < number; i++) {
                try {
                    rack = inputOutputOperations.readRack(reader);
                    if (rack != null)
                        racks.add(rack);
                    else {
                        while (rack == null && reader.ready())
                            rack = inputOutputOperations.readRack(reader);
                        if (rack != null)
                            racks.add(rack);
                    }
                }
                catch (EOFException e){ break;}
            }
            return racks;
        }
    }
}
