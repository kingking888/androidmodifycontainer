package com.lody.virtual.client.hook.proxies.bluetooth;

import android.os.Build;

import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.helper.utils.marks.FakeDeviceMark;

import java.lang.reflect.Method;

import mirror.android.bluetooth.IBluetooth;
import mirror.android.bluetooth.IBluetoothManager;

/**
 * @see android.bluetooth.BluetoothManager
 */
public class IBluetoothMangerStub extends BinderInvocationProxy {
    public static final String SERVICE_NAME = Build.VERSION.SDK_INT >= 17 ?
            "bluetooth_manager" :
            "bluetooth";

    public IBluetoothMangerStub() {
        super(IBluetoothManager.Stub.asInterface, SERVICE_NAME);
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        addMethodProxy(new GetAddress());
        addMethodProxy(new GetBluetoothAddressSdk23());
    }

    @FakeDeviceMark("fake MAC")
    private static class GetAddress extends StaticMethodProxy {

        GetAddress() {
            super("getAddress");
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctech","bluetoothMac:"+getDeviceInfo().bluetoothMac);
//            VASettings
            return getDeviceInfo().bluetoothMac;
        }
    }


    @FakeDeviceMark("fake MAC")
    private static class GetBluetoothAddressSdk23 extends StaticMethodProxy {

        GetBluetoothAddressSdk23() {
            super("getBluetoothAddressSdk23");
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.d("gctech","bluetoothMac:"+getDeviceInfo().bluetoothMac);
            return getDeviceInfo().bluetoothMac;
        }
    }
}
