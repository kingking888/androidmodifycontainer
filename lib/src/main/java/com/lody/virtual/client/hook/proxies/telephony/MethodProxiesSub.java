package com.lody.virtual.client.hook.proxies.telephony;

import android.os.Bundle;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.NeighboringCellInfo;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.helper.utils.marks.FakeLocMark;
import com.lody.virtual.remote.vloc.VCell;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lody
 */
@SuppressWarnings("ALL")
class MethodProxiesSub {

    static class GetSubscriptionProperty extends MethodProxy
    {

        @Override
        public String getMethodName() {
            return "getSubscriptionProperty";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctech","getSubscriptionPropertygetSubscriptionPropertygetSubscriptionProperty");
            return method.invoke(who, args);
        }
    }


}
