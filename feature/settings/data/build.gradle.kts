plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.koin)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hyperdesign.settings.data"
}

dependencies {
    implementation(project(":feature:settings:domain"))
    implementation(project(":common:data"))
    implementation(project(":core:contract"))

    implementation(libs.kotlinx.coroutines.core)
}
