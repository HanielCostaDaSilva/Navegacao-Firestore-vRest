package com.example.navegacao1.model.dados

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.0.106:3000/" // Para emulador Android

    val usuarioService: UsuarioServiceIF by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioServiceIF::class.java)
    }
}
