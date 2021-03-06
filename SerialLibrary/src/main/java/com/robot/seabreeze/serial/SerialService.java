package com.robot.seabreeze.serial;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.serial.listener.OnOpenSerialPortListener;
import com.robot.seabreeze.serial.listener.OnSerialPortDataListener;
import com.robot.seabreeze.serial.listener.ReceivedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import anet.channel.util.StringUtils;

import static com.robot.seabreeze.serial.SerialConfig.DEV;

/**
 * User: milan
 * Time: 2019/3/19 12:37
 * Des:
 */
public class SerialService extends Service implements OnOpenSerialPortListener, OnSerialPortDataListener {

    public static final String ACTION_ACTION = "action_action";
    public static final String ACTION_VOICE = "action_voice";
    public static final String ACTION_CRUISE = "action_cruise";
    public static final String ACTION_SCAN = "action_scan";

    public static final String EXTRA_MOTION = "extra_motion";

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private SerialConfig mConfig;

    private LocalBroadcastManager mManager;
    private SerialReceiver serialReceiver;

    public static final String CHANNEL_ID_STRING = "service_01";

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) SerialControl.getInstance().getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID_STRING, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING).build();
            startForeground(1, notification);
        }

        SerialConfig.Builder builder = new SerialConfig.Builder();
        mConfig = builder
                .configContext(this)
                .configAction(SerialPreferences.getActionNamePre(), SerialPreferences.getActionBaudratePre())
                .configVoice(SerialPreferences.getVoiceNamePre(), SerialPreferences.getVoiceBaudratePre())
                .configCruise(SerialPreferences.getCruiseNamePre(), SerialPreferences.getCruiseBaudratePre())
                .configScan(SerialPreferences.getScanNamePre(), SerialPreferences.getScanBaudratePre())
                .formatDeliveryAction(SerialPreferences.getDeliveryActionPre())
                .formatDeliveryVoice(SerialPreferences.getDeliveryVoicePre())
                .formatDeliveryCruise(SerialPreferences.getDeliveryCruisePre())
                .formatDeliveryScan(SerialPreferences.getDeliveryScanPre())
                .formatReceiveAction(SerialPreferences.getReceiveActionPre())
                .formatReceiveVoice(SerialPreferences.getReceiveVoicePre())
                .formatReceiveCruise(SerialPreferences.getReceiveCruisePre())
                .formatReceiveScan(SerialPreferences.getReceiveScanPre())
                .build();
        mConfig.initManager();

        mManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ACTION);
        intentFilter.addAction(ACTION_VOICE);
        intentFilter.addAction(ACTION_CRUISE);
        intentFilter.addAction(ACTION_SCAN);
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
        mConfig.getScanManager()
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
        if (null != mConfig.getScanManager()) {
            mConfig.getScanManager().closeSerialPort();
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
    public void onDataReceived(final String absolute, final int baudRate, final byte[] bytes) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.equals(absolute, DEV + mConfig.getActionName()) && baudRate == mConfig.getActionBaudrate()) {
                    receivedActionData(bytes);
                } else if (TextUtils.equals(absolute, DEV + mConfig.getVoiceName()) && baudRate == mConfig.getVoiceBaudrate()) {
                    receivedVoiceData(bytes);
                } else if (TextUtils.equals(absolute, DEV + mConfig.getCruiseName()) && baudRate == mConfig.getCruiseBaudrate()) {
                    receivedCruiseData(bytes);
                } else if (TextUtils.equals(absolute, DEV + mConfig.getScanName()) && baudRate == mConfig.getScanBaudrate()) {
                    receivedScanData(bytes);
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

    private void receivedScanData(byte[] bytes) {
        switch (mConfig.getReceiveScan()) {
            case Format.Receive.BYTETOHEX:
                String msg = HexUtils.byte2HexStr(bytes);
                if (!TextUtils.isEmpty(msg) || msg.length() > 10) {
                    String[] split = msg.split(" ");
                    String judgeScan = judgeScan(split);
                    if (!TextUtils.isEmpty(judgeScan)) {

                        notifyScanData(judgeScan);
                    }
                }
                break;
            case Format.Receive.CUSTOM:
                notifyScanData(bytes);
                break;
            default:
                notifyScanData(new String(bytes));
                break;
        }
    }

    private String judgeScan(String[] scanArray) {
        String returnStr = null;
        if (scanArray.length > 7) {
            String s0 = scanArray[0];
            String s1 = scanArray[1];
            String s2 = scanArray[2];
            String s3 = scanArray[3];
            String s4 = scanArray[4];
            String s5 = scanArray[5];
            if (TextUtils.equals(s0, "55") && TextUtils.equals(s1, "AA")//标准格式
                    && TextUtils.equals(s2, "30")//命令字
                    && TextUtils.equals(s3, "00")//成功
            ) {

                List<String> scanList = Arrays.asList(scanArray);
                List<String> subList = scanList.subList(6, scanList.size() - 1);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    returnStr = String.join("", subList);
                } else {
                    StringBuffer buffer = new StringBuffer();
                    for (String s : subList) {
                        buffer.append(s);
                    }
                    returnStr = buffer.toString();
                }

                if (!TextUtils.equals(s4, "08") || !TextUtils.equals(s5, "00")) {//暂定身份证八位，这里判断不是八位
                    if (!TextUtils.equals(s4, "04") || !TextUtils.equals(s5, "00")) {//暂定银行卡四位，这里判断不是四位
                        returnStr = HexUtils.hexStringToString(returnStr);
                    }
                }
            }
        }
        return returnStr;
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

    private void notifyScanData(String info) {
        synchronized (SerialControl.getInstance().getObservers()) {
            for (ReceivedListener observer : SerialControl.getInstance().getObservers()) {
                observer.onScanReceived(info);
            }
        }
    }

    private void notifyScanData(byte[] bytes) {
        synchronized (SerialControl.getInstance().getObservers()) {
            for (ReceivedListener observer : SerialControl.getInstance().getObservers()) {
                observer.onScanReceived(bytes);
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

    public void sendScanData(String motion) {
        switch (mConfig.getDeliveryScan()) {
            case Format.Delivery.HEXTOBYTE:
                byte[] bOutArray = HexUtils.HexToByteArr(motion);
                mConfig.getScanManager().sendBytes(bOutArray);
                break;
            case Format.Delivery.CUSTOM:
                if (mConfig.getScanCustomBytes() == null) {
                    throw new NullPointerException("data is null");
                }
                mConfig.getScanManager().sendBytes(mConfig.getScanCustomBytes());
                break;
            default:
                mConfig.getScanManager().sendBytes(motion.getBytes());
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
                } else if (action.equals(ACTION_SCAN)) {
                    sendScanData(motion);
                }
            }
        }
    }


}
