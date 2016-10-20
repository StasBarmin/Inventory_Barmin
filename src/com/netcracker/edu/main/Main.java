package com.netcracker.edu.main;

import com.netcracker.edu.inventory.model.Device;


public class Main {

    public static void main(String[] args) {



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
