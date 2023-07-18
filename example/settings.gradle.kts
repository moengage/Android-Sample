import org.gradle.kotlin.dsl.execution.ProgramText.Companion.from

enableFeaturePreview("VERSION_CATALOGS")
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("moengage"){
            from("com.moengage:android-dependency-catalog:3.1.0")
        }
    }
}
rootProject.name = "example"
include(":app")
