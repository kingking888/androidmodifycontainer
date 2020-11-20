package com.gc.virtual;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.hardware.Camera;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;


import com.lody.virtual.client.GcContants;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.VClientImpl;
import com.lody.virtual.client.hook.CameraPreviewCallback;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.delegate.AppInstrumentation;
import com.lody.virtual.client.hook.proxies.account.AccountManagerStub;
import com.lody.virtual.client.hook.proxies.alarm.AlarmManagerStub;
import com.lody.virtual.client.hook.proxies.am.ActivityManagerStub;
import com.lody.virtual.client.hook.proxies.am.HCallbackStub;
import com.lody.virtual.client.hook.proxies.appops.AppOpsManagerStub;
import com.lody.virtual.client.hook.proxies.appwidget.AppWidgetManagerStub;
import com.lody.virtual.client.hook.proxies.audio.AudioManagerStub;
import com.lody.virtual.client.hook.proxies.backup.BackupManagerStub;
import com.lody.virtual.client.hook.proxies.bluetooth.BluetoothStub;
import com.lody.virtual.client.hook.proxies.bluetooth.IBluetoothMangerStub;
import com.lody.virtual.client.hook.proxies.clipboard.ClipBoardStub;
import com.lody.virtual.client.hook.proxies.connectivity.ConnectivityStub;
import com.lody.virtual.client.hook.proxies.content.ContentServiceStub;
import com.lody.virtual.client.hook.proxies.context_hub.ContextHubServiceStub;
import com.lody.virtual.client.hook.proxies.devicepolicy.DevicePolicyManagerStub;
import com.lody.virtual.client.hook.proxies.display.DisplayStub;
import com.lody.virtual.client.hook.proxies.dropbox.DropBoxManagerStub;
import com.lody.virtual.client.hook.proxies.fingerprint.FingerprintManagerStub;
import com.lody.virtual.client.hook.proxies.graphics.GraphicsStatsStub;
import com.lody.virtual.client.hook.proxies.imms.MmsStub;
import com.lody.virtual.client.hook.proxies.input.InputMethodManagerStub;
import com.lody.virtual.client.hook.proxies.isms.ISmsStub;
import com.lody.virtual.client.hook.proxies.isub.ISubStub;
import com.lody.virtual.client.hook.proxies.job.JobServiceStub;
import com.lody.virtual.client.hook.proxies.libcore.LibCoreStub;
import com.lody.virtual.client.hook.proxies.location.LocationManagerStub;
import com.lody.virtual.client.hook.proxies.media.router.MediaRouterServiceStub;
import com.lody.virtual.client.hook.proxies.media.session.SessionManagerStub;
import com.lody.virtual.client.hook.proxies.mount.MountServiceStub;
import com.lody.virtual.client.hook.proxies.network.NetworkManagementStub;
import com.lody.virtual.client.hook.proxies.notification.NotificationManagerStub;
import com.lody.virtual.client.hook.proxies.persistent_data_block.PersistentDataBlockServiceStub;
import com.lody.virtual.client.hook.proxies.phonesubinfo.PhoneSubInfoStub;
import com.lody.virtual.client.hook.proxies.pm.PackageManagerStub;
import com.lody.virtual.client.hook.proxies.power.PowerManagerStub;
import com.lody.virtual.client.hook.proxies.restriction.RestrictionStub;
import com.lody.virtual.client.hook.proxies.search.SearchManagerStub;
import com.lody.virtual.client.hook.proxies.shortcut.ShortcutServiceStub;
import com.lody.virtual.client.hook.proxies.telephony.TelephonyRegistryStub;
import com.lody.virtual.client.hook.proxies.telephony.TelephonyStub;
import com.lody.virtual.client.hook.proxies.usage.UsageStatsManagerStub;
import com.lody.virtual.client.hook.proxies.user.UserManagerStub;
import com.lody.virtual.client.hook.proxies.vibrator.VibratorStub;
import com.lody.virtual.client.hook.proxies.view.AutoFillManagerStub;
import com.lody.virtual.client.hook.proxies.wifi.WifiManagerStub;
import com.lody.virtual.client.hook.proxies.wifi_scanner.WifiScannerStub;
import com.lody.virtual.client.hook.proxies.window.WindowManagerStub;
import com.lody.virtual.client.interfaces.IInjector;
import com.lody.virtual.helper.ImageTool;
import com.lody.virtual.helper.compat.SystemPropertiesCompat;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.XC_MethodReplacement;
import com.taobao.android.dexposed.XposedHelpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import me.weishu.exposed.ExposedBridge;
import mirror.android.net.NetworkInfo;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.N;

/**
 * @author Lody
 *
 */
public final class InvocationStubManager {

    private static InvocationStubManager sInstance = new InvocationStubManager();
    private static boolean sInit;

	private Map<Class<?>, IInjector> mInjectors = new HashMap<>(13);

	private InvocationStubManager() {
	}

	public static InvocationStubManager getInstance() {
		return sInstance;
	}

	void injectAll() throws Throwable {
		for (IInjector injector : mInjectors.values()) {
			injector.inject();
		}
		// XXX: Lazy inject the Instrumentation,
		addInjector(AppInstrumentation.getDefault());
	}

    /**
	 * @return if the InvocationStubManager has been initialized.
	 */
	public boolean isInit() {
		return sInit;
	}


	public void init() throws Throwable {
		if (isInit()) {
			throw new IllegalStateException("InvocationStubManager Has been initialized.");
		}
		injectInternal();
		sInit = true;

	}


	private boolean isExitScan()
	{
		return new File("/sdcard/scan.jpg").exists();
	}


