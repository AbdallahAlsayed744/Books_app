package com.hyperdesign.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

val LocalSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope?> =
    compositionLocalOf { null }