package com.robot.seabreeze.serial.listener;

import java.io.File;

/**
 * Author: MiLan
 * Date: 2019/2/24
 * Description: 打开串口监听
 */
public interface OnOpenSerialPortListener {

    void onSuccess(File device, int i);

    void onFail(File device, Status status);

    enum Status {
        NO_READ_WRITE_PERMISSION,
        OPEN_FAIL
    }
}
