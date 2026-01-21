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

rootProject.name = "AIExpenseTracker"
include(":app")
include(":data")
include(":features")
include(":features:expensehome")
include(":navigation")
include(":features:expenseAdd")
include(":design")
include(":features:camerax")
include(":features:mltoolkit")
include(":test-utils")
