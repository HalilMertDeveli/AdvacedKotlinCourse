package com.atilsamancioglu.besinlerkitabigradlework.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.atilsamancioglu.besinlerkitabigradlework.databinding.FragmentBesinDetayiBinding
import com.atilsamancioglu.besinlerkitabigradlework.util.downloadImage
import com.atilsamancioglu.besinlerkitabigradlework.util.makePlaceHolder
import com.atilsamancioglu.besinlerkitabigradlework.viewModel.FoodDetailViewModel

class BesinDetayiFragment : Fragment() {
    private lateinit var foodDetailViewModelInstance: FoodDetailViewModel
    private var besinId = 0
    private var _binding: FragmentBesinDetayiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBesinDetayiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodDetailViewModelInstance = ViewModelProvider(this).get(FoodDetailViewModel::class.java)

        arguments?.let {
            besinId = BesinDetayiFragmentArgs.fromBundle(it).besinId
        }
        
        foodDetailViewModelInstance.getDataFromRoom(besinId)
        observeLiveData()
    }

    fun observeLiveData() {
        foodDetailViewModelInstance.foodLiveData.observe(viewLifecycleOwner, Observer { food ->
            food?.let {
                binding.besinIsim.text = it.foodName
                binding.besinKalori.text = it.foodCalorie
                binding.besinKarbonhidrat.text = it.foodCarbohydrate
                binding.besinProtein.text = it.foodProtein
                binding.besinyag.text = it.foodOil
                context?.let { context ->
                    binding.besinImage.downloadImage(it.foodImageUrl, makePlaceHolder(context))
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
