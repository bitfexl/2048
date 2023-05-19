package com.github.bitfexl._2048;

import java.util.Arrays;

/**
 * A wrapper for an array of bytes.
 * Implements equals and hashCode.
 * @param value The byte array.
 */
public record ByteArrayWrapper(byte[] value) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteArrayWrapper that = (ByteArrayWrapper) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
