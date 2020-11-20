package com.lody.virtual.client.hook.proxies.am;

import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.helper.utils.VLog;

import java.lang.reflect.Method;

/**
 * @author Lody
 */
@SuppressWarnings("unused")
class AcMethodProxies {

    static class Exec extends MethodProxy
    {
        @Override
        public String getMethodName() {
            return "exec";
        }

        @Override
        public Object call(Object who, Method method, Object... args) {

            VLog.d("gctech","exec:"+method.getName());
            try {
                return super.call(who,method,args);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return 0;
        }


    }

}
