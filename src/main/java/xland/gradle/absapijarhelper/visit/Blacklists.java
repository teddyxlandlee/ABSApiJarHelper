package xland.gradle.absapijarhelper.visit;

import org.objectweb.asm.Opcodes;

public class Blacklists {
    public static boolean accessExcluded(int access) {
        if ((access & Opcodes.ACC_DEPRECATED) != 0) return true;
        if ((access & Opcodes.ACC_PUBLIC) != 0) return false;
        return (access & Opcodes.ACC_PROTECTED) == 0;
    }

    public static boolean annotationExcluded(String descriptor) {
        return "Lorg/jetbrains/annotations/ApiStatus$Internal;".equals(descriptor) ||
               "Lorg/spongepowered/asm/mixin/Mixin;".equals(descriptor);
    }
}
