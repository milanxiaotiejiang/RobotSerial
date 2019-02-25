package com.robot.seabreeze.serial.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.serial.Device;
import com.robot.seabreeze.serial.Format;
import com.robot.seabreeze.serial.R;
import com.robot.seabreeze.serial.SerialControl;
import com.robot.seabreeze.serial.SerialPortFinder;
import com.robot.seabreeze.serial.SerialPreferences;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: MiLan
 * Date: 2019/2/25
 * Description:
 */
public class SerialPortSettingFragment extends Fragment {


    private QMUIGroupListView mGroupListView;
    private QMUIRoundButton mStructure;

    private String[] deviceNameArrays;
    private String[] deviceBaudrateArrays = {"4800", "9600", "1920", "38400", "57600", "115200", "230400",
            "460800", "500000", "576000", "921600", "1000000", "1152000"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.serial_port_setting_fragment_layout, container, false);
        this.mStructure = (QMUIRoundButton) view.findViewById(R.id.structure);
        this.mGroupListView = (QMUIGroupListView) view.findViewById(R.id.groupListView);

        initGroupListView();

        initStructure();

        initDriver();

        return view;
    }


    private final SerialPortFinder finder = new SerialPortFinder();

    private void initDriver() {
        if (finder.canRead()) {
            ArrayList<Device> devices = finder.getDevices();
            deviceNameArrays = new String[devices.size()];
            for (int i = 0; i < devices.size(); i++) {
                deviceNameArrays[i] = devices.get(i).getName();
            }
        }
    }

    public static final int ACTION_DEV_ITEM_ID = 1;
    public static final int ACTION_BAUDRATE_ITEM_ID = 2;
    public static final int VOICE_DEV_ITEM_ID = 3;
    public static final int VOICE_BAUDRATE_ITEM_ID = 4;
    public static final int CRUISE_DEV_ITEM_ID = 5;
    public static final int CRUISE_BAUDRATE_ITEM_ID = 6;

    private void initGroupListView() {

        QMUICommonListItemView actionDevItem = createItemView("串口名", SerialPreferences.getActionNamePre());
        actionDevItem.setTag(ACTION_DEV_ITEM_ID);
        actionDevItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView actionBaudrateItem = createItemView("串口号", String.valueOf(SerialPreferences.getActionBaudratePre()));
        actionBaudrateItem.setTag(ACTION_BAUDRATE_ITEM_ID);
        actionBaudrateItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView actionSwitchItem = mGroupListView.createItemView("Hex发送");
        actionSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        actionSwitchItem.getSwitch().setChecked(SerialPreferences.getDeliveryActionPre() == Format.Delivery.HEXTOBYTE);
        actionSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setDeliveryActionPre(isChecked ? Format.Delivery.HEXTOBYTE : Format.Delivery.DEFAULT);
            }
        });
        QMUICommonListItemView actionReceivedSwitchItem = mGroupListView.createItemView("Hex接受");
        actionReceivedSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        actionReceivedSwitchItem.getSwitch().setChecked(SerialPreferences.getReceiveActionPre() == Format.Receive.BYTETOHEX);
        actionReceivedSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setReceiveActionPre(isChecked ? Format.Receive.BYTETOHEX : Format.Receive.DEFAULT);
            }
        });

        QMUICommonListItemView voiceDevItem = createItemView("串口名", SerialPreferences.getVoiceNamePre());
        voiceDevItem.setTag(VOICE_DEV_ITEM_ID);
        voiceDevItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView voiceBaudrateItem = createItemView("串口号", String.valueOf(SerialPreferences.getVoiceBaudratePre()));
        voiceBaudrateItem.setTag(VOICE_BAUDRATE_ITEM_ID);
        voiceBaudrateItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView voiceSwitchItem = mGroupListView.createItemView("Hex发送");
        voiceSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        voiceSwitchItem.getSwitch().setChecked(SerialPreferences.getDeliveryVoicePre() == Format.Delivery.HEXTOBYTE);
        voiceSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setDeliveryVoicePre(isChecked ? Format.Delivery.HEXTOBYTE : Format.Delivery.DEFAULT);
            }
        });

        QMUICommonListItemView voiceReceivedSwitchItem = mGroupListView.createItemView("Hex接受");
        voiceReceivedSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        voiceReceivedSwitchItem.getSwitch().setChecked(SerialPreferences.getDeliveryVoicePre() == Format.Receive.BYTETOHEX);
        voiceReceivedSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setReceiveVoicePre(isChecked ? Format.Receive.BYTETOHEX : Format.Receive.DEFAULT);
            }
        });

        QMUICommonListItemView cruiseDevItem = createItemView("串口名", SerialPreferences.getCruiseNamePre());
        cruiseDevItem.setTag(CRUISE_DEV_ITEM_ID);
        cruiseDevItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView cruiseBaudrateItem = createItemView("串口号", String.valueOf(SerialPreferences.getCruiseBaudratePre()));
        cruiseBaudrateItem.setTag(CRUISE_BAUDRATE_ITEM_ID);
        voiceBaudrateItem.setId(CRUISE_BAUDRATE_ITEM_ID);
        cruiseBaudrateItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView cruiseSwitchItem = mGroupListView.createItemView("Hex发送");
        cruiseSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        cruiseSwitchItem.getSwitch().setChecked(SerialPreferences.getDeliveryCruisePre() == Format.Delivery.HEXTOBYTE);
        cruiseSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setDeliveryCruisePre(isChecked ? Format.Delivery.HEXTOBYTE : Format.Delivery.DEFAULT);
            }
        });

        QMUICommonListItemView cruiseReceivedSwitchItem = mGroupListView.createItemView("Hex发送");
        cruiseReceivedSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        cruiseReceivedSwitchItem.getSwitch().setChecked(SerialPreferences.getReceiveCruisePre() == Format.Receive.BYTETOHEX);
        cruiseReceivedSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setReceiveCruisePre(isChecked ? Format.Receive.BYTETOHEX : Format.Receive.DEFAULT);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    showSingleChoiceDialog((QMUICommonListItemView) v);
                }
            }
        };

        QMUIGroupListView.newSection(getContext())
                .setTitle("动作设置")
                .addItemView(actionDevItem, onClickListener)
                .addItemView(actionBaudrateItem, onClickListener)
                .addItemView(actionSwitchItem, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle("语音设置")
                .addItemView(voiceDevItem, onClickListener)
                .addItemView(voiceBaudrateItem, onClickListener)
                .addItemView(voiceSwitchItem, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle("导航设置")
                .addItemView(cruiseDevItem, onClickListener)
                .addItemView(cruiseBaudrateItem, onClickListener)
                .addItemView(cruiseSwitchItem, onClickListener)
                .addTo(mGroupListView);
    }

    private QMUICommonListItemView createItemView(CharSequence titleText, String detailText) {
        return mGroupListView.createItemView(null, titleText, detailText,
                QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_NONE);
    }

    private void initStructure() {
        mStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SerialControl.getInstance().stopManager();
                SerialControl.getInstance().startManager(getActivity().getApplicationContext());
                getActivity().finish();
            }
        });
    }


    private void showSingleChoiceDialog(final QMUICommonListItemView v) {
        switch ((int) v.getTag()) {
            case ACTION_DEV_ITEM_ID:
            case VOICE_DEV_ITEM_ID:
            case CRUISE_DEV_ITEM_ID:
                if (deviceNameArrays == null || deviceNameArrays.length == 0) {
                    Toast.makeText(getActivity(), "当前设备没有串口", Toast.LENGTH_SHORT).show();
                    return;
                }
                String devName = String.valueOf(v.getDetailText());
                int devNameCheckedIndex = Arrays.binarySearch(deviceNameArrays, devName);
                dialogShow(v, deviceNameArrays, devNameCheckedIndex);
                break;
            case ACTION_BAUDRATE_ITEM_ID:
            case VOICE_BAUDRATE_ITEM_ID:
            case CRUISE_BAUDRATE_ITEM_ID:
                if (deviceBaudrateArrays == null || deviceBaudrateArrays.length == 0) {
                    Toast.makeText(getActivity(), "当前设备没有串口", Toast.LENGTH_SHORT).show();
                    return;
                }
                int devBaudrate = Integer.valueOf(v.getDetailText().toString());
                int devBaudrateCheckedIndex = Arrays.binarySearch(deviceBaudrateArrays, devBaudrate);
                dialogShow(v, deviceBaudrateArrays, devBaudrateCheckedIndex);
                break;
        }
    }

    private void dialogShow(final QMUICommonListItemView v, final CharSequence[] arrays, int checkedIndex) {
        new QMUIDialog.CheckableDialogBuilder(getActivity())
                .setCheckedIndex(checkedIndex)
                .addItems(arrays, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String actionNamePre = SerialPreferences.getActionNamePre();
                        String voiceNamePre = SerialPreferences.getVoiceNamePre();
                        String cruiseNamePre = SerialPreferences.getCruiseNamePre();

                        switch ((int) v.getTag()) {
                            case ACTION_DEV_ITEM_ID:
                                String actionDevName = String.valueOf(arrays[which]);
                                Logger.e(actionDevName);

                                if (TextUtils.equals(actionDevName, voiceNamePre) || TextUtils.equals(actionDevName, cruiseNamePre)) {
                                    Toast.makeText(getActivity(), "串口已经被占用", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                v.setDetailText(actionDevName);
                                SerialPreferences.setActionNamePre(actionDevName);
                                break;
                            case VOICE_DEV_ITEM_ID:
                                String voiceDevName = String.valueOf(arrays[which]);
                                Logger.e(voiceDevName);

                                if (TextUtils.equals(voiceDevName, actionNamePre) || TextUtils.equals(voiceDevName, cruiseNamePre)) {
                                    Toast.makeText(getActivity(), "串口已经被占用", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                v.setDetailText(voiceDevName);
                                SerialPreferences.setVoiceNamePre(voiceDevName);
                                break;
                            case CRUISE_DEV_ITEM_ID:
                                String cruiseDevName = String.valueOf(arrays[which]);
                                Logger.e(cruiseDevName);

                                if (TextUtils.equals(cruiseDevName, actionNamePre) || TextUtils.equals(cruiseDevName, voiceNamePre)) {
                                    Toast.makeText(getActivity(), "串口已经被占用", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                v.setDetailText(cruiseDevName);
                                SerialPreferences.setCruiseNamePre(cruiseDevName);
                                break;
                            case ACTION_BAUDRATE_ITEM_ID:
                                int actionBaudrate = Integer.valueOf(arrays[which].toString());
                                Logger.e(actionBaudrate);
                                v.setDetailText(String.valueOf(actionBaudrate));
                                SerialPreferences.setActionBaudratePre(actionBaudrate);
                                break;
                            case VOICE_BAUDRATE_ITEM_ID:
                                int voiceBaudrate = Integer.valueOf(arrays[which].toString());
                                Logger.e(voiceBaudrate);
                                v.setDetailText(String.valueOf(voiceBaudrate));
                                SerialPreferences.setVoiceBaudratePre(voiceBaudrate);
                                break;
                            case CRUISE_BAUDRATE_ITEM_ID:
                                int cruiseBaudrate = Integer.valueOf(arrays[which].toString());
                                Logger.e(cruiseBaudrate);
                                v.setDetailText(String.valueOf(cruiseBaudrate));
                                SerialPreferences.setCruiseBaudratePre(cruiseBaudrate);
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

}
