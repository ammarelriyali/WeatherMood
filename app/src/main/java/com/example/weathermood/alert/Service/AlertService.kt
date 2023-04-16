package com.example.weathermood.alert.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.weathermood.MainActivity
import com.example.weathermood.R


private const val CHANNEL_ID = 1
private const val FOREGROUND_ID = 2

class AlertService : Service() {


    private var notificationManager: NotificationManager? = null
    var alertWindowManger: AlertView? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val description = intent?.getStringExtra("description")

        notificationChannel()
        startForeground(FOREGROUND_ID, makeNotification(description!!))

        if (Settings.canDrawOverlays(this)) {
            alertWindowManger = AlertView(this, description)
            alertWindowManger!!.initializeWindowManager()
        }
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


    private fun makeNotification(description: String): Notification {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


        return NotificationCompat.Builder(
            applicationContext,
            "${CHANNEL_ID}"
        )
            .setSmallIcon(com.example.weathermood.R.drawable.twotone_notifications_24)
            .setContentText(description)
            .setContentTitle(applicationContext.getString(com.example.weathermood.R.string.header_notification))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            ).setSound(
                Uri.parse(
                    ("android.resource://"
                            + applicationContext.packageName) + "/" + R.raw.alert
                ), AudioManager.STREAM_NOTIFICATION
            )
            .setVibrate(longArrayOf(200, 200, 200, 200, 200))
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .build()

    }

    private fun notificationChannel() {
        val soundUri: Uri = Uri.parse(
            "android.resource://" +
                    applicationContext.packageName +
                    "/" +
                    com.example.weathermood.R.raw.alert
        )

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: String = getString(com.example.weathermood.R.string.channel_name)
            val description = getString(com.example.weathermood.R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel("${CHANNEL_ID}", name, importance)
            channel.enableVibration(true)
            channel.description = description
            channel.setSound(soundUri,audioAttributes)
            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}