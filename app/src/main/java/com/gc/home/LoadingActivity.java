package com.gc.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.abs.ui.VActivity;
import com.gc.abs.ui.VUiKit;
import com.gc.home.models.PackageAppData;
import com.gc.home.repo.PackageAppDataStorage;
import com.gc.widgets.EatBeansView;
import com.gc.virtual.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;

import java.util.Locale;

/**
 * @author Lody
 */

public class LoadingActivity extends VActivity {

    private static final String PKG_NAME_ARGUMENT = "MODEL_ARGUMENT";
    private static final String KEY_INTENT = "KEY_INTENT";
    private static final String KEY_USER = "KEY_USER";
    private PackageAppData appModel;
    private EatBeansView loadingView;

    public static void launch(Context context, String packageName, int userId) {
        Intent intent = VirtualCore.get().getLaunchIntent(packageName, userId);
        if (intent != null) {
            Intent loadingPageIntent = new Intent(context, LoadingActivity.class);
            loadingPageIntent.putExtra(PKG_NAME_ARGUMENT, packageName);
            loadingPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            loadingPageIntent.putExtra(KEY_INTENT, intent);
            loadingPageIntent.putExtra(KEY_USER, userId);
            context.startActivity(loadingPageIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.gc.R.layout.activity_loading);
        loadingView = (EatBeansView) findViewById(com.gc.R.id.loading_anim);
        int userId = getIntent().getIntExtra(KEY_USER, -1);
        String pkg = getIntent().getStringExtra(PKG_NAME_ARGUMENT);
        appModel = PackageAppDataStorage.get().acquire(pkg);
        if (appModel == null) {
            Toast.makeText(getApplicationContext(), "Open App:" + pkg + " failed.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageView iconView = (ImageView) findViewById(com.gc.R.id.app_icon);
        iconView.setImageDrawable(appModel.icon);
        TextView nameView = (TextView) findViewById(com.gc.R.id.app_name);
        nameView.setText(String.format(Locale.ENGLISH, " 正在打开 %s...", appModel.name));
        Intent intent = getIntent().getParcelableExtra(KEY_INTENT);
        if (intent == null) {
            finish();
            return;
        }
        VirtualCore.get().setUiCallback(intent, mUiCallback);
        VUiKit.defer().when(() -> {
            if (!appModel.fastOpen) {
                try {
                    VirtualCore.get().preOpt(appModel.packageName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            VActivityManager.get().startActivity(intent, userId);
        });

    }

    private final VirtualCore.UiCallback mUiCallback = new VirtualCore.UiCallback() {

        @Override
        public void onAppOpened(String packageName, int userId) throws RemoteException {
            finish();
        }

        @Override
        public void onOpenFailed(String packageName, int userId) throws RemoteException {
            VUiKit.defer().when(() -> {
            }).done((v) -> {
                if (!isFinishing()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(com.gc.R.string.start_app_failed, packageName),
                            Toast.LENGTH_SHORT).show();
                }
            });

            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (loadingView != null) {
            loadingView.startAnim();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (loadingView != null) {
            loadingView.stopAnim();
        }
    }
}
