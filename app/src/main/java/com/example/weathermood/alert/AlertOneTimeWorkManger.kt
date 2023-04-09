package com.example.weathermood.alert

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startForegroundService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weathermood.R

private const val CHANNEL_ID = 2

class AlertOneTimeWorkManger(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        val description = inputData.getString("description")!!
        val type = inputData.getString("type")!!

        if (type=="alert")
            startAlertService(description)
        else{
            notificationChannel()
            with(NotificationManagerCompat.from(context)) {
                val notificationId = 0
                notify(notificationId, createNotification(description,context).build())
            }
        }
        return Result.success()
    }
    private fun createNotification(description: String,context: Context) = NotificationCompat.Builder(applicationContext, "$CHANNEL_ID")
        .setSmallIcon(R.drawable.twotone_notifications_24)
        .setContentText(description)
        .setContentTitle(context.getString(R.string.header_notification))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(description)
        )
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        .setAutoCancel(true)

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: String = context.getString(R.string.channel_name)
            val description = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("$CHANNEL_ID",name, importance)
            channel.enableVibration(true)
            channel.description = description
            val notificationManager:NotificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
    private fun startAlertService(description: String) {
        val intent = Intent(applicationContext, AlertService::class.java)
        intent.putExtra("description", description)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(applicationContext, intent)
        } else {
            applicationContext.startService(intent)
        }
    }
}