package com.atilsamancioglu.besinlerkitabigradlework.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.atilsamancioglu.besinlerkitabigradlework.model.Food
import com.atilsamancioglu.besinlerkitabigradlework.service.FoodDatabase
import kotlinx.coroutines.launch

class FoodDetailViewModel(application: Application) : BaseViewModel(application) {
    val foodLiveData = MutableLiveData<Food>()

    fun getDataFromRoom(uuid: Int) {
        launch {
            val dao = FoodDatabase(getApplication()).foodDao()
            val food = dao.getFood(uuid)
            foodLiveData.value = food
        }
    }
}
