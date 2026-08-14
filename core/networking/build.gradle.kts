import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.movieapp.android.library)
    alias(libs.plugins.movieapp.koin)
}


val localProps =
    Properties().apply {
        val f = rootProject.file("local.properties")
        if (f.exists()) f.inputStream().use { load(it) }
    }

android {
    namespace = "com.hyperdesign.books_app.core.networking"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            "String",
            "BOOKS_BASE_URL",
            "\"${localProps.getProperty("BOOKS_BASE_URL", "https://project-gutenberg-free-books-api1.p.rapidapi.com/")}\"",
        )

        buildConfigField(
            "String",
            "BOOKS_ACCESS_TOKEN",
            "\"${localProps.getProperty("BOOKS_ACCESS_TOKEN", "")}\"",
        )
    }
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.ktor.client.mock)
}