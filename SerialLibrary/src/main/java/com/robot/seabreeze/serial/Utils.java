package com.robot.seabreeze.serial;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * User: milan
 * Time: 2019/3/19 12:43
 * Des:
 */
public class Utils {
    public static Boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : infos) {
            String className = info.service.getClassName();
            if (serviceName.equals(className))
                return true;
        }
        return false;
    }
}
