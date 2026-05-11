package com.atilsamancioglu.besinlerkitabigradlework.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.atilsamancioglu.besinlerkitabigradlework.model.Food
import com.atilsamancioglu.besinlerkitabigradlework.service.FoodApiService
import com.atilsamancioglu.besinlerkitabigradlework.service.FoodDatabase
import com.atilsamancioglu.besinlerkitabigradlework.util.PrivateSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FoodListViewModel(application: Application) : BaseViewModel(application) {
    val foodList = MutableLiveData<List<Food>>()
    val foodErrorMessage = MutableLiveData<Boolean>()
    val foodIsLoading = MutableLiveData<Boolean>()
    private val privateSharedPreferencesInstance = PrivateSharedPreferences(getApplication())


    private val foodApiService = FoodApiService()//we get an instance from api service
    private val disposable =
        CompositeDisposable()//it is use and delete structure , we use this struct when we need to send request and after that delete,it comes form RXJava

    fun refreshData() {
        getDataFromEthernet()

        disposable.add(//we are adding to disposable to our composite
            foodApiService.getData()
                .subscribeOn(Schedulers.newThread())//we are subscribe to Single observable(created new thread fot schedulers)
                .observeOn(AndroidSchedulers.mainThread())//we need to observe android(we need to see in andorid main thread )
                .subscribeWith(object : DisposableSingleObserver<List<Food>>() {
                    //We use for single observable
                    override fun onSuccess(foodListInstance: List<Food>) {
                        //we the operation is success
                        saveOnSqlite(foodListInstance)


                    }

                    override fun onError(e: Throwable) {
                        //when the operation has error
                        foodIsLoading.value = false
                        foodErrorMessage.value = true
                        e.printStackTrace()


                    }

                }

                )
        )
    }

    private fun getDataFromEthernet() {
        foodIsLoading.value = true//we set the loading bar visible for loading

    }

    private fun showFoods(foodListInstance: List<Food>) {
        foodList.value = foodListInstance
        foodIsLoading.value = false
        foodErrorMessage.value = false
    }

    private fun saveOnSqlite(foodList: List<Food>) {
        launch {
            val dao = FoodDatabase(getApplication()).foodDao()

            dao.deleteAllFood()

            val uuidList = dao.insertAll(*foodList.toTypedArray())

            var i = 0
            while (i < uuidList.size) {
                foodList[i].uuid = uuidList[i].toInt()
                i += 1

            }

            showFoods(foodList)
        }
        privateSharedPreferencesInstance.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}