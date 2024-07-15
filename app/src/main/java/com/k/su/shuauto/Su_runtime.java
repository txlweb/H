package com.k.su.shuauto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Su_runtime {
    public static String executeRootCommand(String command) {
        try {
            System.out.println(command);
            // 使用ProcessBuilder来启动一个进程，并尝试以root用户身份执行命令
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", command});

            // 读取命令的输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            // 等待命令执行完成
            process.waitFor();

            // 打印输出（或根据需要处理）
            System.out.println("--------------RUNNNNNNNN________________");
            System.out.println(output.toString());
            return output.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "ERROR";
    }
}
