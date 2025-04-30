# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Base Google Play Services rules
-keep class com.google.android.gms.** { *; }
-keep interface com.google.android.gms.** { *; }

# Play Core specific rules
-keep class com.google.android.play.core.** { *; }
-keep interface com.google.android.play.core.** { *; }

# Kotlin coroutines support for KTX
-keep class kotlinx.coroutines.android.** { *; }

# Annotation support
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }

# For newer Play Core versions
-keep class com.google.android.play.core.ktx.** { *; }
-keep class com.google.android.play.core.tasks.** { *; }

# If using internal GMS annotations
-keep class com.google.android.gms.common.internal.** { *; }

# This is generated automatically by the Android Gradle plugin.
-dontwarn com.google.android.gms.common.annotation.NoNullnessRewrite

# This is generated automatically by the Android Gradle plugin.
-dontwarn com.google.android.gms.common.annotation.NoNullnessRewrite