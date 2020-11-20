package com.lody.virtual.client.hook.providers;

import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;

import com.lody.virtual.client.VClientImpl;
import com.lody.virtual.client.hook.base.MethodBox;
import com.lody.virtual.helper.utils.VLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import mirror.android.content.IContentProvider;

/**
 * @author Lody
 */

public class ProviderHook implements InvocationHandler {

    public static final String QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection";

    public static final String QUERY_ARG_SQL_SELECTION_ARGS =
            "android:query-arg-sql-selection-args";
    public static final String QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sort-order";


    private static final Map<String, HookFetcher> PROVIDER_MAP = new HashMap<>();

    static {
        PROVIDER_MAP.put("settings", new HookFetcher() {
            @Override
            public ProviderHook fetch(boolean external, IInterface provider) {
                return new SettingsProviderHook(provider);
            }
        });
        PROVIDER_MAP.put("downloads", new HookFetcher() {
            @Override
            public ProviderHook fetch(boolean external, IInterface provider) {
                return new DownloadProviderHook(provider);
            }
        });
    }

    protected final Object mBase;

    public ProviderHook(Object base) {
        this.mBase = base;
    }

    private static HookFetcher fetchHook(String authority) {
        HookFetcher fetcher = PROVIDER_MAP.get(authority);
        if (fetcher == null) {
            fetcher = new HookFetcher() {
                @Override
                public ProviderHook fetch(boolean external, IInterface provider) {
                    if (external) {
                        return new ExternalProviderHook(provider);
                    }
                    return new InternalProviderHook(provider);
                }
            };
        }
        return fetcher;
    }

    private static IInterface createProxy(IInterface provider, ProviderHook hook) {
        if (provider == null || hook == null) {
            return null;
        }
        return (IInterface) Proxy.newProxyInstance(provider.getClass().getClassLoader(), new Class[]{
                IContentProvider.TYPE,
        }, hook);
    }

    public static IInterface createProxy(boolean external, String authority, IInterface provider) {
        if (provider instanceof Proxy && Proxy.getInvocationHandler(provider) instanceof ProviderHook) {
            return provider;
        }
        ProviderHook.HookFetcher fetcher = ProviderHook.fetchHook(authority);
        if (fetcher != null) {
            ProviderHook hook = fetcher.fetch(external, provider);
            IInterface proxyProvider = ProviderHook.createProxy(provider, hook);
            if (proxyProvider != null) {
                provider = proxyProvider;
            }
        }
        return provider;
    }

    public Bundle call(MethodBox methodBox, String method, String arg, Bundle extras) throws InvocationTargetException {
//        VLog.d("gctech","callcallcallcallcallcallcallcallcall");

        Bundle b =   methodBox.call();
        if (Build.VERSION.SDK_INT >= 24) {
            b.putString("name", "android_id");

//            Bundle[{_track_generation=android.util.MemoryIntArray@5b, value=a832bdcf4b0e6e87, _generation_index=0, _generation=1}]
//
//            Bundle bsub = new Bundle();
//            bsub.putString("value",VClientImpl.get().getDeviceInfo().androidId);
//            bsub.putParcelable("_track_generation",null);
//            bsub.putInt("_generation_index",0);
//            bsub.putInt("_generation",1);
            b.putString("value", VClientImpl.get().getDeviceInfo().androidId);

        } else {
            b.putString("android_id", VClientImpl.get().getDeviceInfo().androidId);
        }
        return b;
    }

    public Uri insert(MethodBox methodBox, Uri url, ContentValues initialValues) throws InvocationTargetException {

        return (Uri) methodBox.call();
    }

    public Cursor query(MethodBox methodBox, Uri url, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder, Bundle originQueryArgs) throws InvocationTargetException {

        VLog.d("gctech","queryqueryqueryqueryqueryqueryqueryquery");
        return (Cursor) methodBox.call();
    }

    public String getType(MethodBox methodBox, Uri url) throws InvocationTargetException {
        return (String) methodBox.call();
    }

    public int bulkInsert(MethodBox methodBox, Uri url, ContentValues[] initialValues) throws InvocationTargetException {
        return (int) methodBox.call();
    }

    public int delete(MethodBox methodBox, Uri url, String selection, String[] selectionArgs) throws InvocationTargetException {
        return (int) methodBox.call();
    }

    public int update(MethodBox methodBox, Uri url, ContentValues values, String selection,
                      String[] selectionArgs) throws InvocationTargetException {
        return (int) methodBox.call();
    }

    public ParcelFileDescriptor openFile(MethodBox methodBox, Uri url, String mode) throws InvocationTargetException {
        return (ParcelFileDescriptor) methodBox.call();
    }

