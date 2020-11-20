package mirror.android.bluetooth;

import mirror.RefClass;

public class BluetoothManager {
    /**
     * @see android.bluetooth.IBluetooth
     * */
    public static Class<?> TYPE = RefClass.load(IBluetooth.class, android.bluetooth.BluetoothManager.class);
}
