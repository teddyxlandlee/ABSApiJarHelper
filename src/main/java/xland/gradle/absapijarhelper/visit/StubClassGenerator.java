package xland.gradle.absapijarhelper.visit;

import org.objectweb.asm.*;
import xland.gradle.absapijarhelper.ClassEntry;
import xland.gradle.absapijarhelper.ClassPool;

import java.util.Objects;

public class StubClassGenerator extends ClassVisitor {
    private final ClassPool cp;
    private ClassEntry entry;
    
    public StubClassGenerator(ClassPool cp, ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
        this.cp = cp;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.entry = Objects.requireNonNull(cp.get(name), name);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitNestHost(String nestHost) {
        if (cp.hasClass(nestHost)) {
            super.visitNestHost(nestHost);
        }
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        if (cp.hasMethodNullable(owner, name, descriptor)) {
            super.visitOuterClass(owner, name, descriptor);
        }
    }

    @Override
    public void visitNestMember(String nestMember) {
        if (cp.hasClass(nestMember)) {
            super.visitNestMember(nestMember);
        }
    }

    @Override
    public void visitPermittedSubclass(String permittedSubclass) {
        if (cp.hasClass(permittedSubclass)) {
            super.visitPermittedSubclass(permittedSubclass);
        }
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        final boolean hasInner = cp.hasClass(name);
        final boolean hasOuter = cp.hasClass(outerName);
        if (hasInner != hasOuter) return;

        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        if (entry.hasField(name, descriptor)) {
            return super.visitRecordComponent(name, descriptor, signature);
        }
        return null;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (entry.hasField(name, descriptor)) {
            return super.visitField(access, name, descriptor, signature, value);
        }
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (!entry.hasMethod(name, descriptor)) return null;
        final MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new StubMethodGenerator(mv);
    }
}
