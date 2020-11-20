package com.gc.utils;

import android.text.TextUtils;

import com.android.launcher3.model.PackageUpdatedTask;
import com.gc.home.GC1MainActivity;
import com.gc.splash.SplashActivity;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.helper.utils.MD5Utils;
import com.lody.virtual.helper.utils.VLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import mirror.android.providers.Settings;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetInstance {
    private static NetInstance sInstance = null;
    public boolean isFirst = false;


    /** @hide */
    public synchronized static NetInstance get() {
        if (sInstance == null) {

            sInstance = new NetInstance();
        }
        return sInstance;
    }


    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
            .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
            .build();



    public static int getTimeCompareSize(String startTime){
        int i=0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//年-月-日 时-分
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            long endTime = System.currentTimeMillis();
            if(endTime-date1.getTime()>0)
            {
                i = -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  i;
    }

    private String[] hosts = new String[]{"napi","api2","api3"};
    private int i = 0;
    private StringBuffer url = new StringBuffer();

    public  void failChangeHost(SplashActivity activity,String card)
    {
        i++;
        if(i>(hosts.length-1))
        {
            activity.showDilog();
            activity.totast("所有服务器不可用，请联系qq：58850842");

            return;
        }


    }
    private String needld = "";
    String time = "";

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }




    public void startValidateDateNew(SplashActivity activity,String card) {


        if(i>=3)
        {
            i = 2;
        }
        url.setLength(0);
        url.append("https://").append(hosts[i]).append(".2cccc.cc/time");


        Request.Builder requestBuilder = new Request.Builder().url(url.toString());

        requestBuilder.method("GET", null);


        Call call = okHttpClient.newCall(requestBuilder.build());


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                failChangeHost(activity, card);
                startValidateDateNew(activity, card);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                time = response.body().string();
                String sign = md5(time + "2233nnn");
                url.setLength(0);
                url.append("https://").append(hosts[i]).append(".2cccc.cc/82/9165/read/").append(card).append("/").append(time).append("/").append(sign).append("/");


                Request.Builder requestBuilder = new Request.Builder().url(url.toString());

                requestBuilder.method("GET", null);


                Call readcall = okHttpClient.newCall(requestBuilder.build());


                readcall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // TODO: 17-1-4  请求失败
                        failChangeHost(activity, card);
                        startValidateDateNew(activity, card);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // TODO: 17-1-4 请求成功
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            boolean flag = json.optBoolean("status");
                            if (flag && json.getString("data").trim().length() > 0) {
                                VLog.d("gctech", "write config:" + flag);


                                String config = json.getJSONObject("data").getString("config");

                                if (config != null && config.trim().length()>0) {
                                    processSuccess(json,card,activity);
                                } else {
                                    url.setLength(0);
                                    url.append("https://").append(hosts[i]).append(".2cccc.cc/82/9165/").append("write").append("/").append(card).append("/").append(time).append("/").append(sign).append("/");
                                    url.append(NativeEngine.getSerious());
                                    Request.Builder requestBuilder = new Request.Builder().url(url.toString());

                                    Call writeCall = okHttpClient.newCall(requestBuilder.build());


                                    writeCall.enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            // TODO: 17-1-4  请求失败
                                            failChangeHost(activity, card);
                                            startValidateDateNew(activity, card);
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            try {
                                                processSuccess(new JSONObject(response.body().string()),card,activity);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                }
                            }
                            else
                            {
                                activity.totast(json.optString("cnres"));

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.showDilog();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }





        public void processSuccess(JSONObject resultPre,String card,SplashActivity activity )
        {
            try {


                url.setLength(0);
                url.append("https://").append(hosts[i]).append(".2cccc.cc/2/").append("9165/").append(card).append("/duomigaiji/").append(Math.random() * 1000000).append("/");
                Request.Builder requestBuilder = new Request.Builder().url(url.toString());

                Call writeCall = okHttpClient.newCall(requestBuilder.build());


                writeCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // TODO: 17-1-4  请求失败
                        failChangeHost(activity, card);
                        startValidateDateNew(activity, card);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject result = new JSONObject(response.body().string());

                            if(result.has("data") && result.optBoolean("status"))
                            {
                                String endtime =  result.getJSONObject("data").getString("endtime");
                                activity.setText("到期日期:"+endtime);

                                //no
                                if(getTimeCompareSize(endtime)==-1)
                                {
                                    activity.setText("到期日期:"+endtime+",已经过期");
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            activity.showDilog();
                                        }
                                    });

                                    activity.totast("到期日期:"+endtime+",已经过期");
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            activity.showDilog();
                                        }
                                    });
                                }
                                else if( !NativeEngine.getSerious().equals(resultPre.getJSONObject("data").getString("config")))
                                {
                                    activity.setText("卡密已经在其他机子上登录");
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            activity.showDilog();
                                        }
                                    });
                                }
                                else
                                {
                                    GC1MainActivity.goMain(activity);
                                    activity.finish();
                                    activity.totast("到期日期:"+endtime);
                                    activity.dismiss();
                                }
                            }
                            else
                            {
                                activity.totast("卡密已经过期");
                                activity.setText("卡密已经过期");
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.showDilog();
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });





            } catch (Exception e) {
                e.printStackTrace();
            }
        }



}
