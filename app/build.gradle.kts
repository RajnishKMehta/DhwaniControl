plugins {
    id("com.android.application")
}

val applicationId = "io.github.rajnishkmehta.dhwanicontrol"

// VERSIONING
val versionMajor = 0
val versionMinor = 5
val versionPatch = 0
val versionBuild =
    System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull() ?: 1

val appVersionCode =
    versionMajor * 10000 +
    versionMinor * 100 +
    versionPatch

val appVersionName =
    "$versionMajor.$versionMinor.$versionPatch"

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

// FAIL FAST FOR RELEASE BUILDS
gradle.taskGraph.whenReady {

    val isReleaseTask = allTasks.any {
        it.name.contains("Release", ignoreCase = true)
    }

    if (isReleaseTask && !hasReleaseSigning) {
        error(
            "Missing release signing environment variables."
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

        sourceCompatibility =
            JavaVersion.VERSION_17

        targetCompatibility =
            JavaVersion.VERSION_17
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
        "androidx.activity:activity-ktx:1.10.1"
    )

    implementation(
        "androidx.fragment:fragment-ktx:1.8.9"
    )
}
