plugins {
    id("com.android.application")
}

val appId = "io.github.rajnishkmehta.dhwanicontrol"

// VERSIONING
val appVersionName = "1.0.0"
val appVersionCode = 1

// Extract versions from appVersionName
val versionParts = appVersionName.split(".")
val versionMajor = versionParts.getOrNull(0)?.toIntOrNull() ?: 0
val versionMinor = versionParts.getOrNull(1)?.toIntOrNull() ?: 0
val versionPatch = versionParts.getOrNull(2)?.toIntOrNull() ?: 0

val versionBuild = System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull() ?: 1

// SIGNING
val keystorePassword =
    System.getenv("KEYSTORE_PASSWORD") ?: ""

val keyAliasEnv =
    System.getenv("KEY_ALIAS") ?: ""

val keyPasswordEnv =
    System.getenv("KEY_PASSWORD") ?: ""

val hasReleaseSigning =
    keystorePassword.isNotBlank() &&
    keyAliasEnv.isNotBlank() &&
    keyPasswordEnv.isNotBlank()

// TASK - Print app info for CI/CD
tasks.register("printAppInfo") {
    doLast {
        println(
            """
            {
              "applicationId": "$appId",
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

// FAIL FAST FOR RELEASE BUILDS
gradle.taskGraph.whenReady {
    val releaseTaskNames = setOf("assembleRelease", "bundleRelease", "packageRelease")
    val isReleaseTask = allTasks.any { task ->
        releaseTaskNames.any { it.equals(task.name, ignoreCase = true) }
    }

    if (isReleaseTask && !hasReleaseSigning) {
        error(
            "Missing release signing environment variables."
        )
    }
}

// ANDROID CONFIG
android {

    namespace = appId

    compileSdk = 36

    defaultConfig {

        applicationId = appId

        minSdk = 29
        targetSdk = 36

        versionCode = appVersionCode
        versionName = appVersionName
    }

    sourceSets {
        getByName("main") {
            res.srcDirs(mutableSetOf("src/main/res", "src/main/res/overlay"))
        }
    }

    signingConfigs {

        create("release") {

            storeFile = file("release.jks")

            storePassword = keystorePassword
            keyAlias = keyAliasEnv
            keyPassword = keyPasswordEnv
        }
    }

    buildTypes {

        debug {

            applicationIdSuffix = ".debug"

            versionNameSuffix = ".$versionBuild-debug"

            resValue(
                "string",
                "app_name",
                "DC Debug"
            )
        }

        release {

            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            if (hasReleaseSigning) {
                signingConfig =
                    signingConfigs.getByName("release")
            }

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
        resValues = true
    }

    packaging {

        resources {

            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}"
            )
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
}

// DEPENDENCIES
dependencies {

    implementation(
        "androidx.core:core-ktx:1.18.0"
    )

    implementation(
        "androidx.appcompat:appcompat:1.7.1"
    )

    implementation(
        "com.google.android.material:material:1.13.0"
    )

    implementation(
        "androidx.constraintlayout:constraintlayout:2.2.1"
    )

    implementation(
        "androidx.recyclerview:recyclerview:1.4.0"
    )

    implementation(
        "androidx.viewpager2:viewpager2:1.1.0"
    )

    implementation(
        "androidx.preference:preference-ktx:1.2.1"
    )

    implementation(
        "androidx.activity:activity-ktx:1.13.0"
    )

    implementation(
        "androidx.fragment:fragment-ktx:1.8.9"
    )
}
