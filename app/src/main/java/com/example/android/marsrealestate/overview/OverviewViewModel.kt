package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.await

enum class MarsAPiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsAPiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<MarsAPiStatus>
        get() = _status

    private val _property = MutableLiveData<List<MarsProperty>>()
    val property: LiveData<List<MarsProperty>>
        get() = _property
    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty: LiveData<MarsProperty>
        get() = _navigateToSelectedProperty

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {
        coroutineScope.launch {
            val getPropertiesDeferred = MarsApi.retrofitService.getProperties()
            try {

                _status.value = MarsAPiStatus.LOADING
                var listResult = getPropertiesDeferred.await()
                _status.value = MarsAPiStatus.DONE
                if (!listResult.isNullOrEmpty()) {
                    _property.value = listResult

                }
                // _response.value = "Success: ${getPropertiesDeferred}"

            } catch (t: Throwable) {
                _status.value = MarsAPiStatus.ERROR
                _property.value = ArrayList()
            }

        }


    }

    fun displayPropertyDetail(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }
    fun displayPropertyDetailComplete(){
        _navigateToSelectedProperty.value =null
    }
}