    public AssetFileDescriptor openAssetFile(MethodBox methodBox, Uri url, String mode) throws InvocationTargetException {
        return (AssetFileDescriptor) methodBox.call();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
        try {
            processArgs(method, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
//        VLog.d("gctech", method.getName());


        if(args!=null) {
            VLog.d("gctech", "-----------------------------------");
            for (Object obj:args) {

                if(obj!=null)
                VLog.d("gctech", method.getName() + ":" + obj.toString());

            }
            VLog.d("gctech", "-----------------------------------");
        }
        MethodBox methodBox = new MethodBox(method, mBase, args);
        int start = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 ? 1 : 0;
        try {
            String name = method.getName();
            if ("call".equals(name)) {
                String methodName = (String) args[start];
                String arg = (String) args[start + 1];
                Bundle extras = (Bundle) args[start + 2];


//                if(arg!=null) {
//
//                        VLog.d("gctech", "call:"+methodName + ":" + arg);
//
//                }

                if(methodName.equals("GET_secure") && arg.equals("android_id"))
                {

//                    Bundle bsub = new Bundle();
//                    bsub.putString("value",VClientImpl.get().getDeviceInfo().androidId);
//                    bsub.putParcelable("_track_generation",null);
//                    bsub.putInt("_generation_index",0);
//                    bsub.putInt("_generation",1);
//                    b.putBundle("value", bsub);

                    return  wrapBundle("android_id",VClientImpl.get().getDeviceInfo().androidId);
                }


                if(methodName.equals("GET_system"))
                {



                    return  wrapBundle("android_id",VClientImpl.get().getDeviceInfo().androidId);
                }





                if(methodName.equals("GET_system") && arg.startsWith("time"))
                {
                    arg = "time_12_24";
                }


                return call(methodBox, methodName, arg, extras);
            } else if ("insert".equals(name)) {
                Uri url = (Uri) args[start];
                ContentValues initialValues = (ContentValues) args[start + 1];
                return insert(methodBox, url, initialValues);
            } else if ("getType".equals(name)) {
                return getType(methodBox, (Uri) args[0]);
            } else if ("delete".equals(name)) {
                Uri url = (Uri) args[start];
                String selection = (String) args[start + 1];
                String[] selectionArgs = (String[]) args[start + 2];
                return delete(methodBox, url, selection, selectionArgs);
            } else if ("bulkInsert".equals(name)) {
                Uri url = (Uri) args[start];
                ContentValues[] initialValues = (ContentValues[]) args[start + 1];
                return bulkInsert(methodBox, url, initialValues);
            } else if ("update".equals(name)) {
                Uri url = (Uri) args[start];
                ContentValues values = (ContentValues) args[start + 1];
                String selection = (String) args[start + 2];
                String[] selectionArgs = (String[]) args[start + 3];
                return update(methodBox, url, values, selection, selectionArgs);
            } else if ("openFile".equals(name)) {
                Uri url = (Uri) args[start];
                String mode = (String) args[start + 1];
                return openFile(methodBox, url, mode);
            } else if ("openAssetFile".equals(name)) {
                Uri url = (Uri) args[start];
                String mode = (String) args[start + 1];
                return openAssetFile(methodBox, url, mode);
            } else if ("query".equals(name)) {
                Uri url = (Uri) args[start];
                String[] projection = (String[]) args[start + 1];
                String selection = null;
                String[] selectionArgs = null;
                String sortOrder = null;
                Bundle queryArgs = null;
                if (Build.VERSION.SDK_INT >= 26) {
                    queryArgs = (Bundle) args[start + 2];
                    if (queryArgs != null) {
                        selection = queryArgs.getString(QUERY_ARG_SQL_SELECTION);
                        selectionArgs = queryArgs.getStringArray(QUERY_ARG_SQL_SELECTION_ARGS);
                        sortOrder = queryArgs.getString(QUERY_ARG_SQL_SORT_ORDER);
                    }
                } else {
                    selection = (String) args[start + 2];
                    selectionArgs = (String[]) args[start + 3];
                    sortOrder = (String) args[start + 4];
                }

                return query(methodBox, url, projection, selection, selectionArgs, sortOrder, queryArgs);
            }
            return methodBox.call();
        } catch (Throwable e) {
            e.printStackTrace();
            VLog.d("ProviderHook", "call: %s (%s) with error", method.getName(), Arrays.toString(args));
            if (e instanceof InvocationTargetException) {
                throw e.getCause();
            }
            throw e;
        }
    }
    private Bundle wrapBundle(String name, String value) {
        Bundle bundle = new Bundle();
        if (Build.VERSION.SDK_INT >= 24) {
            bundle.putString("name", name);
            bundle.putString("value", value);
        } else {
            bundle.putString(name, value);
        }
        return bundle;
    }
    protected void processArgs(Method method, Object... args) {

    }

    public interface HookFetcher {
        ProviderHook fetch(boolean external, IInterface provider);
    }
}
