package com.robot.seabreeze;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.fanfan.robot.common.arch.QMUISwipeBackActivityManager;
import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.log.inner.FileTree;
import com.robot.seabreeze.log.inner.LogcatTree;
import com.robot.seabreeze.serial.SerialControl;

/**
 * Author: MiLan
 * Date: 2019/2/25
 * Description:
 */
public class NovelApp extends MultiDexApplication {

    private static NovelApp instance;

    public static NovelApp getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (BuildConfig.DEBUG) {
            Logger.getLogConfig()
                    .configAllowLog(true)
                    .configShowBorders(false);
            Logger.plant(new FileTree(this, "Logger"));
//            Logger.plant(new ConsoleTree());
            Logger.plant(new LogcatTree());
        }

        SerialControl.getInstance().init(this);
        QMUISwipeBackActivityManager.init(this);
    }
}
