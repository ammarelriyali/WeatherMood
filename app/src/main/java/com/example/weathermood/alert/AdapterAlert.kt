package com.example.weathermood.alert

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermood.databinding.ItemRvAlertBinding
import com.example.weathermood.model.MyAlert
import java.text.SimpleDateFormat
import java.util.*

class AdapterAlert(
    var data: List<MyAlert> = listOf(),
    private val onDelete: (MyAlert) -> Unit,
) : RecyclerView.Adapter<AdapterAlert.ViewHolder>() {
    lateinit var context: Context

    inner class ViewHolder(val binding: ItemRvAlertBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    fun setList(data: List<MyAlert>) {
        this.data = data
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: ItemRvAlertBinding =
            ItemRvAlertBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvLocationAlert.text=data[position].city
        holder.binding.tvEventAlert.text=data[position].event
        holder.binding.tvFromTime.text=getDate(data[position].dateForm)+" "+data[position].hourFrom+":"+data[position].minuteFrom
        holder.binding.tvToTime.text=getDate(data[position].dateTo)+" "+data[position].hourTo+":"+data[position].minuteTo
        holder.binding.ibDeleteAlert.setOnClickListener(){onDelete(data[position])}
        holder.binding.tvAlertType.text=data[position].typeOfAlert
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
}
