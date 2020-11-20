package com.lody.virtual.client.hook.proxies.phonesubinfo;

import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.Inject;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.helper.utils.marks.FakeDeviceMark;

import java.lang.reflect.Method;

import mirror.com.android.internal.telephony.IPhoneSubInfo;

/**
 * @author Lody
 */
@Inject(MethodProxies.class)
public class PhoneSubInfoStub extends BinderInvocationProxy {
	public PhoneSubInfoStub() {
		super(IPhoneSubInfo.Stub.asInterface, "iphonesubinfo");
	}
//	static class GetLine1Number extends MethodProxy {
//
//		@Override
//		public String getMethodName() {
//			return "getLine1Number";
//		}
//
//		@Override
//		public Object call(Object who, Method method, Object... args) throws Throwable {
//			VLog.d("gctech","getLine1Number:"+getDeviceInfo().line1Number);
//			return getDeviceInfo().line1Number;
//		}
//
//	}

	@Override
	protected void onBindMethods() {
		super.onBindMethods();
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getNaiForSubscriber"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getImeiForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getDeviceSvn"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getDeviceSvnUsingSubId"));
//		addMethodProxy(new GetSubscriberId());
//		addMethodProxy(new GetLine1Number());
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getSubscriberIdForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getGroupIdLevel1"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getLine1Number"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getGroupIdLevel1ForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getLine1Number"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getLine1NumberForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getLine1AlphaTag"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getLine1AlphaTagForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getMsisdn"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getMsisdnForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getVoiceMailNumber"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getVoiceMailNumberForSubscriber"));
//		addMethodProxy(new ReplaceCallingPkgMethodProxy("getVoiceMailAlphaTag"));
//		addMethodProxy(new ReplaceLastPkgMethodProxy("getVoiceMailAlphaTagForSubscriber"));

//		addMethodProxy(new GetSimState());
//		addMethodProxy(new GetNetworkCountryIso());
//		addMethodProxy(new GetNetworkOperator());
//		addMethodProxy(new GetNetworkOperatorName());
//		addMethodProxy(new GetSubscriberId());

	}




//
//	@FakeDeviceMark("fake device id.")
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
//
//
//	@FakeDeviceMark("fake device id.")
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
//	@FakeDeviceMark("fake device id.")
//	static class GetNetworkOperator extends MethodProxy {
//
//
//		@Override
//		public String getMethodName() {
//			return "getNetworkOperator";
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
//	static class GetNetworkOperatorName extends MethodProxy {
//
//		@Override
//		public String getMethodName() {
//			return "getNetworkOperatorName";
//		}
//
//		@Override
//		public Object call(Object who, Method method, Object... args) throws Throwable {
////            VLog.d("gctect",getDeviceInfo().simOperatorName);
//			return getDeviceInfo().simOperatorName;
////            return "tiancaichenweiming";//imei
//		}
//	}




	@FakeDeviceMark("fake subscriber id.")
	static class GetSubscriberId extends MethodProxy {

		@Override
		public String getMethodName() {
			return "getSubscriberId";
		}


		@Override
		public Object call(Object who, Method method, Object... args) throws Throwable {
			VLog.e("gctect","getSubscriberId:");
			return super.call(who,method,args);
//            return "tiancaichenweiming";//imei
		}
	}
}