	public void hookDevice()
	{



		mirror.android.os.Build.BRAND.set(VClientImpl.get().getDeviceInfo().brand);
		mirror.android.os.Build.PRODUCT.set(VClientImpl.get().getDeviceInfo().brand);
		mirror.android.os.Build.DEVICE.set(VClientImpl.get().getDeviceInfo().brand);
		mirror.android.os.Build.MODEL.set(VClientImpl.get().getDeviceInfo().model);
		mirror.android.os.Build.HARDWARE.set("qcom");
//			mirror.android.os.Build.SDK.set(VClientImpl.get().getDeviceInfo().sdk);
//		mirror.android.os.Build.DISPLAY.set(VClientImpl.get().getDeviceInfo().display);

		mirror.android.os.Build.SERIAL.set(VClientImpl.get().getDeviceInfo().serial);


//		XposedHelpers.setStaticObjectField(findClass, "CPU_ABI", "armeabi-v7a");
//		XposedHelpers.setStaticObjectField(findClass, "CPU_ABI2", "armeabi");
//		XposedHelpers.setStaticObjectField(findClass, "BOARD", "sdm660");
//		XposedHelpers.setStaticObjectField(findClass, "HARDWARE", "qcom");
//		mirror.android.os.Build.CPU_ABI.set(VClientImpl.get().getDeviceInfo().cpu_abi);
//		mirror.android.os.Build.CPU_ABI2.set(VClientImpl.get().getDeviceInfo().cpu_abi2);



		mirror.android.os.Build.CPU_ABI.set("armeabi-v7a");
		mirror.android.os.Build.CPU_ABI2.set("armeabi");
//			VLog.d("gctech",mirror.android.os.Build.SERIAL.get());
		mirror.android.os.Build.MANUFACTURER.set(VClientImpl.get().getDeviceInfo().brand);
//		mirror.android.os.Build.HOST.set("");
//			mirror.android.os.Build..set("");
//		mirror.android.os.Build.ID.set(VClientImpl.get().getDeviceInfo().brand);

//		mirror.android.os.Build.RADIO.set("");
		mirror.android.os.Build.BOARD.set("sdm660");
//		mirror.android.os.Build.FINGERPRINT.set(VClientImpl.get().getDeviceInfo().model+VClientImpl.get().getDeviceInfo().serial);
//			mirror.android.os.Build.PLATFORM.set("sdm660");
		mirror.android.os.Version.RELEASE.set(VClientImpl.get().getDeviceInfo().release);

//		WifiInfo.mMacAddress.set();


//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//
//				HashMap map = Settings.NameValueCache.mValues.get(Settings.Secure.sNameValueCache.get());
//				while(true) {
//
//
//					if(map.get("android_id")!=VClientImpl.get().getDeviceInfo().androidId) {
//						map.put("android_id", VClientImpl.get().getDeviceInfo().androidId);
//					}
//
//					SystemClock.sleep(50);
////						VLog.d("gctech", "android_id:"+ Settings.Secure.getString.call(VirtualCore.get().getContext().getContentResolver(),"android_id"));
////						VLog.d("gctech",  "android_id:"+Settings);
////						VLog.d("gctech","ro.product.model:"+SystemPropertiesCompat.get("ro.product.model",""));
////
//				}
//			}
//		}).start();







//		NativeEngine.addMap("ro.product.cpu.abi","ffffffffff");
//		NativeEngine.addMap("ro.product.cpu.abi2","fsdfsadfsdafasd");
//		mirror.android.os.Build.CPU_ABI.set("fffffffffff");
//		mirror.android.os.Build.CPU_ABI2.set("eeeeeeeeeee");
	}


