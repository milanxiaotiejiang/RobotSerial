
[ ![Download](https://api.bintray.com/packages/milanxiaotiejiang/RobotLog/RobotLog/images/download.svg) ](https://bintray.com/milanxiaotiejiang/RobotLog/RobotLog/_latestVersion)

## 如何使用它
	dependencies { 
		implementation 'com.seabreezerobot:LogSerial:v1.0.0'
	}

## 初始化
    自定义Application中启动默认参数
	SerialControl.getInstance().startManager(this);

	可以自定义
    SerialConfig.Builder builder = new SerialConfig.Builder();
    SerialConfig config = builder
                    .configContext(this)
                    .configAction("ttyS1", 9600)
                    .configVoice("ttyS2", 115200)
                    .configCruise("ttyS3", 38400)
                    .formatDeliveryAction(Format.Delivery.HEXTOBYTE)
                    .formatReceiveAction(Format.Receive.CUSTOM)
                    .formatDeliveryVoice(Format.Delivery.DEFAULT)
                    .formatReceiveVoice(Format.Receive.BYTETOHEX)
                    .formatDeliveryCruise(Format.Delivery.HEXTOBYTE)
                    .formatReceiveCruise(Format.Receive.DEFAULT)
                    .build();
    config.initManager();
    
## 使用
###
    设置页面
    <activity
                android:name=".SettingActivity"
                android:theme="@style/Serial.Compat" />

    <FrameLayout
            android:id="@+id/fragment_container"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

    getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new SerialPortSettingFragment())
                    .commit();

###
    使用页面
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

### 1.动作
      SerialControl.getInstance().sendActionData(action);

### 2.语音
      SerialControl.getInstance().sendVoiceData(voice);

### 3.导航
      SerialControl.getInstance().sendCruiseData(cruise);

### 回调
    ReceiveData receiveData = new ReceiveData()

## LICENSE

    Copyright (c) 2019-present, Log Contributors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.