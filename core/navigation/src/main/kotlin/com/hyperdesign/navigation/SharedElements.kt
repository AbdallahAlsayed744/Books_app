package com.hyperdesign.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope

val LocalSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope?> =
    compositionLocalOf { null }


fun bookPosterKey(bookId: Int): String = "book-poster-$bookId"

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedBookElement(key: Any): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return this
    return with(sharedScope) {
        this@sharedBookElement.sharedElement(
            rememberSharedContentState(key = key),
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
        )
    }
}
