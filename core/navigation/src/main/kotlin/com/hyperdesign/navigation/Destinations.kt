package com.hyperdesign.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
data object Home: NavKey

@Serializable
data object Search: NavKey

@Serializable
data object Settings: NavKey

@Serializable
data object BookDetails: NavKey

@Serializable
data object BookList: NavKey

@Serializable
data object Favourites: NavKey

