package com.lody.virtual.client.hook.proxies.phonesubinfo;

import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.helper.utils.marks.FakeDeviceMark;

import java.lang.reflect.Method;

/**
 * @author Lody
 */
@SuppressWarnings("ALL")
class MethodProxies {

    @FakeDeviceMark("fake device id")
    static class GetDeviceId extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getDeviceId";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctech","GetDeviceId1:"+getDeviceInfo().deviceId);
            return getDeviceInfo().deviceId;
        }
    }


    static class GetLine1AlphaTagForSubscriber extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getLine1AlphaTagForSubscriber";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctect","getLine1AlphaTagForSubscriber:"+getDeviceInfo().simOperatorName);
            return getDeviceInfo().simOperatorName;
//            return "tiancaichenweiming";//imei
        }
    }

    @FakeDeviceMark("fake line1Number id")
    static class GetLine1Number extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getLine1Number";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctech","getLine1Number:"+getDeviceInfo().line1Number);
            return getDeviceInfo().line1Number;
        }

    }
//
//
    static class GetLine1NumberForDisplay extends MethodProxy
    {
        @Override
        public String getMethodName() {
            return "getLine1NumberForDisplay";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctech","getLine1NumberForDisplay:"+getDeviceInfo().line1Number);
            return getDeviceInfo().line1Number;
        }
    }
//
    static class GetDeviceIdForSubscriber extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getDeviceIdForSubscriber";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctect","getSubscriberId:"+getDeviceInfo().subscriberId);
            return getDeviceInfo().subscriberId;
        }

    }
//
    @FakeDeviceMark("fake subscriberId")
    static class GetSubscriberIdForSubscriber extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getSubscriberIdForSubscriber";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctect","getSubscriberIdForSubscriber:"+getDeviceInfo().subscriberId);
            return getDeviceInfo().subscriberId;
        }

    }

    @FakeDeviceMark("fake iccid")
    static class GetIccSerialNumber extends MethodProxy {

        @Override
        public String getMethodName() {
            return "getIccSerialNumber";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctect","GetIccSerialNumberGetIccSerialNumberGetIccSerialNumberGetIccSerialNumberGetIccSerialNumberGetIccSerialNumber");
            return getDeviceInfo().iccId;
//            return "getIccSerialNumber";
        }
    }


    static class getIccSerialNumberForSubscriber extends GetIccSerialNumber {
        @Override
        public String getMethodName() {
            return "getIccSerialNumberForSubscriber";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctect","getIccSerialNumberForSubscriber");

            return getDeviceInfo().iccId;
        }
    }
}
