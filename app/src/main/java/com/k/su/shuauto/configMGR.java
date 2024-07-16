package com.k.su.shuauto;

import static com.k.su.shuauto.MainActivity.run_sh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class configMGR  extends AppCompatActivity {
    private RecyclerView fileListRecyclerView;
    private FileAdapter fileAdapter;
    private final List<FileModel> fileList = new ArrayList<>();
    public static TextView csl = null;
    public static int select_id = 0;
    Timer t = new Timer();
    String d = "";
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    dw_file a = new dw_file();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        d=this.getApplicationContext().getFilesDir().toString();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cfglst);

        fileListRecyclerView = findViewById(R.id.recyclerView2);
        fileListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProgressBar pb = findViewById(R.id.progressBar3);
        pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 执行一些耗时的操作
                String vu = "https://github.com/txlweb/H/releases/download/1/cfg.zip";
                a.init(vu,d+"/cfg.zip");
                a.run();
            }
        }).start();
        t.schedule(new TimerTask() {
            public void run()
            {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(a.is_ok()){
                            new File(d+"/cfgs/").mkdir();
                            FileUtils.unzipFile(configMGR.this,d+"/cfg.zip",d+"/cfgs/");
                            File directory = new File(configMGR.this.getApplicationContext().getFilesDir().toString()+"/cfgs/"); // 替换为实际目录路径
                            File[] files = directory.listFiles();
                            if (files != null) {
                                for (File file : files) {
                                    if(file.isFile()){
                                        fileList.add(new FileModel(file.getName(),  file.getName(), file.length()));
                                    }
                                }
                            }
                            Button rne = findViewById(R.id.rn);
                            csl = findViewById(R.id.sh_do);
                            rne.setOnClickListener(View -> {
                                try {
                                    run_sh("cp \""+configMGR.this.getApplicationContext().getFilesDir().toString()+"/cfgs/"+fileList.get(select_id).getFileName()+"\" /data/骑士和平配置");
                                    Toast.makeText(configMGR.this, "修改完成.", Toast.LENGTH_SHORT).show();

                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            // 创建并设置适配器
                            fileAdapter = new FileAdapter(fileList);
                            fileListRecyclerView.setAdapter(fileAdapter);
                            pb.setVisibility(View.GONE);
                            t.cancel();
                        }else {
                            Toast.makeText(configMGR.this, a.getNow()+"/"+a.getAll(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        },0,1000);
        // 获取文件列表并填充数据模型
//        TextView tv = findViewById(R.id.textView3);
//        tv.setText("Kernel version = "+getKernelVersion());
        //System.out.println(getKernelVersion());

        //按钮功能绑定

    }
}
