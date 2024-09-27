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
include(":core:data")
include(":feature")
include(":feature:recipes-list")
include(":core:di")
include(":core:domain")
include(":feature:recipes-list:data")
include(":feature:recipes-list:domain")
include(":ui-theme")
include(":feature:onboarding")
