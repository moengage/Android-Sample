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
        mavenCentral()
    }
    versionCatalogs {
        create("moengage"){
            from("com.moengage:android-dependency-catalog:5.0.0")
        }
    }
}
rootProject.name = "example"
include(":app")
