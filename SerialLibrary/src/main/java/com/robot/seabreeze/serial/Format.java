package com.robot.seabreeze.serial;

/**
 * Author: MiLan
 * Date: 2019/2/24
 * Description: 解析格式
 */
public class Format {

    public enum Delivery {
        DEFAULT,
        HEXTOBYTE,
        CUSTOM
    }

    public enum Receive {
        DEFAULT,
        BYTETOHEX,
        CUSTOM
    }

}
