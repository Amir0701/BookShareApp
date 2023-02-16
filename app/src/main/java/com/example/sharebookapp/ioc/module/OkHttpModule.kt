package com.example.sharebookapp.ioc.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
class OkHttpModule {
    @Provides
    fun okHttpClient(okHttpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient{
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(okHttpLoggingInterceptor)
            .build()
    }

    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}