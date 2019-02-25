package com.robot.seabreeze.serial;

/**
 * Author: MiLan
 * Date: 2019/2/25
 * Description:
 */
public class SerialPreferences {

    private static String PRE_ACTION_NAME = "PRE_ACTION_NAME";
    private static String PRE_AVOICE_NAME = "PRE_AVOICE_NAME";
    private static String PRE_ACRUISE_NAME = "PRE_ACRUISE_NAME";

    private static String PRE_AACTION_BAUDRATE = "PRE_AACTION_BAUDRATE";
    private static String PRE_AVOICE_BAUDRATE = "PRE_AVOICE_BAUDRATE";
    private static String PRE_ACRUISE_BAUDRATE = "PRE_ACRUISE_BAUDRATE";

    private static String PRE_ADELIVERY_ACTION = "PRE_ADELIVERY_ACTION";
    private static String PRE_ADELIVERY_VOICE = "PRE_ADELIVERY_VOICE";
    private static String PRE_ADELIVERY_CRUISE = "PRE_ADELIVERY_CRUISE";

    private static String PRE_ARECEIVE_ACTION = "PRE_ARECEIVE_ACTION";
    private static String PRE_ARECEIVE_VOICE = "PRE_ARECEIVE_VOICE";
    private static String PRE_ARECEIVE_CRUISE = "PRE_ARECEIVE_CRUISE";

    public static void setActionNamePre(String actionName) {
        PreferencesUtils.putString(SerialControl.getInstance().getContext(), PRE_ACTION_NAME, actionName);
    }

    public static String getActionNamePre() {
        return PreferencesUtils.getString(SerialControl.getInstance().getContext(), PRE_ACTION_NAME, "ttyXRUSB2");
    }

    public static void setVoiceNamePre(String voiceName) {
        PreferencesUtils.putString(SerialControl.getInstance().getContext(), PRE_AVOICE_NAME, voiceName);
    }

    public static String getVoiceNamePre() {
        return PreferencesUtils.getString(SerialControl.getInstance().getContext(), PRE_AVOICE_NAME, "ttyS4");
    }

    public static void setCruiseNamePre(String cruiseName) {
        PreferencesUtils.putString(SerialControl.getInstance().getContext(), PRE_ACRUISE_NAME, cruiseName);
    }

    public static String getCruiseNamePre() {
        return PreferencesUtils.getString(SerialControl.getInstance().getContext(), PRE_ACRUISE_NAME, "ttyXRUSB0");
    }

    public static void setActionBaudratePre(int actionBaudrate) {
        PreferencesUtils.putInt(SerialControl.getInstance().getContext(), PRE_AACTION_BAUDRATE, actionBaudrate);
    }

    public static int getActionBaudratePre() {
        return PreferencesUtils.getInt(SerialControl.getInstance().getContext(), PRE_AACTION_BAUDRATE, 9600);
    }

    public static void setVoiceBaudratePre(int voiceBaudrate) {
        PreferencesUtils.putInt(SerialControl.getInstance().getContext(), PRE_AVOICE_BAUDRATE, voiceBaudrate);
    }

    public static int getVoiceBaudratePre() {
        return PreferencesUtils.getInt(SerialControl.getInstance().getContext(), PRE_AVOICE_BAUDRATE, 115200);
    }

    public static void setCruiseBaudratePre(int cruiseBaudrate) {
        PreferencesUtils.putInt(SerialControl.getInstance().getContext(), PRE_ACRUISE_BAUDRATE, cruiseBaudrate);
    }

    public static int getCruiseBaudratePre() {
        return PreferencesUtils.getInt(SerialControl.getInstance().getContext(), PRE_ACRUISE_BAUDRATE, 38400);
    }

    public static void setDeliveryActionPre(@Format.Delivery int deliveryAction) {
        PreferencesUtils.putInt(SerialControl.getInstance().getContext(), PRE_ADELIVERY_ACTION, deliveryAction);
    }


    public static @Format.Delivery
    int getDeliveryActionPre() {
        return PreferencesUtils.getInt(SerialControl.getInstance().getContext(), PRE_ADELIVERY_ACTION, Format.Delivery.DEFAULT);
    }

    public static void setDeliveryVoicePre(@Format.Delivery int deliveryVoice) {
        PreferencesUtils.putInt(SerialControl.getInstance().getContext(), PRE_ADELIVERY_VOICE, deliveryVoice);
    }

    public static @Format.Delivery
    int getDeliveryVoicePre() {
        return PreferencesUtils.getInt(SerialControl.getInstance().getContext(), PRE_ADELIVERY_VOICE, Format.Delivery.DEFAULT);
    }

    public static void setDeliveryCruisePre(@Format.Delivery int deliveryCruise) {
        PreferencesUtils.putInt(SerialControl.getInstance().getContext(), PRE_ADELIVERY_CRUISE, deliveryCruise);
    }

    public static @Format.Delivery
    int getDeliveryCruisePre() {
        return PreferencesUtils.getInt(SerialControl.getInstance().getContext(), PRE_ADELIVERY_CRUISE, Format.Delivery.DEFAULT);
    }

    public static void setReceiveActionPre(@Format.Receive int receiveAction) {
        PreferencesUtils.putInt(SerialControl.getInstance().getContext(), PRE_ARECEIVE_ACTION, receiveAction);
    }


    public static @Format.Receive
    int getReceiveActionPre() {
        return PreferencesUtils.getInt(SerialControl.getInstance().getContext(), PRE_ARECEIVE_ACTION, Format.Receive.DEFAULT);
    }

    public static void setReceiveVoicePre(@Format.Receive int receiveVoice) {
        PreferencesUtils.putInt(SerialControl.getInstance().getContext(), PRE_ARECEIVE_VOICE, receiveVoice);
    }

    public static @Format.Receive
    int getReceiveVoicePre() {
        return PreferencesUtils.getInt(SerialControl.getInstance().getContext(), PRE_ARECEIVE_VOICE, Format.Receive.DEFAULT);
    }

    public static void setReceiveCruisePre(@Format.Receive int receiveCruise) {
        PreferencesUtils.putInt(SerialControl.getInstance().getContext(), PRE_ARECEIVE_CRUISE, receiveCruise);
    }

    public static @Format.Receive
    int getReceiveCruisePre() {
        return PreferencesUtils.getInt(SerialControl.getInstance().getContext(), PRE_ARECEIVE_CRUISE, Format.Receive.DEFAULT);
    }
}
