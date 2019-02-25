package com.robot.seabreeze.serial;

import com.robot.seabreeze.log.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Author: MiLan
 * Date: 2019/2/24
 * Description: Driver
 */
public class Driver {

    private String mDriverName;
    private String mDeviceRoot;

    public Driver(String name, String root) {
        mDriverName = name;
        mDeviceRoot = root;
    }

    public ArrayList<File> getDevices() {
        ArrayList<File> devices = new ArrayList<>();
        File dev = new File("/dev");

        if (!dev.exists()) {
            Logger.e("getDevices: " + dev.getAbsolutePath() + " 不存在");
            return devices;
        }
        if (!dev.canRead()) {
            Logger.e("getDevices: " + dev.getAbsolutePath() + " 没有读取权限");
            return devices;
        }

        File[] files = dev.listFiles();

        int i;
        for (i = 0; i < files.length; i++) {
            if (files[i].getAbsolutePath().startsWith(mDeviceRoot)) {
                devices.add(files[i]);
            }
        }
        return devices;
    }

    public String getName() {
        return mDriverName;
    }

}
