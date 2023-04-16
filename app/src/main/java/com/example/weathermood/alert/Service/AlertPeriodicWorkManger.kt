package com.example.weathermood.alert.Service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.mvvm.DB.LocalDataClass
import com.example.mvvm.retroit.RemotelyDataSource
import com.example.weathermood.R
import com.example.weathermood.alert.mvvm.repository.RepositoryAlert
import com.example.weathermood.model.AlertModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import java.text.SimpleDateFormat
import java.util.*

private const val CHANNEL_ID = 2
class AlertPeriodicWorkManger(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {


    val repository = RepositoryAlert(LocalDataClass(context), RemotelyDataSource())

    override suspend fun doWork(): Result {
        if (!isStopped) {
            val id = inputData.getLong("id", -1)
            getData(id.toInt())
        }
        return Result.success()
    }

    private suspend fun getData(id: Int) {
        val alert = repository.getAlertDB(id)
        repository.getAlert(alert.lat.toString(), alert.log.toString()).catch {
            Log.i(
                "TAG",
                "getData: ${it.message}"
            )
        }.collect() {
            if (it.isSuccessful) {
                if (checkTimeLimit(alert)) {
                    delay(getDelay(alert))
                    if (it.body()?.alerts.isNullOrEmpty()) {
                        setOneTimeWorkManger(
                            it.body()?.current?.weather?.get(0)?.description ?: "",
                            alert.typeOfAlert,
                        )
                    } else {
                        val x = it?.body()?.alerts?.get(0)?.tags?.filter { it == alert.event }
                        setOneTimeWorkManger(

                            (if (x.isNullOrEmpty()) x?.get(0) else it.body()?.current?.weather?.get(
                                0
                            )?.description ?: "")!!,
                            alert.typeOfAlert
                        )
                    }
                } else {
                    repository.deleteAlert(alert)
                    WorkManager.getInstance().cancelAllWorkByTag("$id")
                }

            } else {
                repository.deleteAlert(alert)
                WorkManager.getInstance().cancelAllWorkByTag("$id")
                Log.i("TAG", "getData: ${it.code()} ${it.message()} ${it.body().toString()}")
            }
        }


    }

    private fun getDelay(alert: AlertModel): Long {
        val time =(alert.hourTo.toLong()*3600000+alert.minuteTo.toLong()*60000)-(alert.hourFrom.toLong()*3600000+alert.minuteFrom.toLong()*60000)
        return time

    }

    @SuppressLint("MissingPermission")
    private fun setOneTimeWorkManger(description: String, typeOfAlert: String) {
        if (typeOfAlert == "alert")
            startAlertService(description)
        else {
            notificationChannel()
            with(NotificationManagerCompat.from(context)) {
                val notificationId = 0
                notify(notificationId, createNotification(description, context).build())
            }
        }
    }


    private fun checkTimeLimit(alert: AlertModel): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = convertDateToLong(date)
        Log.i("TAG", "checkTimeLimit: ${alert.dateForm}  $dayNow  ${alert.dateTo}")
        return dayNow >= alert.dateForm!!
                &&
                dayNow <= alert.dateTo!!
    }


    private fun convertDateToLong(date: String): Long {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val timestamp: Date = simpleDateFormat.parse(date) as Date
        return timestamp.time
    }

    private fun createNotification(description: String, context: Context) =
        NotificationCompat.Builder(applicationContext, "$CHANNEL_ID")
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
            val channel = NotificationChannel("$CHANNEL_ID", name, importance)
            channel.enableVibration(true)
            channel.description = description
            val notificationManager: NotificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun startAlertService(description: String) {
        val intent = Intent(applicationContext, AlertService::class.java)
        intent.putExtra("description", description)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(applicationContext, intent)
        } else {
            applicationContext.startService(intent)
        }
    }

}