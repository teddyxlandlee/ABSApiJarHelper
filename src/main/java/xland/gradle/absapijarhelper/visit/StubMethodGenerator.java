package xland.gradle.absapijarhelper.visit;

import org.objectweb.asm.*;

final class StubMethodGenerator extends MethodVisitor {
    private final MethodVisitor realDelegate;

    StubMethodGenerator(MethodVisitor mv) {
        super(Opcodes.ASM9);
        this.realDelegate = mv;
    }

    @Override
    public void visitParameter(String name, int access) {
        realDelegate.visitParameter(name, access);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return realDelegate.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return realDelegate.visitAnnotation(descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return realDelegate.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        realDelegate.visitAnnotableParameterCount(parameterCount, visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        return realDelegate.visitParameterAnnotation(parameter, descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        realDelegate.visitAttribute(attribute);
    }

    private static final String T_CCE = Type.getInternalName(IncompatibleClassChangeError.class);
    private static final String ERROR_MESSAGE = "This is the API jar.\n" +
            "Obtain source code for the implementation, or the Mod for testing.";

    @Override
    public void visitCode() {
        realDelegate.visitCode();
        // Generate stub
        realDelegate.visitTypeInsn(Opcodes.NEW, T_CCE);
        realDelegate.visitInsn(Opcodes.DUP);
        realDelegate.visitLdcInsn(ERROR_MESSAGE);
        realDelegate.visitMethodInsn(Opcodes.INVOKESPECIAL, T_CCE, "<init>", "(Ljava/lang/String;)V", false);
        realDelegate.visitMaxs(-1, -1);
    }

    @Override
    public void visitEnd() {
        realDelegate.visitEnd();
    }
}
