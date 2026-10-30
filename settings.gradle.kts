pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // این خط برای دانلود کتابخانه API جدید (libxposed) از JitPack اضافه شد
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "CherryIOSMenuModule"
include(":app")
// دیگر نیازی به includeBuild("libxposed-api") و include(":libxposed-compat") نیست
