# --- General Android & Java 21 ---
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, Exceptions
-dontpreverify

# --- DhwaniControl Specific Rules ---
# Keep all activity, service and receiver classes to ensure entry points are not stripped
-keep class io.github.rajnishkmehta.dhwanicontrol.** extends android.app.Activity { *; }
-keep class io.github.rajnishkmehta.dhwanicontrol.** extends android.app.Service { *; }
-keep class io.github.rajnishkmehta.dhwanicontrol.** extends android.content.BroadcastReceiver { *; }

# Keep ViewBinding classes
-keep class io.github.rajnishkmehta.dhwanicontrol.databinding.** { *; }

# Keep Feature and UI models
-keep class io.github.rajnishkmehta.dhwanicontrol.core.feature.** { *; }
-keep class io.github.rajnishkmehta.dhwanicontrol.home.FeatureCardUiModel { *; }

# --- Kotlin Specifics ---
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @kotlin.jvm.JvmField <fields>;
}

# --- AndroidX & Material Design ---
-keep class androidx.appcompat.** { *; }
-keep class com.google.android.material.** { *; }
-dontwarn androidx.**
-dontwarn com.google.android.material.**

# --- Coroutines ---
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keep class kotlinx.coroutines.ServiceLoaderCapability { *; }
-keep class kotlinx.coroutines.android.AndroidExceptionPreHandler { *; }
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory { *; }
-dontwarn kotlinx.coroutines.**
