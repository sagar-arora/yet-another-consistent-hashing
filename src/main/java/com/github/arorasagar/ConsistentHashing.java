package com.github.arorasagar;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ConsistentHashing<T extends Node> {

    boolean add(T node);

    boolean addAll(Collection<T> nodes);

    boolean contains(T node);

    boolean remove(T node);

    Set<T> getNodes();

    Optional<T> locate(String key);

    Set<T> locate(String key, int count);

    int size();
}
