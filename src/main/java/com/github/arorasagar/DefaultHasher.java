package com.github.arorasagar;

import net.openhft.hashing.LongHashFunction;

import java.util.Comparator;
import java.util.function.Function;

public enum DefaultHasher implements Hasher {

    MURMUR_HASH(LongHashFunction::murmur_3);

    Function<Long, LongHashFunction> hashFunction;

    DefaultHasher(Function<Long, LongHashFunction> hashFunction) {
        this.hashFunction = hashFunction;
    }

    @Override
    public long getHash(String key, long seed) {
        return hashFunction.apply(seed).hashChars(key);
    }
}
