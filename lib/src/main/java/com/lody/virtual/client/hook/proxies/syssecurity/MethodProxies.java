package com.lody.virtual.client.hook.proxies.syssecurity;

import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.helper.utils.marks.FakeDeviceMark;

import java.lang.reflect.Method;


public class MethodProxies {


//    static class  GetString extends MethodProxy
//    {
//        @Override
//        public String getMethodName() {
//            return "getString";
//        }
//
//        @Override
//        public Object call(Object who, Method method, Object... args) throws Throwable {
//
//        //            return super.call(who, method, args);
//                VLog.d("gctech","getString");
//                return "333333";
//        }
//    }


    static class  Guery extends MethodProxy
    {
        @Override
        public String getMethodName() {
            return "query";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctech","query");
                        return super.call(who, method, args);
//
//            return "333333";
        }
    }


}