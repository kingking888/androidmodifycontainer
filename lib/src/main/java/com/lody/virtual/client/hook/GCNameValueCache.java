package com.lody.virtual.client.hook;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import com.lody.virtual.helper.utils.VLog;

import java.util.HashMap;

import mirror.android.providers.Settings;

public class GCNameValueCache{
    private final String mVersionSystemProperty;
    private final HashMap<String, String> mValues = new HashMap<>();
    private long mValuesVersion = 0;
    private final Uri mUri;
    GCNameValueCache(String versionSystemProperty, Uri uri) {
        mVersionSystemProperty = versionSystemProperty;
        mUri = uri;
    }
    String getString(ContentResolver cr, String name) {

        if (!mValues.containsKey(name)) {
            String value = null;
            Cursor c = null;
            try {
                c = cr.query(mUri, new String[] { "value" },
                          "name=?", new String[]{name}, null);
                if (c != null && c.moveToNext()) value = c.getString(0);
                mValues.put(name, value);
            } catch (SQLException e) {
                // SQL error: return null, but don't cache it.
                VLog.e("gctech", "Can't get key " + name + " from " + mUri, e);
            } finally {
                if (c != null) c.close();
            }
            return value;
        } else {
            return mValues.get(name);
        }
    }
}
