plugins {
    alias(libs.plugins.movieapp.android.feature)
}

android {
    namespace = "com.hyperdesign.settings.presentation"
}

dependencies {
    implementation(project(":feature:settings:domain"))
}
