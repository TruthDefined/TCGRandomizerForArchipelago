package utils;

public class ByteUtils {
    public static Object readBytes(byte[] source, Index index, int length) {
        if (index.value + length > source.length) {
            throw new IndexOutOfBoundsException("Not enough bytes to read " + length + " bytes.");
        }

        if (length == 1) {
            return source[index.value++];
        }

        byte[] result = new byte[length];
        System.arraycopy(source, index.value, result, 0, length);
        index.value += length;
        return result;
    }

    // Simple wrapper for passing index by reference
    public static class Index {
        public int value;
        public Index(int value) {
            this.value = value;
        }
    }
}
