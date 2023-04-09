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
        repository.getAlert(alert.lat.toString(), alert.log.toString()).catch {
            Log.i(
                "TAG",
                "getData: ${it.message}"
            )
        }.collect() {
            if (it.isSuccessful) {
                if (checkTimeLimit(alert)) {
                    if (it.body()?.alerts.isNullOrEmpty()) {
                        setOneTimeWorkManger(
                            alert.id,
                            it.body()?.current?.weather?.get(0)?.description ?: "",
                            alert.typeOfAlert,
                        )
                    } else {
                        val x =it?.body()?.alerts?.get(0)?.tags?.filter { it==alert.event }
                        setOneTimeWorkManger(
                            alert.id,
                            (if (x.isNullOrEmpty()) x?.get(0) else it.body()?.current?.weather?.get(0)?.description ?: "")!!,
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

    private fun setOneTimeWorkManger(id: Int?, description: String, typeOfAlert: String) {
        val data = Data.Builder()
        data.putString("description", description)
        data.putString("type", typeOfAlert)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            AlertOneTimeWorkManger::class.java,
        )
            .setInitialDelay(0, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
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