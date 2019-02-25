package com.robot.seabreeze.serial;

/**
 * Author: MiLan
 * Date: 2019/2/24
 * Description: 解析格式
 */
public @interface Format {

    @interface Delivery {
        int DEFAULT = 0;
        int HEXTOBYTE = 1;
        int CUSTOM = 2;
    }

    @interface Receive {
        int DEFAULT = 0;
        int BYTETOHEX = 1;
        int CUSTOM = 2;
    }

}
