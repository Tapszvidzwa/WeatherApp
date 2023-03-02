package com.example.weatherapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.ApiConstants
import com.example.weatherapp.api.NetworkResult
import com.example.weatherapp.data.repository.WeatherDataRepository
import com.example.weatherapp.di.IoDispatcher
import com.example.weatherapp.ui.UiState
import com.example.weatherapp.utils.ColorUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: WeatherDataRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState>
        get() = _uiState

    fun retry() {
        getWeatherData(repository.getLastSavedLocation())
    }

    fun getWeatherData(locationName: String) {

        _uiState.value = UiState(screenState = ScreenState.LOADING, locationName = locationName)
        repository.updateLocationName(locationName.trim())

        if (locationName.isEmpty()) {
            _uiState.value = UiState(
                screenState = ScreenState.ERROR,
                locationName = repository.getLastSavedLocation(),
                errorMsg = "Please enter a valid city name to display weather"
            )
        } else {

            viewModelScope.launch(ioDispatcher) {
                val response = repository.getGeoCoordinates(repository.getLastSavedLocation())

                when (response) {
                    is NetworkResult.Error -> {
                        _uiState.value = UiState(
                            screenState = ScreenState.ERROR,
                            locationName = locationName,
                            errorMsg = response.message.toString()
                        )
                    }

                    is NetworkResult.Success -> {
                        response.data?.let { coordinates ->
                            getWeatherData(
                                latitude = coordinates[0].lat,
                                longitude = coordinates[0].lon
                            )
                        }
                    }
                }
            }
        }
    }

    fun getWeatherDataForCurrentLocation(latitude: Double, longitude: Double) {
        // In ideal scenario, I would make this a  parallel async call using coroutines, but wanted to demonstrate rxjava
        getLocationCityName(latitude, longitude)
        getWeatherData(latitude, longitude)
    }

    // Api call using rxjava
    private fun getLocationCityName(latitude: Double, longitude: Double) {
        val call: Disposable = repository.getLocationName(latitude, longitude)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    it?.response()?.body()?.get(0)?.name?.let { name ->
                        repository.updateLocationName(name)
                        _uiState.value = _uiState.value.copy(
                            locationName = name
                        )
                    }
                },
                {
                    _uiState.value = UiState(
                        screenState = ScreenState.ERROR,
                        locationName = repository.getLastSavedLocation(),
                        errorMsg = it.message.toString()
                    )
                }
            )

        compositeDisposable.add(call)
    }

    private fun getWeatherData(latitude: Double, longitude: Double) {

        viewModelScope.launch(ioDispatcher) {
            val response = repository.getWeatherData(
                lat = latitude,
                lon = longitude
            )

            when (response) {
                is NetworkResult.Error -> {
                    _uiState.value = UiState(
                        screenState = ScreenState.ERROR,
                        locationName = repository.getLastSavedLocation(),
                        errorMsg = response.message.toString()
                    )
                }

                is NetworkResult.Success -> {
                    response.data?.current?.let {
                        _uiState.value = _uiState.value.copy(
                            weatherCondition = response.data.current.weather[0].main,
                            temp = response.data.current.temp,
                            feelsLike = response.data.current.feels_like,
                            windSpeed = response.data.current.wind_speed,
                            humidity = response.data.current.humidity,
                            locationName = repository.getLastSavedLocation(),
                            backgroundColor = ColorUtil.getBackgroundColor(response.data.current.weather[0].id),
                            imageIcon = ApiConstants.OPEN_WEATHER_ICON_URL + response.data.daily[0].weather[0].icon + ".png",
                            screenState = ScreenState.SUCCESS
                        )
                    }
                }
            }
        }
    }

    // Clear any rxjava disposables
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

enum class ScreenState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}