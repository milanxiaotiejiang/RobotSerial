package com.robot.seabreeze.serial;

import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.serial.listener.ReceivedListener;

/**
 * Author: MiLan
 * Date: 2019/2/25
 * Description: 回调
 */
public abstract class ReceiveData implements ReceivedListener {
    @Override
    public void onActionReceived(String info) {
        Logger.e(info);
    }

    @Override
    public void onVoiceReceived(String info) {
        Logger.e(info);
    }

    @Override
    public void onCruiseReceived(String info) {
        Logger.e(info);
    }

    @Override
    public void onScanReceived(String info) {
        Logger.e(info);
    }

    @Override
    public void onActionReceived(byte[] bytes) {

    }

    @Override
    public void onVoiceReceived(byte[] bytes) {

    }

    @Override
    public void onCruiseReceived(byte[] bytes) {

    }

    @Override
    public void onScanReceived(byte[] bytes) {

    }
}