	public void hookByC()
	{


		//		if (!d.isNull(aVar.buildManufacturer)) {
//			mVar = l.a(loadPackageParam).a;
//			str = aVar.buildManufacturer;
//			XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", str);
//			mVar.c.put("ro.product.manufacturer", str);
//			mVar = l.a(loadPackageParam).a;
//			str = aVar.buildManufacturer;
//			XposedHelpers.setStaticObjectField(Build.class, "BRAND", str);
//			mVar.c.put("ro.product.brand", str);
//			mVar = l.a(loadPackageParam).a;
//			str = aVar.buildManufacturer;
//			XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", str);
//			mVar.c.put("ro.product.name", str);
//		}
//		d.a();
//		if (!d.isNull(aVar.buildModel)) {
//			mVar = l.a(loadPackageParam).a;
//			str = aVar.buildModel;
//			XposedHelpers.setStaticObjectField(Build.class, "MODEL", str);
//			mVar.c.put("ro.product.model", str);
//			mVar = l.a(loadPackageParam).a;
//			str = aVar.buildModel;
//			XposedHelpers.setStaticObjectField(Build.class, "DEVICE", str);
//			mVar.c.put("ro.product.device", str);
//			mVar = l.a(loadPackageParam).a;
//			str = aVar.buildModel;
//			XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", str);
//			mVar.c.put("ro.hardware", str);
//		}
//		d.a();
//		if (!d.isNull(aVar.buildSerial)) {
//			mVar = l.a(loadPackageParam).a;
//			str = aVar.buildSerial;
//			XposedHelpers.setStaticObjectField(Build.class, "SERIAL", str);
//			mVar.c.put("ro.serialno", str);
//		}
		NativeEngine.hookProperty(VClientImpl.get().getDeviceInfo().getMap());
		NativeEngine.addMap("ro.product.model",VClientImpl.get().getDeviceInfo().model);
		NativeEngine.addMap("ro.hardware","qcom");
		NativeEngine.addMap("no.such.thing",VClientImpl.get().getDeviceInfo().serial);

//
//		mirror.android.os.Build.CPU_ABI.set("armeabi-v7a");
//		mirror.android.os.Build.CPU_ABI2.set("armeabi");

		NativeEngine.addMap("ro.product.cpu.abi","armeabi-v7a");
		NativeEngine.addMap("ro.product.cpu.abi2","armeabi");




		NativeEngine.addMap("ro.product.brand",VClientImpl.get().getDeviceInfo().brand);
		NativeEngine.addMap("ro.product.board","sdm660");
		NativeEngine.addMap("ro.product.manufacturer",VClientImpl.get().getDeviceInfo().brand);
		NativeEngine.addMap("ro.build.version.release",VClientImpl.get().getDeviceInfo().release);

		NativeEngine.addMap("ro.product.product",VClientImpl.get().getDeviceInfo().brand);

		NativeEngine.addMap("ro.product.name",VClientImpl.get().getDeviceInfo().brand);

//		NativeEngine.addMap("ro.build.id",VClientImpl.get().getDeviceInfo().brand);
//		NativeEngine.addMap("ro.build.display.id",VClientImpl.get().getDeviceInfo().display);
		NativeEngine.addMap("ro.product.locale.language","zh");
		NativeEngine.addMap("ro.product.locale.region","CN");
//			NativeEngine.addMap("ro.board.platform","sdm6620");

		NativeEngine.addMap("gsm.sim.operator.iso-country","cn,cn");
		NativeEngine.addMap("gsm.sim.state",VClientImpl.get().getDeviceInfo().simState);
		NativeEngine.addMap("gsm.sim.operator.numeric",VClientImpl.get().getDeviceInfo().simOperator+","+VClientImpl.get().getDeviceInfo().simOperator);
		NativeEngine.addMap("gsm.operator.alpha",VClientImpl.get().getDeviceInfo().simOperatorName);
		NativeEngine.addMap("gsm.operator.alpha.2",VClientImpl.get().getDeviceInfo().simOperatorName);
		NativeEngine.addMap("sys.settings_system_version","0");
		NativeEngine.addMap("sys.settings_secure_version","0");
		NativeEngine.addMap("ro.telephony.sim.count","2");
		NativeEngine.addMap("ro.build.selinux","1");

		NativeEngine.addMap("ro.serialno",VClientImpl.get().getDeviceInfo().serial);
		NativeEngine.addMap("ro.build.fingerprint",VClientImpl.get().getDeviceInfo().model+VClientImpl.get().getDeviceInfo().serial);

		NativeEngine.addMap("ro.build.version.sdk","22");

	}

	public  boolean contain(String str,String[] v) {
		if (!(str == null || v == null)) {
			for (String str2 : v) {
				if (str.matches(".*(\\W|^)" + str2 + "(\\W|$).*")) {
					return true;
				}
			}
		}
		return false;
	}


	public static boolean containEndWith(String str, String[] set) {
		if (!(str == null || set == null)) {
			for (String endsWith : set) {
				if (str.endsWith(endsWith)) {
					return true;
				}
			}
		}
		return false;
	}


	public static boolean containEq(String str, String[] set) {
		if (!(str == null || set == null)) {
			for (String eq : set) {
				if (str.equals(eq)) {
					return true;
				}
			}
		}
		return false;
	}



	public static final String[] APPS = new String[]{"com.tencent.mm","com.fde.DomesticDigitalCopy", "com.directv.application.android.go.production", "com.res.bby", "dk.excitor.dmemail", "com.BHTV", "com.bradfordnetworks.bma", "com.apriva.mobile.bams", "com.apriva.mobile.aprivapay", "pl.pkobp.iko", "au.com.auspost", "com.rogers.citytv.phone", "com.zenprise", "net.flixster.android", "com.starfinanz.smob.android.sfinanzstatus", "com.ovidos.yuppi", "klb.android.lovelive", "klb.android.lovelive_en", "com.nintendo.zaaa", "com.incube.epub", "com.airwatch.androidagent", "com.zappware.twintv.d3", "com.starfinanz.mobile.android.pushtan", "com.stofa.webtv", "com.barclays.android.barclaysmobilebanking", "com.bskyb.skygo", "com.hanaskcard.rocomo.potal", "com.hanabank.ebk.channel.android.hananbank", "com.ahnlab.v3mobileplus", "com.good.android.gfe", "it.phoenixspa.inbank", "dk.tv2.tv2play", "com.enterproid.divideinstaller", "com.isis.mclient.verizon.activity", "com.isis.mclient.atnt.activity", "be.telenet.yelo", "no.rdml.android.mobiletv", "uk.co.barclays.barclayshomeowner", "com.mcafee.apps.emmagent", "com.virginmedia.tvanywhere", "com.amis.mobiatv", "it.telecomitalia.cubovision", "nl.ziggo.android.tv", "com.orange.fr.ocs", "com.adb.android.app.iti", "com.mobileiron"};
	public static final String[] ROOTS = new String[]{"supersu", "superuser", "Superuser", "noshufou", "xposed", "rootcloak", "chainfire", "titanium", "Titanium", "substrate", "greenify", "daemonsu", "root", "busybox", "titanium", ".tmpsu", "su", "rootcloak2"};
	public static final String[] SHELLS = new String[]{"su", "which", "busybox", "pm", "am", "sh", "ps"};
	public static final String[] h = new String[]{"tool-checker"};



	public void hookCameral()
	{



		DexposedBridge.hookAllMethods(Camera.class, "setPreviewCallback", new com.taobao.android.dexposed.XC_MethodHook() {
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				if(!isExitScan())return;
				Camera camera = (Camera) param.thisObject;
				param.args[0] = new CameraPreviewCallback((Camera.PreviewCallback)param.args[0]);
			}
		});

