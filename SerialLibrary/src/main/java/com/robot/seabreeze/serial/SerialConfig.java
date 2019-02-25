package com.robot.seabreeze.serial;

import android.content.Context;
import android.text.TextUtils;

import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.serial.listener.ReceivedListener;
import com.robot.seabreeze.serial.listener.OnOpenSerialPortListener;
import com.robot.seabreeze.serial.listener.OnSerialPortDataListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MiLan
 * Date: 2019/2/24
 * Description:
 */
public class SerialConfig implements OnOpenSerialPortListener, OnSerialPortDataListener {

    private static final String DEV = "/dev/";

    private Context context;

    private String actionName;
    private String voiceName;
    private String cruiseName;

    private int actionBaudrate;
    private int voiceBaudrate;
    private int cruiseBaudrate;

    private int deliveryAction;
    private int deliveryVoice;
    private int deliveryCruise;

    private int receiveAction;
    private int receiveVoice;
    private int receiveCruise;

    private byte[] actionCustomBytes;
    private byte[] voiceCustomBytes;
    private byte[] cruiseCustomBytes;

    private SerialPortManager mActionManager;
    private SerialPortManager mVoiceManager;
    private SerialPortManager mCruiseManager;

    public SerialConfig(Builder builder) {
        context = builder.context;

        actionName = builder.actionName;
        voiceName = builder.voiceName;
        cruiseName = builder.cruiseName;

        actionBaudrate = builder.actionBaudrate;
        voiceBaudrate = builder.voiceBaudrate;
        cruiseBaudrate = builder.cruiseBaudrate;

        deliveryAction = builder.deliveryAction;
        deliveryVoice = builder.deliveryVoice;
        deliveryCruise = builder.deliveryCruise;

        receiveAction = builder.receiveAction;
        receiveVoice = builder.receiveVoice;
        receiveCruise = builder.receiveCruise;

        if (builder.deliveryAction == Format.Delivery.CUSTOM) {
            actionCustomBytes = builder.actionCustomBytes;
        }
        if (builder.deliveryVoice == Format.Delivery.CUSTOM) {
            voiceCustomBytes = builder.voiceCustomBytes;
        }
        if (builder.deliveryCruise == Format.Delivery.CUSTOM) {
            cruiseCustomBytes = builder.cruiseCustomBytes;
        }
    }

    public void initManager() {
        mActionManager = initManager("action", actionName, actionBaudrate);
        mVoiceManager = initManager("voice", voiceName, voiceBaudrate);
        mCruiseManager = initManager("cruise", cruiseName, cruiseBaudrate);
    }

    private SerialPortManager initManager(String tag, String devName, int baudRate) {
        SerialPortManager manager = new SerialPortManager();
        manager.setTag(tag)
                .setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(this)
                .openSerialPort(new File(DEV + devName), baudRate);
        return manager;
    }

