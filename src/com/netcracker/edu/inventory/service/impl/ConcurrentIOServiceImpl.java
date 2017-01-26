package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.FeelableEntity;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.service.ConcurrentIOService;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by barmin on 20.01.2017.
 */
public class ConcurrentIOServiceImpl implements ConcurrentIOService {
    ExecutorService executorService = Executors.newFixedThreadPool(8);
    ConcurrentInputOtputOperations iO = new ConcurrentInputOtputOperations();

    public Future parallelOutputElements(Collection<FeelableEntity> elements, OutputStream outputStream) {
        ConcurrentInputOtputOperations.OutputElements outputElements = iO.new OutputElements(elements,outputStream);
        return executorService.submit(outputElements);
    }

    public Future<Collection<FeelableEntity>> parallelInputElements(int number, InputStream inputStream) {
        ConcurrentInputOtputOperations.InputElements inputElements = iO.new InputElements(number, inputStream);
        return executorService.submit(inputElements);
    }

    public Future parallelWriteElements(Collection<FeelableEntity> elements, Writer writer) {
        ConcurrentInputOtputOperations.WriteElements writeElements = iO.new WriteElements(elements,writer);
        return executorService.submit(writeElements);
    }

    public Future<Collection<FeelableEntity>> parallelReadElements(int number, Reader reader) {
        ConcurrentInputOtputOperations.ReadElements readElements = iO.new ReadElements(number, reader);
        return executorService.submit(readElements);
    }

    public Future parallelOutputRacks(Collection<Rack> racks, OutputStream outputStream) {
        ConcurrentInputOtputOperations.OutputRacks outputRacks = iO.new OutputRacks(racks,outputStream);
        return executorService.submit(outputRacks);
    }

    public Future<Collection<Rack>> parallelInputRacks(int number, InputStream inputStream) {
        ConcurrentInputOtputOperations.InputRacks inputRacks = iO.new InputRacks(number, inputStream);
        return executorService.submit(inputRacks);
    }

    public Future parallelWriteRacks(Collection<Rack> racks, Writer writer) {
        ConcurrentInputOtputOperations.WriteRacks writeRacks = iO.new WriteRacks(racks,writer);
        return executorService.submit(writeRacks);
    }

    public Future<Collection<Rack>> parallelReadRacks(int number, Reader reader) {
        ConcurrentInputOtputOperations.ReadRacks readRacks = iO.new ReadRacks(number, reader);
        return executorService.submit(readRacks);
    }
}
