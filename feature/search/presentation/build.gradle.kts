plugins {
    alias(libs.plugins.movieapp.android.feature)
}

android {
    namespace = "com.hyperdesign.search.presentation"
}

dependencies {

    implementation(project(":feature:search:domain"))

}