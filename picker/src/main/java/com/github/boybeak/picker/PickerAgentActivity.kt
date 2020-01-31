package com.github.boybeak.picker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.ArrayMap
import android.util.Log
import java.io.File


class PickerAgentActivity : AppCompatActivity() {

    companion object {

        private val TAG = PickerAgentActivity::class.java.simpleName!!

        private const val REQUEST_CODE_GALLERY = 100
        private const val REQUEST_CODE_CAMERA = 200
    }

    private var id: String? = null
    private var mode: Int = 0
    private var multipleSelect: Boolean = false
    private var mimeType: String? = null

    /*private var requestCode: Int = 0
    private var resultCode: Int = RESULT_CANCELED
    private var data: Intent? = null*/

    private var cameraTempUri: Uri? = null
    private var cameraTempFile: File? = null

    private val multipleResultUris = ArrayList<Triple>()

    private lateinit var copyTask: CopyTask
    private val copyCallback = object : CopyTask.Callback {
        override fun onProgress(progress: Int) {

        }

        override fun onPostExecute(map: ArrayMap<Uri, File>) {
            if (multipleSelect) {
                multipleResultUris.forEach {
                    if (it.file == null) {
                        it.file = map[it.uri]
                    }
                }
                commitMultiple()
//                Picker.actionCallback(id, multipleResultUris, files)
            } else {
                val uri = map.keyAt(0)
                val file = map.valueAt(0)
                if (file.exists()) {
                    Picker.actionCallback(id, uri, file, false)
                } else {
                    Picker.cancelCallback(id)
                }
                finish()
            }
        }

        override fun onCancel() {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_agent)

        copyTask = CopyTask(this, copyCallback)

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

        /*this.requestCode = requestCode
        this.resultCode = resultCode
        this.data = data*/

        if (resultCode == RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE_GALLERY -> {
                    if (data == null) {
                        Picker.cancelCallback(id)
                        return
                    }
                    if (multipleSelect) {

                        val uriList = ArrayList<Uri>()

                        if (data.data != null) {
                            val uri = data.data
                            uriList.add(uri)
                        } else {
                            if (data.clipData != null) {
                                val mClipData = data.clipData
                                for (i in 0 until mClipData.itemCount) {
                                    val item = mClipData.getItemAt(i)
                                    val uri = item.uri
                                    uriList.add(uri)
                                }
                            }
                        }
                        if (uriList.isEmpty()) {
                            Picker.cancelCallback(id)
                            finish()
                        } else {
                            val noRealPathUris = ArrayList<Uri>()
                            for (uri in uriList) {
                                val realPath = UriParser.getRealPath(this, uri, mimeType!!)
                                Log.v(TAG, "onActivityResult REALPATH=$realPath")
                                if (TextUtils.isEmpty(realPath) || !File(realPath).exists()) {
                                    multipleResultUris.add(Triple(uri, null, false))
                                    noRealPathUris.add(uri)
                                } else {
                                    multipleResultUris.add(Triple(uri, File(realPath), true))
                                }
                            }

                            if (noRealPathUris.isEmpty()) {
                                commitMultiple()
                            } else {
                                val uriArray = arrayOfNulls<Uri>(noRealPathUris.size)
                                noRealPathUris.toArray(uriArray)
                                copyTask.execute(*uriArray)
                            }
                        }
                    } else {
                        val uri = data.data!!
                        val realPath = UriParser.getRealPath(this, uri, mimeType!!)
                        if (TextUtils.isEmpty(realPath) || !File(realPath).exists()) {
                            copyTask.execute(uri)
                        } else {
                            Picker.actionCallback(id, uri, File(realPath), true)
                            finish()
                        }

                    }

                }
                REQUEST_CODE_CAMERA -> {
                    Picker.actionCallback(id, cameraTempUri, cameraTempFile, true)
                    finish()
                }
            }
        } else {
            Picker.cancelCallback(id)
            finish()
        }
    }

    private fun commitMultiple() {
        val uris = ArrayList<Uri>()
        val files = ArrayList<File>()
        val matches = ArrayList<Boolean>()

        multipleResultUris.forEach {
            uris.add(it.uri)
            files.add(it.file!!)
            matches.add(it.isMatch)
        }

        Picker.actionCallback(id, uris, files, matches)
        finish()
    }

    inner class Triple(val uri: Uri, var file: File?, val isMatch: Boolean)

}
