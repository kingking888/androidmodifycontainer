# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lody/Desktop/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepclasseswithmembernames class * {
    native <methods>;
}

-optimizationpasses 7

# 保留sdk系统自带的一些内容 【例如：-keepattributes *Annotation* 会保留Activity的被@override注释的onCreate、onDestroy方法等】
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# 避免混淆泛型
-keepattributes Signature
# 抛出异常时保留代码行号,保持源文件以及行号
-keepattributes SourceFile,LineNumberTable

-keep interface android.*.*.**{*;}
-keep interface com.lody.*.**{*;}

-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}

#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

-keep class android.telephony.**{*;}

-keep class com.android.wificonfig.**{*;}

-keep class android.**{*;}

-keep class com.squareup.okhttp.**{*;}
-dontwarn javax.annotation.**
-keep class javax.annotation.**{*;}
-dontwarn org.apache.commons.**
-keep class org.apache.commons.**{*;}
-dontwarn javax.script.**
-keep class javax.script.**{*;}
-dontwarn android.app.**
-keep class android.app.**{*;}
-dontwarn android.content.res.**
-keep class android.content.res.**{*;}
-dontwarn com.android.internal.**
-keep class com.android.internal.**{*;}
-keep class org.slf4j.**{*;}
-keep class com.zhy.http.okhttp.**{*;}
-keep class com.gc.home.**{*;}
-keep class com.lbe.**{*;}

-keep class com.gc.abs.**{*;}

-keep class android.content.pm.**{*;}
-keep class com.google.android.apps.**{*;}

-keep class org.jdeferred.**{*;}
-dontwarn org.slf4j.**

-dontwarn org.codehaus.mojo.**
-keep class org.codehaus.mojo.**{*;}
-keep class com.gc.**{*;}
-dontwarn android.os.**
-keep class android.os.**{*;}
-keep class com.google.code.**{*;}
-keep class com.lody.**{*;}
-keep class org.apache.*.*.**{*;}
-keep class com.android.support.*.**{*;}
-keep class android.**.**.**{*;}
-keep class com.lody.virtual.client.ipc.**{*;}
-keep class mirror.**{*;}
-keep class  com.gc.home.models.**{*;}
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**
# 保留R下面的资源
-keep class **.R$* {*;}
 # 保留枚举类不被混淆
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 # 保留我们自定义控件（继承自View）不被混淆
 -keep public class * extends android.view.View{
     *** get*();
     void set*(***);
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }

 # 保留Parcelable序列化类不被混淆
 -keep class * implements android.os.Parcelable {
     public static final android.os.Parcelable$Creator *;
 }

 # 保留Serializable序列化的类不被混淆
 -keepnames class * implements java.io.Serializable
 -keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     !static !transient <fields>;
     !private <fields>;
     !private <methods>;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }


