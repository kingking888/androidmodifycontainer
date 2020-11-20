package com.lody.virtual.server.device;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.RemoteException;

import com.lody.virtual.client.DataCleanManager;
import com.gc.virtual.VirtualCore;
import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.helper.utils.FileUtils;
import com.gc.virtual.RandomUitls1;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.VDeviceInfo;
import com.lody.virtual.server.IDeviceInfoManager;
import com.lody.virtual.server.pm.VAppManagerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import mirror.android.app.ActivityManagerNative;
import mirror.android.app.IActivityManager;

/**
 * @author Lody
 */

public class VDeviceManagerService extends IDeviceInfoManager.Stub {

    private static VDeviceManagerService sInstance = new VDeviceManagerService();
    private final SparseArray<VDeviceInfo> mDeviceInfos = new SparseArray<>();
    private DeviceInfoPersistenceLayer mPersistenceLayer = new DeviceInfoPersistenceLayer(this);
    private UsedDeviceInfoPool mPool = new UsedDeviceInfoPool();
    private static String[] brands = {"huawei","samsang","vivo"};
    private static String[] products = {"huawei","samsang","vivo",""};

    private static String[] sdks = {"19","20","21"};

    private static String[] models = {"XT531","samsang I559","samsang I889"};

    private static String[] CPUABIS = {"armeabi","armeabi-v7a","arm64-v8a"};

    private  JSONArray phoneModel = null;

    public static VDeviceManagerService get() {
        return sInstance;
    }


    private final class UsedDeviceInfoPool {
        List<String> deviceIds = new ArrayList<>();
        List<String> androidIds = new ArrayList<>();
        List<String> wifiMacs = new ArrayList<>();
        List<String> bluetoothMacs = new ArrayList<>();
        List<String> iccIds = new ArrayList<>();
    }

