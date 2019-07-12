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

#不混淆某个类
-keep public class name.huihui.example.Test { *; }
#不混淆某个类的子类
-keep public class * extends name.huihui.example.Test { *; }
#不混淆所有类名中包含了“model”的类及其成员
-keep public class **.*model*.** {*;}
#不混淆某个接口的实现
-keep class * implements name.huihui.example.TestInterface { *; }
#不混淆某个类的构造方法
-keepclassmembers class name.huihui.example.Test {
    public <init>();
}
#不混淆某个类的特定的方法
-keepclassmembers class name.huihui.example.Test {
    public void test(java.lang.String);
}
#不混淆某个类的内部类
-keep class name.huihui.example.Test$* {
        *;
 }
#两个常用的混淆命令，注意：
#一颗星表示只是保持该包下的类名，而子包下的类名还是会被混淆；
#两颗星表示把本包和所含子包下的类名都保持；
-keep class com.suchengkeji.android.ui.**
-keep class com.suchengkeji.android.ui.*
#用以上方法保持类后，你会发现类名虽然未混淆，但里面的具体方法和变量命名还是变了，
#如果既想保持类名，又想保持里面的内容不被混淆，我们就需要以下方法了

#不混淆某个包所有的类
-keep class com.suchengkeji.android.bean.** { *; }
#在此基础上，我们也可以使用Java的基本规则来保护特定类不被混淆，比如我们可以用extend，implement等这些Java规则。如下
# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

-keep public class com.sqy.kingdbdemo.entity.Student { *; }

