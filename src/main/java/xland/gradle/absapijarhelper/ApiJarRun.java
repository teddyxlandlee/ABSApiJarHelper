package xland.gradle.absapijarhelper;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import xland.gradle.absapijarhelper.visit.ClassFilterVisitor;
import xland.gradle.absapijarhelper.visit.StubClassGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

@SuppressWarnings("unused") // API
public class ApiJarRun implements BiConsumer<File, File> {
    private static void fillClassPool(JarFile jarFile, ClassPool cp) throws IOException {
        for (JarEntry entry : wrapEnumeration(jarFile.entries())) {
            if (!entry.getName().endsWith(".class")) continue;

            ClassReader cr;
            try (final InputStream is = jarFile.getInputStream(entry)) {
                cr = new ClassReader(is);
            }
            ClassFilterVisitor cv = new ClassFilterVisitor(cp::add);
            cr.accept(cv, 7 /*Skip Code, Debug, Frames*/);
        }
    }

    @Override
    public void accept(File in, File out) {
        ClassPool cp = new ClassPool();
        try (JarFile jarFile = new JarFile(in)) {
            fillClassPool(jarFile, cp);
            final Manifest manifest = jarFile.getManifest();

            try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(out.toPath()), manifest)) {
                for (ClassEntry classEntry : cp) {
                    JarEntry jarEntry = jarFile.getJarEntry(classEntry.getFilename());
                    Objects.requireNonNull(jarEntry, classEntry::getFilename);

                    ClassReader cr;
                    try (final InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                        cr = new ClassReader(inputStream);
                    }
                    ClassWriter cw = new ClassWriter(3);
                    ClassVisitor cv = new StubClassGenerator(cp, cw);
                    cr.accept(cv, 0);

                    jos.putNextEntry(jarEntry);
                    jos.write(cw.toByteArray());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static <T> Iterable<? extends T> wrapEnumeration(Enumeration<T> e) {
        return () -> new Iterator<T>() {
            @Override public boolean hasNext() {
                return e.hasMoreElements();
            }
            @Override public T next() {
                return e.nextElement();
            }
        };
    }
}