    public VDeviceManagerService() {
        mPersistenceLayer.read();
        for (int i = 0; i < mDeviceInfos.size(); i++) {
            VDeviceInfo info = mDeviceInfos.valueAt(i);
            addDeviceInfoToPool(info);
            VLog.d("gctech",i+":create deviceinfo:"+info.wifiMac+":"+info.brand);
        }

        if(mDeviceInfos.size()>0 && mDeviceInfos.get(0)!=null)
        {
            deviceId = mDeviceInfos.get(0).deviceId;
            mac = mDeviceInfos.get(0).wifiMac;
            serial = mDeviceInfos.get(0).serial;
            androidId = mDeviceInfos.get(0).androidId;
            sdk = mDeviceInfos.get(0).sdk;
            brand = mDeviceInfos.get(0).sdk;
            model = mDeviceInfos.get(0).model;
        }


        phoneModel = new JSONArray();
        StringBuffer json = new StringBuffer();
        try {
            InputStream in =  VirtualCore.get().getContext().getAssets().open("phone.json");

            BufferedReader mBufferReader = new BufferedReader(new InputStreamReader(in));
            String str = null;

            while ((str = mBufferReader.readLine()) !=null)
            {
                json.append(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            JSONObject mJson = new JSONObject(json.toString());


            JSONObject phoneList =  mJson.getJSONObject("PhoneList");

            Iterator<String> it =  phoneList.keys();
            while (it.hasNext())
            {
                String key =  it.next();

                JSONArray phoneSubJson = phoneList.getJSONArray(key);
                //get some phone info to
                VLog.d("gctect","ziji:"+phoneSubJson.toString());

                for (int i = 0; i < phoneSubJson.length();i++) {
                    phoneModel.put(phoneSubJson.getJSONObject(i));
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addDeviceInfoToPool(VDeviceInfo info) {
        mPool.deviceIds.add(info.deviceId);
        mPool.androidIds.add(info.androidId);
        mPool.wifiMacs.add(info.wifiMac);
        mPool.bluetoothMacs.add(info.bluetoothMac);
        mPool.iccIds.add(info.iccId);
    }

    @Override
    public VDeviceInfo getDeviceInfo(int userId) throws RemoteException {
        VDeviceInfo info;
        synchronized (mDeviceInfos) {
//            info = mDeviceInfos.get(userId);
//            if (info == null) {

                if(mDeviceInfos.size()>0 && mDeviceInfos.get(0)!=null) {
                    info = mDeviceInfos.get(0);
                    VLog.d("gctech","1:getDeviceInfo deviceinfo:"+info.wifiMac+":"+info.brand+":"+this.toString());
                }
                else
                {
                    info = generateRandomDeviceInfo();
                    mDeviceInfos.append(0,info);
                    VLog.d("gctech","2:getDeviceInfo deviceinfo:"+info.wifiMac+":"+info.brand+":"+this.toString());
                }


                mDeviceInfos.put(userId, info);
                mPersistenceLayer.save();
//            }
        }
        return info;
    }

    @Override
    public void updateDeviceInfo(int userId, VDeviceInfo info) throws RemoteException {
        synchronized (mDeviceInfos) {
            if (info != null) {
                mDeviceInfos.put(userId, info);
                mPersistenceLayer.save();
            }
        }
    }

    private VDeviceInfo generateRandomDeviceInfo() {

        RandomUitls1 randomUitls1 = new RandomUitls1();
        VDeviceInfo info = new VDeviceInfo();
        String value;
//        do {
            value = generate10(15);
            info.deviceId = value;
//        } while (mPool.deviceIds.contains(value));
//        do {
            value = generate16(16);
            info.androidId = value;
//        } while (mPool.androidIds.contains(value));
//        do {
            value = generateMac();
            info.wifiMac = value;
//        } while (mPool.wifiMacs.contains(value));
//        do {
            value = generateMac();
            info.bluetoothMac = value;
//        } while (mPool.bluetoothMacs.contains(value));

//        do {
        value = generate10(20);
        info.iccId = value;
//        } while (mPool.iccIds.contains(value));

        int l = phoneModel.length();
        JSONObject rJson = null;
        try {

        rJson =  phoneModel.getJSONObject(r.nextInt(l)) ;



        boolean isEx = true;

        do {



            info.ssid = randomUitls1.wifiName();
            info.bluetoothMac = generateMac();
            info.brand = rJson.getString("buildManufacturer");
            info.serial = generateSerial();
            info.sdk = generateSdk();
            info.model = rJson.getString("buildModel");
            info.line1Number = randomUitls1.simLine1Number();
            info.product = rJson.getString("buildModel");
            info.release = "Android "+randomUitls1.buildVersion();

            isEx = (info.release == null || info.product == null || info.line1Number == null || info.model == null || info.sdk == null || info.serial == null || info.brand == null || info.bluetoothMac ==null  || info.ssid ==null) ;
        }
        while (isEx);


        info.simSerialNumber = randomUitls1.simSerialNumber(randomUitls1.simType);
        info.simState = randomUitls1.simSimState(randomUitls1.simType);
        info.simCountryIso = randomUitls1.simCountryIso(randomUitls1.simType);
        info.simOperator = randomUitls1.simOperator(randomUitls1.simType);
        info.simOperatorName = randomUitls1.simOperatorName(randomUitls1.simType);
        info.subscriberId = randomUitls1.simSubscriberId(randomUitls1.simType);


        info.cpu_abi = CPUABIS[r.nextInt(CPUABIS.length)];
        info.cpu_abi2 = "armeabi";
        VLog.d("gctech",info.toString());
    } catch (JSONException e) {
        e.printStackTrace();
    }


        //create phone data

        addDeviceInfoToPool(info);


        return info;
    }



    private Random r = new Random();



    public  void dexInject(){
        ActivityManager mActivityManager = null;
        Method mRemoveTask;

        try {
            Class<?> ActivityThread = Class.forName("android.app.ActivityThread");

//            Method[] methods = ActivityThread.getMethods();
//            for (Method method : methods) {
//                Log.e("clear_task", method.getName());
//            }

            Method method = ActivityThread.getMethod("currentActivityThread");
            Object currentActivityThread = method.invoke(ActivityThread);//获取currentActivityThread 对象

            Method method2 = currentActivityThread.getClass().getMethod("getApplication");
            Context CONTEXT_INSTANCE =(Context)method2.invoke(currentActivityThread);//获取 Context对象


            Class<?> activityManagerClass = Class.forName("android.app.ActivityManager");
            mActivityManager = (ActivityManager) CONTEXT_INSTANCE.getSystemService(Context.ACTIVITY_SERVICE);

            mRemoveTask = activityManagerClass.getMethod("removeTask", new Class[] { int.class, int.class });
            mRemoveTask.setAccessible(true);


            List<ActivityManager.RecentTaskInfo> recents = mActivityManager.getRecentTasks(Integer.MAX_VALUE, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
            // Start from 1, since we don't want to kill ourselves!
            for( int i=1; i < recents.size(); i++ ) {

//                mRemoveTask.invoke(mActivityManager, recents.get(i).persistentId, 0 );
                VLog.d("gctech","persistentId:"+recents.get(i).persistentId);
//                IActivityManager.removeTask.call(ActivityManagerNative.getDefault.call() , recents.get(i).persistentId);
                mRemoveTask.invoke(mActivityManager,recents.get(i).persistentId,0);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void killAndClearApp()
    {
        List<InstalledAppInfo> userIds = VirtualCore.get().getInstalledApps(0);
        for (int i = 0; i < userIds.size(); i++) {

            for(int j = 0 ; j < userIds.get(i).getInstalledUsers().length;j++)
            {
//
////
////
//                File appDir = new File(userIds.get(i).getApplicationInfo(userIds.get(i).getInstalledUsers()[j]).dataDir);
////
//                FileUtils.deleteDir(appDir);
////
////                VLog.d("gctech","----------------------------"+VEnvironment.getDataUserPackageDirectory(userIds.get(i).getInstalledUsers()[j],userIds.get(i).packageName).getAbsoluteFile());
//
//
////                VLog.d("gctech","----------------------------"+);
//
                FileUtils.deleteDir(VEnvironment.getDataUserPackageDirectory(userIds.get(i).getInstalledUsers()[j],userIds.get(i).packageName));


            }



//            ShellUtils.CommandResult cr =  ShellUtils.execCommand(" pm clear  "+userIds.get(i).packageName,true);
//
//            VLog.d("gctech","-----------------------"+" pm clear  "+userIds.get(i).packageName+":"+cr.errorMsg);
//            IActivityManager am = (IActivityManager) ActivityManagerNative.gDefault.get()
        }

//        dexInject();
//        FileUtils.deleteDir( VEnvironment.getDalvikCacheDirectory());
//        VActivityManagerService.get().removeALl();
        VAppManagerService.get().restoreFactoryState();

        VirtualCore.get().killAllApps();
        DataCleanManager.cleanApplicationData(VirtualCore.get().getContext());



//        ShellUtils.execCommand("kill "+VirtualCore.get().getSystemPid(),false);


    }
    public VDeviceInfo rebuild()
    {
//        isCreate = true;
        mDeviceInfos.removeAtRange(0,mDeviceInfos.size());
        mPool.deviceIds.removeAll(mPool.deviceIds);
        mPool.androidIds.removeAll(mPool.androidIds);
        mPool.wifiMacs.removeAll(mPool.wifiMacs);
        mPool.bluetoothMacs.removeAll(mPool.bluetoothMacs);
        mPool.iccIds.removeAll(mPool.iccIds);
        mPersistenceLayer.save();

        deviceId = null;
        androidId = null;
        mac = null;
        brand = null;
        serial = null;


        VDeviceInfo deviceInfo = null;
        try {
            deviceInfo = this.getDeviceInfo(0);
            VLog.d("gctech","create deviceinfo:"+deviceInfo.wifiMac+":"+deviceInfo.brand);
            deviceInfo.createBack();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        killAndClearApp();
        return deviceInfo;
    }


    public void replace(VDeviceInfo info)
    {
//        isCreate = true;
        mDeviceInfos.removeAtRange(0,mDeviceInfos.size());
        mPool.deviceIds.removeAll(mPool.deviceIds);
        mPool.androidIds.removeAll(mPool.androidIds);
        mPool.wifiMacs.removeAll(mPool.wifiMacs);
        mPool.bluetoothMacs.removeAll(mPool.bluetoothMacs);
        mPool.iccIds.removeAll(mPool.iccIds);
        mDeviceInfos.append(0,info);
        mPersistenceLayer.save();

        if(mDeviceInfos.size()>0 && mDeviceInfos.get(0)!=null)
        {
            deviceId = mDeviceInfos.get(0).deviceId;
            mac = mDeviceInfos.get(0).wifiMac;
            serial = mDeviceInfos.get(0).serial;
            androidId = mDeviceInfos.get(0).androidId;
            sdk = mDeviceInfos.get(0).sdk;
            brand = mDeviceInfos.get(0).brand;
            model = mDeviceInfos.get(0).model;
        }

        VDeviceInfo deviceInfo = null;
        try {
            deviceInfo = this.getDeviceInfo(0);
            killAndClearApp();
            VLog.d("gctech","create deviceinfo:"+deviceInfo.wifiMac+":"+deviceInfo.brand);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    SparseArray<VDeviceInfo> getDeviceInfos() {
        return mDeviceInfos;
    }


    private  static  String deviceId = null;

    private static String generate10(int length) {



            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(random.nextInt(9)+1);

            }



        return sb.toString();
    }


    private  static  String androidId = null;
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

    private  static  String mac = null;


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

            mac = sb.toString();

        return mac;
    }


    private static String brand = null;

    private static String generateBrand() {
        if(brand==null) {
            Random random = new Random();
            brand = brands[random.nextInt(brands.length)];
        }
        return brand;
    }



    private static String model = null;

    private static String generateModel() {
        if(model==null) {
            Random random = new Random();
            model = models[random.nextInt(models.length)];
        }
        return model;
    }


    private static String sdk = null;

    private static String generateSdk() {
        if(sdk==null) {
            Random random = new Random();
            sdk = sdks[random.nextInt(sdks.length)];
        }
        return sdk;
    }

    private  static  String serial = null;

    @SuppressLint("HardwareIds")
    private static String generateSerial() {

        if(serial==null) {
            String s;
            if (Build.SERIAL == null || Build.SERIAL.length() <= 0) {
                s = "0123456789ABCDEF";
            } else {
                s = Build.SERIAL;
            }
            List<Character> list = new ArrayList<>();
            for (char c : s.toCharArray()) {
                list.add(c);
            }
            Collections.shuffle(list);
            StringBuilder sb = new StringBuilder();
            for (Character c : list) {
                sb.append(c);
            }

            serial = sb.toString();
        }
        return serial;
    }
}
