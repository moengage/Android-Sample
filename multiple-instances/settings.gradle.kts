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
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
    versionCatalogs {
        create("moengage") {
            from("com.moengage:android-dependency-catalog:2.0.0")
        }
    }
}
rootProject.name = "multiple-instances"
include(":app")
