package com.robot.seabreeze.serial.listener;

/**
 * Author: MiLan
 * Date: 2019/2/24
 * Description: 数据连接监听
 */
public interface OnSerialPortDataListener {

    /**
     * 数据接收
     *
     * @param bytes 接收到的数据
     */
    void onDataReceived(String absolute, int baudRate, byte[] bytes);

    /**
     * 数据发送
     *
     * @param bytes 发送的数据
     */
    void onDataSent(String absolute, int baudRate, byte[] bytes);
}
