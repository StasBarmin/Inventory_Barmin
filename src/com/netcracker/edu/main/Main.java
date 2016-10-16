package com.netcracker.edu.main;

import com.netcracker.edu.inventory.model.Device;
import com.netcracker.edu.inventory.model.impl.Battery;
import com.netcracker.edu.inventory.model.impl.RackArrayImpl;
import com.netcracker.edu.inventory.model.impl.Switch;
import com.netcracker.edu.inventory.service.impl.ServiceImpl;

public class Main {

    public static void main(String[] args) {


        RackArrayImpl rack1 = new RackArrayImpl(5, "batar" );
        Battery b1 = new Battery();
        String g = b1.getType();
        b1.setType("batar");
        int f = b1.getIn();
        b1.setIn(33);
        Switch s1 = new Switch();
        s1.setType("batar");
        s1.setIn(34);
        boolean res = rack1.insertDevToSlot(b1, 3);
        boolean res1 = rack1.insertDevToSlot(s1, 4);
        /** String s2 = dev2.getType();*/
        int sizef = rack1.getSize();
        System.out.println(sizef);
        ServiceImpl rt = new ServiceImpl();
        Device[] yu = new Device[3];
        rt.sortByIN(yu);
    }

    public static void print(String[] args) {
        for (int i = 0; i < args.length; i++){
            System.out.println(args[i]);
        }
    }

    public static void sort(String[] args) {
        boolean flag = true;

        while (flag){
            flag = false;
            for (int i = 0; i < args.length - 1; i++){
                if (args[i].compareTo(args[i + 1]) > 0) {
                    String temp = args[i + 1];
                    args[i + 1] = args[i];
                    args[i] = temp;
                    flag = true;
                }
            }
        }
    }
}
