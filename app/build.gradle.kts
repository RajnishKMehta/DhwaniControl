plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val applicationId = "io.github.rajnishkmehta.dhwanicontrol"

// VERSIONING
val versionMajor = 0
val versionMinor = 2
val versionPatch = 1
val versionBuild = System.getenv("GITHUB_RUN_NUMBER")?.toInt() ?: 1

val appVersionCode =
    versionMajor * 10000 +
    versionMinor * 100 +
    versionPatch

val appVersionName = "$versionMajor.$versionMinor.$versionPatch"

// TASK
tasks.register("printAppInfo") {
    doLast {
        println(
            """
            {
              "applicationId": "$applicationId",
              "versionMajor": $versionMajor,
              "versionMinor": $versionMinor,
              "versionPatch": $versionPatch,
              "versionBuild": $versionBuild,
              "version": "$appVersionName",
              "versionCode": $appVersionCode
            }
            """.trimIndent()
        )
    }
}

// ANDROID CONFIG
android {
    namespace = applicationId
    compileSdk = 36

    defaultConfig {
        applicationId = applicationId
        minSdk = 29
        targetSdk = 36

        versionCode = appVersionCode
        versionName = appVersionName
    }

    signingConfigs {
        create("release") {
            storeFile = file("release.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
            keyAlias = System.getenv("KEY_ALIAS") ?: ""
            keyPassword = System.getenv("KEY_PASSWORD") ?: ""
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                rootProject.file("proguard-rules.pro")
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }
}

// Kotlin config
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}

// DEPENDENCIES
dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("io.coil-kt:coil:2.7.0")
}
