package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.impl.Battery;
import com.netcracker.edu.inventory.model.impl.Router;
import com.netcracker.edu.inventory.model.impl.Switch;
import com.netcracker.edu.inventory.service.DeviceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by oleksandr on 19.10.16.
 */
public class DeviceServiceImplTest {

    DeviceService deviceService;

    @Before
    public void setUp() throws Exception {
        deviceService = new DeviceServiceImpl();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void isCastableTo() throws Exception {
        Battery battery = new Battery();
        Switch aSwitch = new Switch();

        boolean result1 = new DeviceServiceImpl().isCastableTo(battery, Battery.class);
        boolean result2 = new DeviceServiceImpl().isCastableTo(battery, Router.class);
        boolean result3 = new DeviceServiceImpl().isCastableTo(battery, Device.class);
        boolean result4 = new DeviceServiceImpl().isCastableTo(aSwitch, Router.class);
        boolean result5 = new DeviceServiceImpl().isCastableTo(null, Device.class);
        boolean result6 = new DeviceServiceImpl().isCastableTo(battery, null);

        assertTrue(result1);
        assertFalse(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertFalse(result5);
        assertFalse(result6);
    }

    @Test
    public void isValidDeviceForInsertToRack() throws Exception {
        Battery battery = new Battery();
        battery.setIn(5);

        boolean result = deviceService.isValidDeviceForInsertToRack(battery);

        assertTrue(result);
    }

    @Test
    public void isValidDeviceForInsertToRack_DeviceNull() throws Exception {
        boolean result = deviceService.isValidDeviceForInsertToRack(null);

        assertFalse(result);
    }

    @Test
    public void isValidDeviceForInsertToRack_IN0() throws Exception {
        Battery battery = new Battery();

        boolean result = deviceService.isValidDeviceForInsertToRack(battery);

        assertFalse(result);
    }

    @Test
    public void isValidDeviceForInsertToRack_TypeNull() throws Exception {
        Device deviceNoType = new Battery() {
            @Override
            public String getType() {
                return null;
            }
        };
        deviceNoType.setIn(5);

        boolean result = deviceService.isValidDeviceForInsertToRack(deviceNoType);

        assertFalse(result);
    }

}