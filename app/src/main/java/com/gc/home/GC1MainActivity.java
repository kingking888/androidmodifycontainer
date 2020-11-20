package com.gc.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.R;
import com.gc.home.models.PackageAppData;
import com.gc.utils.GCLogic;
import com.kyleduo.switchbutton.SwitchButton;
import com.gc.virtual.VirtualCore;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.InstalledAppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.REORDER_TASKS;

public class GC1MainActivity extends AppCompatActivity {

    private Button mAddApp;
    private Button mNewDevices;
    private Button mRestore;
    private SwitchButton mStartAppSwitch = null;
//    private GridView mAppList;
    private TextView mInfo;

    private ImageView mAppIcon = null;

    private String currentPackage = null;
    private TextView mAppName = null;

    public static void goMain(Context context) {
        Intent intent = new Intent(context, GC1MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private SharedPreferences mPrefrence;

    private ImageView mUninstall;


    private void checkLauch()
    {
        if( mStartAppSwitch.isChecked() && currentPackage!=null && currentPackage.length()>0) {
            Toast.makeText(GC1MainActivity.this, "一键新机成功!3秒内开启应用", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(GC1MainActivity.this, "一键新机成功!", Toast.LENGTH_SHORT).show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);

                GC1MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if( mStartAppSwitch.isChecked() && currentPackage!=null && currentPackage.length()>0)
                        {
                            LoadingActivity.launch(GC1MainActivity.this, currentPackage, 0);
                        }
                    }
                });

            }
        }).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.gc.R.layout.activity_main);
        mPrefrence = this.getPreferences(MODE_PRIVATE);
        mUninstall = (ImageView)findViewById(com.gc.R.id.del);
        mAppIcon =  (ImageView)findViewById(com.gc.R.id.app_icon);
        mAppName = (TextView)findViewById(R.id.app_name);
        mAddApp = (Button)this.findViewById(com.gc.R.id.add_app);
        mNewDevices = (Button)this.findViewById(com.gc.R.id.new_device);
        mRestore = (Button)this.findViewById(com.gc.R.id.restore);
        mStartAppSwitch = (SwitchButton)this.findViewById(com.gc.R.id.start_app_switch);
        mStartAppSwitch.setChecked(mPrefrence.getBoolean("ischecked",false));

        mUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog alertDialog2 = new AlertDialog.Builder(GC1MainActivity.this)
                        .setTitle("温馨提示")
                        .setMessage("是否卸载？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<InstalledAppInfo> list =  VirtualCore.get().getInstalledApps(0);


                                for(InstalledAppInfo appinfo:list)
                                {

                                    VirtualCore.get().uninstallPackage(appinfo.packageName);

                                }

                                mAppIcon.setImageDrawable(null);
                                mAppName.setText("");
                            }
                        })

                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                alertDialog2.show();





            }
        });


        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);//获取系统的连接服务
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();//获取网络的连接情况

        VLog.d("gctech","type::::"+activeNetInfo.getType()+"");

        mStartAppSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPrefrence.edit().putBoolean("ischecked",b).commit();
            }
        });




//        Settings.NameValueCache.mValues.set(Settings.Secure.sNameValueCache.get(),map);
//        Settings.Secure.sNameValueCache.set(Settings.Secure.sNameValueCache.get());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.RESTART_PACKAGES,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_NUMBERS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
//        mAppList = (GridView)this.findViewById(R.id.app);
        mInfo = (TextView)this.findViewById(com.gc.R.id.info);
        GCLogic.get().createRe();
//        String txt = FileUtils.readTxtFile(GCLogic.BACK_FILE);
//        mInfo.setText(txt);
        mAppIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentPackage!=null) {

                    LoadingActivity.launch(GC1MainActivity.this, currentPackage, 0);
                }
                else
                {
                    Toast.makeText(GC1MainActivity.this, "未安装应用!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAddApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListAppActivity.gotoListApp(GC1MainActivity.this);
            }
        });
//
        mNewDevices.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
               String txt =   GCLogic.get().newDevices();

//
//
//               txt = txt.replace(",","\r\n");
                mInfo.setText(txt);



                checkLauch();

//                TelephonyManager mTele = (TelephonyManager)GC1MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
//
//                VLog.d("gctect",mTele.getLine1Number());
            }
        });


//
        mRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!new File(GCLogic.RE_FILE).exists() || FileUtils.readTxtFile(GCLogic.RE_FILE).length()==0)
                {
                    Toast.makeText(GC1MainActivity.this, "无恢复文件!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String txt = GCLogic.get().restore();
                mInfo.setText(txt);
                Toast.makeText(GC1MainActivity.this, "恢复成功!", Toast.LENGTH_SHORT).show();

                checkLauch();
            }
        });
//
        refresh();


//        mAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                LoadingActivity.launch(GC1MainActivity.this, appData.get(position).packageName, 0);
//            }
//        });
    }

    private List<PackageAppData>  appData = new ArrayList<>();
    public  void refresh()
    {
        List<InstalledAppInfo> list =  VirtualCore.get().getInstalledApps(0);
        appData.removeAll(appData);
//        for (int i = 0 ;i < list.size();i++)
//        {

        if(list.size()>0) {
            PackageAppData tmpApp = new PackageAppData(this, list.get(list.size() - 1));
            appData.add(tmpApp);
            mAppIcon.setImageDrawable(tmpApp.icon);

            currentPackage = tmpApp.packageName;
            mAppName.setText(tmpApp.name);
        }


//        }

//        InstalledAppListAdapter mAppListDapter = new InstalledAppListAdapter(this.getLayoutInflater(),appData,this);
//        mAppList.setAdapter(mAppListDapter);
//        mAppListDapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
