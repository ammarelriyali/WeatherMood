package com.example.weathermood.favourite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermood.databinding.ItemRvDaliyBinding
import com.example.weathermood.databinding.ItemRvFavouriteBinding
import com.example.weathermood.model.FavouriteLocation

class FavouriteAdapter (val data: List<FavouriteLocation> = listOf()): RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteAdapter.ViewHolder {
         context = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: ItemRvFavouriteBinding = ItemRvFavouriteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =data.size

    override fun onBindViewHolder(holder: FavouriteAdapter.ViewHolder, position: Int) {

    }
    inner class ViewHolder(val binding: ItemRvFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    fun setData(data:List<FavouriteLocation>){

    }
}
