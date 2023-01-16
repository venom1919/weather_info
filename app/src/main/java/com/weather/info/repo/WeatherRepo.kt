package com.weather.info.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.weather.info.model.CurrentWeather
import com.weather.info.network.WeatherApi
import com.weather.info.network.WeatherResponse
import com.weather.info.network.asDomainModel

class WeatherRepo {
    private val currentWeather = MutableLiveData<WeatherResponse>()
    val currentWeatherLD: LiveData<CurrentWeather> =
        Transformations.map(currentWeather) {
            it.asDomainModel()
        }

    private val _forecasts = MutableLiveData<List<ForecastRepo>>()
    val forecasts: LiveData<List<ForecastRepo>>
        get() = _forecasts

    private var _widgetWeather: CurrentWeather? = null
    val widgetWeather: CurrentWeather?
        get() = _widgetWeather


    /**
     * Gets weather information for current location from the
     * Weather API service and updates the _currentWeather
     * and _forecasts LiveData. Retrofit makes suspending
     * functions main-safe.
     */
    suspend fun getWeatherAndForecasts(lat: Double, lon: Double) {
        try {
            currentWeather.value = WeatherApi.retrofitService.getCurrentWeather(lat, lon)
            val forecastResponse = WeatherApi.retrofitService.getForecast(lat, lon)
            _forecasts.value = forecastResponse.asDomainModel()
        } catch (t: Throwable) {
            throw Throwable(t)
        }
    }

    /**
     * Gets weather information for user's input from Weather API Retrofit
     * service and updates the _currentWeather and _forecasts LiveData.
     */
    suspend fun getWeatherAndForecasts(cityName: String) {
        try {
            currentWeather.value = WeatherApi.retrofitService.getCurrentWeather(cityName)
            val forecastResponse = WeatherApi.retrofitService.getForecast(cityName)
            _forecasts.value = forecastResponse.asDomainModel()
        } catch (t: Throwable) {
            throw Throwable(t)
        }
    }

    /**
     * Gets weather information for current location.
     * This is to update the widget when app is in background.
     */
    suspend fun getWeather(lat: Double, lon: Double) {
        try {
            _widgetWeather = WeatherApi.retrofitService.getCurrentWeather(lat, lon).asDomainModel()
        } catch (t: Throwable) {
            Timber.d(t)
        }
    }

}