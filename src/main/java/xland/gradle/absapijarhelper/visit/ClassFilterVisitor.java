package xland.gradle.absapijarhelper.visit;

import org.objectweb.asm.*;
import xland.gradle.absapijarhelper.ClassEntry;

import java.util.function.Consumer;

public class ClassFilterVisitor extends ClassVisitor {
    private final Consumer<? super ClassEntry> entryConsumer;
    private ClassEntry entry;
    private boolean isExcluded;

    public ClassFilterVisitor(Consumer<? super ClassEntry> entryConsumer) {
        super(Opcodes.ASM9);
        this.entryConsumer = entryConsumer;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (Blacklists.accessExcluded(access)) {
            this.isExcluded = true;
            return;
        }
        this.entry = new ClassEntry(name);
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
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (isExcluded) return null;
        if (Blacklists.accessExcluded(access)) {
            this.isExcluded = true;
            return null;
        }
        return new FieldFilterVisitor(name, descriptor, entry);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (isExcluded) return null;
        if (Blacklists.accessExcluded(access)) {
            this.isExcluded = true;
            return null;
        }
        return new MethodFilterVisitor(name, descriptor, entry);
    }

    @Override
    public void visitEnd() {
        if (!isExcluded) {
            this.entryConsumer.accept(entry);
        }
    }
}
