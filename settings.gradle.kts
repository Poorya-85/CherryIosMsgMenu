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
        // این خط برای دانلود API قدیمی Xposed اضافه شد
        maven(url = "https://api.xposed.info/")
    }
}

rootProject.name = "CherryIOSMenuModule"
include(":app")
// خطوط مربوط به includeBuild و libxposed-compat حذف شدند تا تداخلی پیش نیاید
