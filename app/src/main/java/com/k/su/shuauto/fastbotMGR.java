package com.k.su.shuauto;

import static com.k.su.shuauto.MainActivity.run_sh;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class fastbotMGR extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fastbot); // 设置Activity的布局文件
        if(!new File(this.getApplicationContext().getFilesDir().toString()+"/kerenl.sh").isFile()){
            Toast.makeText(this, "还没有启动过一次,请先正常启动一次之后再打开.", Toast.LENGTH_SHORT).show();
            finish();
        }
        openApp(this,"com.tencent.tmgp.pubgmhd","com.epicgames.ue4.SplashActivity");
        try {
            run_sh("cp \""+this.getApplicationContext().getFilesDir().toString()+"/kerenl.sh\" /data/");
            run_sh("chmod 777 /data/kerenl.sh");
            ProcessBuilder processBuilder = new ProcessBuilder("su", "-c","sh" ,"/data/kerenl.sh");
            processBuilder.directory(new java.io.File("/data"));
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            OutputStream outputStream = process.getOutputStream();
            outputStream.write("1\n".getBytes());
            outputStream.flush(); // 确保数据被发送
            outputStream.write("1\n".getBytes());
            outputStream.flush(); // 确保数据被发送
            outputStream.write("1\n".getBytes());
            outputStream.flush(); // 确保数据被发送
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Output: " + line);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        finish();

    }
}
