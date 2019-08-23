package com.robot.seabreeze.serial;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.serial.listener.OnOpenSerialPortListener;
import com.robot.seabreeze.serial.listener.OnSerialPortDataListener;
import com.robot.seabreeze.serial.listener.ReceivedListener;

import java.io.File;

/**
 * User: milan
 * Time: 2019/3/19 12:37
 * Des:
 */
public class SerialService extends Service implements OnOpenSerialPortListener, OnSerialPortDataListener {

    public static final String ACTION_ACTION = "action_action";
    public static final String ACTION_VOICE = "action_voice";
    public static final String ACTION_CRUISE = "action_cruise";

    public static final String EXTRA_MOTION = "extra_motion";

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private SerialConfig mConfig;

    private LocalBroadcastManager mManager;
    private SerialReceiver serialReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        SerialConfig.Builder builder = new SerialConfig.Builder();
        mConfig = builder
                .configContext(this)
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

        mManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ACTION);
        intentFilter.addAction(ACTION_VOICE);
        intentFilter.addAction(ACTION_CRUISE);
        serialReceiver = new SerialReceiver();
        mManager.registerReceiver(serialReceiver, intentFilter);

        mConfig.getActionManager()
                .setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(this);
        mConfig.getVoiceManager()
                .setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(this);
        mConfig.getCruiseManager()
                .setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mConfig.getActionManager()) {
            mConfig.getActionManager().closeSerialPort();
        }
        if (null != mConfig.getVoiceManager()) {
            mConfig.getVoiceManager().closeSerialPort();
        }
        if (null != mConfig.getCruiseManager()) {
            mConfig.getCruiseManager().closeSerialPort();
        }
        mManager.unregisterReceiver(serialReceiver);
    }

    @Override
    public void onSuccess(File device, int baudRate) {
        Logger.i(String.format("串口 [%s] 打开成功   波特率 %s", device.getPath(), baudRate));
    }

    @Override
    public void onFail(File device, Status status) {
        switch (status) {
            case NO_READ_WRITE_PERMISSION:
                Logger.e(device.getPath() + " 没有读写权限");
                break;
            case OPEN_FAIL:
            default:
                Logger.e(device.getPath() + " 串口打开失败");
                break;
        }
    }

    @Override
    public void onDataReceived(String absolute, final int baudRate, final byte[] bytes) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (baudRate == mConfig.getActionBaudrate()) {
                    receivedActionData(bytes);
                } else if (baudRate == mConfig.getVoiceBaudrate()) {
                    receivedVoiceData(bytes);
                } else if (baudRate == mConfig.getCruiseBaudrate()) {
                    receivedCruiseData(bytes);
                }
            }
        });
    }

    private void receivedActionData(byte[] bytes) {
        switch (mConfig.getReceiveAction()) {
            case Format.Receive.BYTETOHEX:
                String msg = HexUtils.byte2HexStr(bytes);
                notifyActionData(msg);
                break;
            case Format.Receive.CUSTOM:
                notifyActionData(bytes);
                break;
            default:
                notifyActionData(new String(bytes));
                break;
        }
    }

    private void receivedVoiceData(byte[] bytes) {
        switch (mConfig.getReceiveVoice()) {
            case Format.Receive.BYTETOHEX:
                String msg = HexUtils.byte2HexStr(bytes);
                notifyVoiceData(msg);
                break;
            case Format.Receive.CUSTOM:
                notifyVoiceData(bytes);
                break;
            default:
                notifyVoiceData(new String(bytes));
                break;
        }
    }

    private void receivedCruiseData(byte[] bytes) {
        switch (mConfig.getReceiveCruise()) {
            case Format.Receive.BYTETOHEX:
                String msg = HexUtils.byte2HexStr(bytes);
                notifyCruiseData(msg);
                break;
            case Format.Receive.CUSTOM:
                notifyCruiseData(bytes);
                break;
            default:
                notifyCruiseData(new String(bytes));
                break;
        }
    }

    @Override
    public void onDataSent(String absolute, int baudRate, byte[] bytes) {
        Logger.i("send success " + baudRate);
    }

    private void notifyActionData(String info) {
        synchronized (SerialControl.getInstance().getObservers()) {
            for (ReceivedListener observer : SerialControl.getInstance().getObservers()) {
                observer.onActionReceived(info);
            }
        }
    }

    private void notifyActionData(byte[] bytes) {
        synchronized (SerialControl.getInstance().getObservers()) {
            for (ReceivedListener observer : SerialControl.getInstance().getObservers()) {
                observer.onActionReceived(bytes);
            }
        }
    }

    private void notifyVoiceData(String info) {
        synchronized (SerialControl.getInstance().getObservers()) {
            for (ReceivedListener observer : SerialControl.getInstance().getObservers()) {
                observer.onVoiceReceived(info);
            }
        }
    }

    private void notifyVoiceData(byte[] bytes) {
        synchronized (SerialControl.getInstance().getObservers()) {
            for (ReceivedListener observer : SerialControl.getInstance().getObservers()) {
                observer.onVoiceReceived(bytes);
            }
        }
    }

    private void notifyCruiseData(String info) {
        synchronized (SerialControl.getInstance().getObservers()) {
            for (ReceivedListener observer : SerialControl.getInstance().getObservers()) {
                observer.onCruiseReceived(info);
            }
        }
    }

    private void notifyCruiseData(byte[] bytes) {
        synchronized (SerialControl.getInstance().getObservers()) {
            for (ReceivedListener observer : SerialControl.getInstance().getObservers()) {
                observer.onCruiseReceived(bytes);
            }
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

    class SerialReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String motion = intent.getStringExtra(EXTRA_MOTION);
            if (action != null && motion != null) {

                if (action.equals(ACTION_ACTION)) {
                    sendActionData(motion);
                } else if (action.equals(ACTION_VOICE)) {
                    sendVoiceData(motion);
                } else if (action.equals(ACTION_CRUISE)) {
                    sendCruiseData(motion);
                }
            }
        }
    }


}
