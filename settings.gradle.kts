pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "inpost-jvm-sdk"

include(
    ":inpost-bom",
    ":inpost-core",
    ":inpost-spring-starter",
    ":inpost-contract-tests",
)