    @Override
    public void onSuccess(File device, int baudRate) {
        Logger.e(String.format("串口 [%s] 打开成功   波特率 %s", device.getPath(), baudRate));
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
    public void onDataReceived(String absolute, int baudRate, byte[] bytes) {
        if (baudRate == actionBaudrate) {
            receivedActionData(bytes);
        } else if (baudRate == voiceBaudrate) {
            receivedVoiceData(bytes);
        } else if (baudRate == cruiseBaudrate) {
            receivedCruiseData(bytes);
        }
    }

    private void receivedActionData(byte[] bytes) {
        switch (receiveAction) {
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
        switch (receiveVoice) {
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
        switch (receiveCruise) {
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
        Logger.e("send success " + baudRate);
    }

    private List<ReceivedListener> mObservers = new ArrayList<>();

    private void notifyActionData(String info) {
        synchronized (mObservers) {
            for (ReceivedListener observer : mObservers) {
                observer.onActionReceived(info);
            }
        }
    }

    private void notifyActionData(byte[] bytes) {
        synchronized (mObservers) {
            for (ReceivedListener observer : mObservers) {
                observer.onActionReceived(bytes);
            }
        }
    }

    private void notifyVoiceData(String info) {
        synchronized (mObservers) {
            for (ReceivedListener observer : mObservers) {
                observer.onVoiceReceived(info);
            }
        }
    }

    private void notifyVoiceData(byte[] bytes) {
        synchronized (mObservers) {
            for (ReceivedListener observer : mObservers) {
                observer.onVoiceReceived(bytes);
            }
        }
    }

    private void notifyCruiseData(String info) {
        synchronized (mObservers) {
            for (ReceivedListener observer : mObservers) {
                observer.onCruiseReceived(info);
            }
        }
    }

    private void notifyCruiseData(byte[] bytes) {
        synchronized (mObservers) {
            for (ReceivedListener observer : mObservers) {
                observer.onCruiseReceived(bytes);
            }
        }
    }


    public Context getContext() {
        return context;
    }

    public String getActionName() {
        return actionName;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public String getCruiseName() {
        return cruiseName;
    }

    public int getActionBaudrate() {
        return actionBaudrate;
    }

    public int getVoiceBaudrate() {
        return voiceBaudrate;
    }

    public int getCruiseBaudrate() {
        return cruiseBaudrate;
    }

    public int getDeliveryAction() {
        return deliveryAction;
    }

    public int getDeliveryVoice() {
        return deliveryVoice;
    }

    public int getDeliveryCruise() {
        return deliveryCruise;
    }

    public int getReceiveAction() {
        return receiveAction;
    }

    public int getReceiveVoice() {
        return receiveVoice;
    }

    public int getReceiveCruise() {
        return receiveCruise;
    }

    public byte[] getActionCustomBytes() {
        return actionCustomBytes;
    }

    public byte[] getVoiceCustomBytes() {
        return voiceCustomBytes;
    }

    public byte[] getCruiseCustomBytes() {
        return cruiseCustomBytes;
    }

    public SerialPortManager getActionManager() {
        return mActionManager;
    }

    public SerialPortManager getVoiceManager() {
        return mVoiceManager;
    }

    public SerialPortManager getCruiseManager() {
        return mCruiseManager;
    }

    public List<ReceivedListener> getObservers() {
        return mObservers;
    }

    public static final class Builder {

        private Context context;

        private String actionName;
        private String voiceName;
        private String cruiseName;

        private int actionBaudrate;
        private int voiceBaudrate;
        private int cruiseBaudrate;

        private int deliveryAction;
        private int deliveryVoice;
        private int deliveryCruise;

        private int receiveAction;
        private int receiveVoice;
        private int receiveCruise;

        private byte[] actionCustomBytes;
        private byte[] voiceCustomBytes;
        private byte[] cruiseCustomBytes;

        public Builder() {
            actionName = "ttyXRUSB2";
            voiceName = "ttyS4";
            cruiseName = "ttyXRUSB0";

            actionBaudrate = 9600;
            voiceBaudrate = 115200;
            cruiseBaudrate = 38400;

            deliveryAction = Format.Delivery.DEFAULT;
            deliveryVoice = Format.Delivery.DEFAULT;
            deliveryCruise = Format.Delivery.DEFAULT;

            receiveAction = Format.Receive.DEFAULT;
            receiveVoice = Format.Receive.DEFAULT;
            receiveCruise = Format.Receive.DEFAULT;
        }

        public Builder configContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder configAction(String actionName, int actionBaudRate) {
            if (TextUtils.isEmpty(actionName)) {
                throw new NullPointerException("actionName == null");
            }
            this.actionName = actionName;
            actionBaudrate = actionBaudRate;
            return this;
        }

        public Builder configVoice(String voiceName, int voiceBaudRate) {
            if (TextUtils.isEmpty(voiceName)) {
                throw new NullPointerException("voiceName == null");
            }
            this.voiceName = voiceName;
            voiceBaudrate = voiceBaudRate;
            return this;
        }

        public Builder configCruise(String cruiseName, int cruiseBaudRate) {
            if (TextUtils.isEmpty(cruiseName)) {
                throw new NullPointerException("cruiseName == null");
            }
            this.cruiseName = cruiseName;
            cruiseBaudrate = cruiseBaudRate;
            return this;
        }

        public Builder formatDeliveryAction(@Format.Delivery int delivery) {
            deliveryAction = delivery;
            return this;
        }

        public Builder formatDeliveryVoice(@Format.Delivery int delivery) {
            deliveryVoice = delivery;
            return this;
        }

        public Builder formatDeliveryCruise(@Format.Delivery int delivery) {
            deliveryCruise = delivery;
            return this;
        }

        public Builder formatReceiveAction(@Format.Receive int receive) {
            receiveAction = receive;
            return this;
        }

        public Builder formatReceiveVoice(@Format.Receive int receive) {
            receiveVoice = receive;
            return this;
        }

        public Builder formatReceiveCruise(@Format.Receive int receive) {
            receiveCruise = receive;
            return this;
        }

        public Builder customAction(byte[] bytes) {
            if (bytes == null) {
                throw new NullPointerException("bytes == null");
            }
            deliveryAction = Format.Delivery.CUSTOM;
            actionCustomBytes = bytes;
            return this;
        }

        public Builder customVoice(byte[] bytes) {
            if (bytes == null) {
                throw new NullPointerException("bytes == null");
            }
            deliveryVoice = Format.Delivery.CUSTOM;
            voiceCustomBytes = bytes;
            return this;
        }

        public Builder customCruise(byte[] bytes) {
            if (bytes == null) {
                throw new NullPointerException("bytes == null");
            }
            deliveryCruise = Format.Delivery.CUSTOM;
            cruiseCustomBytes = bytes;
            return this;
        }

        public SerialConfig build() {
            if (TextUtils.equals(actionName, voiceName) ||
                    TextUtils.equals(actionName, cruiseName) ||
                    TextUtils.equals(voiceName, cruiseName)) {
                throw new RuntimeException("Same serial slogan");
            }
            return new SerialConfig(this);
        }
    }

}
