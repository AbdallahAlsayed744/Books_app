package com.hyperdesign.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.hyperdesign.navigation.Favourites
import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.presentation.FavouritesScreen

class FavouritesEntryProvider : FeatureEntryProvider {
    override fun EntryProviderScope<NavKey>.install(
        backStack: NavBackStack<NavKey>
    ) {
        entry<Favourites> {
            FavouritesScreen()
        }
    }
}