		DexposedBridge.hookAllMethods(Camera.class, "setOneShotPreviewCallback", new com.taobao.android.dexposed.XC_MethodHook() {
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				if(!isExitScan())return;
				Camera camera =  (Camera) param.thisObject;
				param.args[0] = new CameraPreviewCallback((Camera.PreviewCallback)param.args[0]);
			}
		});

		DexposedBridge.hookAllMethods(Camera.class, "setPreviewCallbackWithBuffer", new com.taobao.android.dexposed.XC_MethodHook() {
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				if(!isExitScan())return;
				Camera camera = (Camera) param.thisObject;
				param.args[0] = new CameraPreviewCallback((Camera.PreviewCallback)param.args[0]);
			}
		});



		DexposedBridge.hookAllMethods(Application.class, "attach", new com.taobao.android.dexposed.XC_MethodHook() {
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				try {
					Class<?> mmScanUI = ((Context) param.args[0]).getClassLoader().loadClass("com.tencent.mm.plugin.scanner.ui.BaseScanUI");
					XposedBridge.hookAllMethods(mmScanUI, "onPreviewFrame", new XC_MethodHook() {
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

							if(!isExitScan())return;
							Camera.Size previewSize = (((Camera)param.args[1]).getParameters()).getPreviewSize();
							byte[] testYuv = ImageTool.getTestYuvImg(previewSize.width, previewSize.height);
							if (testYuv != null) {
								param.args[0] = testYuv;
							}
							else
							{

								Toast.makeText(VirtualCore.get().getContext(), " 没有图片", Toast.LENGTH_SHORT).show();
							}


						}
					});
					DexposedBridge.findAndHookMethod(mmScanUI, "onPreviewFrame", new Object[]{byte[].class, Camera.class, new XC_MethodHook() {
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							if(!isExitScan())return;
							Camera.Size previewSize = ((Camera)param.args[1]).getParameters().getPreviewSize();
							byte[] testYuv = ImageTool.getTestYuvImg(previewSize.width, previewSize.height);
							if (testYuv != null) {
								param.args[0] = testYuv;
							}
						}
					}});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

//		XposedHelpers.findClass(Camera.class, "setPreviewCallback", new Object[]{Camera.PreviewCallback.class, new C01261()});
//		XposedHelpers.findClass(Camera.class, "setOneShotPreviewCallback", new Object[]{Camera.PreviewCallback.class, new C01272()});
//		XposedHelpers.findClass(Camera.class, "setPreviewCallbackWithBuffer", new Object[]{Camera.PreviewCallback.class, new C01283()});
//		XposedHelpers.findClass(Application.class, "attach", new Object[]{Context.class, new C01314()});
	}
	public void hookGaiji(Context context)
	{

		File mCpu = new File("/sdcard/cpuinfo");

		if(!mCpu.exists()) {
			try {
				FileUtils.writeToFile(VirtualCore.get().getContext().getAssets().open("cpuinfo"), mCpu);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ExposedBridge.patchAppClassLoader(context);
		Class findClass = XposedHelpers.findClass("android.os.Build", context.getClassLoader());
		XposedHelpers.setStaticObjectField(findClass, "CPU_ABI", "armeabi-v7a");
		XposedHelpers.setStaticObjectField(findClass, "CPU_ABI2", "armeabi");
		XposedHelpers.setStaticObjectField(findClass, "BOARD", "sdm660");
		XposedHelpers.setStaticObjectField(findClass, "HARDWARE", "qcom");

		Class PackageInfo = XposedHelpers.findClass("android.content.pm.PackageInfo",context.getClassLoader());
		Class ApplicationPackageManager = XposedHelpers.findClass("android.app.ApplicationPackageManager",context.getClassLoader());

		XposedBridge.hookInitPackageResources(new MYXC_InitPackageResources());
		//<manifest xmlns:android="http://schemas.android.com/apk/res/android" android:versionCode="1360" android:versionName="6.7.3" android:installLocation="auto" package="com.tencent.mm" platformBuildVersionCode="26" platformBuildVersionName="8.0.0">
		XposedHelpers.findConstructorBestMatch(PackageInfo, new com.taobao.android.dexposed.XC_MethodHook() {
			protected void afterHookedMethod(MethodHookParam param) throws Throwable  {
						XposedHelpers.setObjectField(param.thisObject,"versionCode","1360");
						XposedHelpers.setObjectField(param.thisObject,"versionName","6.7.3");
						XposedHelpers.setObjectField(param.thisObject,"versionName","6.7.3");
						VLog.d("gctech---",param.getResult().toString());
			}
		});


		DexposedBridge.findAndHookMethod(XposedHelpers.findClass("android.os.Debug",context.getClassLoader()),"isDebuggerConnected", XC_MethodReplacement.returnConstant(Boolean.valueOf(false)));
		if (!Build.TAGS.equals("release-keys")) {

			XposedHelpers.setStaticObjectField(Build.class, "TAGS", "release-keys");

		}


//		DexposedBridge.findAndHookMethod(XposedHelpers.findClass("java.lang.Class",context.getClassLoader()),  "forName",String.class, Boolean.TYPE, ClassLoader.class,  new com.taobao.android.dexposed.XC_MethodHook() {
//
//			protected final void beforeHookedMethod(MethodHookParam methodHookParam) {
//
//				String str = (String) methodHookParam.args[0];
//				VLog.d("gctech","DexposedBridge File"+str);
//
//				if (str == null) {
//					return;
//				}
//				if (str.equals("de.robv.android.xposed.XposedBridge") || str.equals("de.robv.android.xposed.XC_MethodReplacement")) {
//					methodHookParam.setThrowable(new ClassNotFoundException());
//
//				}
//			}
//		});


//		DexposedBridge.findAndHookMethod(XposedHelpers.findClass("java.lang.StackTraceElement",context.getClassLoader()),  "getClassName", new com.taobao.android.dexposed.XC_MethodHook() {
//
//			protected final void  afterHookedMethod(MethodHookParam methodHookParam) {
//
//				String str = (String) methodHookParam.args[0];
//				VLog.d("gctech","StackTraceElement File"+str);
//
//				if (str == null) {
//					return;
//				}
//				if (str.equals("de.robv.android.xposed.XposedBridge") || str.equals("de.robv.android.xposed.XC_MethodReplacement")) {
//					methodHookParam.setResult(false);
//
//				}
//			}
//		});

		DexposedBridge.hookMethod(XposedHelpers.findConstructorExact(File.class, String.class), new com.taobao.android.dexposed.XC_MethodHook() {
			protected final void beforeHookedMethod(MethodHookParam methodHookParam) {
				String fileName = "";

				if(methodHookParam.args[0] instanceof String)
				{
					fileName = methodHookParam.args[0].toString();
				}else if(methodHookParam.args[0] instanceof File){
					fileName = ((File) methodHookParam.args[0]).getAbsolutePath();
				}

//                VLog.d("gctech","DexposedBridge File"+fileName);
//				VLog.d("gctech","File hookAllConstructors"+fileName);
				if(fileName.length()>0) {
					if (fileName.endsWith("su")) {

						methodHookParam.args[0] = "/system/xbin/FAKEJUNKFILE";
					} else if (fileName.endsWith("busybox")) {

						methodHookParam.args[0] = "/system/xbin/FAKEJUNKFILE";

					}
					else if (contain(fileName, ROOTS)) {

						methodHookParam.args[0] = "/system/app/FAKEJUNKFILE.apk";
					}
				}

			}
		});
		DexposedBridge.hookMethod(XposedHelpers.findConstructorExact(File.class, String.class, String.class), new com.taobao.android.dexposed.XC_MethodHook() {
			protected final void beforeHookedMethod(MethodHookParam methodHookParam) {

//				VLog.d("gctech","DexposedBridge 2 String File"+ methodHookParam.args[1]);
					if (((String) methodHookParam.args[1]).equalsIgnoreCase("su")) {

						methodHookParam.args[1] = "FAKEJUNKFILE";
					} else if (((String) methodHookParam.args[1]).contains("busybox")) {

						methodHookParam.args[1] = "FAKEJUNKFILE";
					}
					else if (contain(methodHookParam.args[1].toString(), ROOTS)) {

						methodHookParam.args[1] = "FAKEJUNKFILE.apk";
					}
					else if(methodHookParam.args[1].toString().equals("de.robv.android.xposed.installer"))
					{
						methodHookParam.setThrowable(new FileNotFoundException());
					}

			}
		});

		//xposed hook
		DexposedBridge.findAndHookMethod(android.provider.Settings.Secure.class,"getString",ContentResolver.class,String.class,new com.taobao.android.dexposed.XC_MethodHook(){
			protected void afterHookedMethod(MethodHookParam param) throws Throwable  {
//				VLog.d("gctech","DexposedBridge getString"+param.args[1].toString());

				if(param.args[1].equals("android_id"))
				{
					param.setResult(VClientImpl.get().getDeviceInfo().androidId);
				}
			}
		});

//TelephonyManager.class, "getDeviceId",
		DexposedBridge.findAndHookMethod(android.provider.Settings.System.class,"getString",ContentResolver.class,String.class,new com.taobao.android.dexposed.XC_MethodHook(){
			protected void afterHookedMethod(MethodHookParam param) throws Throwable  {
//				for(Object obj:param.args)
//				{
//					VLog.d("gctech","DexposedBridge getString"+obj.toString());
//				}


				if(param.args[1].equals("android_id"))
				{
					param.setResult(VClientImpl.get().getDeviceInfo().androidId);
				}
			}
		});


		DexposedBridge.hookAllMethods(TelephonyManager.class,"getDeviceId",new com.taobao.android.dexposed.XC_MethodHook(){
			protected void afterHookedMethod(MethodHookParam param) throws Throwable  {
//				for(Object obj:param.args)
//				{
//					VLog.d("gctech","DexposedBridge getString"+obj.toString());
//				}


					param.setResult(VClientImpl.get().getDeviceInfo().deviceId);

			}
		});


		try {
			DexposedBridge.hookAllConstructors(File.class,new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void beforeHookedMethod(MethodHookParam methodHookParam) {
					int i = 0;

					if (methodHookParam.args.length == 1) {
						if (methodHookParam.args[0].equals("/proc/cpuinfo")) {
							methodHookParam.args[0] = GcContants.GC_REPALCE;
						}
					} else if (methodHookParam.args.length == 2 && !File.class.isInstance(methodHookParam.args[0])) {
						Object obj = "";
						while (i < 2) {
							if (methodHookParam.args[i] != null) {
								if (methodHookParam.args[i].equals("/proc/cpuinfo")) {
									methodHookParam.args[i] = GcContants.GC_REPALCE;
								}
								obj = new StringBuilder(String.valueOf(obj)).append(methodHookParam.args[i]).append(":").toString();
							}
							i++;
						}
					}
				}
			});
			DexposedBridge.findAndHookMethod(java.lang.Runtime.class, "exec", String[].class, String[].class, File.class, new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void beforeHookedMethod(MethodHookParam methodHookParam) {
					int i = 0;

					if (methodHookParam.args.length == 1) {
						if (methodHookParam.args[0].equals("/proc/cpuinfo")) {
							methodHookParam.args[0] = GcContants.GC_REPALCE;
						}
					} else if (methodHookParam.args.length == 2 && !File.class.isInstance(methodHookParam.args[0])) {
						Object obj = "";
						while (i < 2) {
							if (methodHookParam.args[i] != null) {
								if (methodHookParam.args[i].equals("/proc/cpuinfo")) {
									methodHookParam.args[i] = GcContants.GC_REPALCE;
								}
								obj = new StringBuilder(String.valueOf(obj)).append(methodHookParam.args[i]).append(":").toString();
							}
							i++;
						}
					}
				}
			});

			DexposedBridge.hookAllConstructors(ProcessBuilder.class,  new com.taobao.android.dexposed.XC_MethodHook() {
						protected final void beforeHookedMethod(MethodHookParam methodHookParam) {

							if (methodHookParam.args[0] != null) {
								String[] strArr = (String[]) methodHookParam.args[0];
								Object obj = "";
								for (String str : strArr) {
									obj = new StringBuilder(String.valueOf(obj)).append(str).append(":").toString();
									if (str == "/proc/cpuinfo") {
										strArr[1] = GcContants.GC_REPALCE;
									}
								}
								methodHookParam.args[0] = strArr;
							}
						}
					}
			);

			DexposedBridge.findAndHookMethod(java.util.regex.Pattern.class, "matcher", CharSequence.class, new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void beforeHookedMethod(MethodHookParam methodHookParam) {

					if (methodHookParam.args.length == 1 && methodHookParam.args[0].equals("/proc/cpuinfo")) {
						methodHookParam.args[0] = GcContants.GC_REPALCE;
					}
				}
			});


			XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", VClientImpl.get().getDeviceInfo().brand);

			XposedHelpers.setStaticObjectField(Build.class, "BRAND", VClientImpl.get().getDeviceInfo().brand);

			XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", VClientImpl.get().getDeviceInfo().brand);

			XposedHelpers.setStaticObjectField(Build.class, "MODEL", VClientImpl.get().getDeviceInfo().model);

			XposedHelpers.setStaticObjectField(Build.class, "DEVICE", VClientImpl.get().getDeviceInfo().model);

			XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", VClientImpl.get().getDeviceInfo().model);

			XposedHelpers.setStaticObjectField(Build.class, "SERIAL", VClientImpl.get().getDeviceInfo().serial);


           DexposedBridge.hookAllMethods(NetworkInfo.class, "getType", new com.taobao.android.dexposed.XC_MethodHook() {
                protected final void afterHookedMethod(MethodHookParam methodHookParam) {
                    methodHookParam.setResult(1);
                }
            });

            XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE", VClientImpl.get().getDeviceInfo().release);

//			XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK_INT", 22);
//			Version.SDK_INT.set(22);
			DexposedBridge.findAndHookMethod(XposedHelpers.findClass("android.app.ApplicationPackageManager",context.getClassLoader()), "getInstalledApplications", Integer.TYPE, new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void afterHookedMethod(MethodHookParam methodHookParam) {
//					VLog.d("gctech","getInstalledApplications");
					List list = (List) methodHookParam.getResult();
					Iterator it = list.iterator();
					while (it.hasNext()) {
						String str = ((ApplicationInfo) it.next()).packageName;
						if (str != null && contain(str,ROOTS)) {
							VLog.d("gctech","remove Applications:"+str);
							it.remove();

						}
					}
					methodHookParam.setResult(list);
				}
			});
			DexposedBridge.findAndHookMethod(XposedHelpers.findClass("android.app.ApplicationPackageManager",context.getClassLoader()), "getInstalledPackages", Integer.TYPE, new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void afterHookedMethod(MethodHookParam methodHookParam) {
//					VLog.d("gctech","getInstalledPackages");
					List list = (List) methodHookParam.getResult();
					Iterator it = list.iterator();
					while (it.hasNext()) {
						String str = ((PackageInfo) it.next()).packageName;
						if (str != null && contain(str,ROOTS)) {
							VLog.d("gctech","remove getInstalledPackages:"+str);
							it.remove();
						}
					}
					methodHookParam.setResult(list);
				}
			});
			DexposedBridge.findAndHookMethod(XposedHelpers.findClass("android.app.ApplicationPackageManager",context.getClassLoader()), "getPackageInfo",String.class, Integer.TYPE, new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void beforeHookedMethod(MethodHookParam methodHookParam) {
//					VLog.d("gctech","getPackageInfo");
					String str = (String) methodHookParam.args[0];
					if (str != null && contain(str,ROOTS)) {
						methodHookParam.args[0] = "com.gc";
					}
				}
			});
			DexposedBridge.findAndHookMethod(XposedHelpers.findClass("android.app.ApplicationPackageManager",context.getClassLoader()), "getApplicationInfo", String.class, Integer.TYPE, new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void beforeHookedMethod(MethodHookParam methodHookParam) {
					String str = (String) methodHookParam.args[0];
//					VLog.d("gctech","getApplicationInfo");
					if (str != null && contain(str,ROOTS)) {
						methodHookParam.args[0] = "com.gc.XApp";

					}
				}
			});
			DexposedBridge.findAndHookMethod(XposedHelpers.findClass("android.app.ActivityManager",context.getClassLoader()), "getRunningServices", Integer.TYPE, new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void afterHookedMethod(MethodHookParam methodHookParam) {
//					VLog.d("gctech","getRunningServices");
					List list = (List) methodHookParam.getResult();
					Iterator it = list.iterator();
					while (it.hasNext()) {
						String str = ((ActivityManager.RunningServiceInfo) it.next()).process;
						if (str != null && contain(str,ROOTS)) {
							it.remove();
							VLog.d("gctech","remove getRunningServices:"+str);

						}
					}
					methodHookParam.setResult(list);
				}
			});
			DexposedBridge.findAndHookMethod(XposedHelpers.findClass("android.app.ActivityManager",context.getClassLoader()), "getRunningTasks", Integer.TYPE, new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void afterHookedMethod(MethodHookParam methodHookParam) {
//					VLog.d("gctech","getRunningTasks");
					List list = (List) methodHookParam.getResult();
					Iterator it = list.iterator();
					while (it.hasNext()) {
						String flattenToString = ((ActivityManager.RunningTaskInfo) it.next()).baseActivity.flattenToString();
						if (flattenToString != null && contain(flattenToString,ROOTS)) {
							it.remove();
							VLog.d("gctech","remove getRunningTasks:"+flattenToString);
						}
					}
					methodHookParam.setResult(list);
				}
			});
			DexposedBridge.findAndHookMethod(XposedHelpers.findClass("android.app.ActivityManager",context.getClassLoader()), "getRunningAppProcesses",new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void afterHookedMethod(MethodHookParam methodHookParam) {
//					VLog.d("gctech","getRunningAppProcesses");
					List list = (List) methodHookParam.getResult();
					Iterator it = list.iterator();
					while (it.hasNext()) {
						String str = ((ActivityManager.RunningAppProcessInfo) it.next()).processName;
						if (str != null && contain(str,ROOTS)) {
							it.remove();
							VLog.d("gctech","remove getRunningAppProcesses:"+str);
						}
					}
					methodHookParam.setResult(list);
				}
			});

            DexposedBridge.findAndHookMethod(java.lang.Runtime.class, "exec", String[].class, String[].class, File.class, new com.taobao.android.dexposed.XC_MethodHook() {

//				protected final void afterHookedMethod(MethodHookParam methodHookParam) {
//					int i = 0;
//
//					String[] strArr = (String[]) methodHookParam.args[0];
//					if (strArr != null && strArr.length > 0) {
//						String str = strArr[0];
//
//						String str2 = "";
//						for (String str3 : strArr) {
//							str2 = str2 + " " + str3;
//							VLog.d("gctech", "runtime exec:" + str3);
//						}
//
//						VLog.d("gctech", "runtime exec:" + str2 + "-----" + str2.equals(" getprop persist.sys.hardcoder.name"));
//						if (str2.equals(" getprop persist.sys.hardcoder.name")) {
//                            methodHookParam.setResult(null);
//
////							VLog.d("gctech", "runtime exec:" + methodHookParam.getResult());
//							methodHookParam.setThrowable(new IOException());
//							return;
//						}
//					}
//				}


                protected final void beforeHookedMethod(MethodHookParam methodHookParam) {
                    int i = 0;

                    String[] strArr = (String[]) methodHookParam.args[0];
                    if (strArr != null && strArr.length > 0) {
                        String str = strArr[0];

                        String str2 = "";
                        for (String str3 : strArr) {
                            str2 = str2 + " " + str3;
							VLog.d("gctech","runtime exec:"+str3);
                        }

//						VLog.d("gctech","runtime exec:"+str2+"-----"+str2.equals(" getprop persist.sys.hardcoder.name"));
//                        if(str2.equals(" getprop persist.sys.hardcoder.name"))
//                        {
////                            methodHookParam.setResult("sdm850");
//							methodHookParam.setThrowable(new IOException());
//							VLog.d("gctech","runtime exec:"+methodHookParam.getResult());
//                            return;
//                        }



//                        if (containEndWith(str,SHELLS)) {

                            if (str.equals("su") || str.endsWith("/su")) {
                                methodHookParam.setThrowable(new IOException());
                            } else if (containEq("pm",SHELLS) && (str.equals("pm") || str.endsWith("/pm"))) {
                                if (strArr.length >= 3 && strArr[1].equalsIgnoreCase("list") && strArr[2].equalsIgnoreCase("packages")) {
                                    methodHookParam.args[0] = new String[]{"com.tencent.mm"};
                                } else if (strArr.length >= 3 && ((strArr[1].equalsIgnoreCase("dump") || strArr[1].equalsIgnoreCase("path")) && contain(strArr[2], APPS))) {
                                    methodHookParam.args[0] = new String[]{strArr[0], strArr[1], "FAKE.JUNK.PACKAGE"};
                                }
                            } else if (containEq("ps",SHELLS) && (str.equals("ps") || str.endsWith("/ps"))) {
                                methodHookParam.args[0] = new String[]{"/"};
                            } else if (containEq("which",SHELLS) && (str.equals("which") || str.endsWith("/which"))) {
                                methodHookParam.setThrowable(new IOException());
                            } else if (containEq("busybox",SHELLS) && contain("busybox", strArr)) {
                                methodHookParam.setThrowable(new IOException());
                            } else if (containEq("sh",SHELLS) && (str.equals("sh") || str.endsWith("/sh"))) {
                                methodHookParam.setThrowable(new IOException());
                            } else {
                                methodHookParam.setThrowable(new IOException());
                            }

//                        }
                    }
                }
            });




			DexposedBridge.findAndHookMethod(java.lang.Runtime.class, "loadLibrary", String.class, ClassLoader.class, new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void beforeHookedMethod(MethodHookParam methodHookParam) {

					String str = (String) methodHookParam.args[0];


					if (str != null && contain(str, h)) {
//						VLog.d("gctech","loadLibrary"+str);
						methodHookParam.setResult(null);
					}
				}
			});
			DexposedBridge.hookMethod(XposedHelpers.findConstructorExact(ProcessBuilder.class, String[].class), new com.taobao.android.dexposed.XC_MethodHook() {
				protected final void beforeHookedMethod(MethodHookParam methodHookParam) {
					int i = 0;

					if (methodHookParam.args[0] != null) {
						String[] strArr = (String[]) methodHookParam.args[0];
//						if (j.ae) {
							String str = "ProcessBuilder Command:";
							for (String str2 : strArr) {
								str = str + " " + str2;
								VLog.d("gctech","loadLibrary"+str);
							}




							if(strArr[0].equals("getprop") && strArr[1].equals("persist.sys.hardcoder.name"))
							{
								methodHookParam.setResult("sdm850");
							}
//						}VLog.d("gctech","loadLibrary"+);
						if (containEndWith(strArr[0], SHELLS)) {
							strArr[0] = "FAKEJUNKCOMMAND";
							methodHookParam.args[0] = strArr;
						}

					}
				}
			});



