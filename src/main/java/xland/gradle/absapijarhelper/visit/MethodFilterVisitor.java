package xland.gradle.absapijarhelper.visit;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import xland.gradle.absapijarhelper.ClassEntry;

final class MethodFilterVisitor extends MethodVisitor {
    private final String name, desc;
    private final ClassEntry classEntry;
    private boolean isExcluded;

    MethodFilterVisitor(String name, String desc, ClassEntry classEntry) {
        super(Opcodes.ASM9);
        this.name = name;
        this.desc = desc;
        this.classEntry = classEntry;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (isExcluded) return null;
        if (Blacklists.annotationExcluded(descriptor)) {
            this.isExcluded = true;
        }
        return null;
    }

    @Override
    public void visitEnd() {
        if (!isExcluded) {
            classEntry.addMethod(name, desc);
        }
    }
}
