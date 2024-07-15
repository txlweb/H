package com.k.su.shuauto;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class update extends AppCompatActivity {
    public static String readFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public static boolean is_all_new(String tmp){
        String vu = "https://github.com/txlweb/H/releases/download/1/v.txt";
        String mv = "240705";
        dw_file a = new dw_file();
        a.init(vu,tmp+"/v.txt");
        return readFile("/data/v.txt").contains(mv);
    }
    public static boolean dw_all(String tmp){
        String vu = "https://github.com/txlweb/H/releases/download/1/ka.sh";
        String mv = "240705";
        dw_file a = new dw_file();
        a.init(vu,tmp+"/ka.data");
        return readFile("/data/v.txt").contains(mv);
    }
    String d = "";
    boolean vxd = false;
    static ProgressBar p = null;
    dw_file a = new dw_file();
    dw_file b = new dw_file();
    Timer t = new Timer();
    dw_file c = new dw_file();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        d=this.getApplicationContext().getFilesDir().toString();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        p=findViewById(R.id.progressBar2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 执行一些耗时的操作
                String vu = "https://github.com/txlweb/H/releases/download/1/ka.sh";
                a.init(vu,d+"/ka.data");
                a.run();
                b.init("https://github.com/txlweb/H/releases/download/1/kb.sh",d+"/kb.data");
                c.init("https://github.com/txlweb/H/releases/download/1/v.txt",d+"/v.txt");
            }
        }).start();

//        b.run();
        t.schedule(new TimerTask() {
            public void run()
            {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(a.is_ok()){
                            if(!vxd){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        b.run();
                                    }
                                }).start();
                                vxd = true;
                            }
                            p.setMax(b.getAll());
                            p.setProgress(b.getNow());
                            if(b.is_ok()){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        c.run();
                                    }
                                }).start();
                                //
                                if(c.is_ok()){
                                    Toast.makeText(update.this, "更新完成,需要重启应用来完成更新.", Toast.LENGTH_SHORT).show();
                                    t.cancel();
                                    finish();
                                }

                            }
                            System.out.println(b.getAll());
                            System.out.println(b.getNow());
                        }else {
                            p.setMax(a.getAll());
                            p.setProgress(a.getNow());
                        }
                    }
                });
            }
        },0,200);

    }

}
