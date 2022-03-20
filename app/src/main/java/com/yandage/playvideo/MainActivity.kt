package com.yandage.playvideo

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import java.net.URLConnection
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"
    private var videos = ArrayList<String>()
    lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)

        getVideos()

        videoView.setOnCompletionListener { playVideo() }

        startService(Intent(this, StatusReportService::class.java))
    }

    private fun getVideos() {
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val files = downloadDir.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isFile && isVideoFile(file.toString())) {
                    this.videos.add(file.toString())
                }
                Log.d(tag, file.toString())
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        getVideos()
    }

    override fun onStart() {
        super.onStart()
        playVideo()
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
    }

    override fun onResume() {
        super.onResume()
        videoView.resume()
    }

    private fun isVideoFile(path: String): Boolean {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(path)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)

//        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("video")
    }

    private fun playVideo() {
        val randIndex = Random.nextInt(0, videos.size)
        Log.d(tag, videos[randIndex])
        videoView.setVideoPath(videos[randIndex])
        videoView.start()
    }
}