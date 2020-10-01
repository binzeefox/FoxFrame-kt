@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.utils

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.FileUtils
import android.os.FileUtils.copy
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.binzeefox.kt_foxframe.BuildConfig
import com.binzeefox.kt_foxframe.core.FoxCore
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.random.Random

/**
 * Uri工具
 *
 * @author 狐彻
 * 2020/09/28 21:25
 */
class UriTools(val uri: Uri) {

    // 常量池
    // @author 狐彻 2020/09/28 21:39
    companion object {

        // 提供器处理者
        // @author 狐彻 2020/09/28 21:29
        val resolver: ContentResolver
            get() = FoxCore.application.contentResolver

        /**
         * 通过文件获取Uri
         *
         * @author 狐彻 2020/09/28 21:40
         */
        fun fileToUri(file: File, authority: String): Uri =
            FileProvider.getUriForFile(FoxCore.application, authority, file)

        ///////////////////////////////////////////////////////////////////////////
        // 拓展函数
        ///////////////////////////////////////////////////////////////////////////

        val Uri.outputStream: OutputStream?
            get() = UriTools(this).outputStream

        val Uri.inputStream: InputStream?
            get() = UriTools(this).inputStream

        val Uri.mimeType: String?
            get() = UriTools(this).mimeType

        val Uri.filePath: String?
            get() = UriTools(this).filePath
    }

    // 获输出流
    // @author 狐彻 2020/09/28 21:30
    val outputStream: OutputStream?
        get() = resolver.openOutputStream(uri)

    // 输入流
    // @author 狐彻 2020/09/28 21:31
    val inputStream: InputStream?
        get() = resolver.openInputStream(uri)

    // 文件类型
    // @author 狐彻 2020/09/28 21:37
    val mimeType: String?
        get() = resolver.getType(uri)

    // 获取文件路径
    // @author 狐彻 2020/09/28 22:08
    val filePath: String?
        get() {
            val scheme = uri.scheme //前缀
            var path: String? = null
            if (scheme == null) //空前缀，直接获取路径
                path = uri.path
            if (ContentResolver.SCHEME_FILE == scheme) //文件前缀
                path = uri.path
            if (ContentResolver.SCHEME_CONTENT == scheme) {  //Content前缀
                val displayName = kotlin.run {
                    val cursor = resolver.query(uri, null, null, null, null);
                    cursor?.let {
                        if (it.moveToFirst())
                            it.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        else null
                    }
                } ?: "${System.currentTimeMillis()}${Random.nextInt(0, 9999)}.${
                    MimeTypeMap.getSingleton().getExtensionFromMimeType(
                        resolver.getType(uri)
                    )
                }"

                val ins: InputStream? = resolver.openInputStream(uri)
                ins?.also {
                    File("${FoxCore.application.externalCacheDir!!.absolutePath}/$displayName")
                        .apply {
                            val fos = FileOutputStream(this)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                copy(ins, fos)
                            } else IOUtil.copy(ins, fos)
                            fos.close()
                            ins.close()
                        }.absolutePath
                }
            }
            return path
        }
}