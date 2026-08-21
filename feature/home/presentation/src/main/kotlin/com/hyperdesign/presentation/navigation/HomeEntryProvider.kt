package com.hyperdesign.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.navigation.Home
import com.hyperdesign.presentation.HomeScreen

class HomeEntryProvider: FeatureEntryProvider {
    override fun EntryProviderScope<NavKey>.install(
        backStack: NavBackStack<NavKey>
    ) {
        entry<Home> {
            HomeScreen()
        }
    }
}