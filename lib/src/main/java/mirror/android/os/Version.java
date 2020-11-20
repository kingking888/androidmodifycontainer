package mirror.android.os;

import mirror.RefClass;
import mirror.RefStaticObject;

public class Version {
    public static Class<?> TYPE = RefClass.load(Version.class, android.os.Build.VERSION.class);
    public static RefStaticObject<String> RELEASE;
    public static RefStaticObject<Integer> SDK_INT;
}
