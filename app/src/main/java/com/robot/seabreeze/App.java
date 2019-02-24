package com.robot.seabreeze;

import android.app.Application;

import com.robot.seabreeze.serial.Format;
import com.robot.seabreeze.serial.SerialConfig;
import com.robot.seabreeze.serial.SerialControl;

/**
 * Author: MiLan
 * Date: 2019/2/25
 * Description:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SerialConfig.Builder builder = new SerialConfig.Builder();
        SerialConfig config = builder
                .configAction("ttyXRUSB2", 9600)
                .configVoice("ttyS4", 115200)
                .configCruise("ttyXRUSB0", 38400)
                .formatDeliveryAction(Format.Delivery.HEXTOBYTE)
                .formatReceiveAction(Format.Receive.CUSTOM)
                .formatDeliveryVoice(Format.Delivery.DEFAULT)
                .formatReceiveVoice(Format.Receive.BYTETOHEX)
                .formatDeliveryCruise(Format.Delivery.HEXTOBYTE)
                .formatReceiveCruise(Format.Receive.DEFAULT)
                .build();
        SerialControl.getInstance().startManager(config);
    }
}
