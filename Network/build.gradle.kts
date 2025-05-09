plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.colin.library.android.network"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        //*部分网络请求异常重试次数
        buildConfigField("int", "RETRY", "3")
        //部分网络请求需要现实弹框，避免快速请求，弹框消失【500ms】
        buildConfigField("long", "DELAY", "500L")
        //网络请求超时时长限制
        buildConfigField("long", "TIMEOUT", "10000L")
        //*测试服务器地址
        buildConfigField("String", "URL_DEBUG", "\"http://14.103.238.200\"")
        //发布服务器地址
        buildConfigField("String", "URL_RELEASE", "\"https://app.wanwuzhinan.top\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    compileOnly(project(":Utils"))
    compileOnly(libs.bundles.androidCommon)
    compileOnly(libs.bundles.squareup)
    compileOnly(libs.androidx.lifecycle.viewmodel.ktx)

}