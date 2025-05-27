plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.grish.buy4all4"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.grish.buy4all4"
        minSdk = 32
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom.v3200))
    implementation(libs.google.firebase.analytics)
    implementation(libs.glide)
    implementation(libs.firebase.storage)
    implementation(libs.navigation.fragment)
    annotationProcessor(libs.compiler)
    implementation(libs.firebase.firestore.v2400)
    implementation(libs.firebase.storage.v2000)
    implementation(libs.play.services.drive)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.firebase.auth.v2101)
    implementation(libs.appcompat.v161)
    implementation(libs.material.v190)
    implementation (libs.constraintlayout.v214)
    implementation (libs.recyclerview)
    implementation (libs.firebase.firestore.v2491)
    implementation (libs.firebase.storage.v2030)
    implementation (libs.glide.v4160)
    annotationProcessor (libs.compiler.v4160)
    implementation (libs.cloudinary.android)
    implementation (libs.cardview)
    implementation (libs.firebase.database.v2005)
    implementation (libs.firebase.auth.v2105)
    implementation (libs.picasso)
    implementation (libs.glide.v4160)
    annotationProcessor (libs.compiler.v4160)
}
