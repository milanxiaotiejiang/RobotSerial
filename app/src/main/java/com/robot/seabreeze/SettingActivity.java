package com.robot.seabreeze;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.robot.seabreeze.serial.SerialControl;
import com.robot.seabreeze.serial.setting.SerialPortSettingFragment;

/**
 * Author: MiLan
 * Date: 2019/2/25
 * Description:
 */
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new SerialPortSettingFragment())
                .commit();
    }
}
