package com.gc.splash;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.home.FlurryROMCollector;
import com.gc.home.GC1MainActivity;
import com.gc.utils.NetInstance;
import com.gc.virtual.VirtualCore;

import com.gc.R;
import com.gc.VCommends;
import com.gc.abs.ui.VActivity;
import com.gc.abs.ui.VUiKit;
import com.lody.virtual.helper.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import jonathanfinerty.once.Once;

public class SplashActivity extends VActivity {


    private SharedPreferences mUser = null;
    private TextView mCard = null;

    public void totast(String msg)
    {


        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SplashActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        });
    }


    public void setText(String str)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCard.setText(str);
            }
        });

    }


    AlertDialog dialog = null;
    public void showDilog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.card, null);
        //设置对话框布局
        dialog.setView(dialogView);
        dialog.show();
        EditText codeName = (EditText) dialogView.findViewById(R.id.code);

        Button btnOk = (Button) dialogView.findViewById(R.id.action_ok);
//        Button btnCancel = (Button) dialogView.findViewById(R.id.action_cancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeName.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(SplashActivity.this, "请输入卡密!", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    FileUtils.writeToFile(code.getBytes(),new File("/sdcard/key"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                mUser.edit().putString("card",code).commit();
                NetInstance.get().isFirst = true;
                NetInstance.get().startValidateDateNew(SplashActivity.this,code);

            }
        });


    }

    public void dismiss()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog!=null) {
                    dialog.dismiss();
                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        @SuppressWarnings("unused")
        boolean enterGuide = !Once.beenDone(Once.THIS_APP_INSTALL, VCommends.TAG_NEW_VERSION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mUser = this.getPreferences(MODE_PRIVATE);

        mCard = (TextView) this.findViewById(R.id.card);

        mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDilog();
            }
        });

        VUiKit.defer().when(() -> {
            if (!Once.beenDone("collect_fabric")) {
                FlurryROMCollector.startCollect();
                Once.markDone("collect_fabric");
            }
            long time = System.currentTimeMillis();
            doActionInThread();
            time = System.currentTimeMillis() - time;
            long delta = 100L - time;
            if (delta > 0) {
                VUiKit.sleep(delta);
            }
        }).done((res) -> {

//            String card = FileUtils.readTxtFile("/sdcard/key");
//
////           String card =  mUser.getString("card","");
//
//           if(card.trim().length()==0)
//           {
//               mCard.setText("未有激活记录");
//
//               showDilog();
//           }
//           else {
//
//               NetInstance.get().startValidateDateNew(SplashActivity.this,card);
//           }

            GC1MainActivity.goMain(SplashActivity.this);
            SplashActivity.this.finish();

        });
    }


    private void doActionInThread() {
        if (!VirtualCore.get().isEngineLaunched()) {
            VirtualCore.get().waitForEngine();
        }
    }
}
