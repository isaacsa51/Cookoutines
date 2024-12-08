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

rootProject.name = "Cookoutines"

include(":app")
include(":ui-theme")
include(":core:di")
include(":core:data")
include(":core:domain")
include(":feature")
include(":feature:recipes-list")
include(":feature:recipes-list:data")
include(":feature:recipes-list:domain")
include(":feature:onboarding")
include(":feature:onboarding:data")
include(":feature:onboarding:domain")
include(":feature:instructions")
include(":feature:instructions:domain")
include(":feature:instructions:data")
include(":feature:saved")
include(":feature:saved:domain")
include(":feature:search")
