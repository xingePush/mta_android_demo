package com.tencent.mtademo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tencent.stat.StatAppMonitor;
import com.tencent.stat.StatNativeCrashReport;
import com.tencent.stat.StatService;

import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        setupTest();
    }

    private void setupTest() {
        // 自定义事件
        Button buttonKVEv = findViewById(R.id.buttonCustomKVEvent);
        buttonKVEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 参数信息
                Properties properties = new Properties();
                properties.setProperty("myKey", "myValue");
                properties.setProperty("status", "succ");
                // 调用自定义事件接口
                StatService.trackCustomKVEvent(context, "CustomKvEvent", properties);
            }
        });

        // 自定义时长事件
        Button buttonDuKVEv = findViewById(R.id.buttonCustomKVDuEv);
        buttonDuKVEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 参数信息
                Properties properties = new Properties();
                properties.setProperty("myKey", "myValue");
                properties.setProperty("status", "succ");
                String eventId = "CustomKVTimeEv";
                // trackCustomBeginKVEvent和trackCustomEndKVEvent成对全匹配
                StatService.trackCustomBeginKVEvent(context, eventId, properties);
                // do something begin...
                long startTime = System.currentTimeMillis();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }
                long endTime = System.currentTimeMillis();
                // do something end...
                StatService.trackCustomEndKVEvent(context, eventId, properties);

                // 或者调用以下接口，时间由客户自己计算
                // 将ms转成s
                int costTime = (int) ((endTime - startTime) / 1000);
                StatService.trackCustomKVTimeIntervalEvent(context,
                        costTime, eventId + "xx", properties);
            }
        });

        // java Crash
        Button buttonJavaCrash = findViewById(R.id.buttonJavaCrash);
        buttonJavaCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make a java crash, just for test
                int i = 0;
                int b = 3 / i;
                Log.e("test", "b is " + b);
            }
        });

        // jni crash
        Button buttonJniCrash = findViewById(R.id.buttonJNICrash);
        buttonJniCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make a c/c++/jni crash, just for test
                StatNativeCrashReport.doNativeCrashTest();
            }
        });

        // 接口监控
        Button buttonMonitorEv = findViewById(R.id.buttonMonitor);
        buttonMonitorEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something begin...
                long startTime = System.currentTimeMillis();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }
                long endTime = System.currentTimeMillis();
                long cost = endTime - startTime;
                // do something end...
                StatAppMonitor statAppMonitor = new StatAppMonitor("myCostApiMonitor");
                statAppMonitor.setMillisecondsConsume(cost);
                statAppMonitor.setReturnCode(0);
                // other settings
                StatService.reportAppMonitorStat(context, statAppMonitor);
            }
        });

        // 登陆页
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }
}
