package com.lody.virtual.client.hook.proxies.telephony;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.lody.virtual.client.hook.base.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.helper.utils.marks.FakeDeviceMark;

import java.lang.reflect.Method;

import mirror.com.android.internal.telephony.ITelephony;

/**
 * @author Lody
 */
@Inject(MethodProxies.class)
public class TelephonyStub extends BinderInvocationProxy {

	public TelephonyStub() {
		super(ITelephony.Stub.asInterface, Context.TELEPHONY_SERVICE);
	}

	@Override
	protected void onBindMethods() {
		super.onBindMethods();



//		addMethodProxy(new ReplaceCallingPkgMethodProxy("isOffhook"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getLine1NumberForDisplay"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("isOffhookForSubscriber"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("isRingingForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("call"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("isRinging"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("isIdle"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("isIdleForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("isRadioOn"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("isRadioOnForSubscriber"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("isSimPinEnabled"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getCdmaEriIconIndex"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getCdmaEriIconIndexForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getCdmaEriIconMode"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getCdmaEriIconModeForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getCdmaEriText"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getCdmaEriTextForSubscriber"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getNetworkTypeForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getDataNetworkType"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getDataNetworkTypeForSubscriber"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getVoiceNetworkTypeForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getLteOnCdmaMode"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getLteOnCdmaModeForSubscriber"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getCalculatedPreferredNetworkType"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getPcscfAddress"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getLine1AlphaTagForDisplay"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getMergedSubscriberIds"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getRadioAccessFamily"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("isVideoCallingEnabled"));

//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getSimOperator"));

//		addMethodProxy(new GetSimState());
//		addMethodProxy(new GetNetworkCountryIso());
//		addMethodProxy(new GetSimOperator());
//		addMethodProxy(new GetSimOperatorName());
//		addMethodProxy(new GetSubscriberId());



//		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		addMethodProxy(new GetSubscriberId());
//		VLog.d("gctect","TelephonyStubTelephonyStubTelephonyStubTelephonyStubTelephonyStubTelephonyStub");

	}


//
//	@FakeDeviceMark("fake sim state.")
//	static class GetSimState extends MethodProxy {
//
//
//
//		@Override
//		public String getMethodName() {
//			return "getSimState";
//		}
//
//		@Override
//		public Object call(Object who, Method method, Object... args) throws Throwable {
//
//			VLog.d("gctect", getDeviceInfo().simState);
//			return getDeviceInfo().simState;
////            return "tiancaichenweiming";//imei
//		}
//	}


//	@FakeDeviceMark("fake sim state.")
//	static class GetSimState extends MethodProxy {
//
//
//
//		@Override
//		public String getMethodName() {
//			return "getSimState";
//		}
//
//		@Override
//		public Object call(Object who, Method method, Object... args) throws Throwable {
//
//		    VLog.d("gctect", getDeviceInfo().simState);
//			return getDeviceInfo().simState;
////            return "tiancaichenweiming";//imei
//		}
//	}
//
//
//	@FakeDeviceMark("fake country iso.")
//	static class GetNetworkCountryIso extends MethodProxy {
//
//		@Override
//		public String getMethodName() {
//			return "getNetworkCountryIso";
//		}
//
//		@Override
//		public Object call(Object who, Method method, Object... args) throws Throwable {
//			VLog.d("gctect",getDeviceInfo().simCountryIso);
//			return getDeviceInfo().simCountryIso;
////            return "tiancaichenweiming";//imei
//		}
//	}
//
//	@FakeDeviceMark("fake sim operator.")
//	static class GetSimOperator extends MethodProxy {
//
//
//		@Override
//		public String getMethodName() {
//			return "getSimOperator";
//		}
//
//		@Override
//		public Object call(Object who, Method method, Object... args) throws Throwable {
//			VLog.d("gctect",getDeviceInfo().simOperator);
//			return getDeviceInfo().simOperator;
////            return "tiancaichenweiming";//imei
//		}
//	}
//
//
//
//
//	@FakeDeviceMark("fake NetworkOperatorName.")
//	static class GetSimOperatorName extends MethodProxy {
//
//		@Override
//		public String getMethodName() {
//			return "getSimOperatorName";
//		}
//
//		@Override
//		public Object  afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
//            VLog.d("gctect",getDeviceInfo().simOperatorName);
//
//			result =  getDeviceInfo().simOperatorName;
//			return getDeviceInfo().simOperatorName;
////            return "tiancaichenweiming";//imei
//		}
//	}
//
//
//
//
//	@FakeDeviceMark("fake subscriberId id.")
//	static class GetSubscriberId extends MethodProxy {
//
//		@Override
//		public String getMethodName() {
//			return "getSubscriberId";
//		}
//
//
//		@Override
//		public Object call(Object who, Method method, Object... args) throws Throwable {
//
//			VLog.d("gctect","subscriberId:"+getDeviceInfo().simOperatorName);
//			return getDeviceInfo().subscriberId;
////            return "tiancaichenweiming";//imei
//		}
//	}
}
