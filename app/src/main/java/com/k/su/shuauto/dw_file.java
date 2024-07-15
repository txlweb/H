package com.k.su.shuauto;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class dw_file extends Thread{
    private String fileUrl = "";
    private String fileName = "";
    private int all = 0;
    private int now = -1;
    private boolean ok = false;
    public void init(String fileUrl_, String fileName_){
        this.fileUrl =fileUrl_;
        this.fileName=fileName_;
    }
    public void run(){
        this.ok = false;
        try {
            URL url = new URL(this.fileUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            int responseCode = httpURLConnection.getResponseCode();

            // 总是检查HTTP响应码
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 初始化文件输出流
                FileOutputStream fileOutputStream = new FileOutputStream(this.fileName);

                // 初始化输入流
                InputStream inputStream = httpURLConnection.getInputStream();

                // 从输入流读取数据并写入文件输出流
                this.all = httpURLConnection.getContentLength();
                this.now = 0;
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                    this.now += bytesRead;
                    // 可以在这里添加进度条更新
                }
                this.ok = true;
                fileOutputStream.close();
                inputStream.close();
                httpURLConnection.disconnect();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean is_ok(){
        return this.ok || this.all == this.now;
    }
    public int getAll() {
        return this.all;
    }

    public int getNow() {
        return this.now;
    }
}
