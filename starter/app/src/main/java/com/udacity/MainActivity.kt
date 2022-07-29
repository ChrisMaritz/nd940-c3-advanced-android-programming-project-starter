package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var url : String
    private lateinit var title : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        val radioButtonGlide = findViewById<View>(R.id.glide_button)
        val radioButtonRetro = findViewById<View>(R.id.retrofit_button)
        val radioButtonUdacity = findViewById<View>(R.id.loadapp_button)

        radioButtonGlide.setOnClickListener {
            url = "https://github.com/bumptech/glide"
            title = "Glide"
            Log.i("download", url.toString())
        }

        radioButtonRetro.setOnClickListener {
            url = "https://github.com/square/retrofit"
            title = "Udacity"
            Log.i("download", url.toString())
        }

        radioButtonUdacity.setOnClickListener {
            url = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
            title = "Retrofit"
            Log.i("download", url.toString())
        }



        custom_button.setOnClickListener {
            custom_button.buttonState = ButtonState.Loading
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE)
                    as DownloadManager

            val cursor = downloadManager.query(DownloadManager
                .Query()
                .setFilterById(downloadID))

            if (cursor.count == 0 || !cursor.moveToFirst()) {
                return
            }

            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

            if(status == DownloadManager.STATUS_SUCCESSFUL){
                custom_button.buttonState = ButtonState.Completed
                Log.i("Download", "Hello")
            }else if(status == DownloadManager.STATUS_FAILED){
                Log.i("Download", "Failed")
            }
        }
    }



    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(title)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.


    }

    private fun radioButton(radioButton: RadioButton){
        when(radioButton.id){

        }
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
