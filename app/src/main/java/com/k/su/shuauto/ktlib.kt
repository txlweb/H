package com.k.su.shuauto

import android.content.Context
import com.k.su.shuauto.FileUtils.copyFileFromAssets
import com.k.su.shuauto.FileUtils.unzipFile
import java.io.File

class ktlib {
    fun unpk(out_path: String, res_name:String, context: Context): Boolean {
        val filesDir: File = context.filesDir
        val unzippedFilePath = out_path // 解压目标完整路径
        File(filesDir.toString() + File.separator + "/"+res_name).delete()
        if (copyFileFromAssets(context, res_name,res_name)) {
            if(!unzipFile(
                    context,
                    filesDir.toString() + File.separator + "/"+res_name,
                    unzippedFilePath
                )){
                return false;
            }
        }else{
            return true;
        }

        return false;
    }
}