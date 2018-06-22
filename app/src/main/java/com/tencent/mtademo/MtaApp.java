package com.tencent.mtademo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.stat.StatConfig;
import com.tencent.stat.StatCrashCallback;
import com.tencent.stat.StatCrashReporter;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;


/**
 * Created by foreachli on 2018/6/21.
 */

public class MtaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        boolean debugMode = CommonUtils.isApkInDebug(this);
        Log.e("MtaSDK", "debugMode:" + debugMode);
        // 根据状态打开/关闭logcat日志，logcat标签：MtaSDK
        // TODO 上线时，切记关闭
        StatConfig.setDebugEnable(debugMode);
        setupOtherConfig(this);

        // 开启MTA功能统计，注册ActivityLifecycle，实现页面、活跃等基础统计埋点
        StatService.registerActivityLifecycleCallbacks(this);
    }

    private void setupOtherConfig(Context context) {
        // 设置10分钟为周期的上报策略
        StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
        StatConfig.setSendPeriodMinutes(10);

        // 是否开启java crash捕获，默认开启
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJavaCrashHandlerStatus(true);
        // 是否开启c/c++ crash捕获，默认关闭
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJniNativeCrashStatus(true);
        // 设置crash后的回调
        StatCrashReporter.getStatCrashReporter(getApplicationContext()).addCrashCallback(new StatCrashCallback() {

            @Override
            public void onJniNativeCrash(String tombstoneMsg) {
                Log.d("Test", "Native crash happened, tombstone message:" +tombstoneMsg);
            }

            @Override
            public void onJavaCrash(Thread thread, Throwable throwable) {
                Log.d("Test", "Java crash happened, thread: " + thread + ",Throwable:" +throwable.toString());
            }
        });


    }
}
