package com.k.su.shuauto;

import static com.k.su.shuauto.kernel_dev_select.csl;

import android.content.Context;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class fs extends Thread{
    private String  vv1 = "";
    private static Context ts=null;
    private static StringBuffer output = new StringBuffer("--------------开始安装驱动--------------\r\n");
    public void set(String v1,Context tss){
        output = new StringBuffer("--------------开始安装驱动--------------\r\n");
        vv1=v1;
        ts=tss;
    }
    public void run(){
        try {
            prun_sh(vv1);
            if(!Objects.requireNonNull(output.toString()).contains("成功")){
                kernel_dev_select.mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ts, "刷入失败或已经刷过了!", Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                kernel_dev_select.mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ts, "刷入成功!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public static String prun_sh(String shell) throws RemoteException {
        try {

            // 使用ProcessBuilder来启动一个进程，并尝试以root用户身份执行命令
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", shell});

            // 读取命令的输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            System.out.println("--------------开始安装驱动--------------");
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
                kernel_dev_select.mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        csl.setText(output);
                    }
                });
                if(output.toString().contains("后重启")){
                    process.destroyForcibly();
                    kernel_dev_select.mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ts, "已经阻止脚本重启手机!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            reader.close();

            // 等待命令执行完成
            process.waitFor();

            // 打印输出（或根据需要处理）

            System.out.println(output.toString());
            kernel_dev_select.mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    csl.setText(output);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }
    String opt(){
        return output.toString();
    }
}
