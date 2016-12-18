package com.netcracker.edu.inventory.service.impl;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.impl.Battery;
import com.netcracker.edu.inventory.model.impl.Router;
import com.netcracker.edu.inventory.model.impl.Switch;
import com.netcracker.edu.inventory.model.impl.WifiRouter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test only methods are presents
 *
 * Created by makovetskyi on 11/4/2016.
 */
public class UtilitiesTest {

    Utilities utilities;

    @Before
    public void before() throws Exception {
        utilities = new Utilities();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void sortByIN() throws Exception {
        utilities.sortByIN(new Device[0]);
    }

    @Test
    public void filtrateByType() throws Exception {
        utilities.filtrateByType(null, null);
    }

}