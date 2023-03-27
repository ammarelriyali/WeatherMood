package com.example.weathermood.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermood.databinding.ItemRvDaliyBinding
import com.example.weathermood.model.Daily
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter (
    private var daily: List<Daily> = listOf<Daily>(),
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
        holder.binding.tvDaliyHeader.text=getDate(daily[position].dt)
        holder.binding.tvDaily.text=daily[0].weather[0].description
        holder.binding.tvTempDaliy.text=daily[0].temp.day.toString()
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    private fun getDate(s: Long): String? {
        try {
            Log.i(TAG, "getDate: " + s)
            val sdf = SimpleDateFormat("FFFF")
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