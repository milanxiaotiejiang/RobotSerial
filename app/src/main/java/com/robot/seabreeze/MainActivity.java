package com.robot.seabreeze;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robot.seabreeze.log.Logger;
import com.robot.seabreeze.serial.HexUtils;
import com.robot.seabreeze.serial.ReceiveData;
import com.robot.seabreeze.serial.SerialControl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private android.widget.Button btnaction;
    private android.widget.Button btnvoice;
    private android.widget.Button btncruise;
    private Button btnSetting;
    private android.widget.EditText etaction;
    private android.widget.EditText etvoice;
    private android.widget.EditText etcruise;
    private EditText etactionreceived;
    private EditText etvoicereceived;
    private EditText etcruisereceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.etcruisereceived = (EditText) findViewById(R.id.et_cruise_received);
        this.etvoicereceived = (EditText) findViewById(R.id.et_voice_received);
        this.etactionreceived = (EditText) findViewById(R.id.et_action_received);
        this.etcruise = (EditText) findViewById(R.id.et_cruise);
        this.etvoice = (EditText) findViewById(R.id.et_voice);
        this.etaction = (EditText) findViewById(R.id.et_action);
        this.btncruise = (Button) findViewById(R.id.btn_cruise);
        this.btnvoice = (Button) findViewById(R.id.btn_voice);
        this.btnaction = (Button) findViewById(R.id.btn_action);
        this.btnSetting = findViewById(R.id.btn_setting);

        btnaction.setOnClickListener(this);
        btnvoice.setOnClickListener(this);
        btncruise.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        SerialControl.getInstance().startManager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SerialControl.getInstance().registerObserver(receiveData);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SerialControl.getInstance().unRegisterObserver(receiveData);
    }

    @Override
    protected void onDestroy() {
        SerialControl.getInstance().stopManager();
        super.onDestroy();
    }

    ReceiveData receiveData = new ReceiveData() {
        @Override
        public void onActionReceived(String info) {
            super.onActionReceived(info);
            etactionreceived.setText(info);
        }

        @Override
        public void onActionReceived(byte[] bytes) {
            super.onActionReceived(bytes);
            String feedback = HexUtils.hexStringToString(HexUtils.byte2HexStr(bytes));
            etactionreceived.setText(feedback);
        }

        @Override
        public void onVoiceReceived(String info) {
            super.onVoiceReceived(info);
            etvoicereceived.setText(info);
            if (info.contains("WAKE UP!")) {

                String str;
                if (info.contains("score:")) {
                    str = info.substring(info.indexOf("angle:") + 6, info.indexOf("score:"));
                } else {
                    str = info.substring(info.indexOf("angle:") + 6, info.indexOf("##### IFLYTEK"));
                }
                int angle = Integer.parseInt(str.trim());

                Logger.e("解析到应该旋转的角度 : " + angle);
            }
        }

        @Override
        public void onVoiceReceived(byte[] bytes) {
            super.onVoiceReceived(bytes);
        }

        @Override
        public void onCruiseReceived(String info) {
            super.onCruiseReceived(info);
            etcruisereceived.setText(info);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_action:
                String action = etaction.getText().toString();
                if (TextUtils.isEmpty(action)) {
                    return;
                }
                SerialControl.getInstance().sendActionData(action);
                break;
            case R.id.btn_voice:
                String voice = etvoice.getText().toString();
                if (TextUtils.isEmpty(voice)) {
                    return;
                }
                SerialControl.getInstance().sendVoiceData(voice);
                break;
            case R.id.btn_cruise:
                String cruise = etcruise.getText().toString();
                if (TextUtils.isEmpty(cruise)) {
                    return;
                }
                SerialControl.getInstance().sendCruiseData(cruise);
                break;
            case R.id.btn_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
        }
    }
}
