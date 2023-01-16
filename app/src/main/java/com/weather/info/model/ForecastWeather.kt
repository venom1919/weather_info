package com.weather.info.model

data class ForecastWeather(val dayOfTheWeek: String,
                           val temperature: String,
                           val icon: String, val min_temp: String,
                           val max_temp: String)