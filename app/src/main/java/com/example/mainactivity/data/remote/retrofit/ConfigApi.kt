package com.example.mainactivity.data.remote.retrofit


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigApi {
    companion object {
        fun getApiService(): ServiceApi {
            val interceptor = Interceptor {chain ->
                val req = chain.request()
                val requestHeader =  req.newBuilder()
                    .addHeader("Authorization", "token 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")
                    .build()
                chain.proceed(requestHeader)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ServiceApi::class.java)
        }
    }
}