# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson
-keep class de.wetterapp.data.model.** { *; }
-keepattributes *Annotation*

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
