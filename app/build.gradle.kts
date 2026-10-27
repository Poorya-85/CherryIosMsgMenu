plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.module"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.module"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // برای ماژول‌های Xposed بهتره خاموش باشه تا کدها حذف نشن
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // برای اینکه فایل‌های داخل META-INF (مثل java_init.list) در APK قرار بگیرن
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // استفاده از API قدیمی و سازگار با LSPosed
    compileOnly("de.robv.android.xposed:api:82")
}
