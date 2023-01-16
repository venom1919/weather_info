package com.weather.info.config

class DefaultConfig : WeatherConfig{

    override fun baseURL(): String {
        return "api.openweathermap.org/data/2.5/forecast/daily?"
    }

    override fun getApiKey(): String {
        return "f8eb3ba1ba9adb6de7d07a44153bcb7f"
    }

}