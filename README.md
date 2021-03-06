
[ ![Download](https://api.bintray.com/packages/milanxiaotiejiang/SerialLibrary/SerialLibrary/images/download.svg?version=v1.0.0) ](https://bintray.com/milanxiaotiejiang/SerialLibrary/SerialLibrary/v1.0.0/link)

## 如何使用它
	dependencies { 
		implementation 'com.seabreezerobot:SerialLibrary:v1.0.2'
	}

## 初始化

    QMUISwipeBackActivityManager.init(this);

    自定义Application中启动默认参数
	SerialControl.getInstance().init(this);

## 使用
###
#### 设置页面
    <activity
                android:name=".SettingActivity"
                android:theme="@style/Serial.NoActionBar.AppTheme" />

    继承 QMUIFragmentActivity

    SerialPortSettingFragment fragment = SerialPortSettingFragment.newInstance();
    getSupportFragmentManager()
              .beginTransaction()
              .add(getContextViewId(), fragment, fragment.getClass().getSimpleName())
              .addToBackStack(fragment.getClass().getSimpleName())
              .commit();

###
#### 使用页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

#### 1.动作
      SerialControl.getInstance().sendActionData(action);

#### 2.语音
      SerialControl.getInstance().sendVoiceData(voice);

#### 3.导航
      SerialControl.getInstance().sendCruiseData(cruise);

#### 回调
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