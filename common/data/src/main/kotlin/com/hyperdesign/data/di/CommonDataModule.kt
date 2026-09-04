package com.hyperdesign.data.di
import com.hyperdesign.data.connectivity.AndroidConnectivityObserver
import com.hyperdesign.domain.connectivity.ConnectivityObserver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonDataModule = module {
    single<ConnectivityObserver> { AndroidConnectivityObserver(androidContext()) }
}
