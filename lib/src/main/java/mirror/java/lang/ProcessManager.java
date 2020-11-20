package mirror.java.lang;

import mirror.RefClass;
import mirror.RefStaticMethod;
import mirror.RefStaticObject;

public class ProcessManager {
    public static Class<?> TYPE = RefClass.load(ProcessManager.class,"java.lang.ProcessManager");

   public static   RefStaticObject<Object> instance;
    public static   RefStaticMethod<Object> getInstance;
}
