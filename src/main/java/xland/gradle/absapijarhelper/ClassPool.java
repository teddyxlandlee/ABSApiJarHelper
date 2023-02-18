package xland.gradle.absapijarhelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ClassPool implements Iterable<ClassEntry> {
    private final Map<String, ClassEntry> map;

    public ClassPool() {
        this.map = new LinkedHashMap<>();
    }

    public void add(ClassEntry classEntry) {
        map.put(classEntry.getName(), classEntry);
    }

    public ClassEntry get(String className) {
        return map.get(className);
    }

    public boolean hasClass(String s) {
        return map.containsKey(s);
    }

    public boolean hasMethodNullable(String owner, @Nullable String name, @Nullable String desc) {
        if (!hasClass(owner)) return false;
        if (name == null && desc == null) return true;

        return this.get(owner).getMethods().stream()
                .anyMatch(n -> {
                    if (Objects.equals(n.getName(), name)) {
                        return desc == null || Objects.equals(n.getDesc(), desc);
                    }
                    return false;
                });
    }

    @NotNull
    @Override
    public Iterator<ClassEntry> iterator() {
        return map.values().iterator();
    }
}
