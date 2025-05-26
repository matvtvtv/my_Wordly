plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.mywordle"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mywordle"
        minSdk = 28
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lombok)
    implementation(libs.flexbox)
    implementation(libs.work)
    implementation(libs.core)
    implementation(libs.okhttp)
    implementation(libs.convertergson)
    implementation(libs.retrofit)
    androidTestImplementation(libs.espresso.core)


    annotationProcessor(libs.lombok)
}
