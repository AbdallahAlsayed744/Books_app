plugins {
    `kotlin-dsl`
}

group = "com.hyperdesign.books_app.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly("com.android.tools.build:gradle:9.0.1")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0")
    compileOnly("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.2.0-2.0.2")
    compileOnly("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.2.0")
    compileOnly("androidx.room:room-gradle-plugin:2.7.0")

}

gradlePlugin {

    plugins {
        register("androidApplication") {
            id = "hyperdesign.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "hyperdesign.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "hyperdesign.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "hyperdesign.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "hyperdesign.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
        register("androidRoom") {
            id = "hyperdesign.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("koin") {
            id = "hyperdesign.koin"
            implementationClass = "KoinConventionPlugin"
        }
    }
}