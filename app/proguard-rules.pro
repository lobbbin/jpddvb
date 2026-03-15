# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /sdk/tools/proguard/proguard-android.txt

# Keep data classes
-keep class com.popop.lifesimulator.data.models.** { *; }
-keep class com.popop.lifesimulator.data.database.** { *; }

# Keep Room entities
-keep @androidx.room.Entity class *
-keepclassmembers class * {
    @androidx.room.* <fields>;
}

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ComponentSupplier { *; }

# Keep JSON serialization
-keepattributes Signature
-keepattributes *Annotation*
-keep class org.json.** { *; }
