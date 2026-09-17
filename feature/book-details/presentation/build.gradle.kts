plugins {
    alias(libs.plugins.movieapp.android.feature)
}

android {
    namespace = "com.hyperdesign.presentation"
}

dependencies {

    implementation(project(":feature:book-details:domain"))

}