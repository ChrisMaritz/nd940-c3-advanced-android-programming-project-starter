package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var title : String
    var url: String? = null
    var notificationManager : NotificationManager? = null
    var completeOrNot = false
    var status1 = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager

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
            if(url == null){
                Toast.makeText(applicationContext, "Please select link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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
                completeOrNot = true
                custom_button.buttonState = ButtonState.Completed
                status1 = "complete"
                createChannel(
                    getString(R.string.notification_complete),
                    "download complete"
                )
                notificationManager?.sendNotification("Download done", this@MainActivity)
                Log.i("Download", "Hello")
            }else if(status == DownloadManager.STATUS_FAILED){
                completeOrNot = false
                Log.i("Download", "Failed")
                status1 = "failed"
                custom_button.buttonState = ButtonState.Failed
                notificationManager?.sendNotification("Download failed", this@MainActivity)
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
            downloadManager.enqueue(request)


    }

    private fun createChannel(channelId : String, channelName : String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download Complete"

            val notificationManager = this@MainActivity.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }

    }

    fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context){
        val contentIntent = Intent(applicationContext, DetailActivity::class.java)

        contentIntent.putExtra("STATUS_HERE", status1)
        contentIntent.putExtra("TITLE", title)
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.notification_complete)
        )
            .setContentTitle(applicationContext
                .getString(R.string.notification_title))
            .setContentText(messageBody)
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setAutoCancel(true)
            .setColor(resources.getColor(R.color.colorAccent))
            .setContentText("Expand to open details button")
            .addAction(R.drawable.abc_vector_test, "Details", contentPendingIntent)
        notify(1, builder.build())
    }



}
