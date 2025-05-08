plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}
val PATH_LIBS = "${rootDir.absolutePath}/app/libs"
android {
    namespace = "com.colin.library.android.widget"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        aidl = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
            kotlin.srcDirs("src/main/kotlin")
            aidl.srcDirs("src/main/aidl","com.colin.library.android.widget")
            jniLibs.srcDirs(PATH_LIBS)
        }
    }

}

dependencies {
    compileOnly(fileTree(mapOf("dir" to PATH_LIBS, "include" to listOf("*.aar", "*.jar"))))
    compileOnly(project(":Utils"))
    compileOnly(libs.tbssdk)
    compileOnly(libs.gson)
    implementation(libs.bundles.androidCommon)
}