package com.github.arorasagar;

interface Partition<T extends Node> {

    T getNode();

    void setSlot(long slot);

    long getSlot();

    String getPartitionKey();
}
