plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.learn.storyappbyoby"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.learn.storyappbyoby"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    //datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //live data
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    //ktx
    implementation("androidx.activity:activity-ktx:1.8.0")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //OkHttp logging interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    //lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //splash-screen
    implementation("com.airbnb.android:lottie:3.4.1")
    testImplementation("junit:junit:4.13.2")

    //EXIF
    implementation("androidx.exifinterface:exifinterface:1.3.6")


}