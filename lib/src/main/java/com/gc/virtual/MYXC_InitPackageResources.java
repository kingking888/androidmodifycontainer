package com.gc.virtual;

import de.robv.android.xposed.callbacks.XC_InitPackageResources;

public class MYXC_InitPackageResources extends XC_InitPackageResources {
    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        if ("com.tencent.mm".equals(resparam.packageName)) {
//            resparam.res.
//            resparam.res.setReplacement(0x90c009f, "客户");
        }
    }
}
