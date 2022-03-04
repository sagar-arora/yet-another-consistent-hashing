package com.github.arorasagar;

public interface Hasher {

    long getHash(String key, long seed);
}
