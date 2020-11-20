package com.lody.virtual.client.hook.proxies.am;

import android.app.ActivityManager;

import com.lody.virtual.client.hook.base.Inject;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;

import mirror.android.app.IActivityManager;
import mirror.java.lang.ProcessManager;

/**
 * @author Lody
 * @see IActivityManager
 * @see ActivityManager
 */

@Inject(AcMethodProxies.class)
public class ProcessMangerStub extends MethodInvocationProxy {

    public ProcessMangerStub() {
        super(new MethodInvocationStub<>(ProcessManager.getInstance.call()));
    }

    @Override
    public void inject() throws Throwable {
        ProcessManager.instance.set(this.getInvocationStub().getProxyInterface());
    }




    @Override
    protected void onBindMethods() {
        super.onBindMethods();


    }

    @Override
    public boolean isEnvBad() {
        return true;
    }


}
