package com.gc.settings;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.abs.ui.VActivity;
import com.gc.virtual.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.InstalledAppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weishu
 * @date 18/2/15.
 */

public class TaskManageActivity extends VActivity {

    private ListView mListView;
    private List<TaskManageInfo> mInstalledApps = new ArrayList<>();
    private AppManageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.gc.R.layout.activity_list);
        mListView = (ListView) findViewById(com.gc.R.id.list);
        mAdapter = new AppManageAdapter();
        mListView.setAdapter(mAdapter);

        loadAsync();
    }

    private void loadAsync() {
        defer().when(this::loadApp).done((v) -> mAdapter.notifyDataSetChanged());
    }

    private void loadApp() {

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (am == null) {
            return;
        }

        List<TaskManageInfo> ret = new ArrayList<>();
        List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        if (infoList == null) {
            return;
        }
        List<ActivityManager.RunningAppProcessInfo> retList = new ArrayList<>();
        String hostPkg = VirtualCore.get().getHostPkg();
        for (ActivityManager.RunningAppProcessInfo info : infoList) {
            if (VActivityManager.get().isAppPid(info.pid)) {
                List<String> pkgList = VActivityManager.get().getProcessPkgList(info.pid);
                if (pkgList.contains(hostPkg)) {
                    continue;
                }
                String processName = VActivityManager.get().getAppProcessName(info.pid);
                if (processName != null) {
                    info.processName = processName;
                }
                info.pkgList = pkgList.toArray(new String[pkgList.size()]);
                info.uid = VUserHandle.getAppId(VActivityManager.get().getUidByPid(info.pid));
                retList.add(info);
            }
        }

        PackageManager packageManager = getPackageManager();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : retList) {
            TaskManageInfo info = new TaskManageInfo();
            info.name = runningAppProcessInfo.processName;
            info.pid = runningAppProcessInfo.pid;
            info.uid = runningAppProcessInfo.uid;

            Drawable icon = getResources().getDrawable(android.R.drawable.sym_def_app_icon);
            if (runningAppProcessInfo.pkgList != null) {
                for (String pkg : runningAppProcessInfo.pkgList) {
                    InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(pkg, 0);
                    if (installedAppInfo != null) {
                        Drawable drawable = installedAppInfo.getApplicationInfo(0).loadIcon(packageManager);
                        if (drawable != null) {
                            icon = drawable;
                            break;
                        }
                    }
                }
            }
            info.icon = icon;
            ret.add(info);
        }
        mInstalledApps.clear();
        mInstalledApps.addAll(ret);
    }

    class AppManageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mInstalledApps.size();
        }

        @Override
        public TaskManageInfo getItem(int position) {
            return mInstalledApps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder(TaskManageActivity.this, parent);
                convertView = holder.root;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TaskManageInfo item = getItem(position);

            holder.button.setText(com.gc.R.string.task_manage_uninstall);
            holder.label.setText(item.name);
            holder.icon.setImageDrawable(item.icon);

            holder.button.setOnClickListener(v -> {
                VActivityManager.get().killApplicationProcess(item.name.toString(), item.uid);
                holder.button.postDelayed(TaskManageActivity.this::loadAsync, 300);
            });

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView icon;
        TextView label;
        Button button;

        View root;

        ViewHolder(Context context, ViewGroup parent) {
            root = LayoutInflater.from(context).inflate(com.gc.R.layout.item_task_manage, parent, false);
            icon = root.findViewById(com.gc.R.id.item_app_icon);
            label = root.findViewById(com.gc.R.id.item_app_name);
            button = root.findViewById(com.gc.R.id.item_app_button);
        }
    }

    static class TaskManageInfo {
        CharSequence name;
        int uid;
        int pid;
        Drawable icon;
    }
}
