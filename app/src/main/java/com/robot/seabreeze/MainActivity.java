package com.robot.seabreeze;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

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
    private Button btnStopMove;
    private EditText etTime;

    LinearLayout ll;

    private Handler mHandler = new Handler();

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
        this.btnStopMove = findViewById(R.id.btn_stop_move);
        this.etTime = findViewById(R.id.time);

        btnaction.setOnClickListener(this);
        btnvoice.setOnClickListener(this);
        btncruise.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnStopMove.setOnClickListener(this);
        SerialControl.getInstance().startManager();

        ll = findViewById(R.id.ll_test);

        String[] action = getResources().getStringArray(R.array.action);
        String[] actionOrder = getResources().getStringArray(R.array.action_order);

        for (int i = 0; i < action.length; i++) {
            Button view = new Button(this);
            view.setText(action[i]);
            view.setTag(actionOrder[i]);
            view.setOnClickListener(this);
            ll.addView(view);
        }

        findViewById(R.id.btn_scan_clear).setOnClickListener(this);
        findViewById(R.id.btn_start_qr_nfc).setOnClickListener(this);
        findViewById(R.id.btn_junp_nfc).setOnClickListener(this);
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

    private StringBuffer mStringBuffer = new StringBuffer();

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
            mStringBuffer.append(info + "\n\n\n--------------------------------------------\n\n\n");
            etvoicereceived.setText(mStringBuffer.toString());
            if (info.contains("WAKE UP!")) {
                SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.stop_all_action));
                String str;
                if (info.contains("score:")) {
                    str = info.substring(info.indexOf("angle:") + 6, info.indexOf("score:"));
                } else {
                    str = info.substring(info.indexOf("angle:") + 6, info.indexOf("##### IFLYTEK"));
                }
                int angle = Integer.parseInt(str.trim());

                Logger.e("解析到应该旋转的角度 : " + angle);

                if (SerialControl.getInstance().isContrary()) {
                    if (0 <= angle && angle < 30) {
                        SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.lift_turn_large_angle));
                    } else if (30 <= angle && angle <= 60) {
                        SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.lift_turn_angle));
                    } else if (120 <= angle && angle <= 150) {
                        SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.right_turn_angle));
                    } else if (150 < angle && angle <= 180) {
                        SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.right_turn_large_angle));
                    }
                } else {
                    if (0 <= angle && angle < 30) {
                        SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.right_turn_large_angle));
                    } else if (30 <= angle && angle <= 60) {
                        SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.right_turn_angle));
                    } else if (120 <= angle && angle <= 150) {
                        SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.lift_turn_angle));
                    } else if (150 < angle && angle <= 180) {
                        SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.lift_turn_large_angle));
                    }
                }

                SerialControl.getInstance().sendVoiceData("BEAM 0\n\r");//0
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

        @Override
        public void onScanReceived(String info) {
            super.onScanReceived(info);
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
            case R.id.btn_stop_move:
                String time = etTime.getText().toString();
                if (TextUtils.isEmpty(time)) {
                    SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.stop_all_action));
                    SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.right_turn_large_angle));
                } else {
                    int cTime = Integer.valueOf(time);
                    SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.stop_all_action));
                    Logger.e("stop");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SerialControl.getInstance().sendActionData(NovelApp.getInstance().getResources().getString(R.string.right_turn_large_angle));
                            Logger.e("move");
                        }
                    }, cTime);
                }
                break;
            case R.id.btn_scan_clear:
                SerialControl.getInstance().sendScanData("55AA21010000DF");
                break;
            case R.id.btn_start_qr_nfc:
                SerialControl.getInstance().sendScanData("55AA21010009D6");
                break;
            case R.id.btn_junp_nfc:
                SerialControl.getInstance().sendScanData("55AA2101000FD0");
                break;
        }
        String tag = (String) v.getTag();
        if (!TextUtils.isEmpty(tag)) {
            Logger.e(tag);
            SerialControl.getInstance().sendActionData(tag);
        }
    }
}
