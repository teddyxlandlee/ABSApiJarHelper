package xland.gradle.absapijarhelper;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClassEntry {
    private final String name;
    private final Set<NameAndDesc> fields, methods;

    public ClassEntry(String name) {
        this.name = name;
        this.fields = new LinkedHashSet<>();
        this.methods = new LinkedHashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return name + ".class";
    }

    public void addField(String name, String desc) {
        fields.add(new NameAndDesc(name, desc));
    }

    public void addMethod(String name, String desc) {
        methods.add(new NameAndDesc(name, desc));
    }

    @SuppressWarnings("unused")
    public Set<NameAndDesc> getFields() {
        return Collections.unmodifiableSet(fields);
    }

    public Set<NameAndDesc> getMethods() {
        return Collections.unmodifiableSet(methods);
    }

    public boolean hasField(String name, String desc) {
        return fields.contains(new NameAndDesc(name, desc));
    }

    public boolean hasMethod(String name, String desc) {
        return methods.contains(new NameAndDesc(name, desc));
    }
}
