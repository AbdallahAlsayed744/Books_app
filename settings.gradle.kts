pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Books_app"
include(":app")
include(":common:data")
include(":common:domain")
include(":common:presentation")
include(":core:networking")
include(":core:contract")
include(":core:database")
include(":core:navigation")
include(":core:design-system")
include(":feature:home")
include(":feature:booklist")
include(":feature:home:data")
include(":feature:home:domain")
include(":feature:home:presentation")
include(":feature:search:data")
include(":feature:search:domain")
include(":feature:search:presentation")
include(":feature:favourites:data")
include(":feature:favourites:domain")
include(":feature:favourites:presentation")
include(":feature:settings:data")
include(":feature:settings:domain")
include(":feature:settings:presentation")
