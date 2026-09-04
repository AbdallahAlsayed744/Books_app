package com.hyperdesign.networking

import com.hyperdesign.books_app.core.networking.BuildConfig

object NetworkConstants {
    const val BASE_URL: String = BuildConfig.BOOKS_BASE_URL

    //val IMAGE_BASE_URL: String = BuildConfig.TMDB_IMAGE_BASE_URL
    const val ACCESS_TOKEN: String = BuildConfig.BOOKS_ACCESS_TOKEN

    val HAS_ACCESS_TOKEN: Boolean = ACCESS_TOKEN.isNotBlank()

//fun imageUrl(path: String?, size: String = "w500"): String =
//    path?.takeIf { it.isNotBlank() }?.let { "$IMAGE_BASE_URL$size$it" }.orEmpty()

}