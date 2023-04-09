package com.example.weathermood.alert


import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.mvvm.DB.LocalDataClass
import com.example.mvvm.retroit.RemotelyDataSource
import com.example.weathermood.alert.mvvm.repository.RepositoryAlert
import com.example.weathermood.model.AlertModel
import kotlinx.coroutines.flow.catch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
        Log.i("TAG", "getData: -----------------${alert.lat}  ${alert.log}")
        repository.getAlert(alert.lat.toString(), alert.log.toString()).catch {
            Log.i(
                "TAG",
                "getData: ${it.message}"
            )
        }.collect() {
            Log.i("TAG", "getData: -----------------1$id")
            if (it.isSuccessful) {
                Log.i("TAG", "getData: -----------------2$id ${checkTimeLimit(alert)}")
                if (checkTimeLimit(alert)) {
                    Log.i("TAG", "getData: -----------------3$id")
                    val delay: Long = getDelay(alert)
                    if (it.body()?.alerts.isNullOrEmpty()) {
                        setOneTimeWorkManger(
                            delay,
                            alert.id,
                            it.body()?.current?.weather?.get(0)?.description ?: "",
                        )
                    } else {
                        setOneTimeWorkManger(
                            delay,
                            alert.id,
                            it?.body()?.alerts?.get(0)?.tags?.get(0) ?: "",
                        )
                    }
                } else {
                    Log.i("TAG", "getData: -----------------4$id")
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

    private fun setOneTimeWorkManger(delay: Long, id: Int?, description: String) {
        Log.i("TAG", "getData: -----------------5$id")
        val data = Data.Builder()
        data.putString("description", description)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            AlertOneTimeWorkManger::class.java,
        )
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
    }

    private fun getDelay(alert: AlertModel): Long {
        val hour =
            TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute =
            TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return (alert.minuteFrom + alert.hourFrom) - ((hour + minute) - (2 * 3600L))
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


}