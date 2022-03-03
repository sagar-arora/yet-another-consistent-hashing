package com.github.arorasagar;

import java.util.Objects;

public class ReplicationPartition<T extends Node> implements Partition<T> {

    private final T node;
    private final int index;
    private long slot;

    public ReplicationPartition(T node, int index) {
        this.node = node;
        this.index = index;
    }

    @Override
    public T getNode() {
        return null;
    }

    @Override
    public void setSlot(long slot) {
        this.slot = slot;
    }

    @Override
    public long getSlot() {
        return this.slot;
    }

    @Override
    public String getPartitionKey() {
        return node.getKey() +
                "," +
                index;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ReplicationPartition)) {
            return false;
        }

        ReplicationPartition<T> that = (ReplicationPartition<T>) o;

        return this.slot == that.slot
                && this.index == that.index
                && Objects.equals(this.node, that.getNode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.index, this.node, this.slot);
    }

}
