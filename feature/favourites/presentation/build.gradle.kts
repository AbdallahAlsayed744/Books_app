plugins {
    alias(libs.plugins.movieapp.android.feature)
}

android {
    namespace = "com.hyperdesign.favourites.presentation"
}

dependencies {
    implementation(project(":feature:favourites:domain"))
}
