plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.koin)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hyperdesign.favourites.data"
}

dependencies {
    implementation(project(":feature:favourites:domain"))
    implementation(project(":common:data"))
    implementation(project(":core:database"))
    implementation(project(":core:contract"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.room.ktx)
}
