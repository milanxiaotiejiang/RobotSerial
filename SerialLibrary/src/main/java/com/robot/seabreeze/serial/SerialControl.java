package com.robot.seabreeze.serial;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.robot.seabreeze.serial.listener.ReceivedListener;

import java.util.ArrayList;
import java.util.List;

import static com.robot.seabreeze.serial.SerialService.ACTION_ACTION;
import static com.robot.seabreeze.serial.SerialService.ACTION_CRUISE;
import static com.robot.seabreeze.serial.SerialService.ACTION_VOICE;
import static com.robot.seabreeze.serial.SerialService.EXTRA_MOTION;


/**
 * Author: MiLan
 * Date: 2019/2/24
 * Description: 串口控制
 */
public class SerialControl {

    private Context mContext;
    private LocalBroadcastManager mManager;

    private static SerialControl singleton;

    private SerialControl() {
    }

    public static SerialControl getInstance() {
        if (singleton == null) {
            synchronized (SerialControl.class) {
                if (singleton == null) {
                    singleton = new SerialControl();
                }
            }
        }
        return singleton;
    }

    public void init(Context context) {
        mContext = context;
        mManager = LocalBroadcastManager.getInstance(mContext);
    }

    public void startManager() {
        if (mContext == null) {
            return;
        }
        mContext.startService(new Intent(mContext, SerialService.class));
    }


    public void stopManager() {
        mContext.stopService(new Intent(mContext, SerialService.class));
    }

    public void sendActionData(String motion) {
        Utils.isServiceRunning(mContext, SerialService.class.getSimpleName());
        if (mManager == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_ACTION);
        intent.putExtra(EXTRA_MOTION, motion);
        mManager.sendBroadcast(intent);
    }

    public void sendVoiceData(String motion) {
        Utils.isServiceRunning(mContext, SerialService.class.getSimpleName());
        if (mManager == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_VOICE);
        intent.putExtra(EXTRA_MOTION, motion);
        mManager.sendBroadcast(intent);
    }

    public void sendCruiseData(String motion) {
        Utils.isServiceRunning(mContext, SerialService.class.getSimpleName());
        if (mManager == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(ACTION_CRUISE);
        intent.putExtra(EXTRA_MOTION, motion);
        mManager.sendBroadcast(intent);
    }

    public Context getContext() {
        return mContext;
    }

    public List<ReceivedListener> getObservers() {
        return mObservers;
    }

    private final List<ReceivedListener> mObservers = new ArrayList<>();

    /**
     * 注册观察者
     */
    public void registerObserver(ReceivedListener observer) {
        synchronized (mObservers) {
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }
    }

    /**
     * 反注册观察者
     */
    public void unRegisterObserver(ReceivedListener observer) {
        synchronized (mObservers) {
            mObservers.remove(observer);
        }
    }
}
