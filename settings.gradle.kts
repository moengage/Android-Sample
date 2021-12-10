enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("moengage") {
            from("com.moengage:android-dependency-catalog:1.0.4")
        }
    }
}
include(":kotlin-sample", ":java-sample")
