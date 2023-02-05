package com.example.sharebookapp.ioc

import android.util.Log
import com.example.sharebookapp.data.repository.Api
import com.example.sharebookapp.data.repository.SingScope
import com.example.sharebookapp.util.Utils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [OkHttpModule::class])
class ApiModule {
    @Provides
    fun getApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @SingScope
    @Provides
    fun retrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory, gson: Gson): Retrofit{
        Log.i("Sco", "Retro")
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    fun gson(): Gson{
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory{
        return GsonConverterFactory.create(gson)
    }
}