package com.robot.seabreeze.serial.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.serial.Device;
import com.robot.seabreeze.serial.R;
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
    private QMUIWindowInsetLayout mSerialportlayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.serial_port_setting_fragment_layout, container, false);
        this.mSerialportlayout = (QMUIWindowInsetLayout) view.findViewById(R.id.serial_port_layout);
        this.mGroupListView = (QMUIGroupListView) view.findViewById(R.id.groupListView);

        initGroupListView();

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


    private void initGroupListView() {

        QMUICommonListItemView actionDevItem = createItemView("动作串口名", SerialPreferences.getActionNamePre());
        actionDevItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView actionBaudrateItem = createItemView("动作串口号", String.valueOf(SerialPreferences.getActionBaudratePre()));
        actionBaudrateItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView voiceDevItem = createItemView("语音串口名", SerialPreferences.getVoiceNamePre());
        voiceDevItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView voiceBaudrateItem = createItemView("语音串口号", String.valueOf(SerialPreferences.getVoiceBaudratePre()));
        voiceBaudrateItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView cruiseDevItem = createItemView("导航串口名", SerialPreferences.getCruiseNamePre());
        cruiseDevItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView cruiseBaudrateItem = createItemView("导航串口号", String.valueOf(SerialPreferences.getCruiseBaudratePre()));
        cruiseBaudrateItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    if (((QMUICommonListItemView) v).getAccessoryType() == QMUICommonListItemView.ACCESSORY_TYPE_SWITCH) {
                        ((QMUICommonListItemView) v).getSwitch().toggle();
                    }
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
        QMUIGroupListView.newSection(getContext())
                .setTitle("串口设置")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(actionDevItem, onClickListener)
                .addItemView(actionBaudrateItem, onClickListener)
                .addItemView(voiceDevItem, onClickListener)
                .addItemView(voiceBaudrateItem, onClickListener)
                .addItemView(cruiseDevItem, onClickListener)
                .addItemView(cruiseBaudrateItem, onClickListener)
                .addTo(mGroupListView);
    }

    private QMUICommonListItemView createItemView(CharSequence titleText, String detailText) {
        return mGroupListView.createItemView(null, titleText, detailText,
                QMUICommonListItemView.HORIZONTAL, QMUICommonListItemView.ACCESSORY_TYPE_NONE);
    }


}
