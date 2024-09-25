package com.serranoie.android.di

import com.serranoie.android.cookoutines.BuildConfig
import com.serranoie.android.data.remote.SpoonacularApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DependenciesProvider {
    private const val API_KEY = BuildConfig.API_KEY
    private const val BASE_URL = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder()
                .header("Authorization", API_KEY)
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder().addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): SpoonacularApi {
        return retrofit.create(SpoonacularApi::class.java)
    }
}