plugins {
    alias(libs.plugins.movieapp.android.application)
    alias(libs.plugins.movieapp.android.compose)
    alias(libs.plugins.movieapp.koin)
}

android {
    namespace = "com.hyperdesign.books_app"

    defaultConfig {
        applicationId = "com.hyperdesign.books_app"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

}

dependencies {

    implementation(project(":core:design-system"))
    implementation(project(":core:navigation"))
    implementation(project(":core:contract"))
    implementation(project(":core:networking"))
    implementation(project(":core:database"))

    implementation(project(":common:data"))
    implementation(project(":common:presentation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.workmanager)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.androidx.work.runtime.ktx)
}