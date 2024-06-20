import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")    // kapt 추가  (Room 사용)
}

// ChatGPT api key 보안 설정
val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

//val properties = Properties()
//properties.load(FileInputStream(rootProject.file("local.properties")))


fun getApiKey(propertyKey: String): String {
    return properties.getProperty(propertyKey)
}

//fun getApiKey(propertyKey: String): String {
//    return properties.getProperty("api.key")
//}

android {
    namespace = "com.itda.android_c_teamproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.itda.android_c_teamproject"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ChatGPT api key 보안 설정
        buildConfigField("String", "API_KEY", "\"${getApiKey("api.key")}\"")
        // buildConfigField("String", "api_key", getApiKey("api.key"))
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    
    // viewBinding { enable = true }
}

dependencies {

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")       // 런타임 라이브러리
    annotationProcessor("androidx.room:room-compiler:$room_version") // 애노태이션 컴파일러
    implementation("androidx.room:room-ktx:$room_version")

    kapt("androidx.room:room-compiler:$room_version")

// Glide 의존성 추가
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // retrofit 의존성 추가
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")             // 추가
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0") // 추가

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}