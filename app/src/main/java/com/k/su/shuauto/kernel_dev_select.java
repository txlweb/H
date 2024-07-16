package com.k.su.shuauto;


import static com.k.su.shuauto.MainActivity.getKernelVersion;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rikka.shizuku.Shizuku;

public class kernel_dev_select extends AppCompatActivity {
    private RecyclerView fileListRecyclerView;
    private FileAdapter fileAdapter;
    private final List<FileModel> fileList = new ArrayList<>();
    public static TextView csl = null;
    public static boolean ku_zy = false;
    public static int select_id = 0;


    private boolean isOnForground(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        if(appProcessInfoList == null){
            return false;
        }

        String packageName = context.getPackageName();
        for(ActivityManager.RunningAppProcessInfo processInfo : appProcessInfoList){
            if(processInfo.processName.equals(packageName) && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ){
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Timer().schedule(new TimerTask() {
            public void run()
            {
                if(!isOnForground(kernel_dev_select.this)){
                    finish();
                }
            }
        },0,200);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pluginmgr);

        fileListRecyclerView = findViewById(R.id.recyclerView2);
        fileListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 获取文件列表并填充数据模型
//        TextView tv = findViewById(R.id.textView3);
//        tv.setText("Kernel version = "+getKernelVersion());
        //System.out.println(getKernelVersion());
        File directory = new File(this.getApplicationContext().getFilesDir().toString()+"/qx/"); // 替换为实际目录路径
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if(file.getName().contains(".sh")){
                    fileList.add(new FileModel(file.getName(),  file.getName(), file.length()));
                }
            }
        }
        Button rne = findViewById(R.id.rn);
        csl = findViewById(R.id.sh_do);
        rne.setOnClickListener(View -> {
            csl.setVisibility(android.view.View.VISIBLE);
            fs a = new fs();
            a.set("sh \""+this.getApplicationContext().getFilesDir().toString()+"/qx/"+fileList.get(select_id).getFileName()+"\"",this);
            a.start();

            mainHandler.sendEmptyMessage(0);
        });
        // 创建并设置适配器
        fileAdapter = new FileAdapter(fileList);
        fileListRecyclerView.setAdapter(fileAdapter);
        //按钮功能绑定

    }
    static Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // 在这里处理消息并更新UI
            csl.setText("/");
        }
    };
}
