plugins {
    alias(libs.plugins.movieapp.android.feature)
}

android {
    namespace = "com.hyperdesign.home.presentation"
}

dependencies {

    implementation(project(":feature:home:domain"))

}