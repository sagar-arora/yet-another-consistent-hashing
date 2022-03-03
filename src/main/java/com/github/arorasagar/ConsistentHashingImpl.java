package com.github.arorasagar;

import java.util.*;

public class ConsistentHashingImpl<T extends Node> implements ConsistentHashing<T> {

    private final int multiplicity;
    private final Hasher hasher;
    private final Map<T, Set<Partition<T>>> nodes = new HashMap<>();
    private final NavigableMap<Long, Partition<T>> ring = new TreeMap<>();

    public ConsistentHashingImpl(Hasher hasher, int multiplicity) {
        this.multiplicity = multiplicity;
        this.hasher = hasher;
    }

    @Override
    public boolean add(T node) {

        if (!nodes.containsKey(node)) {
            List<Partition<T>> partitions = new ArrayList<>();
            for (int i = 0; i < multiplicity; i++) {
                partitions.add(new ReplicationPartition<>(node, i));
            }

            for (Partition<T> partition : partitions) {
                String key = partition.getPartitionKey();
                int i = 0;
                long hash = -1;
                do {
                    hash = hasher.getHash(key, i++);
                } while (ring.containsKey(hash));
                partition.setSlot(hash);
                ring.put(hash, partition);
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<T> nodes) {

        for (T node : nodes) {
            add(node);
        }

        return true;
    }

    @Override
    public boolean contains(T node) {
        return nodes.containsKey(node);
    }

    @Override
    public boolean remove(T node) {

        if (!nodes.containsKey(node)) return false;

        Set<Partition<T>> partitions = nodes.get(node);

        for (Partition<T> partition: partitions) {
            long slot = partition.getSlot();
            ring.remove(slot);
        }

        return true;
    }

    @Override
    public Set<T> getNodes() {
        return null;
    }

    @Override
    public Optional<T> locate(String key) {
        return Optional.empty();
    }

    @Override
    public Set<T> locate(String key, int count) {
        return null;
    }

    @Override
    public int size() {
        return nodes.size();
    }
}
