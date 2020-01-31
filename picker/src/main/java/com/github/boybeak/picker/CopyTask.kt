package com.github.boybeak.picker

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.ArrayMap
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.security.MessageDigest
import kotlin.experimental.and


open class CopyTask(context: Context, private var callback: Callback?) : AsyncTask<Uri, Int, ArrayMap<Uri, File>>() {

    private val weakContext = WeakReference(context)
    private val outDir: File = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        File(context.externalCacheDir, "picker")
    } else {
        File(context.cacheDir, "picker")
    }

    init {
        outDir.mkdirs()
    }

    override fun doInBackground(vararg uris: Uri): ArrayMap<Uri, File> {
        val files = ArrayMap<Uri, File>()
        publishProgress(0)
        uris.forEachIndexed { index, uri ->
            files[uri] = doCopy(uri)
            publishProgress(index + 1)
        }
        return files
    }

    private fun doCopy(uri: Uri): File {
        val context = weakContext.get() ?: throw IllegalStateException("context is null")
        val inStream = context.contentResolver.openInputStream(uri)
        inStream ?: throw IllegalStateException("inStream is null")
        val outFileNameMD5 = getMD5(uri) + "." + getMimeType(context!!, uri)
        val outFile = File(outDir, outFileNameMD5)
        if (!outFile.exists()) {
            val outFileTmp = File(outDir, "$outFileNameMD5.tmp")
            val outStream = FileOutputStream(outFileTmp)
            var size = 0
            val bytes = ByteArray(1024)
            do {
                val length = inStream.read(bytes)
                if (length != -1) {
                    outStream.write(bytes, 0, length)
                }
                size = length
            } while (size != -1)
            outStream.flush()
            outStream.close()
            inStream.close()

            outFileTmp.renameTo(outFile)
        }
        return outFile
    }

    private fun getMD5(uri: Uri): String {
        val md5 = MessageDigest.getInstance("MD5")
        val result = md5.digest(uri.toString().toByteArray(Charsets.UTF_8))
        val sb = StringBuilder()
        for (b in result) sb.append(String.format("%02x", b and 0xFF.toByte()))
        return sb.toString()
    }

    private fun getMimeType(context: Context, uri: Uri): String? {
        val extension: String
        //Check uri format to avoid null
        extension = if (uri.scheme == ContentResolver.SCHEME_CONTENT) { //If scheme is a content
            val mime: MimeTypeMap = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else { //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        }
        return extension
    }

    override fun onPostExecute(result: ArrayMap<Uri, File>?) {
        super.onPostExecute(result)
        callback?.onPostExecute(result!!)
        release()
    }

    override fun onCancelled() {
        super.onCancelled()
        callback?.onCancel()
        release()
    }

    override fun onCancelled(result: ArrayMap<Uri, File>?) {
        super.onCancelled(result)
        callback?.onCancel()
        release()
    }

    private fun release() {
        weakContext.clear()
        callback = null
    }

    interface Callback {
        fun onProgress(progress: Int)
        fun onPostExecute(map: ArrayMap<Uri, File>)
        fun onCancel()
    }
}