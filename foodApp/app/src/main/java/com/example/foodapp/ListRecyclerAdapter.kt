package com.example.foodapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.RecyclerRowBinding

class ListRecyclerAdapter(val foodList: ArrayList<String>, val idList: ArrayList<Int>) : RecyclerView.Adapter<ListRecyclerAdapter.FoodHolder>() {

    class FoodHolder(private val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(foodName: String) {
            binding.recyclerRowText.text = foodName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding  = RecyclerRowBinding.inflate(inflater, parent, false)
        return FoodHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val foodName = foodList[position]
        holder.bind(foodName)

        holder.itemView.setOnClickListener {
            // click proccess
        }

    }
}
