# Additional flags to pass to Proguard when processing a binary that uses
# MediaPipe.

# Keep public members of our public interfaces. This also prevents the
# obfuscation of the corresponding methods in classes implementing them,
# such as implementations of PacketCallback#process.
-keep public interface com.google.mediapipe.framework.* {
  public *;
}

# This method is invoked by native code.
-keep public class com.google.mediapipe.framework.Packet {
  public static *** create(***);
  public long getNativeHandle();
  public void release();
}

# This method is invoked by native code.
-keep public class com.google.mediapipe.framework.PacketCreator {
  *** releaseWithSyncToken(...);
}

# This method is invoked by native code.
-keep public class com.google.mediapipe.framework.MediaPipeException {
  <init>(int, byte[]);
}

# Required to use PacketCreator#createProto
-keep class com.google.mediapipe.framework.ProtoUtil$SerializedMessage { *; }

# Add to ensure the build stability:
-keep class com.google.mediapipe.solutioncore.** {*;}
-keep class com.google.protobuf.** {*;}
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {*;}

# This is generated automatically by the Android Gradle plugin.
-dontwarn javax.lang.model.SourceVersion
-dontwarn javax.lang.model.element.Element
-dontwarn javax.lang.model.element.ElementKind
-dontwarn javax.lang.model.element.Modifier
-dontwarn javax.lang.model.type.TypeMirror
-dontwarn javax.lang.model.type.TypeVisitor
-dontwarn javax.lang.model.util.SimpleTypeVisitor8