package com.gc.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.os.RemoteException;

import com.gc.virtual.VirtualCore;
import com.lody.virtual.client.ipc.VDeviceManager;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.VDeviceInfo;

import java.io.File;
import java.io.IOException;

import static android.content.Context.ACTIVITY_SERVICE;

public class GCLogic {

    private static GCLogic sInstance = null;
    public static String BACK_FILE = "/sdcard/back.txt";

    public static  final String RE_FILE = "/sdcard/re.txt";



    /** @hide */
    public synchronized static GCLogic get() {
        if (sInstance == null) {

            sInstance = new GCLogic();
        }
        return sInstance;
    }


    public GCLogic()
    {

        String str = System.currentTimeMillis()+"_build.prop";
//         new Thread(new Runnable() {
//             @Override
//             public void run() {
//                  while (true)
//                  {
//                      String str = FileUtils.readTxtFile("/sdcard/back.txt");
//
//                      if(str.length()>0)
//                      {
//                          restore();
//                          new File("/sdcard/back.txt");
//                      }
//                      SystemClock.sleep(1000);
//                  }
//             }
//         }).start();
    }


    /**
     *
     * @param packageName 要清除数据的应用的包名
     */
    private void clearUserData(String packageName){
        try {

            // 获取其他应用的上下文
//            Context c = VirtualCore.get().getContext().createPackageContext(packageName,
//                    Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            ActivityManager am = (ActivityManager)
                    VirtualCore.get().getContext().getSystemService(Context.ACTIVITY_SERVICE);
            // 清除对应应用的数据 需要 这个权限(这个权限是系统应用才能有的)"android.permission.CLEAR_APP_USER_DATA"
            am.clearApplicationUserData();
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }


   static class ClearUserDataObserver extends IPackageDataObserver.Stub {



       @Override
       public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

       }
   }


    /**
     * press one key start new devices
     */
    public synchronized String newDevices()
    {



        VEnvironment.getDeviceInfoFile().delete();
        VDeviceInfo deviceInfo = VDeviceManager.get().rebuild();

//        android.os.Process.killProcess(android.os.Process.myPid());
//        Intent intent = VirtualCore.get().getContext().getPackageManager().getLaunchIntentForPackage(VirtualCore.get().getContext().getPackageName());
//        PendingIntent restartIntent = PendingIntent.getActivity(VirtualCore.get().getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager mgr = (AlarmManager)VirtualCore.get().getContext(). getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
//        System.exit(0);

//        deviceInfo.createBack();


//        clearUserData("com.gc");
//        List<InstalledAppInfo> userIds = VirtualCore.get().getInstalledApps(0);
//        for (int i = 0; i < userIds.size(); i++) {


////            ShellUtils.execCommand("pm clear "+userIds.get(i).packageName,true);
//
////            VirtualCore.get().clearPackage(userIds.get(i).packageName);
//
//
////            clearUserData(userIds.get(i).packageName);
//            for(int j = 0 ; j < userIds.get(i).getInstalledUsers().length;j++)
//            {
//
////
////
//                File appDir = new File(userIds.get(i).getApplicationInfo(userIds.get(i).getInstalledUsers()[j]).dataDir);
//
//                FileUtils.deleteDir(appDir);
//
//                VLog.d("gctech","----------------------------"+appDir.getAbsolutePath()+":"+userIds.get(i).packageName);
//
//            }
//
//
//
//            ShellUtils.CommandResult cr =  ShellUtils.execCommand("am force-stop "+userIds.get(i).packageName,true);
//
//            VLog.d("gctech","-----------------------"+"am force-stop "+userIds.get(i).packageName);
//
//        }


//        VirtualCore.get().killAllApps();

//        ShellUtils.execCommand(" am kill-all ",true);
//        VirtualCore.get().get

//        界面只要显示 品牌 型号 串号 安卓ID MAC 手机号 运营商

//        StringBuffer newDevices = new StringBuffer();
//
//        newDevices.append("品牌:").append(deviceInfo.brand).append("\r\n");
//        newDevices.append("型号:").append(deviceInfo.model).append("\r\n");
//        newDevices.append("串号:").append(deviceInfo.deviceId).append("\r\n");
//        newDevices.append("安卓ID:").append(deviceInfo.androidId).append("\r\n");
//        newDevices.append("MAC:").append(deviceInfo.wifiMac).append("\r\n");
//        newDevices.append("手机号:").append(deviceInfo.line1Number).append("\r\n");
//        newDevices.append("运营商:").append(deviceInfo.simOperatorName).append("\r\n");
//        newDevices.append("release:").append(deviceInfo.release).append("\r\n");
//        newDevices.append("ssid:").append(deviceInfo.ssid).append("\r\n");
//        newDevices.append("蓝牙mac:").append(deviceInfo.bluetoothMac).append("\r\n");
       return getDevicesStr(deviceInfo);
//        Build.BRAND.set(deviceInfo.brand);

    }

    public String getDevicesStr(VDeviceInfo deviceInfo)
    {
        StringBuffer newDevices = new StringBuffer();

        newDevices.append("品牌:").append(deviceInfo.brand).append("\r\n");
        newDevices.append("型号:").append(deviceInfo.model).append("\r\n");
        newDevices.append("串号:").append(deviceInfo.deviceId).append("\r\n");
        newDevices.append("安卓ID:").append(deviceInfo.androidId).append("\r\n");
        newDevices.append("MAC:").append(deviceInfo.wifiMac).append("\r\n");
        newDevices.append("手机号:").append(deviceInfo.line1Number).append("\r\n");
        newDevices.append("运营商:").append(deviceInfo.simOperatorName).append("\r\n");
        newDevices.append("release:").append(deviceInfo.release).append("\r\n");
        newDevices.append("ssid:").append(deviceInfo.ssid).append("\r\n");
        newDevices.append("蓝牙mac:").append(deviceInfo.bluetoothMac).append("\r\n");
        return newDevices.toString();
    }


    public String restore()
    {
        VDeviceInfo deviceInfo =  VDeviceInfo.restore("/sdcard/re.txt");
        VDeviceManager.get().push(deviceInfo);


        try {
            FileUtils.writeToFile(FileUtils.readTxtFile(RE_FILE).getBytes(),new File(BACK_FILE));
            FileUtils.writeToFile("".getBytes(),new File(RE_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        VirtualCore.get().killAllApps();

//        ActivityManager am = (ActivityManager) VirtualCore.get().getContext(). getSystemService(ACTIVITY_SERVICE);
//        am.restartPackage("com.gc");

//        List<InstalledAppInfo> userIds = VirtualCore.get().getInstalledApps(0);
//        for (int i = 0; i < userIds.size(); i++) {
//
//            ShellUtils.CommandResult cr =  ShellUtils.execCommand("am force-stop "+userIds.get(i).packageName,true);
//
//            VLog.d("gctech","-----------------------"+"am force-stop "+userIds.get(i).packageName);
//
//        }
//        VClientImpl.get().startIOUniformer();

//        File re = new File(RE_FILE);
//
//        if(re.exists())
//        {
//            re.delete();
//        }

        return getDevicesStr(deviceInfo);
    }

    public void createRe()
    {
        File re = new File(RE_FILE);

        if(!re.exists())
        {
            try {
                re.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        File back = new File(BACK_FILE);

        if(!back.exists())
        {
            try {
                back.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String backTxt()
    {
      return  FileUtils.readTxtFile("/sdcard/back.txt");
    }



//    public  static
}
