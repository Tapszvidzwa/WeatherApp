package com.example.weatherapp.api

import okhttp3.OkHttpClient

object HttpClient {
    val client: OkHttpClient by lazy { OkHttpClient() }
}