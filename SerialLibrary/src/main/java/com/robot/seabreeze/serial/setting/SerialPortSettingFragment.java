package com.robot.seabreeze.serial.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.serial.Device;
import com.robot.seabreeze.serial.Format;
import com.robot.seabreeze.serial.R;
import com.robot.seabreeze.serial.SerialConfig;
import com.robot.seabreeze.serial.SerialControl;
import com.robot.seabreeze.serial.SerialPortFinder;
import com.robot.seabreeze.serial.SerialPreferences;

import java.util.ArrayList;

/**
 * Author: MiLan
 * Date: 2019/2/25
 * Description:
 */
public class SerialPortSettingFragment extends Fragment {


    private QMUIGroupListView mGroupListView;
    private QMUIRoundButton mStructure;

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
            Logger.e(devices);
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

        QMUICommonListItemView actionSwitchItem = mGroupListView.createItemView("是否Hex解析");
        actionSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        actionSwitchItem.getSwitch().setChecked(SerialPreferences.getDeliveryActionPre() == Format.Delivery.HEXTOBYTE);
        actionSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setDeliveryActionPre(isChecked ? Format.Delivery.HEXTOBYTE : Format.Delivery.DEFAULT);
            }
        });

        QMUICommonListItemView voiceDevItem = createItemView("串口名", SerialPreferences.getVoiceNamePre());
        voiceDevItem.setTag(VOICE_DEV_ITEM_ID);
        voiceDevItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView voiceBaudrateItem = createItemView("串口号", String.valueOf(SerialPreferences.getVoiceBaudratePre()));
        voiceBaudrateItem.setTag(VOICE_BAUDRATE_ITEM_ID);
        voiceBaudrateItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView voiceSwitchItem = mGroupListView.createItemView("是否Hex解析");
        voiceSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        voiceSwitchItem.getSwitch().setChecked(SerialPreferences.getDeliveryVoicePre() == Format.Delivery.HEXTOBYTE);
        voiceSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setDeliveryVoicePre(isChecked ? Format.Delivery.HEXTOBYTE : Format.Delivery.DEFAULT);
            }
        });

        QMUICommonListItemView cruiseDevItem = createItemView("串口名", SerialPreferences.getCruiseNamePre());
        cruiseDevItem.setTag(CRUISE_DEV_ITEM_ID);
        cruiseDevItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView cruiseBaudrateItem = createItemView("串口号", String.valueOf(SerialPreferences.getCruiseBaudratePre()));
        cruiseBaudrateItem.setTag(CRUISE_BAUDRATE_ITEM_ID);
        voiceBaudrateItem.setId(CRUISE_BAUDRATE_ITEM_ID);
        cruiseBaudrateItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView cruiseSwitchItem = mGroupListView.createItemView("是否Hex解析");
        cruiseSwitchItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        cruiseSwitchItem.getSwitch().setChecked(SerialPreferences.getDeliveryCruisePre() == Format.Delivery.HEXTOBYTE);
        cruiseSwitchItem.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SerialPreferences.setDeliveryCruisePre(isChecked ? Format.Delivery.HEXTOBYTE : Format.Delivery.DEFAULT);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    showSingleChoiceDialog((int) v.getTag());
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
            }
        });
    }

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    private void showSingleChoiceDialog(final int tag) {
        final String[] items = new String[]{"选项1", "选项2", "选项3"};
        final int checkedIndex = 1;
        new QMUIDialog.CheckableDialogBuilder(getActivity())
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (tag) {
                            case ACTION_DEV_ITEM_ID:
                                Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                                break;
                            case ACTION_BAUDRATE_ITEM_ID:

                                break;
                            case VOICE_DEV_ITEM_ID:

                                break;
                            case VOICE_BAUDRATE_ITEM_ID:

                                break;
                            case CRUISE_DEV_ITEM_ID:

                                break;
                            case CRUISE_BAUDRATE_ITEM_ID:

                                break;
                        }

                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}
