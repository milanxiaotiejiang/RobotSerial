package com.robot.seabreeze.serial.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fanfan.robot.common.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.serial.Device;
import com.robot.seabreeze.serial.Format;
import com.robot.seabreeze.serial.PreferencesUtils;
import com.robot.seabreeze.serial.R;
import com.robot.seabreeze.serial.SerialControl;
import com.robot.seabreeze.serial.SerialPortFinder;
import com.robot.seabreeze.serial.SerialPreferences;
import com.robot.seabreeze.serial.view.CustomRadioGroup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: MiLan
 * Date: 2019/2/25
 * Description:
 */
public class SerialPortSettingFragment extends QMUIFragment {

    public static SerialPortSettingFragment newInstance() {
        Bundle args = new Bundle();
        SerialPortSettingFragment fragment = new SerialPortSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private QMUITopBarLayout mTopbar;

    private CustomRadioGroup rgPreviewAngle;
    private RadioButton openMirror;
    private RadioButton closeMirror;

    private QMUIGroupListView mGroupListView;
    private QMUIRoundButton mStructure;

    private String[] deviceNameArrays;
    private String[] deviceBaudrateArrays = {"4800", "9600", "1920", "38400", "57600", "115200", "230400",
            "460800", "500000", "576000", "921600", "1000000", "1152000"};

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.serial_port_setting_fragment_layout, null);

        mTopbar = (QMUITopBarLayout) view.findViewById(R.id.topbar);
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mTopbar.setTitle("串口设置");

