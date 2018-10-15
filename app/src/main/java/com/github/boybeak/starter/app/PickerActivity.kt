package com.github.boybeak.starter.app

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.*
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_picker.*
import java.io.File
import android.net.Uri
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import com.bumptech.glide.request.RequestOptions
import com.github.boybeak.permission.Callback
import com.github.boybeak.permission.PH
import com.github.boybeak.picker.*
import com.github.boybeak.starter.activity.de.DragExitToolbarActivity
import com.github.boybeak.adapter.extension.Footer
import com.github.boybeak.starter.app.adapter.FileImpl
import com.github.boybeak.starter.widget.BorderDecoration


class PickerActivity : DragExitToolbarActivity() {

    private var adapter: com.github.boybeak.adapter.DataBindingAdapter? = null

    private val footerEvent = object : FooterEvent {
        override fun onFooterClick() {
            AlertDialog.Builder(this@PickerActivity)
                    .setItems(R.array.get_image, object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            when(which) {
                                0 -> {
                                    Picker.gallery().image().multiple(true).go(this@PickerActivity, object : MultipleCallback {
                                        override fun onGet(id: String, uris: MutableList<Uri>, files: MutableList<File>) {
                                            adapter!!.addAll(files, com.github.boybeak.adapter.Converter<File, FileImpl> { data, _ -> FileImpl(data) }).autoNotify()
                                        }

                                        override fun onCancel(id: String) {

                                        }

                                    })
                                }
                                1 -> {
                                    val dir = File(externalCacheDir, "images")
                                    if (!dir.exists()) {
                                        dir.mkdirs()
                                    }

                                    val cameraTempFile = File(dir, System.currentTimeMillis().toString() + ".jpg")
                                    val uri = FileProvider.getUriForFile(this@PickerActivity, "$packageName.provider", cameraTempFile)
                                    Picker.camera().image().output(uri, cameraTempFile).go(this@PickerActivity, object : SingleCallback {
                                        override fun onGet(id: String, uri: Uri, file: File) {
                                            adapter!!.add(FileImpl(file)).autoNotify()
                                        }

                                        override fun onCancel(id: String) {

                                        }

                                    })
                                }
                            }
                        }

                    })
                    .show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)

        adapter = com.github.boybeak.adapter.DataBindingAdapter(this)
        val glm = recycler_view.layoutManager as GridLayoutManager
        val lookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position == 0) {
                    return glm.spanCount
                }
                return 1
            }

            override fun isSpanIndexCacheEnabled(): Boolean {
                return true
            }

        }
        recycler_view.adapter = adapter
        glm.spanSizeLookup = lookup
        recycler_view.addItemDecoration(BorderDecoration(24))

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)

        showProfile("")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_picker, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId) {
            R.id.picker_get_single_image -> {
                getSingleImage()
            }
            R.id.picker_get_multiple_image -> {
                getMultipleImages()
            }
            R.id.picker_get_single_video -> {
                getSingleVideo()
            }
            R.id.picker_get_multiple_videos -> {
                getMultipleVideos()
            }
            R.id.picker_capture_image -> {
                captureImage()
            }
            R.id.picker_capture_video -> {
                captureVideo()
            }
        }
        return true
    }

    private fun showProfile(file: File) {
        Glide.with(this).load(file).apply(
                RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.trump)
        ).into(profile)
    }

    private fun showProfile(url: String) {
        Glide.with(this).load(url).apply(
                RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.trump)
        ).into(profile)
    }

    fun changeProfile(view: View) {

        val callback = object : SingleCallback {
            override fun onGet(id: String, uri: Uri, file: File) {
                showProfile(file)
                name.text = file.absolutePath
            }

            override fun onCancel(id: String) {
                name.text = ""
            }

        }
        AlertDialog.Builder(this)
                .setItems(R.array.get_image, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when(which) {
                            0 -> {
                                Picker.gallery().go(this@PickerActivity, callback)
                            }
                            1 -> {
                                PH.ask(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .go(this@PickerActivity, object : Callback {
                                            override fun onGranted(permissions: MutableList<String>) {
                                                val dir = File(externalCacheDir, "images")
                                                if (!dir.exists()) {
                                                    dir.mkdirs()
                                                }

                                                val cameraTempFile = File(dir, System.currentTimeMillis().toString() + ".jpg")
                                                val uri = FileProvider.getUriForFile(this@PickerActivity, "$packageName.provider", cameraTempFile)
                                                Picker.camera().output(uri, cameraTempFile).go(this@PickerActivity, callback)
                                            }

                                            override fun onDenied(permission: String) {

                                            }

                                        })

                            }
                        }
                    }

                })
                .show()
    }

    private fun getSingleImage() {
        Picker.gallery().image().go(this, object : SingleCallback {
            override fun onGet(id: String, uri: Uri, file: File) {
                adapter!!.add(FileImpl(file)).autoNotify()
            }

            override fun onCancel(id: String) {

            }

        })
    }

    private fun getMultipleImages() {
        Picker.gallery().image().multiple(true).go(this, object : MultipleCallback {
            override fun onGet(id: String, uris: MutableList<Uri>, files: MutableList<File>) {
                adapter!!.addAll(files, object : com.github.boybeak.adapter.Converter<File, FileImpl> {
                    override fun convert(data: File?, adapter: com.github.boybeak.adapter.DataBindingAdapter): FileImpl {
                        return FileImpl(data)
                    }

                }).autoNotify()
            }

            override fun onCancel(id: String) {

            }

        })
    }

    private fun getSingleVideo() {
        Picker.gallery().video().go(this, object : SingleCallback {
            override fun onGet(id: String, uri: Uri, file: File) {
                Toast.makeText(this@PickerActivity, file.absolutePath, Toast.LENGTH_SHORT).show()
            }

            override fun onCancel(id: String) {

            }

        })
    }

    private fun getMultipleVideos() {
        Picker.gallery().video().multiple(true).go(this, object : MultipleCallback {
            override fun onCancel(id: String) {

            }

            override fun onGet(id: String, uris: MutableList<Uri>, files: MutableList<File>) {

            }

        })
    }

    private fun captureImage() {
        val dir = File(externalCacheDir, "images")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val cameraTempFile = File(dir, System.currentTimeMillis().toString() + ".jpg")
        val uri = FileProvider.getUriForFile(this, "$packageName.provider", cameraTempFile)

//        val cameraTempFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "${System.currentTimeMillis()}.jpg")

        Picker.camera().image().output(uri, cameraTempFile).go(this, object : SingleCallback {
            override fun onGet(id: String, uri: Uri, file: File) {

            }

            override fun onCancel(id: String) {

            }

        })
    }

    private fun captureVideo() {
        val dir = File(externalCacheDir, "videos")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val cameraTempFile = File(dir, System.currentTimeMillis().toString() + ".mp4")
        val uri = FileProvider.getUriForFile(this, "$packageName.provider", cameraTempFile)
        Picker.camera().video().durationLimit(10).output(uri, cameraTempFile).go(this, object : SingleCallback {
            override fun onGet(id: String, uri: Uri, file: File) {
                Toast.makeText(this@PickerActivity, file.absolutePath, Toast.LENGTH_SHORT).show()
            }

            override fun onCancel(id: String) {

            }

        })
    }

}
