package com.hyperdesign.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.navigation.Search
import com.hyperdesign.presentation.SearchScreen

class SearchEntryProvider: FeatureEntryProvider {

    override fun EntryProviderScope<NavKey>.install(
        backStack: NavBackStack<NavKey>
    ) {
        entry<Search> {
            SearchScreen()
        }
    }
}