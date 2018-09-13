package com.github.boybeak.picker

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore

object UriParser {

    private val TAG = UriParser::class.java.simpleName

    private fun isDocumentContentUri(uri: Uri): Boolean {
        return "content".equals(uri.scheme, true)
                && "com.android.providers.media.documents".equals(uri.authority, true)
    }

    fun getRealPath(context: Context, uri: Uri, mime: String): String? {
        if (isDocumentContentUri(uri)) {
            return getContentDocumentRealPath(context, uri)
        }
        return getContentRealPath(context, uri, mime)
    }

    private fun getContentDocumentRealPath(context: Context, uri: Uri): String {
        val wholeID = DocumentsContract.getDocumentId(uri)
        val typeAndId = wholeID.split(":")
        val type = typeAndId[0]
        val id = typeAndId.dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        var column: Array<String>?
        var sel: String?
        when(type) {
            "image" -> {
                column = arrayOf(MediaStore.Images.Media.DATA)
                sel = MediaStore.Images.Media._ID + "=?"
            }
            "video" -> {
                column = arrayOf(MediaStore.Video.Media.DATA)
                sel = MediaStore.Video.Media._ID + "=?"
            }
            else -> {
                column = arrayOf(MediaStore.Images.Media.DATA)
                sel = MediaStore.Images.Media._ID + "=?"
            }
        }
        val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null)
        var filePath = ""
        val columnIndex = cursor!!.getColumnIndex(column[0])
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return filePath
    }

    private fun getContentRealPath(context: Context, uri: Uri, mime: String): String {
        var result: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.getPath()
        } else {
            cursor.moveToFirst()
            var idx = 0
            if (mime.contains(Picker.IMAGE)) {
                idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            } else if (mime.contains(Picker.VIDEO)) {
                idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
            }
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

}
