package com.weather.info.config

interface WeatherConfig {

    fun baseURL(): String
    fun getApiKey(): String

}