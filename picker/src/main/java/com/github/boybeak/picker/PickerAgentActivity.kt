package com.github.boybeak.picker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import java.io.File


class PickerAgentActivity : Activity() {

    companion object {

        private val TAG = PickerAgentActivity::class.java.simpleName!!

        private const val REQUEST_CODE_GALLERY = 100
        private const val REQUEST_CODE_CAMERA = 200
    }

    private var id: String? = null
    private var mode: Int = 0
    private var multipleSelect: Boolean = false
    private var mimeType: String? = null

    private var requestCode: Int = 0
    private var resultCode: Int = RESULT_CANCELED
    private var data: Intent? = null

    private var cameraTempUri: Uri? = null
    private var cameraTempFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View(this))

        id = intent.getStringExtra(Picker.KEY_ID)
        mode = intent.getIntExtra(Picker.KEY_MODE, 0)
        when (mode) {
            Picker.MODE_GALLERY -> {
                mimeType = intent.getStringExtra(Picker.KEY_MIME_TYPE)
                multipleSelect = intent.getBooleanExtra(Picker.KEY_ALLOW_MULTIPLE, false)
                getContentFromGallery(mimeType!!, multipleSelect)
            }
            Picker.MODE_CAMERA -> {
                mimeType = intent.getStringExtra(Picker.KEY_MIME)
                val cameraTempUri: Uri = intent.getParcelableExtra(Picker.KEY_OUTPUT)
                val cameraTempFile = File(intent.getStringExtra(Picker.KEY_OUTPUT_FILE))
                val durationLimit = intent.getIntExtra(Picker.KEY_DURATION_LIMIT, -1)
                val quality = intent.getIntExtra(Picker.KEY_QUALITY, Camera.QUALITY_LOW)
                getContentFromCamera(mimeType!!, cameraTempUri, cameraTempFile, durationLimit, quality)
            }
        }

    }

    private fun getContentFromGallery(mimeType: String, multipleSelect: Boolean) {
        try {
            val it = Intent(Intent.ACTION_GET_CONTENT)
            it.type = mimeType
            it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multipleSelect)
            startActivityForResult(it, REQUEST_CODE_GALLERY)
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

    private fun getContentFromCamera(mime: String, output: Uri, file: File, durationLimit: Int, @Camera.Quality quality: Int) {
        try {

            val it = Intent(when(mime) {
                Picker.IMAGE -> {
                    MediaStore.ACTION_IMAGE_CAPTURE
                }
                Picker.VIDEO -> {
                    MediaStore.ACTION_VIDEO_CAPTURE
                }
                else -> {
                    MediaStore.ACTION_IMAGE_CAPTURE
                }
            })

            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            it.putExtra(MediaStore.EXTRA_OUTPUT, output)
            if (mime == Picker.VIDEO) {
                if (durationLimit > 0) {
                    it.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit)
                    MediaStore.EXTRA_VIDEO_QUALITY
                }
                it.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality)
            }

            startActivityForResult(it, REQUEST_CODE_CAMERA)

            cameraTempUri = output
            cameraTempFile = file
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        this.requestCode = requestCode
        this.resultCode = resultCode
        this.data = data

        Log.v(TAG, "onActivityResult $requestCode $resultCode")
        Toast.makeText(this, "onActivityResult $requestCode $resultCode", Toast.LENGTH_SHORT).show()

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (resultCode == RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE_GALLERY -> {
                    if (data == null) {
                        Picker.cancelCallback(id)
                        return
                    }
                    if (multipleSelect) {

                        val uriList = ArrayList<Uri>()
                        val fileList = ArrayList<String>()

                        if (data!!.data != null) {
                            val uri = data!!.data
                            uriList.add(uri)
                            fileList.add(Picker.getRealPathFromURI(this, uri, mimeType))
                        } else {
                            if (data!!.clipData != null) {
                                val mClipData = data!!.clipData
                                for (i in 0 until mClipData.itemCount) {
                                    val item = mClipData.getItemAt(i)
                                    val uri = item.uri
                                    uriList.add(uri)
                                    fileList.add(Picker.getRealPathFromURI(this, uri, mimeType))
                                }
                            }
                        }
                        if (uriList.isEmpty()) {
                            Picker.cancelCallback(id)
                        } else {
                            Picker.actionCallback(id, uriList, List(fileList.size){
                                File(fileList[it])
                            })
                        }
                    } else {
                        val realPath = Picker.getRealPathFromURI(this, data!!.data, mimeType)
                        if (TextUtils.isEmpty(realPath) or !File(realPath).exists()) {
                            Picker.cancelCallback(id)
                        } else {
                            Picker.actionCallback(id, data!!.data, File(realPath))
                        }

                    }

                }
                REQUEST_CODE_CAMERA -> {
                    Log.v(TAG, "$cameraTempUri ${cameraTempFile!!.absolutePath}")
                    Picker.actionCallback(id, cameraTempUri, cameraTempFile)
                }
            }
        } else {
            Picker.cancelCallback(id)
        }
    }

}
