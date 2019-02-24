package com.robot.seabreeze.serial;

import java.io.File;

/**
 * Author: MiLan
 * Date: 2019/2/24
 * Description:
 */
public class Dev {

    private File devFile;
    private int baudrate;

    public Dev(File devFile, int baudrate) {
        this.devFile = devFile;
        this.baudrate = baudrate;
    }

    public File getDevFile() {
        return devFile;
    }

    public void setDevFile(File devFile) {
        this.devFile = devFile;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }
}
