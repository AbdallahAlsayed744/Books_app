package com.hyperdesign.presentation.navigation
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.hyperdesign.navigation.BookDetails
import com.hyperdesign.navigation.FeatureEntryProvider
import com.hyperdesign.presentation.BookDetailsScreen

class BookDetailsEntryProvider : FeatureEntryProvider {
    override fun EntryProviderScope<NavKey>.install(backStack: NavBackStack<NavKey>) {
        entry<BookDetails> { key ->
            BookDetailsScreen(
                bookId = key.bookId,
                onBack = { backStack.removeLastOrNull() },
            )
        }
    }
}
