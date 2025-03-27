pluginManagement {
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

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Rate And Review"

// applications
include(":app:movierating")
// feature
include(":feature:user")
include(":feature:settings")
//core
include(":core:common")
include(":core:storage")
include(":core:network")
include(":core:ui")
include(":core:model")
