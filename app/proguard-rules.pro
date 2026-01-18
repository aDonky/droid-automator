# Add project specific ProGuard rules here.

# Keep Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep serializable classes
-keep,includedescriptorclasses class com.droidautomator.**$$serializer { *; }
-keepclassmembers class com.droidautomator.** {
    *** Companion;
}
-keepclasseswithmembers class com.droidautomator.** {
    kotlinx.serialization.KSerializer serializer(...);
}
