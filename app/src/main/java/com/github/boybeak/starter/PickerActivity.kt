package com.github.boybeak.starter

import android.Manifest
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_picker.*
import java.io.File
import android.net.Uri
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.github.boybeak.picker.*


class PickerActivity : AppCompatActivity() {

    private val adapter = ImageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)

        recycler_view.adapter = adapter

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)

        showProfile("")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_picker, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
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
                                val dir = File(externalCacheDir, "images")
                                if (!dir.exists()) {
                                    dir.mkdirs()
                                }

                                val cameraTempFile = File(dir, System.currentTimeMillis().toString() + ".jpg")
                                val uri = FileProvider.getUriForFile(this@PickerActivity, "$packageName.provider", cameraTempFile)
                                Picker.camera().output(uri, cameraTempFile).go(this@PickerActivity, callback)
                            }
                        }
                    }

                })
                .show()
    }

    private fun getSingleImage() {
        Picker.gallery().image().go(this, object : SingleCallback {
            override fun onGet(id: String, uri: Uri, file: File) {
                adapter.add(file)
            }

            override fun onCancel(id: String) {

            }

        })
    }

    private fun getMultipleImages() {
        Picker.gallery().image().multiple(true).go(this, object : MultipleCallback {
            override fun onGet(id: String, uris: MutableList<Uri>, files: MutableList<File>) {
                adapter.addAll(files)
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

    private class ImageHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val image = itemView.findViewById<AppCompatImageView>(R.id.image)!!
    }

    private class ImageAdapter : RecyclerView.Adapter<ImageHolder>(){

        private val files = ArrayList<File>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_image, parent, false)
            return ImageHolder(view)
        }

        override fun getItemCount(): Int {
            return files.size
        }

        override fun onBindViewHolder(holder: ImageHolder, position: Int) {
            Glide.with(holder.itemView.context).load(files[position]).into(holder.image)
        }

        fun add(file: File) {
            files.add(file)
            notifyDataSetChanged()
        }

        fun addAll(files: List<File>) {
            this.files.addAll(files)
            notifyDataSetChanged()
        }

    }
}
