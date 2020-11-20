package com.lody.virtual.remote;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.StringCompress;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lody
 */
public class VDeviceInfo implements Parcelable {

    public String deviceId = "";
    public String androidId = "";
    public String wifiMac = "" ;
    public String bluetoothMac = "";
    public String iccId = "";
    public String serial ="";
    public String gmsAdId ="";
    public String brand ="";
    public String sdk= "";
    public String rom = "";
    public String product= "";
    public String model= "";
    public String hardware = "";
    public String host = "";
    public String display ="";

    public String release= "";
    public String line1Number= "";
    public String subscriberId= "";
    public String simOperator= "";
    public String simCountryIso= "";
    public String simOperatorName= "";
    public String simSerialNumber= "";
    public String simState= "";

    public String ssid = "";
    public String bssid = "";

    public String cpu_abi = "";
    public String cpu_abi2 = "";

    private Map map = new HashMap();



    public Map getMap()
    {
//        if(map.size()==0)
//        {
            map.put("ro.product.model",model);
            map.put("ro.product.brand",brand);
//        }
        return map;
    }


    public String getVal(String key)
    {
        if(map.size()==0)
        {
            map.put("ro.product.model",model);
            map.put("ro.product.brand",brand);
        }
        return map.get(key)==null?"":map.get(key).toString();
    }

//      randomJsonObject.put("android.os.Build.ro.product.manufacturer", buildPhoneRandom.getManufacturer());
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
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceId);
        dest.writeString(this.androidId);
        dest.writeString(this.wifiMac);
        dest.writeString(this.bluetoothMac);
        dest.writeString(this.iccId);
        dest.writeString(this.serial);
        dest.writeString(this.gmsAdId);
        dest.writeString(this.brand);
        dest.writeString(this.sdk);
        dest.writeString(this.rom);
        dest.writeString(this.product);
        dest.writeString(this.model);
        dest.writeString(this.hardware);
        dest.writeString(this.host);
        dest.writeString(this.display);

        dest.writeString(this.release);
        dest.writeString(this.line1Number);
        dest.writeString(this.subscriberId);
        dest.writeString(this.simOperator);
        dest.writeString(this.simCountryIso);
        dest.writeString(this.simOperatorName);
        dest.writeString(this.simSerialNumber);
        dest.writeString(this.simState);
        dest.writeString(this.ssid);
        dest.writeString(this.bssid);
        dest.writeString(this.cpu_abi);
        dest.writeString(this.cpu_abi2);


    }

    public VDeviceInfo() {}

    private String sep = ",";

    private static String key = "02382806317288247651724808335364486688373814576145862312627555536274566215783322128077125742364311480726848256606346650208288150";

    @Override
    public String toString() {

        StringBuffer str = new StringBuffer();

//        str.append( deviceId).append(sep).append(androidId).append(sep).append(wifiMac).append(sep).append(bluetoothMac).append(sep).append(iccId).append(sep).append(serial).append(sep).append(gmsAdId).append(sep).append(brand).append(sep).append(sdk);
//        str.append(sep).append(rom).append(sep).append(product).append(sep).append(model).append(sep).append(hardware).append(sep);
//        str.append(host).append(sep).append(display).append(sep).append(release);
//        str.append(sep).append(line1Number).append(sep).append(subscriberId).append(sep).append(simOperator);
//        str.append(sep).append(simCountryIso);
//        str.append(sep).append(simOperatorName);
//        str.append(sep).append(simSerialNumber).append(sep).append(simState).append(sep).append(ssid).append(sep).append(bssid);



        str.append( deviceId).append(sep).append(androidId).append(sep).append(wifiMac).append(sep).append(bluetoothMac).append(sep).append(brand).append(sep).append(sdk);
        str.append(sep).append(model).append(sep).append(release);
        str.append(sep).append(line1Number).append(sep).append(serial).append(sep);
        str.append(subscriberId).append(sep).append(simOperator);
        str.append(sep).append(simCountryIso);
        str.append(sep).append(simOperatorName);
        str.append(sep).append(simSerialNumber).append(sep).append(simState).append(sep).append(iccId).append(sep).append(ssid);
//        .append(sep).append(ssid).append(sep).append(bssid);
//        VLog.d("gctech",StringCompress.zip(str.toString()));
//        return  new String(encrypt(str.toString().getBytes()));
        return str.toString();
    }

    public VDeviceInfo(Parcel in) {
        this.deviceId = in.readString();
        this.androidId = in.readString();
        this.wifiMac = in.readString();
        this.bluetoothMac = in.readString();
        this.iccId = in.readString();
        this.serial = in.readString();
        this.gmsAdId = in.readString();
        this.brand = in.readString();
        this.sdk = in.readString();
        this.rom = in.readString();
        this.product = in.readString();
        this.model = in.readString();
        this.hardware = in.readString();
        this.host = in.readString();
        this.display = in.readString();
        //write to sdcard

        this.release = in.readString();
        this.line1Number = in.readString();
        this.subscriberId = in.readString();

        this.simOperator = in.readString();
        this.simCountryIso = in.readString();
        this.simOperatorName = in.readString();
        this.simSerialNumber = in.readString();
        this.simState = in.readString();
        this.ssid = in.readString();
        this.bssid = in.readString();

        this.cpu_abi = in.readString();
        this.cpu_abi2 = in.readString();

    }

    public  void createBack()
    {
        File back = new File("/sdcard/back.txt");
        synchronized (back) {

            if (back.exists()) {
                back.delete();
                try {
                    back.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                RandomAccessFile file = new RandomAccessFile(back, "rws");

                file.write(this.toString().getBytes());
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int keyStr = 0x45333;
    public static byte[] encrypt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        int key = keyStr;
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (bytes[i] ^ key);
            key = bytes[i];
        }
        return bytes;
    }


    public static byte[] decrypt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        int key = keyStr;
        for (int i = len - 1; i > 0; i--) {
            bytes[i] = (byte) (bytes[i] ^ bytes[i - 1]);
        }
        bytes[0] = (byte) (bytes[0] ^ key);
        return bytes;
    }
    public synchronized static VDeviceInfo  restore(String path)
    {
        VDeviceInfo deviceInfo = new VDeviceInfo();






//        deviceInfoStr = StringCompress.unzip(deviceInfoStr);
            String deviceInfoStr =  FileUtils.readTxtFile("/sdcard/re.txt");

//        deviceInfoStr =  new String(decrypt(deviceInfoStr.getBytes()));
//        VLog.d("gctech","restorerestorerestorerestorerestore:"+deviceInfoStr);
            String[] str = deviceInfoStr.split(",");


//
//        str.append( deviceId).append(sep).append(androidId).append(sep).append(wifiMac).append(sep).append(bluetoothMac).append(sep).append(brand).append(sep).append(sdk);
//        str.append(sep).append(product).append(sep).append(model).append(sep).append(release);
//        str.append(sep).append(line1Number);



            if (deviceInfoStr.length() >= 12) {
                deviceInfo.deviceId = str[0];
                deviceInfo.androidId = str[1];
                deviceInfo.wifiMac = str[2];
                deviceInfo.bluetoothMac = str[3];
                deviceInfo.brand = str[4];
                deviceInfo.sdk = str[5];
                deviceInfo.product = str[6];
                deviceInfo.model = str[6];
                deviceInfo.release = str[7];
                deviceInfo.line1Number = str[8];
//                deviceInfo.cpu_abi = str[10];
//                deviceInfo.cpu_abi2 = str[11];
//                deviceInfo.iccId = str[12];
                deviceInfo.serial = str[9];
                deviceInfo.subscriberId = str[10];
                deviceInfo.simOperator = str[11];
                deviceInfo.simCountryIso = str[12];
                deviceInfo.simOperatorName = str[13];
                deviceInfo.simSerialNumber = str[14];
                deviceInfo.simState = str[15];
                deviceInfo.iccId = str[16];
                deviceInfo.ssid = str[17];
//                str.append(subscriberId).append(sep).append(simOperator);
//                str.append(sep).append(simCountryIso);
//                str.append(sep).append(simOperatorName);
//                str.append(sep).append(simSerialNumber).append(sep).append(simState);
            }

//        deviceInfo.deviceId = str[0];
//        deviceInfo.androidId = str[1];
//        deviceInfo.wifiMac = str[2];
//        deviceInfo.bluetoothMac = str[3];
//        deviceInfo.iccId = str[4];
//        deviceInfo.serial = str[5];
//        deviceInfo.gmsAdId = str[6];
//        deviceInfo.brand = str[7];
//        deviceInfo.sdk = str[8];
//        deviceInfo.rom = str[9];
//        deviceInfo.product = str[10];
//        deviceInfo.model = str[11];
//        deviceInfo.hardware = str[12];
//        deviceInfo.host = str[13];
//        deviceInfo.display = str[14];
//        deviceInfo.release = str[15];
//        deviceInfo.line1Number = str[16];
//        deviceInfo.subscriberId = str[17];
//        deviceInfo.simOperator = str[18];
//        deviceInfo.simCountryIso = str[19];
//        deviceInfo.simOperatorName = str[20];
//        deviceInfo.simSerialNumber = str[21];
//        deviceInfo.simState = str[22];
//        deviceInfo.ssid = str[23];
//        deviceInfo.bssid = str[24];
        return deviceInfo;
    }

    public static final Parcelable.Creator<VDeviceInfo> CREATOR = new Parcelable.Creator<VDeviceInfo>() {
        @Override
        public VDeviceInfo createFromParcel(Parcel source) {
            return new VDeviceInfo(source);
        }

        @Override
        public VDeviceInfo[] newArray(int size) {
            return new VDeviceInfo[size];
        }
    };

    public File getWifiFile(int userId) {
        File wifiMacFie = VEnvironment.getWifiMacFile(userId);
        if (!wifiMacFie.exists()) {
            try {
                RandomAccessFile file = new RandomAccessFile(wifiMacFie, "rws");
                file.write((wifiMac + "\n").getBytes());
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wifiMacFie;
    }
}
