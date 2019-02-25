package com.robot.seabreeze.serial;

import android.content.Context;

import com.robot.seabreeze.serial.listener.ReceivedListener;


/**
 * Author: MiLan
 * Date: 2019/2/24
 * Description: 串口控制
 */
public class SerialControl {

    private Context mContext;
    private SerialConfig mConfig;

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

    public Context getContext() {
        return mContext;
    }

    public void startManager(Context context) {
        mContext = context;
        SerialConfig.Builder builder = new SerialConfig.Builder();
        mConfig = builder
                .configContext(context)
                .configAction(SerialPreferences.getActionNamePre(), SerialPreferences.getActionBaudratePre())
                .configVoice(SerialPreferences.getVoiceNamePre(), SerialPreferences.getVoiceBaudratePre())
                .configCruise(SerialPreferences.getCruiseNamePre(), SerialPreferences.getCruiseBaudratePre())
                .formatDeliveryAction(SerialPreferences.getDeliveryActionPre())
                .formatDeliveryVoice(SerialPreferences.getDeliveryVoicePre())
                .formatDeliveryCruise(SerialPreferences.getDeliveryCruisePre())
                .formatReceiveAction(SerialPreferences.getReceiveActionPre())
                .formatReceiveVoice(SerialPreferences.getReceiveVoicePre())
                .formatReceiveCruise(SerialPreferences.getReceiveCruisePre())
                .build();
        mConfig.initManager();
    }


    public void stopManager() {
        if (null != mConfig.getActionManager()) {
            mConfig.getActionManager().closeSerialPort();
        }
        if (null != mConfig.getVoiceManager()) {
            mConfig.getVoiceManager().closeSerialPort();
        }
        if (null != mConfig.getCruiseManager()) {
            mConfig.getCruiseManager().closeSerialPort();
        }
    }

    public void sendActionData(String motion) {
        switch (mConfig.getDeliveryAction()) {
            case Format.Delivery.HEXTOBYTE:
                byte[] bOutArray = HexUtils.HexToByteArr(motion);
                mConfig.getActionManager().sendBytes(bOutArray);
                break;
            case Format.Delivery.CUSTOM:
                if (mConfig.getActionCustomBytes() == null) {
                    throw new NullPointerException("data is null");
                }
                mConfig.getActionManager().sendBytes(mConfig.getActionCustomBytes());
                break;
            default:
                mConfig.getActionManager().sendBytes(motion.getBytes());
                break;
        }
    }

    public void sendVoiceData(String motion) {
        switch (mConfig.getDeliveryVoice()) {
            case Format.Delivery.HEXTOBYTE:
                byte[] bOutArray = HexUtils.HexToByteArr(motion);
                mConfig.getVoiceManager().sendBytes(bOutArray);
                break;
            case Format.Delivery.CUSTOM:
                if (mConfig.getVoiceCustomBytes() == null) {
                    throw new NullPointerException("data is null");
                }
                mConfig.getVoiceManager().sendBytes(mConfig.getVoiceCustomBytes());
                break;
            default:
                mConfig.getVoiceManager().sendBytes(motion.getBytes());
                break;
        }
    }

    public void sendCruiseData(String motion) {
        switch (mConfig.getDeliveryCruise()) {
            case Format.Delivery.HEXTOBYTE:
                byte[] bOutArray = HexUtils.HexToByteArr(motion);
                mConfig.getCruiseManager().sendBytes(bOutArray);
                break;
            case Format.Delivery.CUSTOM:
                if (mConfig.getCruiseCustomBytes() == null) {
                    throw new NullPointerException("data is null");
                }
                mConfig.getCruiseManager().sendBytes(mConfig.getCruiseCustomBytes());
                break;
            default:
                mConfig.getCruiseManager().sendBytes(motion.getBytes());
                break;
        }
    }

    /**
     * 注册观察者
     */
    public void registerObserver(ReceivedListener observer) {
        synchronized (mConfig.getObservers()) {
            if (!mConfig.getObservers().contains(observer)) {
                mConfig.getObservers().add(observer);
            }
        }
    }

    /**
     * 反注册观察者
     */
    public void unRegisterObserver(ReceivedListener observer) {
        synchronized (mConfig.getObservers()) {
            if (mConfig.getObservers().contains(observer)) {
                mConfig.getObservers().remove(observer);
            }
        }
    }
}
