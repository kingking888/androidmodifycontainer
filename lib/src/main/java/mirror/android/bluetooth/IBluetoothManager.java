package mirror.android.bluetooth;

import android.os.IBinder;
import android.os.IInterface;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IBluetoothManager {
    /**
     * @see android.bluetooth.IBluetooth
     * */
    public static Class<?> TYPE = RefClass.load(IBluetoothManager.class, "android.bluetooth.IBluetoothManager");
    /**
     * @see android.bluetooth.IBluetooth.Stub
     * */
    public static class Stub {
        public static Class<?> TYPE = RefClass.load(IBluetoothManager.Stub.class, "android.bluetooth.IBluetoothManager$Stub");
        @MethodParams({IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}
