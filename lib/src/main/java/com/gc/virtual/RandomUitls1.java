package com.gc.virtual;

import com.lody.virtual.helper.utils.IMEIGen;
import com.lody.virtual.helper.utils.RandomUitls;

import org.apache.commons.text.CharacterPredicates;

import java.util.Random;

public class RandomUitls1 {

    public static final Companion Companion = new Companion();
    public final ANDROID_VERSION androidVersion = ANDROID_VERSION.values()[RandomUitls.nextInt(0, ((Object[]) ANDROID_VERSION.values()).length)];
    public final NETWORK_TYPE networkType = NETWORK_TYPE.values()[RandomUitls.nextInt(0, ((Object[]) NETWORK_TYPE.values()).length)];
    public final SIM_TYPE simType = SIM_TYPE.values()[RandomUitls.nextInt(0, ((Object[]) SIM_TYPE.values()).length)];

     /* compiled from: Random.kt */
    public enum ANDROID_VERSION {
        ICE_CREAM_SANDWICH_MR1("4.0.3", 15),
        JELLY_BEAN_MR1("4.2", 17),
        KITKAT("4.4", 19),
        KITKAT_WATCH("4.4W", 20),
        LOLLIPOP_MR1("5.0", 22),
        M("6.0", 23),
        N("7.0", 24);

        public static  Companion Companion = null;
        private final int versionCode;

        private final String versionName;


         ANDROID_VERSION( String versionName, int versionCode) {
            this.versionName = versionName;
            this.versionCode = versionCode;
        }

        public final int getVersionCode() {
            return this.versionCode;
        }

        public final String getVersionName() {
            return this.versionName;
        }

        static {
            Companion = new Companion();
        }
    }

   /* compiled from: Random.kt */
    public static final class Companion {
        private Companion() {
        }




        public final java.util.Random New() {
            return new java.util.Random();
        }
    }
  /* compiled from: Random.kt */
    public enum LANGUAGES {
        CN("中国_简体", "zh_CN"),
        TW("中国_繁体", "zh_TW"),
        EN("美国", "en_US"),
        JP("日本", "ja_JP");

        public static  Companion Companion = null;

        private final String code;

        private final String label;



         LANGUAGES( String label,  String code) {
            this.label = label;
            this.code = code;
        }

        public final String getCode() {
            return this.code;
        }

        public final String getLabel() {
            return this.label;
        }

        static {
            Companion = new Companion();
        }
    }

    public enum NETWORK_TYPE {
        _2G("2G", String.valueOf(1)),
        _3G("3G", String.valueOf(3)),
        _4G("4G", String.valueOf(13)),
        WIFI("WIFI", "wifi");

        public static  Companion Companion = null;

        private final String code;

        private final String label;


         NETWORK_TYPE( String label,  String code) {

            this.label = label;
            this.code = code;
        }


        public final String getCode() {
            return this.code;
        }


        public final String getLabel() {
            return this.label;
        }

        static {
            Companion = new Companion();
        }
    }
    /* compiled from: Random.kt */
    public enum SIM_COUNTRY_ISO {
        CN("中国", "cn"),
        EN("美国", "en");

        public static  Companion Companion = null;

        private final String code;

        private final String label;

         SIM_COUNTRY_ISO( String label,  String code) {

            this.label = label;
            this.code = code;
        }


        public final String getCode() {
            return this.code;
        }


        public final String getLabel() {
            return this.label;
        }

        static {
            Companion = new Companion();
        }
    }


    public enum SIM_TYPE {
        CMCC("中国移动", "46000", "898600", SIM_COUNTRY_ISO.CN.getCode()),
        CUCC("中国联通", "46001", "898601", SIM_COUNTRY_ISO.CN.getCode()),
        CTCC("中国电信", "46003", "898603", SIM_COUNTRY_ISO.CN.getCode());

        public static  Companion Companion = null;

        private final String label;

        private final String simCode;

        private final String simCountryIso;

        private final String simIccid;



         SIM_TYPE( String label,  String simCode,  String simIccid,  String simCountryIso) {

            this.label = label;
            this.simCode = simCode;
            this.simIccid = simIccid;
            this.simCountryIso = simCountryIso;
        }

        public final String getLabel() {
            return this.label;
        }

        public final String getSimCode() {
            return this.simCode;
        }


        public final String getSimCountryIso() {
            return this.simCountryIso;
        }


        public final String getSimIccid() {
            return this.simIccid;
        }

        static {
            Companion = new Companion();
        }
    }


    public final String buildVersion() {
        return this.androidVersion.getVersionName();
    }



