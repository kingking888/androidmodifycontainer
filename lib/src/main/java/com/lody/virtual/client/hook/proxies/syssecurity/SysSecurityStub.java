package com.lody.virtual.client.hook.proxies.syssecurity;



import com.lody.virtual.client.hook.base.Inject;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.helper.utils.VLog;

import mirror.android.providers.Settings;

@Inject(MethodProxies.class)
public class SysSecurityStub extends MethodInvocationProxy {

    public SysSecurityStub() throws IllegalAccessException, InstantiationException {
        super(new MethodInvocationStub<>(Settings.Secure.sNameValueCache.get()));
    }

    @Override
    public  void inject() throws Throwable
    {
        VLog.d("gctech","--------------------------------------------------------");
        Settings.Secure.sNameValueCache.set(this.getInvocationStub().getProxyInterface());
        VLog.d("gctech","--------------------------------------------------------");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
    }

    @Override
    public boolean isEnvBad() {
        return false;
    }


}
