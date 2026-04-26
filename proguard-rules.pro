-keep class io.github.rajnishkmehta.dhwanicontrol.** extends android.app.Activity { *; }
-keep class io.github.rajnishkmehta.dhwanicontrol.** extends android.app.Service { *; }
-keep class io.github.rajnishkmehta.dhwanicontrol.** extends android.content.BroadcastReceiver { *; }

-keep class android.media.AudioManager { *; }
-keep class android.view.WindowManager { *; }
-keep class androidx.core.view.GestureDetectorCompat { *; }

-allowaccessmodification
-repackageclasses
-optimizationpasses 5
