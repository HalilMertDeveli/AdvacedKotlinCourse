package com.atilsamancioglu.besinlerkitabigradlework.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.atilsamancioglu.besinlerkitabigradlework.databinding.BesinRecyclerRowBinding
import com.atilsamancioglu.besinlerkitabigradlework.model.Food
import com.atilsamancioglu.besinlerkitabigradlework.util.downloadImage
import com.atilsamancioglu.besinlerkitabigradlework.util.makePlaceHolder
import com.atilsamancioglu.besinlerkitabigradlework.view.BesinListesiFragmentDirections

class FoodRecyclerAdapter(val foodList: ArrayList<Food>) : RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder>() {
    class FoodViewHolder(val binding: BesinRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = BesinRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.binding.isim.text = foodList[position].foodName
        holder.binding.kalori.text = foodList[position].foodCalorie
        
        holder.binding.imageView.downloadImage(
            foodList[position].foodImageUrl,
            makePlaceHolder(holder.itemView.context)
        )
        
        holder.itemView.setOnClickListener {
            val action = BesinListesiFragmentDirections.actionBesinListesiFragmentToBesinDetayiFragment(foodList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFoodList(newFoodList: List<Food>) {
        foodList.clear()
        foodList.addAll(newFoodList)
        notifyDataSetChanged()
    }
}
