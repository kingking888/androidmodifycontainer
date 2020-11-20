package mirror.android.content;

import android.os.IBinder;
import android.os.IInterface;

import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

/**
 * @author Lody
 */

public class IContentProvider {

    public static Class<?> TYPE = RefClass.load(IContentProvider.class, "android.content.IContentProvider");

    public static class Stub {
        public static Class<?> TYPE = RefClass.load(IContentService.Stub.class, "android.content.IContentProvider$Stub");
        @MethodParams({IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}
