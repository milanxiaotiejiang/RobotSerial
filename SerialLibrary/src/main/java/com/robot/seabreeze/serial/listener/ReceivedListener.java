package com.robot.seabreeze.serial.listener;

import com.robot.seabreeze.log.Logger;

/**
 * Author: MiLan
 * Date: 2019/2/24
 * Description:
 */
public interface ReceivedListener {
    ReceivedListener NONE = new ReceivedListener() {
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
        public void onActionReceived(byte[] bytes) {

        }

        @Override
        public void onVoiceReceived(byte[] bytes) {

        }

        @Override
        public void onCruiseReceived(byte[] bytes) {

        }
    };

    void onActionReceived(String info);

    void onVoiceReceived(String info);

    void onCruiseReceived(String info);

    void onActionReceived(byte[] bytes);

    void onVoiceReceived(byte[] bytes);

    void onCruiseReceived(byte[] bytes);

}
