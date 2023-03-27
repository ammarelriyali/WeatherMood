package com.example.weathermood.home

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermood.databinding.ItemRvDaliyBinding
import com.example.weathermood.model.Daily
import com.example.weathermood.model.Hourly

class HourlyAdapter(private var hourly: List<Hourly> = listOf<Hourly>(),
) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
    private val TAG: String? = "TAGG"
    lateinit var context: Context

    inner class ViewHolder(val binding: ItemRvDaliyBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

        fun setData(hourly: List<Hourly>) {

        }

}
