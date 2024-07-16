package com.k.su.shuauto; // 替换为你的包名

import androidx.appcompat.app.AppCompatActivity; // 导入AppCompatActivity

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle; // 导入Bundle用于处理Activity的创建和保存状态
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static
    Handler mainHandler = new Handler(Looper.getMainLooper());

    public static boolean ku_zy = false;
    /**
     * Activity是否在前台
     * @param context
     * @return
     */
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
    String d = "";
    dw_file c = new dw_file();
    boolean fn = true;
    Timer t = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        d=this.getApplicationContext().getFilesDir().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                c.init("https://github.com/txlweb/H/releases/download/1/v.txt",d+"/vn.txt");
                c.run();
            }
        }).start();

        new Timer().schedule(new TimerTask() {
            public void run()
            {
                if(!isOnForground(MainActivity.this)){
                    //finish();
                }
            }
        },0,200);
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                // Toast.makeText(MainActivity.this, "SU权限获取成功!", Toast.LENGTH_SHORT).show();
                ku_zy = false;
            } else {
                Toast.makeText(MainActivity.this, "SU权限获取失败!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //new File(this.getApplicationContext().getFilesDir().toString()+"/kernel.sh").delete();
        new ktlib().unpk(this.getApplicationContext().getFilesDir().toString()+"/qx/","qxdev.zip",this);
        new ktlib().unpk(this.getApplicationContext().getFilesDir().toString()+"/","ka.sh",this);//和平
        new ktlib().unpk(this.getApplicationContext().getFilesDir().toString()+"/","kb.sh",this);//PUBG
        if(new File(this.getApplicationContext().getFilesDir().toString()+"/ka.data").isFile()){
            try {
                new File(this.getApplicationContext().getFilesDir().toString()+"/ka.sh").delete();
                run_sh_in("cp \""+this.getApplicationContext().getFilesDir().toString()+"/ka.data\" \""+this.getApplicationContext().getFilesDir().toString()+"/ka.sh\"");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        if(new File(this.getApplicationContext().getFilesDir().toString()+"/kb.data").isFile()){
            try {
                new File(this.getApplicationContext().getFilesDir().toString()+"/kb.sh").delete();
                run_sh_in("cp \""+this.getApplicationContext().getFilesDir().toString()+"/kb.data\" \""+this.getApplicationContext().getFilesDir().toString()+"/kb.sh\"");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 设置Activity的布局文件
        Button shsu = findViewById(R.id.st_shsu);
        Button inqx = findViewById(R.id.QX_kernel);
        TextView v = findViewById(R.id.textView2);
        Switch ss = findViewById(R.id.switch1);
        Switch ss2 = findViewById(R.id.switch3);
        Switch dv = findViewById(R.id.switch2);
        ss.setChecked(true);
        Button nh = findViewById(R.id.st_nh);
        Button hh = findViewById(R.id.Start_lh);
        Button up = findViewById(R.id.upda);
        Button dw = findViewById(R.id.dwcf);
        v.setText("版本"+update.readFile(d+"/v.txt")+" 骑士免费内核 @ 乐鹏\r\n启动器 @ k");
        nh.setEnabled(false);
        hh.setEnabled(false);
        inqx.setEnabled(false);
        nh.setOnClickListener(View->{
            nh.setEnabled(false);
            hh.setEnabled(false);
            openApp(this,"com.tencent.tmgp.pubgmhd","com.epicgames.ue4.SplashActivity");
            try {
                if(ss2.isChecked()){
                    run_sh_in("mv \""+this.getApplicationContext().getFilesDir().toString()+"/kb.sh\" \""+this.getApplicationContext().getFilesDir().toString()+"/kernel.sh\"");
                }else{
                    run_sh_in("mv \""+this.getApplicationContext().getFilesDir().toString()+"/ka.sh\" \""+this.getApplicationContext().getFilesDir().toString()+"/kernel.sh\"");
                }
                run_sh_in("cp \""+this.getApplicationContext().getFilesDir().toString()+"/kernel.sh\" /data/");
                run_sh_in("chmod 777 /data/kernel.sh");
                if(ku_zy){
                    run_sh_in("sh \"/data/kernel.sh\"");
                    return;
                }
                ProcessBuilder processBuilder = new ProcessBuilder("su", "-c","sh" ,"/data/kernel.sh");
                processBuilder.directory(new java.io.File("/data"));
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                // 获取进程的输入流，用于向脚本发送输入
                OutputStream outputStream = process.getOutputStream();
                if(dv.isChecked()){
                    outputStream.write("1\n".getBytes());
                    outputStream.flush(); // 确保数据被发送
                }else {
                    outputStream.write("0\n".getBytes());
                    outputStream.flush(); // 确保数据被发送
                }
                outputStream.flush(); // 确保数据被发送
                outputStream.write("1\n".getBytes());
                outputStream.flush(); // 确保数据被发送
                if(ss.isChecked()){
                    outputStream.write("1\n".getBytes());
                    outputStream.flush(); // 确保数据被发送
                }else {
                    outputStream.write("0\n".getBytes());
                    outputStream.flush(); // 确保数据被发送
                }
                // 读取脚本的输出（如果需要的话）
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Output: " + line);
                }

                // 读取脚本的错误输出（如果需要的话）
                while ((line = errorReader.readLine()) != null) {
                    System.err.println("Error: " + line);
                }

                // 等待脚本执行完成
                int exitCode = process.waitFor();
                System.out.println("Script exited with code " + exitCode);
                finish();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        hh.setOnClickListener(View->{
            nh.setEnabled(false);
            hh.setEnabled(false);
            openApp(this,"com.tencent.tmgp.pubgmhd","com.epicgames.ue4.SplashActivity");
            try {
                run_sh_in("cp \""+this.getApplicationContext().getFilesDir().toString()+"/kernel.sh\" /data/");
                run_sh_in("chmod 777 /data/kernel.sh");
                if(ku_zy){
                    run_sh_in("sh \"/data/kernel.sh\"");
                    return;
                }
                ProcessBuilder processBuilder = new ProcessBuilder("su", "-c","sh" ,"/data/kernel.sh");
                processBuilder.directory(new java.io.File("/data"));
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                // 获取进程的输入流，用于向脚本发送输入
                OutputStream outputStream = process.getOutputStream();
                if(dv.isChecked()){
                    outputStream.write("1\n".getBytes());
                    outputStream.flush(); // 确保数据被发送
                }else {
                    outputStream.write("0\n".getBytes());
                    outputStream.flush(); // 确保数据被发送
                }
                outputStream.write("0\n".getBytes());
                outputStream.flush(); // 确保数据被发送
                if(ss.isChecked()){
                    outputStream.write("1\n".getBytes());
                    outputStream.flush(); // 确保数据被发送
                }else {
                    outputStream.write("0\n".getBytes());
                    outputStream.flush(); // 确保数据被发送
                }
                // 读取脚本的输出（如果需要的话）
                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println("Output: " + line);
//                }

                // 读取脚本的错误输出（如果需要的话）
//                while ((line = errorReader.readLine()) != null) {
//                    System.err.println("Error: " + line);
//                }

                // 等待脚本执行完成
                //int exitCode = process.waitFor();
                //System.out.println("Script exited with code " + exitCode);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        up.setOnClickListener(View -> {
            Intent intent = new Intent();
            intent.setClass(this, update.class);
            startActivity(intent);
        });
            // 注意：这个命令需要root权限，否则将失败

        dw.setOnClickListener(View -> {
            Intent intent = new Intent();
            intent.setClass(this, configMGR.class);
            startActivity(intent);
        });
        inqx.setOnClickListener(View -> {
            Intent intent = new Intent();
            intent.setClass(this, kernel_dev_select.class);
            startActivity(intent);
        });

        t.schedule(new TimerTask() {
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(c.is_ok()){
                            if(!update.readFile(d + "/v.txt").equals(update.readFile(d + "/vn.txt"))){
                                if(fn){
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this, update.class);
                                    startActivity(intent);

                                    fn=false;
                                    t.cancel();
                                    finish();
                                }
                            }else{
                                Toast.makeText(MainActivity.this, "已经是最新的版本了!", Toast.LENGTH_SHORT).show();
                                nh.setEnabled(true);
                                hh.setEnabled(true);
                                inqx.setEnabled(true);
                                shsu.setEnabled(false);
                                t.cancel();
                            }
                        }
                    }
                });
            }
        },0,1000);
    }

    public void openApp(Context context, String packageName, String mainActivity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(packageName);

        // 尝试设置具体的Activity类名
        ComponentName comp = new ComponentName(packageName, mainActivity);
        intent.setComponent(comp);

        // 检查是否有能够处理这个Intent的应用
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "应用未安装", Toast.LENGTH_SHORT).show();
        }
    }

    public static String run_sh(String shell) throws RemoteException {
        if(MainActivity.ku_zy){
            //shizuku mode (不可用)
            return null;
        }else {
            //root
            return Su_runtime.executeRootCommand(shell);
        }
    }
    public String run_sh_in(String shell) throws RemoteException {
        if(MainActivity.ku_zy){
            //shizuku mode (不可用)
            return null;
        }else {
            //root
            return Su_runtime.executeRootCommand(shell);
        }
    }
    public static String getKernelVersion() {

        try {
            System.out.println(run_sh("getprop ro.build.kernel"));
            return run_sh("getprop ro.build.kernel").trim();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}