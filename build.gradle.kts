plugins {
    id("com.android.application") version "9.2.0" apply false
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
