package com.github.arorasagar;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class ConsistentHashingImpl<T extends Node> implements ConsistentHashing<T> {

    @Override
    public boolean add(Node node) {
        return false;
    }

    @Override
    public boolean addAll(Collection nodes) {
        return false;
    }

    @Override
    public boolean contains(Node node) {
        return false;
    }

    @Override
    public boolean remove(Node node) {
        return false;
    }

    @Override
    public Set getNodes() {
        return null;
    }

    @Override
    public Optional locate(String key) {
        return Optional.empty();
    }

    @Override
    public Set locate(String key, int count) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

}
