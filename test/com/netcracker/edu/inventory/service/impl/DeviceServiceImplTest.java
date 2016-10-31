package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.impl.Battery;
import com.netcracker.edu.inventory.model.impl.Router;
import com.netcracker.edu.inventory.model.impl.Switch;
import com.netcracker.edu.inventory.model.impl.WifiRouter;
import com.netcracker.edu.inventory.service.DeviceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Date;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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

    @Test(expected = NotImplementedException.class)
    public void writeDevice() throws Exception {
        deviceService.writeDevice(null, null);
    }

    @Test(expected = NotImplementedException.class)
    public void readDevice() throws Exception {
        deviceService.readDevice(null);
    }

    @Test
    public void outputInputDevice() throws Exception {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        Battery battery = createBattery();
        Router router = createRouter();
        Switch aSwitch = createSwitch();
        WifiRouter wifiRouter = createWifiRouter();

        deviceService.outputDevice(battery, pipedOutputStream);
        deviceService.outputDevice(router, pipedOutputStream);
        deviceService.outputDevice(aSwitch, pipedOutputStream);
        deviceService.outputDevice(wifiRouter, pipedOutputStream);
        pipedOutputStream.close();

        Device result1 = deviceService.inputDevice(pipedInputStream);
        Device result2 = deviceService.inputDevice(pipedInputStream);
        Device result3 = deviceService.inputDevice(pipedInputStream);
        Device result4 = deviceService.inputDevice(pipedInputStream);
        pipedInputStream.close();

        assertEquals(Battery.class, result1.getClass());
        assertBattery(battery, (Battery) result1);
        assertEquals(Router.class, result2.getClass());
        assertRouter(router, (Router) result2);
        assertEquals(Switch.class, result3.getClass());
        assertSwitch(aSwitch, (Switch) result3);
        assertEquals(WifiRouter.class, result4.getClass());
        assertWifiRouter(wifiRouter, (WifiRouter) result4);
    }

    @Test
    public void outputDeviceNull() throws Exception {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);

        deviceService.outputDevice(null, pipedOutputStream);

        assertEquals(0, pipedInputStream.available());

        pipedInputStream.close();
        pipedOutputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void outputDeviceStreamNull() throws Exception {
        deviceService.outputDevice(createSwitch(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void inputDeviceStreamNull() throws Exception {
        deviceService.inputDevice(null);
    }

    @Test(expected = NotImplementedException.class)
    public void serializeDevice() throws Exception {
        deviceService.serializeDevice(null, null);
    }

    @Test(expected = NotImplementedException.class)
    public void deserializeDevice() throws Exception {
        deviceService.deserializeDevice(null);
    }

    static Battery createBattery() {
        Battery battery = new Battery();
        battery.setIn(4);
        battery.setManufacturer("");
        battery.setModel("qwerty");
        battery.setProductionDate(new Date());
        battery.setChargeVolume(5000);
        return battery;
    }

    static Router createRouter() {
        Router router = new Router();
        router.setManufacturer("D-link");
        router.setDataRate(1000);
        return router;
    }

    static Switch createSwitch() {
        Switch aSwitch = new Switch();
        aSwitch.setIn(7);
        aSwitch.setModel("null");
        aSwitch.setManufacturer("Cisco");
        aSwitch.setDataRate(1000000);
        aSwitch.setNumberOfPorts(16);
        return aSwitch;
    }

    static WifiRouter createWifiRouter() {
        WifiRouter wifiRouter = new WifiRouter();
        wifiRouter.setIn(7);
        wifiRouter.setModel(null);
        wifiRouter.setManufacturer("D-link");
        wifiRouter.setSecurityProtocol(" ");
        return wifiRouter;
    }

    static void assertDevice(Device expDevice, Device device) throws Exception {
        assertEquals(expDevice.getIn(), device.getIn());
        assertEquals(expDevice.getType(), device.getType());
        assertEquals(expDevice.getModel(), device.getModel());
        assertEquals(expDevice.getManufacturer(), device.getManufacturer());
        assertEquals(expDevice.getProductionDate(), device.getProductionDate());
    }

    static void assertBattery(Battery expBattery, Battery battery) throws Exception {
        assertDevice(expBattery, battery);
        assertEquals(expBattery.getChargeVolume(), battery.getChargeVolume());
    }

    static void assertRouter(Router expRouter, Router router) throws Exception {
        assertDevice(expRouter, router);
        assertEquals(expRouter.getDataRate(), router.getDataRate());
    }

    static void assertSwitch(Switch expSwitch, Switch aSwitch) throws Exception {
        assertRouter(expSwitch, aSwitch);
        assertEquals(expSwitch.getNumberOfPorts(), aSwitch.getNumberOfPorts());
    }

    static void assertWifiRouter(WifiRouter expWifiRouter, WifiRouter wifiRouter) throws Exception {
        assertRouter(expWifiRouter, wifiRouter);
        assertEquals(expWifiRouter.getSecurityProtocol(), wifiRouter.getSecurityProtocol());
    }

}