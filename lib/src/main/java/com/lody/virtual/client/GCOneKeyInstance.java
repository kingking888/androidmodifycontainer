package com.lody.virtual.client;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;

import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.remote.VDeviceInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import mirror.RefStaticObject;

public class GCOneKeyInstance {


    private static GCOneKeyInstance instance = null;


    public static GCOneKeyInstance getInstance()
    {
        if(instance==null)
        {
            instance = new GCOneKeyInstance();
            instance.OneKey = instance.getClass();
        }
        return instance;
    }

    public String currentFile = "";
    private Map<String,String> mMap = new HashMap<>();

    private Properties properties;


    String[] keys = {"ro.build.id","ro.build.display.id","ro.build.version.incremental","ro.build.version.sdk","ro.build.version.codename","ro.build.version.release",
            "ro.build.date","ro.build.date.utc","ro.product.model","ro.product.brand","ro.product.name","ro.product.device","ro.product.board","ro.product.cpu.abi",
            "ro.product.cpu.abi2","ro.product.manufacturer","ro.board.platform","ro.build.product","ro.build.fingerprint"
    };
    String[] vals = {};
    public RefStaticObject<String>[] valshook = new RefStaticObject[]{};
    public Class OneKey = null;

    public void newDevices()
    {
        currentFile = System.currentTimeMillis()+"_build.prop";



//        ro.build.id=JLS36C   //build的版本号，一般在编译时产生不必修改
//        ro.build.display.id=JLS36C.N7508VZMUAOF1   //显示的版本号，可以任意修改，显示为手机信息的内部版本号
//        ro.build.version.incremental=N7508VZMUAOF1  //版本的增加说明，一般不显示也没必要修改
//        ro.build.version.sdk=18   //系统编译时，使用的SDK版本，勿修改
//        ro.build.version.codename=REL  //版本编码名称，一般不显示也没必要修改
//        ro.build.version.release=4.3   //公布的版本，显示为手机信息的系统版本
//        ro.build.date=Tue Jun  9 20:23:45 KST 2015   //系统编译的时间，没必要修改，显示为手机信息的内核版本栏
//        ro.build.date.utc=1433849025   //系统编译的时间（数字版），没必要修改
//        ro.build.type=user    //系统编译类型：user、userdebug、eng，一般不显示也没必要修改
//        ro.build.user=se.infra   //系统用户名，可以修改称自己的名字
//        ro.build.host=SWDB2805   //系统主机名，可以随便起个英文字母表示的名字
//        ro.build.tags=release-keys   //系统标记，无意义，不修改
//        ro.product.model=SM-N7508V   //机器型号，可以修改成自己认为牛逼的名字
//        ro.product.brand=samsung    //机器品牌，随你修改
//        ro.product.name=hlltezm    //机器名，随你修改
//        ro.product.device=hllte    //设备名，随你修改
//        ro.product.board=MSM8928    //主板名，随你修改
//        ro.product.cpu.abi=armeabi-v7a    //cpu版本 勿修改
//        ro.product.cpu.abi2=armeabi    //cup品牌  勿修改

        properties = new Properties();
        properties.clear();//清空属性表里面的值






        for (int i = 0 ; i < keys.length;i++)
        {
            String v = null;
            if(vals[i].contains("m_"))
            {
              String[] valss = vals[i].split("_");
                try {
                  Method m =  OneKey.getMethod(valss[1]);

                  if(m!=null)
                  {
                      v = (String) m.invoke(this);
                  }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                v = vals[i];
            }
//            properties.setProperty(keys[i],);
        }




        OutputStream fw = null;
        try {

            File file = new File(Environment.getExternalStorageDirectory()+"/"+currentFile);

            if(!file.exists())
            {
                file.createNewFile();
            }
            currentFile = file.getAbsolutePath();
            fw = new FileOutputStream(file);

            NativeEngine.redirectDirectory("/system/build.prop",currentFile);

            this.main();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            properties.store(fw, "gctech");
        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            if(fw!=null)
            {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void back()
    {
        if(properties==null) {
            properties = new Properties();
        }
        try {
            properties.clear();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "back-build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //start hook
        this.main();
    }


//    private VDeviceInfo generateRandomDeviceInfo() {
//        VDeviceInfo info = new VDeviceInfo();
//        String value;
//        do {
//            value = generate10(15);
//            info.deviceId = value;
//        } while (mPool.deviceIds.contains(value));
//        do {
//            value = generate16(16);
//            info.androidId = value;
//        } while (mPool.androidIds.contains(value));
//        do {
//            value = generateMac();
//            info.wifiMac = value;
//        } while (mPool.wifiMacs.contains(value));
//        do {
//            value = generateMac();
//            info.bluetoothMac = value;
//        } while (mPool.bluetoothMacs.contains(value));
//
//        do {
//            value = generate10(20);
//            info.iccId = value;
//        } while (mPool.iccIds.contains(value));
//
//        info.serial = generateSerial();
//
//        addDeviceInfoToPool(info);
//        return info;
//    }




    private static String generate10(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private static String generate16(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int nextInt = random.nextInt(16);
            if (nextInt < 10) {
                sb.append(nextInt);
            } else {
                sb.append((char) (nextInt + 87));
            }
        }
        return sb.toString();
    }

    private static String generateMac() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int next = 1;
        int cur = 0;
        while (cur < 12) {
            int val = random.nextInt(16);
            if (val < 10) {
                sb.append(val);
            } else {
                sb.append((char) (val + 87));
            }
            if (cur == next && cur != 11) {
                sb.append(":");
                next += 2;
            }
            cur++;
        }
        return sb.toString();
    }

    @SuppressLint("HardwareIds")
    private static String generateSerial() {
        String serial;
        if (Build.SERIAL == null || Build.SERIAL.length() <= 0) {
            serial = "0123456789ABCDEF";
        } else {
            serial = Build.SERIAL;
        }
        List<Character> list = new ArrayList<>();
        for (char c : serial.toCharArray()) {
            list.add(c);
        }
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (Character c : list) {
            sb.append(c.charValue());
        }
        return sb.toString();
    }


    public   void main()
    {
//            .addCollector(new PhoneBasicInfoCollector(this, "basic"))       //Andorid设备基本信息（PhoneBasicInfoCollector）
//            .addCollector(new SimInfoCollector(this, "sim"))                //Sim卡信息（SimInfoCollector）同时识别多张Sim卡
//            .addCollector(new CpuInfoCollector(this, "cpu"))                //Cpu信息（CpuInfoCollector）
//            .addCollector(new BoardInfoCollector(this, "board"))            //主板信息（BoardInfoCollector）
//            .addCollector(new BatteryInfoCollector(this, "battery"))        //电池信息（BatteryInfoCollector）
//            .addCollector(new StorageInfoCollector(this, "storage"))        //存储信息（RAM & SD）（StorageInfoCollector）
//            .addCollector(new CameraInfoCollector(this, "camera", true))    //摄像头信息（CameraInfoCollector）
//            .addCollector(new ScreenInfoCollector(this, "screen"))          //屏幕信息（ScreenInfoCollector）
//            .addCollector(new UiInfoCollector(this, "ui"))                  //Ui信息（UiInfoCollector）
//            .addCollector(new SensorInfoCollector(this, "sensor"))          //传感器列表（SensorInfoCollector）
//            .addCollector(new NfcInfoCollector(this, "nfc"))                //NFC信息（NfcInfoCollector）
//            .addCollector(new SystemInfoCollector(this, "system"))          //系统相关信息（Build.prop等）

        hookBasic();

    }


    public  void hookBasic()
    {


        for (int i = 0 ; i < keys.length;i++)
        {
            if(valshook[i]!=null) {
                valshook[i].set(properties.getProperty(keys[i]));
            }
        }
//        mirror.android.os.Build.DEVICE.set("-----------------");
//        mirror.android.os.Build.SERIAL.set();
//        mirror.android.os.Build.BRAND.set("-----------------");
//        mirror.android.os.Build.PRODUCT.set("tiancaichenweiming");
    }









}
