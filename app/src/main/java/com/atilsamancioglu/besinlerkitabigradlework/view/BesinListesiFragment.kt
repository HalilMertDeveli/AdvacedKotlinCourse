package com.atilsamancioglu.besinlerkitabigradlework.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.atilsamancioglu.besinlerkitabigradlework.R
import com.atilsamancioglu.besinlerkitabigradlework.adapter.FoodRecyclerAdapter
import com.atilsamancioglu.besinlerkitabigradlework.databinding.FragmentBesinListesiBinding
import com.atilsamancioglu.besinlerkitabigradlework.viewModel.FoodListViewModel

class BesinListesiFragment : Fragment() {
    private lateinit var viewModel: FoodListViewModel
    private var foodRecyclerAdapter = FoodRecyclerAdapter(arrayListOf())
    private var _binding: FragmentBesinListesiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBesinListesiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FoodListViewModel::class.java)
        viewModel.refreshData()

        binding.besinListRecycler.layoutManager = LinearLayoutManager(context)
        binding.besinListRecycler.adapter = foodRecyclerAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.besinYukleniyor.visibility = View.VISIBLE
            binding.besinHataMesaji.visibility = View.GONE
            binding.besinListRecycler.visibility = View.GONE

            viewModel.refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    fun observeLiveData() {
        viewModel.foodList.observe(viewLifecycleOwner, Observer { foodList ->
            foodList?.let {
                binding.besinListRecycler.visibility = View.VISIBLE
                foodRecyclerAdapter.updateFoodList(it)
            }
        })
        viewModel.foodErrorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                binding.besinHataMesaji.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        viewModel.foodIsLoading.observe(viewLifecycleOwner, Observer { isLoadingCircular ->
            isLoadingCircular?.let {
                binding.besinYukleniyor.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}