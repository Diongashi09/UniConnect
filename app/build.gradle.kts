plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // Google Services plugin
}

android {
    namespace = "com.example.discussfirst"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.discussfirst"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Add packaging options to resolve conflicts
    packagingOptions {
        exclude ("META-INF/NOTICE.md")
        exclude ("META-INF/LICENSE.md")

    }
}

dependencies {
    // Android dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.sun.mail:android-mail:1.6.6") //per email me dergu
    implementation("com.sun.mail:android-activation:1.6.6")

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.6.0")) // Firebase BoM
    implementation("com.google.firebase:firebase-auth") // Firebase Authentication
}

// Apply the Google Services plugin
apply(plugin = "com.google.gms.google-services")
