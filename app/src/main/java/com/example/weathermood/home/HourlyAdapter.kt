package com.example.weathermood.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weathermood.databinding.ItemRvDaliyBinding
import com.example.weathermood.databinding.ItemRvHourlyBinding
import com.example.weathermood.model.Daily
import com.example.weathermood.model.Hourly
import com.example.weathermood.utilities.Helper
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter(private var hourly: List<Hourly> = listOf<Hourly>(),
) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
    lateinit var context: Context

    inner class ViewHolder(val binding: ItemRvHourlyBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyAdapter.ViewHolder {
        context = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: ItemRvHourlyBinding = ItemRvHourlyBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =hourly.size

    override fun onBindViewHolder(holder: HourlyAdapter.ViewHolder, position: Int) {
        holder.binding.tvTimeHourly.text=getDate(hourly[position].dt)
        holder.binding.tvTempHourly.text=hourly[position].temp.toString()+ '\u00B0'.toString()
        Glide.with(context).load(context.getDrawable(Helper.image.get(hourly[position].weather[0].icon)!!))
            .error(context.getDrawable(com.example.weathermood.R.drawable.twotone_error_24)).into(holder.binding.ivHourly)

    }

        fun setData(hourly: List<Hourly>) {
            this.hourly=hourly
            notifyDataSetChanged()
        }
    private fun getDate(s: Long): String? {
        try {
            val sdf = SimpleDateFormat("hh:mm a")
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

}
