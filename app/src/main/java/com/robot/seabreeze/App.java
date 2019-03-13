package com.robot.seabreeze;

import android.app.Application;

import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.log.inner.FileTree;
import com.robot.seabreeze.log.inner.LogcatTree;
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

        if (BuildConfig.DEBUG) {
            Logger.getLogConfig()
                    .configAllowLog(true)
                    .configShowBorders(false);
            Logger.plant(new FileTree(this, "Logger"));
//            Logger.plant(new ConsoleTree());
            Logger.plant(new LogcatTree());
        }

//        SerialConfig.Builder builder = new SerialConfig.Builder();
//        SerialConfig config = builder
//                .configContext(this)
//                .configAction("ttyS1", 9600)
//                .configVoice("ttyS2", 115200)
//                .configCruise("ttyS3", 38400)
//                .formatDeliveryAction(Format.Delivery.HEXTOBYTE)
//                .formatReceiveAction(Format.Receive.CUSTOM)
//                .formatDeliveryVoice(Format.Delivery.DEFAULT)
//                .formatReceiveVoice(Format.Receive.BYTETOHEX)
//                .formatDeliveryCruise(Format.Delivery.HEXTOBYTE)
//                .formatReceiveCruise(Format.Receive.DEFAULT)
//                .build();
//        config.initManager();
        SerialControl.getInstance().startManager(this);
    }
}
