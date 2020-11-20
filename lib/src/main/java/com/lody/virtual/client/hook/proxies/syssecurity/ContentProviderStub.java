package com.lody.virtual.client.hook.proxies.syssecurity;

import mirror.android.content.ContentProviderNative;

import com.lody.virtual.client.hook.base.Inject;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;

@Inject(MethodProxies.class)
public class ContentProviderStub extends MethodInvocationProxy {

    public ContentProviderStub()
    {
        super(new MethodInvocationStub(ContentProviderNative.asInterface.call()));


//        VLog.d(this.getClass().getName(), "Unable to build HookDelegate."+this.);
    }

    @Override
    public void inject() throws Throwable {

//        VLog.d("gctech","--------------------------------------------------------");
//        Settings.NameValueCache.mContentProvider.set(Settings.Secure.sNameValueCache.get(), getInvocationStub().getProxyInterface());
//        VLog.d("gctech","--------------------------------------------------------");

//        ContentResolver.TYPE..set(this.getInvocationStub().getProxyInterface());

//        Settings.NameValueCache.mValues.set(Settings.Secure.sNameValueCache.get(),map);






    }

//    protected void onBindMethods() {
//              addMethodProxy(new Query());
//    }

//    static class Query extends MethodProxy
//    {
//
//        @Override
//        public String getMethodName() {
//            return "getStringForUser";
//        }
//
//        public Object call(Object who, Method method, Object... args) throws Throwable {
//            VLog.d("gctech","QueryQueryQueryQueryQueryQueryQueryQuery");
//            return method.invoke(who, args);
//        }
//
//    }
    @Override
    public boolean isEnvBad() {
        return false;
    }
}
