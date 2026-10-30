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
            isMinifyEnabled = false
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

    // برای اینکه فایل‌های داخل META-INF (مثل java_init.list و module.prop) در APK قرار بگیرن
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // تغییر به API جدید نسخه 102 برای سازگاری با LSPosed 2.2.1
    compileOnly("io.github.libxposed:api:102.0.0")
}
