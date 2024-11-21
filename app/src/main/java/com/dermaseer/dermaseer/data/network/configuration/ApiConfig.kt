package com.dermaseer.dermaseer.data.network.configuration

import com.dermaseer.dermaseer.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) level =
                HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        private val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}
