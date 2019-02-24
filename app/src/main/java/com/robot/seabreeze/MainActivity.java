package com.robot.seabreeze;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.robot.seabreeze.serial.ReceiveData;
import com.robot.seabreeze.serial.SerialControl;
import com.robot.seabreeze.serial.listener.ReceivedListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SerialControl.getInstance().registerObserver(receiveData);


        SerialControl.getInstance().sendActionData("132");
    }

    @Override
    protected void onDestroy() {
        SerialControl.getInstance().unRegisterObserver(receiveData);
        SerialControl.getInstance().stopManager();
        super.onDestroy();
    }

    ReceiveData receiveData = new ReceiveData() {
    };
}
