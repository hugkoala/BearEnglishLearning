# Moshi
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}
-keep class com.bear.englishlearning.data.remote.dto.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*

# Room
-keep class * extends androidx.room.RoomDatabase
