package mirror.android.os;

import mirror.RefClass;
import mirror.RefStaticMethod;

public class SystemProperties {
    public static Class<?> TYPE = RefClass.load(SystemProperties.class,"android.os.SystemProperties");
    public static RefStaticMethod<String> native_get;
}
