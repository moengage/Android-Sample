plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.spotless)
}

/**
 * Formatting is ktlint, driven through Spotless: `./gradlew spotlessCheck` to verify,
 * `./gradlew spotlessApply` to fix. Rules live in `.editorconfig` so the IDE and the build agree.
 */
spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get())
            // Compose theme files are conventionally Color.kt / Type.kt / Shape.kt / Theme.kt even
            // though each holds one differently-named declaration. Set here rather than in
            // .editorconfig because the filename rule does not pick it up from there.
            .editorConfigOverride(
                mapOf(
                    // intellij_idea keeps ktlint_official's multiline-expression-wrapping and forced
                    // signature wrapping off, which is what matches the Studio formatting these files
                    // were written with. Set here, not in .editorconfig: Spotless's ktlint step does
                    // not pick either of these two up from there.
                    "ktlint_code_style" to "intellij_idea",
                    // Compose theme files are conventionally Color.kt / Type.kt / Shape.kt / Theme.kt
                    // even though each holds one differently-named declaration.
                    "ktlint_standard_filename" to "disabled",
                ),
            )
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get())
    }
}
