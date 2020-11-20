package mirror.android.os;


import mirror.RefClass;
import mirror.RefStaticObject;

public class Build {
    public static Class<?> TYPE = RefClass.load(Build.class, android.os.Build.class);
    public static RefStaticObject<String> DEVICE;
    public static RefStaticObject<String> SERIAL;
    public static RefStaticObject<String> BRAND;
    public static RefStaticObject<String> BOARD;
    public static RefStaticObject<String> SDK;
    public static RefStaticObject<String> MANUFACTURER;
    public static RefStaticObject<String> PRODUCT;
    public static RefStaticObject<String> MODEL;
    public static RefStaticObject<String> HARDWARE;
    public static RefStaticObject<String> HOST;
    public static RefStaticObject<String> DISPLAY;
    public static RefStaticObject<String> CPU_ABI;
    public static RefStaticObject<String> CPU_ABI2;
    public static RefStaticObject<String> ID;
    public static RefStaticObject<String> RADIO;
    public static RefStaticObject<String> FINGERPRINT;
    public static RefStaticObject<String> PLATFORM;


}