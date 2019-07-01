package com.robot.seabreeze;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.robot.seabreeze.serial.SerialControl;
import com.robot.seabreeze.serial.setting.SerialPortSettingFragment;

/**
 * Author: MiLan
 * Date: 2019/2/25
 * Description:
 */
public class SettingActivity extends QMUIFragmentActivity {

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.fragment_container, new SerialPortSettingFragment())
//                .commit();
//    }
    @Override
    protected int getContextViewId() {
        return R.id.main_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SerialPortSettingFragment fragment = SerialPortSettingFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(getContextViewId(), fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }
}
