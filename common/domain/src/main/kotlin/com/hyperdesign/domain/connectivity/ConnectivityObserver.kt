package com.hyperdesign.domain.connectivity
import kotlinx.coroutines.flow.Flow

enum class NetworkStatus { AVAILABLE, UNAVAILABLE }

interface ConnectivityObserver {
    val status: Flow<NetworkStatus>

    fun isCurrentlyAvailable(): Boolean
}