    private static Random r = new Random();

    public final String simLine1Number() {
        boolean isUserArea;
        String[] telFirst = (String[]) new String[]{"134", "135", "136", "137", "138", "139", "150", "151", "152", "157", "158", "159", "130", "131", "132", "155", "156", "133", "153"};
        String line1Number = "";
//        if (RandomUitls.nextInt(0, 100) < 30) {
//            isUserArea = true;
//        } else {
//            isUserArea = false;
//        }
//        if (isUserArea) {
            line1Number = "+86";
//        }
        return line1Number + telFirst[r.nextInt(telFirst.length)] + generate16(8);
    }



    private static String generate16(int length) {
            java.util.Random random = new java.util.Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int nextInt = random.nextInt(9);

                    sb.append(nextInt);

            }

        return sb.toString();
    }

    public final String simGetDeviceId() {
        String simDeviceId = "86" + generate16(12);
        return simDeviceId + IMEIGen.INSTANCE.genCode(simDeviceId);
    }


    public final String simSubscriberId( SIM_TYPE simType) {

        return simType.getSimCode() + generate16(10);
    }


    public final String simOperator( SIM_TYPE simType) {

        return simType.getSimCode();
    }


    public final String simCountryIso( SIM_TYPE simType) {

        return simType.getSimCountryIso();
    }


    public final String simOperatorName( SIM_TYPE simType) {

        return simType.getLabel();
    }


    public final String simSimState( SIM_TYPE simType) {

        return String.valueOf(5);
    }


    public final String simSerialNumber( SIM_TYPE simType) {
        return simType.getSimIccid() + generate16(14);
    }

    public final String networkType() {
        return this.networkType.getCode();
    }


    public final String wifiName() {
        String[] strings = (String[]) new String[]{"TP-", "FAST_", "Tenda_", "TP-LINK_", "MERCURY_"};
        return strings[RandomUitls.nextInt(0, ((Object[]) strings).length - 1)] + generate16(8);
    }





//    @NotNull
//    public final JSONObject randomAll() {
//        List buildManufacturerList = CollectionsKt___CollectionsKt.toList(Phones.Companion.getInstance().getPhoneList().keySet());
//        ArrayList buildModelList = Phones.Companion.getInstance().getPhoneList().get((String) buildManufacturerList.get(RandomUtils.nextInt(0, buildManufacturerList.size())));
//        if (buildModelList == null) {
//            Intrinsics.throwNpe();
//        }
//        buildModelList = buildModelList;
//        PhoneModel buildPhoneRandom = (PhoneModel) buildModelList.get(RandomUtils.nextInt(0, buildModelList.size()));
//        JSONObject randomJsonObject = new JSONObject();
//        randomJsonObject.put("android.os.Build.ro.product.manufacturer", buildPhoneRandom.getManufacturer());
//        randomJsonObject.put("android.os.Build.ro.product.model", buildPhoneRandom.getModel());
//        randomJsonObject.put("android.os.Build.ro.serialno", buildSerial());
//        randomJsonObject.put("android.os.Build.VERSION.RELEASE", buildVersion());
//        randomJsonObject.put("android.os.SystemProperties.android_id", androidId());
//        randomJsonObject.put("android.telephony.TelephonyManager.getLine1Number", simLine1Number());
//        randomJsonObject.put("android.telephony.TelephonyManager.getDeviceId", simGetDeviceId());
//        randomJsonObject.put("android.telephony.TelephonyManager.getSubscriberId", simSubscriberId(this.simType));
//        randomJsonObject.put("android.telephony.TelephonyManager.getSimOperator", simOperator(this.simType));
//        randomJsonObject.put("android.telephony.TelephonyManager.getSimCountryIso", simCountryIso(this.simType));
//        randomJsonObject.put("android.telephony.TelephonyManager.getSimOperatorName", simOperatorName(this.simType));
//        randomJsonObject.put("android.telephony.TelephonyManager.getSimSerialNumber", simSerialNumber(this.simType));
//        randomJsonObject.put("android.telephony.TelephonyManager.getSimState", simSimState(this.simType));
//        randomJsonObject.put("android.net.NetworkInfo.getType", networkType());
//        randomJsonObject.put("android.net.wifi.WifiInfo.getSSID", wifiName());
//        randomJsonObject.put("android.net.wifi.WifiInfo.getBSSID", wifiMacAddress());
//        randomJsonObject.put("android.net.wifi.WifiInfo.getMacAddress", wifiMacAddress());
//        return randomJsonObject;
//    }

    public  static  void main(String[] str)
    {
       System.out.print(generate16(128));
    }
}
