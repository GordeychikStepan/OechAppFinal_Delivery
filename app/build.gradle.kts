plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.oechAppFinal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.oechAppFinal"
        minSdk = 30
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    //Picasso for images in RecyclerView
    implementation("com.squareup.picasso:picasso:2.71828")

    //Maps
    implementation("com.yandex.android:maps.mobile:4.4.0-lite")

    //Supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:2.1.5"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.1.5")
    implementation("io.github.jan-tennert.supabase:storage-kt:2.1.5")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:2.1.5")
    implementation("io.ktor:ktor-client-android:2.3.8")
    implementation("io.ktor:ktor-client-core:2.3.8")
    implementation("io.ktor:ktor-utils:2.3.8")

    //debug dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}