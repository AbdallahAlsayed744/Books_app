package com.hyperdesign.networking.di

import com.hyperdesign.books_app.core.networking.BuildConfig
import com.hyperdesign.networking.HttpClientFactory
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<Json> { HttpClientFactory.json }

    single<HttpClient> { HttpClientFactory.create(enableLogging = BuildConfig.DEBUG) }
}