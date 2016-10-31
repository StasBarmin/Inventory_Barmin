package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.Rack;
import com.netcracker.edu.inventory.model.impl.*;
import com.netcracker.edu.inventory.service.RackService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static org.junit.Assert.*;

/**
 * Created by oleksandr on 23.10.16.
 */
public class RackServiceImplTest {

    RackService rackService;

    @Before
    public void setUp() throws Exception {
        rackService = new RackServiceImpl();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test(expected = NotImplementedException.class)
    public void writeRack() throws Exception {
        rackService.writeRack(null, null);
    }

    @Test(expected = NotImplementedException.class)
    public void readRack() throws Exception {
        rackService.readRack(null);
    }

    @Test
    public void outputInputRack() throws Exception {

        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        Switch aSwitch = DeviceServiceImplTest.createSwitch();
        Router router = DeviceServiceImplTest.createRouter();
        router.setIn(5);
        Rack emptyRack = new RackArrayImpl(0, Device.class);
        Rack partlyRack =  new RackArrayImpl(3, Router.class);
        partlyRack.insertDevToSlot(aSwitch, 0);
        partlyRack.insertDevToSlot(router, 2);

        rackService.outputRack(emptyRack, pipedOutputStream);
        rackService.outputRack(partlyRack, pipedOutputStream);
        pipedOutputStream.close();

        Rack result1 = rackService.inputRack(pipedInputStream);
        Rack result2 = rackService.inputRack(pipedInputStream);
        pipedInputStream.close();

        assertRack(emptyRack, result1);
        assertRack(partlyRack, result2);
    }

    @Test
    public void outputRackRackNull() throws Exception {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);

        rackService.outputRack(null, pipedOutputStream);

        assertEquals(0, pipedInputStream.available());

        pipedInputStream.close();
        pipedOutputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputRackStreamNull() throws Exception {
        Rack emptyRack = new RackArrayImpl(0, Device.class);
        rackService.outputRack(emptyRack, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void inputRackNull() throws Exception {
        rackService.inputRack(null);
    }

    @Test(expected = NotImplementedException.class)
    public void serializeRack() throws Exception {
        rackService.serializeRack(null, null);
    }

    @Test(expected = NotImplementedException.class)
    public void deserializeRack() throws Exception {
        rackService.deserializeRack(null);
    }

    static void assertRack(Rack expRack, Rack rack) throws Exception {
        assertEquals(expRack.getSize(), rack.getSize());
        assertEquals(expRack.getTypeOfDevices(), rack.getTypeOfDevices());
        for (int i = 0; i < expRack.getSize(); i++) {
            Device expDevice = expRack.getDevAtSlot(i);
            Device device = rack.getDevAtSlot(i);
            assertEquals(expDevice.getClass(), device.getClass());
            if (expDevice.getClass() == Battery.class) {
                DeviceServiceImplTest.assertBattery((Battery) expDevice, (Battery) device);
                break;
            }
            if (expDevice.getClass() == Router.class) {
                DeviceServiceImplTest.assertRouter((Router) expDevice, (Router) device);
                break;
            }
            if (expDevice.getClass() == Switch.class) {
                DeviceServiceImplTest.assertSwitch((Switch) expDevice, (Switch) device);
                break;
            }
            if (expDevice.getClass() == WifiRouter.class) {
                DeviceServiceImplTest.assertWifiRouter((WifiRouter) expDevice, (WifiRouter) device);
                break;
            }
        }
    }

}