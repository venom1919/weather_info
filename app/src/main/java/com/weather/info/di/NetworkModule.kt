package com.weather.info.di

import android.os.Environment
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideCache(): Cache =
        Cache(Environment.getDownloadCacheDirectory(), 10 * 1024 * 1024)

//    @Provides
//    @Singleton
//    fun provideRetrofit(
//        moshi: Moshi,
//        okHttpClientBuilder: OkHttpClient.Builder,
//        cache: Cache,
//    ): Retrofit = Retrofit.Builder()
//        .baseUrl(.BASE_URL)
//        .client(okHttpClientBuilder.cache(cache).build())
//        .addConverterFactory(MoshiConverterFactory.create(moshi))
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .build()

//    @Provides
//    @Singleton
//    fun provideService(retrofit: Retrofit): WeatherAppAPI =
//        retrofit.create(WeatherAppAPI::class.java)
//
//    @Provides
//    @Singleton
//    fun providePlacesClient(): PlacesClient =
//        PlacesClient(
//            Constants.AlgoliaKeys.APPLICATION_ID,
//            Constants.AlgoliaKeys.SEARCH_API_KEY
//        )

}