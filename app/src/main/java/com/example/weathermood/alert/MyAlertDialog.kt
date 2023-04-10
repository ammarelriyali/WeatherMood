package com.example.weathermood.alert

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.example.mvvm.DB.LocalDataClass
import com.example.weathermood.R
import com.example.weathermood.databinding.FragmentMyAlertBinding
import com.example.weathermood.model.AlertModel
import com.example.weathermood.model.OneCallHome
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MyAlertDialog : DialogFragment() {
    lateinit var _binding:FragmentMyAlertBinding
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var dateForm= 0L
    var hourFrom = 0
    var minuteFrom = 0
    var dateTo = 0L
    var hourTo = 0
    var minuteTo = 0
    var event ="Rain"
    var typeOfAlert="alert"
    lateinit var current :OneCallHome
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyAlertBinding.inflate(inflater)
        val local = LocalDataClass(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {    local.getCall().collect(){
            current=it.get(0)
        } }
        binding.bDeleteAlertDialog.setOnClickListener(){
            dismiss()
        }

        ArrayAdapter.createFromResource(
            requireContext()  ,
            R.array.events_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sAlertType.adapter = adapter
        }

        binding.sAlertType.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                event= resources.getStringArray(R.array.events_options)[position]
                Log.i("TAG", "onCreateView: $event")
            }

        }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            if (i==R.id.rb_alert)
                typeOfAlert="alert"
            else
                typeOfAlert="notification"
        }

        binding.bSaveAlertDialog.setOnClickListener(){
            if (dateForm==0L||dateTo==0L||hourFrom==0||hourTo==0)
                Toast.makeText(requireContext(),resources.getString(R.string.massgeErrorForEnterTimeAndDate),Toast.LENGTH_LONG).show()
            else{
                viewLifecycleOwner.lifecycleScope.launch {
                    val i =local.setAlert(AlertModel( id=0,   dateForm,
                        hourFrom,
                        minuteFrom ,
                        dateTo  ,
                        hourTo,
                        minuteTo ,
                        current.oneCall.lat,
                        current.oneCall.lon,
                        current.city,
                        event ,
                        typeOfAlert
                    ))
                    setPeriodWorkManger(i)
                    setFragmentResult("", Bundle())
                    dismiss()
                }
            }
        }

        binding.bFromDialog.setOnClickListener(){
            getTimeDateCalender()
            val picker=MaterialTimePicker.Builder()
                .setTitleText(resources.getString(R.string.selectTime))
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(hour)
                .setMinute(minute)
                .build()

            val pickerDate=
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(resources.getString(R.string.selectDate))
                .build()
            pickerDate.addOnPositiveButtonClickListener(){
                dateForm=it-18312800000L
                binding.tvTimeForm.text=getDate(it)+"  "+binding.tvTimeForm.text.toString()
            }
            picker.addOnPositiveButtonClickListener(){
                hourFrom=picker.hour
                minuteFrom=picker.minute
                binding.tvTimeForm.text=hourFrom.toString() + ":"+ minuteFrom.toString()
            }
            pickerDate.show(childFragmentManager,"date")
            picker.show(childFragmentManager,"time")
        }
        binding.bToDialog.setOnClickListener(){
            getTimeDateCalender()
            val picker=MaterialTimePicker.Builder()
                .setTitleText(resources.getString(R.string.selectTime))
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(hour)
                .setMinute(minute)
                .build()

            val pickerDate=
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(resources.getString(R.string.selectDate))
                .build()
            pickerDate.addOnPositiveButtonClickListener(){
                dateTo=it
                binding.tvTimeTo.text=getDate(it)+"  "+binding.tvTimeTo.text.toString()
            }
            picker.addOnPositiveButtonClickListener(){
                hourTo=picker.hour
                minuteTo=picker.minute
                binding.tvTimeTo.text=hourTo.toString() + ":"+ minuteTo.toString()
            }
            pickerDate.show(childFragmentManager,"date")
            picker.show(childFragmentManager,"time")
        }
        return _binding.root
    }
    private fun getTimeDateCalender() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }
    private fun getDate(s: Long): String? {
        try {
            val sdf = SimpleDateFormat("dd-MMMM")
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private fun setPeriodWorkManger(id: Long) {

        val data = Data.Builder()
        data.putLong("id", id)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            AlertPeriodicWorkManger::class.java,
            24, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "$id",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

}