        rgPreviewAngle = view.findViewById(R.id.rg_preview_angle);
        rgPreviewAngle.setOnCheckedChangeListener(new CustomRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomRadioGroup group, int checkedId) {
                if (checkedId == R.id.open_mirror_btn) {
                    SerialPreferences.setDirectionRotation(false);
                } else if (checkedId == R.id.close_mirror_btn) {
                    SerialPreferences.setDirectionRotation(true);
                }
            }
        });

        openMirror = view.findViewById(R.id.open_mirror_btn);
        closeMirror = view.findViewById(R.id.close_mirror_btn);
        if (SerialControl.getInstance().isContrary()) {
            closeMirror.setChecked(true);
        } else {
            openMirror.setChecked(true);
        }

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
    public static final int SCAN_DEV_ITEM_ID = 7;
    public static final int SCAN_BAUDRATE_ITEM_ID = 8;

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
        voiceReceivedSwitchItem.getSwitch().setChecked(SerialPreferences.getReceiveVoicePre() == Format.Receive.BYTETOHEX);
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

        QMUICommonListItemView cruiseReceivedSwitchItem = mGroupListView.createItemView("Hex接受");
        cruiseReceivedSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        cruiseReceivedSwitchItem.getSwitch().setChecked(SerialPreferences.getReceiveCruisePre() == Format.Receive.BYTETOHEX);
        cruiseReceivedSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setReceiveCruisePre(isChecked ? Format.Receive.BYTETOHEX : Format.Receive.DEFAULT);
            }
        });

        QMUICommonListItemView scanDevItem = createItemView("串口名", SerialPreferences.getScanNamePre());
        scanDevItem.setTag(SCAN_DEV_ITEM_ID);
        scanDevItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView scanBaudrateItem = createItemView("串口号", String.valueOf(SerialPreferences.getScanBaudratePre()));
        scanBaudrateItem.setTag(SCAN_BAUDRATE_ITEM_ID);
        scanBaudrateItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView scanSwitchItem = mGroupListView.createItemView("Hex发送");
        scanSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        scanSwitchItem.getSwitch().setChecked(SerialPreferences.getDeliveryScanPre() == Format.Delivery.HEXTOBYTE);
        scanSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setDeliveryScanPre(isChecked ? Format.Delivery.HEXTOBYTE : Format.Delivery.DEFAULT);
            }
        });

        QMUICommonListItemView scanReceivedSwitchItem = mGroupListView.createItemView("Hex接受");
        scanReceivedSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        scanReceivedSwitchItem.getSwitch().setChecked(SerialPreferences.getReceiveScanPre() == Format.Receive.BYTETOHEX);
        scanReceivedSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setReceiveScanPre(isChecked ? Format.Receive.BYTETOHEX : Format.Receive.DEFAULT);
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
                .addItemView(actionReceivedSwitchItem, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle("语音设置")
                .addItemView(voiceDevItem, onClickListener)
                .addItemView(voiceBaudrateItem, onClickListener)
                .addItemView(voiceSwitchItem, onClickListener)
                .addItemView(voiceReceivedSwitchItem, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle("导航设置")
                .addItemView(cruiseDevItem, onClickListener)
                .addItemView(cruiseBaudrateItem, onClickListener)
                .addItemView(cruiseSwitchItem, onClickListener)
                .addItemView(cruiseReceivedSwitchItem, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle("扫描仪设置")
                .addItemView(scanDevItem, onClickListener)
                .addItemView(scanBaudrateItem, onClickListener)
                .addItemView(scanSwitchItem, onClickListener)
                .addItemView(scanReceivedSwitchItem, onClickListener)
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
                SerialControl.getInstance().startManager();
                getActivity().finish();
            }
        });
    }


    private void showSingleChoiceDialog(final QMUICommonListItemView v) {
        switch ((int) v.getTag()) {
            case ACTION_DEV_ITEM_ID:
            case VOICE_DEV_ITEM_ID:
            case CRUISE_DEV_ITEM_ID:
            case SCAN_DEV_ITEM_ID:
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
            case SCAN_BAUDRATE_ITEM_ID:
                if (deviceBaudrateArrays == null || deviceBaudrateArrays.length == 0) {
                    Toast.makeText(getActivity(), "当前设备没有串口", Toast.LENGTH_SHORT).show();
                    return;
                }
                String s = v.getDetailText().toString();
                int devBaudrate = Integer.valueOf(s);
                int devBaudrateCheckedIndex = Arrays.binarySearch(deviceBaudrateArrays, String.valueOf(devBaudrate));
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
                        String scanNamePre = SerialPreferences.getScanNamePre();

                        switch ((int) v.getTag()) {
                            case ACTION_DEV_ITEM_ID:
                                String actionDevName = String.valueOf(arrays[which]);
                                Logger.e(actionDevName);

                                if (TextUtils.equals(actionDevName, voiceNamePre) ||
                                        TextUtils.equals(actionDevName, cruiseNamePre) ||
                                        TextUtils.equals(actionDevName, scanNamePre)) {
                                    Toast.makeText(getActivity(), "串口已经被占用", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                v.setDetailText(actionDevName);
                                SerialPreferences.setActionNamePre(actionDevName);
                                break;
                            case VOICE_DEV_ITEM_ID:
                                String voiceDevName = String.valueOf(arrays[which]);
                                Logger.e(voiceDevName);

                                if (TextUtils.equals(voiceDevName, actionNamePre) ||
                                        TextUtils.equals(voiceDevName, cruiseNamePre) ||
                                        TextUtils.equals(voiceDevName, scanNamePre)) {
                                    Toast.makeText(getActivity(), "串口已经被占用", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                v.setDetailText(voiceDevName);
                                SerialPreferences.setVoiceNamePre(voiceDevName);
                                break;
                            case CRUISE_DEV_ITEM_ID:
                                String cruiseDevName = String.valueOf(arrays[which]);
                                Logger.e(cruiseDevName);

                                if (TextUtils.equals(cruiseDevName, actionNamePre) ||
                                        TextUtils.equals(cruiseDevName, voiceNamePre) ||
                                        TextUtils.equals(cruiseDevName, scanNamePre)) {
                                    Toast.makeText(getActivity(), "串口已经被占用", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                v.setDetailText(cruiseDevName);
                                SerialPreferences.setCruiseNamePre(cruiseDevName);
                                break;
                            case SCAN_DEV_ITEM_ID:
                                String scanDevName = String.valueOf(arrays[which]);
                                Logger.e(scanDevName);

                                if (TextUtils.equals(scanDevName, actionNamePre) ||
                                        TextUtils.equals(scanDevName, voiceNamePre) ||
                                        TextUtils.equals(scanDevName, cruiseNamePre)) {
                                    Toast.makeText(getActivity(), "串口已经被占用", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                v.setDetailText(scanDevName);
                                SerialPreferences.setScanNamePre(scanDevName);
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
                            case SCAN_BAUDRATE_ITEM_ID:
                                int scanBaudrate = Integer.valueOf(arrays[which].toString());
                                Logger.e(scanBaudrate);
                                v.setDetailText(String.valueOf(scanBaudrate));
                                SerialPreferences.setScanBaudratePre(scanBaudrate);
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

}
