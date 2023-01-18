package com.weather.info.model

data class CurrentWeather(val city: String,
                          val dateTime: Long,
                          val temp: Double,
                          val pressure: Double,
                          val humidity: Double,
                          val minTemp: Double,
                          val maxTemp: Double,
                          val windSpeed: Double,
                          val weathers: List<WeatherItem>)