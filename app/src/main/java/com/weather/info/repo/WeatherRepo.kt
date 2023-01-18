package com.weather.info.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.weather.info.model.CurrentWeather
import com.weather.info.model.ForecastItem
import com.weather.info.network.WeatherApi
import com.weather.info.network.WeatherDTO
import com.weather.info.network.asDomainModel

class WeatherRepo {

    val currentWeather = MutableLiveData<WeatherDTO>()

    val currentWeatherLD: LiveData<CurrentWeather> =
        Transformations.map(currentWeather) {
            it.asDomainModel()
        }

    private val _forecasts = MutableLiveData<List<ForecastItem>>()
    val forecasts: LiveData<List<ForecastItem>>
        get() = _forecasts

    private var _widgetWeather: CurrentWeather? = null
    val widgetWeather: CurrentWeather?
        get() = _widgetWeather

    suspend fun getWeatherAndForecasts(lat: Double, lon: Double) {
        try {
            currentWeather.value = WeatherApi.retrofitService.getCurrentWeather(lat, lon)
            val forecastResponse = WeatherApi.retrofitService.getForecast(lat, lon)
            _forecasts.value = forecastResponse.asDomainModel()
        } catch (t: Throwable) {
            throw Throwable(t)
        }
    }

    suspend fun getWeatherAndForecasts(cityName: String) {
        try {
            currentWeather.value = WeatherApi.retrofitService.getCurrentWeather(cityName)
            val forecastResponse = WeatherApi.retrofitService.getForecast(cityName)
            _forecasts.value = forecastResponse.asDomainModel()
        } catch (t: Throwable) {
            throw Throwable(t)
        }
    }

    suspend fun getWeather(lat: Double, lon: Double) {
        try {
            _widgetWeather = WeatherApi.retrofitService.getCurrentWeather(lat, lon).asDomainModel()
        } catch (t: Throwable) {
            t.localizedMessage?.let { Log.d(t.message, it) }
        }
    }

}