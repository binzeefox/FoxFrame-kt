package com.binzeefox.kt_foxframe.tools.utils

import android.net.Uri
import java.io.*

/**
 * 文件工具
 *
 * @author 狐彻
 * 2020/09/29 8:44
 */
object IOUtil {
    private const val TAG = "IOUtil"

    ///////////////////////////////////////////////////////////////////////////
    // 复制
    ///////////////////////////////////////////////////////////////////////////

    fun copy(input: InputStream, output: OutputStream){
        val buffer = ByteArray(1024)
        var index = input.read(buffer)
        while (index != -1){
            output.write(buffer, 0, index)
            output.flush()
            index = input.read(buffer)
        }
    }


    fun copy(bytes: ByteArray, output: OutputStream){
        output.write(bytes)
        output.flush()
    }

    fun copy(fis: FileInputStream, targetUri: Uri){
        val resolver = UriTools.resolver
        val os: OutputStream? = resolver.openOutputStream(targetUri)
        os?.also {
            val buffer = ByteArray(1024)
            var read = fis.read(buffer)
            while (read != -1) {
                os.write(buffer, 0, read)
                os.flush()
                read = fis.read(buffer)
            }
        } ?: LogUtil.d(TAG, "copy: 获取输出流失败");
    }

    fun copy(uri: Uri, target: File){
        copy(UriTools(uri).inputStream ?: return, FileOutputStream(target))
    }

    fun copy(source: File, target: File){
        if (!source.exists()){
            LogUtil.i(TAG, "copy: 源文件不存在" );
            return
        }
        if (target.exists()){
            LogUtil.i(TAG, "copy: 目标位置存在文件，请确认并删除后重试");
            return
        }
        if (!target.createNewFile()) {
            LogUtil.w(TAG, "copy: 创建文件失败");
            return
        }

        val fr = FileReader(source)
        val fw = FileWriter(target)
        val buffer = CharArray(1024)
        var index = fr.read(buffer)
        while (index != -1){
            fw.write(buffer, 0, index)
            fw.flush()
            index = fr.read(buffer)
        }
        fr.close()
        fw.close()
    }


    ///////////////////////////////////////////////////////////////////////////
    // 公共方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 读取文件为字符串
     *
     * @author 狐彻 2020/09/29 8:51
     */
    fun readTextFile(file: File): String? {
        if (!file.exists()) return null
        val buffer = ByteArray(1024)
        val sb = StringBuffer()
        val fis = FileInputStream(file)
        while (fis.read(buffer) != -1)
            sb.append(String(buffer))
        fis.close()
        return buffer.toString()
    }


}