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
        return nodes.keySet();
    }

    @Override
    public Optional<T> locate(String key) {
        if (ring.isEmpty()) {
            return Optional.empty();
        }
        return locate(key, 1).stream().findFirst();
    }

    @Override
    public Set<T> locate(String key, int count) {
        Set<T> result = new LinkedHashSet<>();

        if (key != null && count > 0) {
            long hash = hasher.getHash(key, 1);
            if (count < nodes.size()) {
                CircularIterator circularIterator = new CircularIterator(hash);

                while (circularIterator.hasNext()) {
                    Partition<T> partition = circularIterator.next();
                    result.add(partition.getNode());
                }
            } else {
                result.addAll(nodes.keySet());
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConsistentHashingImpl.class.getSimpleName() + "[", "]")
                .add("nodes= " + nodes.size())
                .add("hasher= " + hasher)
                .add("multiplicity= " + multiplicity)
                .toString();
    }

    @Override
    public int size() {
        return nodes.size();
    }

    class CircularIterator implements Iterator<Partition<T>> {

        private final Iterator<Partition<T>> tail;
        private final Iterator<Partition<T>> head;

        public CircularIterator(long slot) {
            head = ring.headMap(slot, false).values().iterator();
            tail = ring.tailMap(slot, true).values().iterator();
        }

        @Override
        public boolean hasNext() {
            return tail.hasNext() || head.hasNext();
        }

        @Override
        public Partition<T> next() {
            return tail.hasNext() ? tail.next() : head.next();
        }
    }
}
