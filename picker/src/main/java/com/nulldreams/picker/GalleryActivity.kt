package com.nulldreams.picker

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_gallery.*
import android.support.v7.widget.SimpleItemAnimator
import android.view.Menu
import android.view.MenuItem

class GalleryActivity : AppCompatActivity() {

    companion object {
        val TAG = GalleryActivity::class.java.simpleName!!
    }

    private var adapter: GalleryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        setSupportActionBar(gallery_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        adapter = GalleryAdapter(this)
        gallery_recycler_view.adapter = adapter
        (gallery_recycler_view.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        gallery_recycler_view.addItemDecoration(GalleryDecoration())

        PickerManager.instance().getThumbnailsAsync(this, object : PickerManager.OnThumbnailsCallback {

            override fun onQueryStart() {

            }

            override fun onPrepared(thumbnails: List<Thumb>) {
                adapter!!.addAll(thumbnails)
            }

            override fun onGetNothing() {

            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gallery, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.gallery_finish) {
            val selectedItems = adapter!!.getSelectedItems()

            if (selectedItems.isEmpty()) {
                setResult(Activity.RESULT_CANCELED)
            } else {
                val pathList = ArrayList<String>()
                selectedItems.forEach {
                    pathList.add(it.path)
                }
                val it = Intent()
                when(PickerManager.instance().getChoiceMode()) {
                    PickerManager.MODE_SINGLE -> {
                        it.putExtra("path", pathList[0])
                    }
                    PickerManager.MODE_MULTIPLE -> {
                        it.putExtra("paths", pathList)
                    }
                }

                setResult(Activity.RESULT_OK, it)
            }
            finish()
            return true
        } else if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }

}
