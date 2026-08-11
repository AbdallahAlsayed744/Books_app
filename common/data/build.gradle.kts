plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.koin)
}

android {
    namespace = "com.hyperdesign.books_app.common.data"
}

dependencies {
//    implementation(project(":feature:settings:domain"))
//    implementation(project(":core:contract"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)
    implementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.mock)
}