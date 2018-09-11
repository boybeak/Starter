package com.github.boybeak.starter.app.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.boybeak.loader.LwDownloader
import com.github.boybeak.starter.app.R
import kotlinx.android.synthetic.main.activity_downloader.*
import java.io.File

class DownloaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloader)

        dwldBtn.setOnClickListener {
            LwDownloader.newInstance()
                    .from("https://wx4.sinaimg.cn/mw690/7aa71322gy1fv4v4431xsg20a00hshdu.gif")
                    .to(File(externalCacheDir, "download" + File.separator + "${System.currentTimeMillis()}.gif"))
                    .start()
        }

    }
}
