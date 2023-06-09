package com.example.weathermood.favourite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermood.databinding.ItemRvFavouriteBinding
import com.example.weathermood.model.FavouriteLocation

class FavouriteAdapter(
    var data: List<FavouriteLocation> = listOf(),
    private val onDelete: (FavouriteLocation) -> Unit,
    private val onCLick: (FavouriteLocation) -> Unit
) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteAdapter.ViewHolder {
        context = parent.context
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: ItemRvFavouriteBinding =
            ItemRvFavouriteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FavouriteAdapter.ViewHolder, position: Int) {
        val favItem = data.get(position)
        holder.binding.tvLocationFav.text =
            if (favItem.city.contains(",")) favItem.city.split(",").last() else favItem.city
        holder.binding.tvAddressFav.text = favItem.city
        holder.binding.ibDeleteFav.setOnClickListener() { onDelete(favItem) }
        holder.binding.cvCvFav.setOnClickListener() { onCLick(favItem) }
    }

    inner class ViewHolder(val binding: ItemRvFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun setList(data: List<FavouriteLocation>) {
        this.data = data
        notifyDataSetChanged()
    }
}
