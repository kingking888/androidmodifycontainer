package com.lody.virtual.client.hook.proxies.connectivity;

import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.helper.utils.VLog;

import java.lang.reflect.Method;

import mirror.android.net.NetworkInfo;


public class MethodProxies {

    static class GetActiveNetworkInfo extends MethodProxy
    {

        @Override
        public String getMethodName() {
            return "getActiveNetworkInfo";
        }


        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {

//            VLog.d("gctech","getActiveNetworkInfo");
            Object info =  method.invoke(who, args);

//            VLog.d("gctech","getActiveNetworkInfo:"+NetworkInfo.mNetworkType.get(info));
//            NetworkInfo.mNetworkType.set(info,1);
//            VLog.d("gctech","getActiveNetworkInfo:"+NetworkInfo.mNetworkType.get(info));


            return info;
        }
    }


    static class GetAllNetworkInfo extends MethodProxy
    {

        @Override
        public String getMethodName() {
            return "getAllNetworkInfo";
        }


        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {

            VLog.d("gctech","getAllNetworkInfo");
            return method.invoke(who, args);
        }
    }

}
