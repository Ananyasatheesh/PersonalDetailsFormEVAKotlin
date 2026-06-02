# PROGAURD - Does 3 works
# * Shrinking - Removes unused code.
# * Obfuscation - Changes names of functions. (Hard for reverse engineering)
# * Optimization - Calculated code values if known

# Previously - .class files -> Proguard -> optimised .class files -> D8 -> .dex files (which android can read easily)
# Now - .class files -> R8 (Proguard + dex compiler (D8)) -> .dex files


# With Proguard enabled (isMinifyEnabled, isShrinResource) in release build type (app size decreased from 6MB to 2.5MB)
# Sometimes when changing classnames (wrk of R8), app may crash. Check it and update

#---------------------xxx---------------------------------------------------------------------

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

-keep class com.example.personaldetailsform_kotlin.model.User { *; }
-keep class com.example.personaldetailsform_kotlin.model.Photo { *; }