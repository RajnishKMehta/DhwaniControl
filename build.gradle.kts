plugins {
    id("com.android.application") version "9.3.0" apply false
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
