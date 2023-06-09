package com.example.weathermood.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weathermood.R
import com.example.weathermood.databinding.ItemRvDaliyBinding
import com.example.weathermood.model.Daily
import com.example.weathermood.shareperf.MySharedPreference
import com.example.weathermood.utilities.Helper
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter (
    private var daily: List<Daily> = listOf(),
) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    private val TAG: String? = "TAGG"
    lateinit var context: Context


    inner class ViewHolder(val binding: ItemRvDaliyBinding) : RecyclerView.ViewHolder(binding.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyAdapter.ViewHolder {
        context = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: ItemRvDaliyBinding = ItemRvDaliyBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyAdapter.ViewHolder, position: Int) {
        if (position==0)
            holder.binding.tvDaliyHeader.text=context.getString(R.string.today)
        else
            holder.binding.tvDaliyHeader.text=getDate(daily[position].dt)
        holder.binding.tvDaily.text=daily[position].weather[0].description
        holder.binding.tvTempDaliy.text=daily[position].temp.day.toString()+ '\u00B0'.toString() +  when(MySharedPreference.getUnits()){
            "metric" -> "C"
            "metric" -> "F"
            else -> "K"
        }
        Glide.with(context).load(AppCompatResources.getDrawable(context,Helper.image.get(daily[position].weather[0].icon)!!))
            .error(AppCompatResources.getDrawable(context,com.example.weathermood.R.drawable.twotone_error_24)).into(holder.binding.ivDaily)

    }

    override fun getItemCount(): Int =daily.size
    private fun getDate(s: Long): String? {
        try {
            val sdf = SimpleDateFormat("EEEE")
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun setData(daily: List<Daily>) {
        this.daily=daily
        notifyDataSetChanged()
    }
}