//



            DexposedBridge.findAndHookMethod(android.provider.Settings.Global.class, "getInt", ContentResolver.class, String.class, Integer.TYPE, new  com.taobao.android.dexposed.XC_MethodHook() {
                protected final void beforeHookedMethod(MethodHookParam methodHookParam) {
                    String str = (String) methodHookParam.args[1];
//                    VLog.d("gctech","Global getInt");
                    if (str != null && "adb_enabled".equals(str)) {
                        methodHookParam.setResult(Integer.valueOf(0));
                        VLog.d("gctech","adb_enabled set");
                    }
                }
            });

        } catch (Exception e) {
			e.printStackTrace();
		}

		hookByC();




		VLog.d("gctech","ro.build.selinux:"+SystemPropertiesCompat.get("ro.build.selinux",""));
	}



	private void injectInternal() throws Throwable {





		if (VirtualCore.get().isMainProcess()) {
			return;
		}
		if (VirtualCore.get().isServerProcess()) {
			addInjector(new ConnectivityStub());
			addInjector(new ActivityManagerStub());
			addInjector(new PackageManagerStub());

			return;
		}
		if (VirtualCore.get().isVAppProcess()) {
			addInjector(new LibCoreStub());
			addInjector(new ActivityManagerStub());
			addInjector(new PackageManagerStub());
			addInjector(HCallbackStub.getDefault());
			addInjector(new ISmsStub());
			addInjector(new ISubStub());
			addInjector(new DropBoxManagerStub());
			addInjector(new NotificationManagerStub());
			addInjector(new LocationManagerStub());
			addInjector(new WindowManagerStub());
			addInjector(new ClipBoardStub());
			addInjector(new MountServiceStub());
			addInjector(new BackupManagerStub());
			addInjector(new TelephonyStub());
			addInjector(new TelephonyRegistryStub());
			addInjector(new PhoneSubInfoStub());
			addInjector(new PowerManagerStub());
			addInjector(new AppWidgetManagerStub());
			addInjector(new AccountManagerStub());
			addInjector(new AudioManagerStub());
			addInjector(new SearchManagerStub());
			addInjector(new ContentServiceStub());
			addInjector(new ConnectivityStub());
			addInjector(new IBluetoothMangerStub());
//			addInjector(new ProcessMangerStub());
//			addInjector(new TelephonySubStub());
//			addInjector(new SysSecurityStub());
//			addInjector(new ContentProviderStub());
			if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
				addInjector(new VibratorStub());
				addInjector(new WifiManagerStub());
				addInjector(new BluetoothStub());
				addInjector(new ContextHubServiceStub());
			}
			if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR1) {
				addInjector(new UserManagerStub());
			}

			if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR1) {
				addInjector(new DisplayStub());
			}
			if (Build.VERSION.SDK_INT >= LOLLIPOP) {
				addInjector(new PersistentDataBlockServiceStub());
				addInjector(new InputMethodManagerStub());
				addInjector(new MmsStub());
				addInjector(new SessionManagerStub());
				addInjector(new JobServiceStub());
				addInjector(new RestrictionStub());
			}
			if (Build.VERSION.SDK_INT >= KITKAT) {
				addInjector(new AlarmManagerStub());
				addInjector(new AppOpsManagerStub());
				addInjector(new MediaRouterServiceStub());
			}
			if (Build.VERSION.SDK_INT >= LOLLIPOP_MR1) {
				addInjector(new GraphicsStatsStub());
				addInjector(new UsageStatsManagerStub());
			}
			if (Build.VERSION.SDK_INT >= M) {
				addInjector(new FingerprintManagerStub());
				addInjector(new NetworkManagementStub());
			}
			if (Build.VERSION.SDK_INT >= N) {
                addInjector(new WifiScannerStub());
                addInjector(new ShortcutServiceStub());
                addInjector(new DevicePolicyManagerStub());
            }
            if (Build.VERSION.SDK_INT >= 26) {
				addInjector(new AutoFillManagerStub());
			}



				hookCameral();


		}
	}

	private void addInjector(IInjector IInjector) {
		mInjectors.put(IInjector.getClass(), IInjector);
	}

	public <T extends IInjector> T findInjector(Class<T> clazz) {
		// noinspection unchecked
		return (T) mInjectors.get(clazz);
	}

	public <T extends IInjector> void checkEnv(Class<T> clazz) {
		IInjector IInjector = findInjector(clazz);
		if (IInjector != null && IInjector.isEnvBad()) {
			try {
				IInjector.inject();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public <T extends IInjector, H extends MethodInvocationStub> H getInvocationStub(Class<T> injectorClass) {
		T injector = findInjector(injectorClass);
		if (injector != null && injector instanceof MethodInvocationProxy) {
			// noinspection unchecked
			return (H) ((MethodInvocationProxy) injector).getInvocationStub();
		}
		return null;
	}

}