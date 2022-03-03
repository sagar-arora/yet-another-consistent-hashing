package com.github.arorasagar;

import net.openhft.hashing.LongHashFunction;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum DefaultHasher implements Hasher {

    MURMUR_HASH(LongHashFunction.murmur_3());

    LongHashFunction longHashFunction;
    DefaultHasher(LongHashFunction longHashFunction) {
        this.longHashFunction = longHashFunction;
    }

    long getHash(String key) {

        return -1;
    }

    @Override
    public long getHash(String key, int seed) {
        return 0;
    }
}
