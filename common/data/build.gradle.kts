plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.koin)
}

android {
    namespace = "com.hyperdesign.books_app.common.data"
}

dependencies {
    api(project(":common:domain"))
    implementation(project(":core:networking"))
    implementation(project(":core:database"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)
    implementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.mock)
}