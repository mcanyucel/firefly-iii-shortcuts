plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.mustafacanyucel.fireflyiiishortcuts"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mustafacanyucel.fireflyiiishortcuts"
        minSdk = 29
        targetSdk = 35
        versionCode = 3
        versionName = "1.1.2"

        manifestPlaceholders["appAuthRedirectScheme"] = "com.mustafacanyucel.fireflyiiishortcuts"

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    dependenciesInfo {
        //  Disable dependency metadata when building APKs (F-Droid reproducable build req.)
        includeInApk = false
        //  Disable dependency metadata when building App Bundles (F-Droid reproducable build req.)
        includeInBundle = false

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.browser)
    implementation(libs.openid.appauth)
    implementation(libs.hilt.android)
    implementation(libs.datastore.preferences)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.room)
    implementation(libs.room.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.activity)
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}