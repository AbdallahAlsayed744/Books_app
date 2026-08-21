plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.koin)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.hyperdesign.data"
}

dependencies {
    implementation(project(":feature:search:domain"))
    implementation(project(":common:data"))
    implementation(project(":core:networking"))
    implementation(project(":core:database"))
    implementation(project(":core:contract"))

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.room.ktx)
}