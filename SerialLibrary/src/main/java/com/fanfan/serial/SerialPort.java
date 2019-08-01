package com.fanfan.serial;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Author: MiLan
 * Date: 2019/2/23
 * Description: so相关
 */
public class SerialPort {

    static {
        System.loadLibrary("serial_port");
    }

    public native FileDescriptor open(String path, int baudrate);

    public native int close();
}
