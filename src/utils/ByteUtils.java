package utils;

public class ByteUtils {
    
    public static byte[] readBytes(byte[] source, Index index, int length) {
        if (index.value + length > source.length) {
            throw new IndexOutOfBoundsException("Not enough bytes to read " + length + " bytes.");
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

    /**
     * Converts a 2-byte little-endian pointer and a given bank number to a full 3-byte ROM address.
     *
     * @param pointer A 2-byte array in little-endian format representing the pointer (e.g., {0x5E, 0x09}).
     * @param bank The bank number that this pointer belongs to (e.g., 0x06).
     * @return The full 3-byte address in the ROM (e.g., 0x06495E).
     * @throws IllegalArgumentException if the pointer is not exactly 2 bytes.
     */
    public static int pointerToFullAddress(byte[] pointer, int bank) {
        if (pointer.length != 2) {
            throw new IllegalArgumentException("Pointer must be exactly 2 bytes.");
        }

        int offset = (pointer[1] & 0xFF) << 8 | (pointer[0] & 0xFF);
        return (bank * 0x4000) + offset;
    }


        
    /**
     * Converts a full 3-byte Game Boy address (e.g., 0x06495E) to a 2-byte little-endian pointer.
     * Assumes the address is in a switchable ROM bank and follows standard bank mapping rules.
     *
     * @param fullAddress The full 3-byte ROM address to convert (e.g., 0x06495E).
     * @return A 2-byte array in little-endian format (e.g., {0x5E, 0x09} for address 0x06495E).
     * @throws IllegalArgumentException if the address does not fall within the valid banked ROM range.
     */
    public static byte[] addressToLittleEndianPointer(int fullAddress) {
        int bank = (fullAddress >> 14) & 0xFF; // Approximate bank number
        int offset = fullAddress - (bank * 0x4000);

        if (offset < 0 || offset > 0x3FFF) {
            throw new IllegalArgumentException("Address does not fall within valid banked ROM range.");
        }

        return new byte[] {
            (byte) (offset & 0xFF),        // Low byte
            (byte) ((offset >> 8) & 0xFF)  // High byte
        };
    }

}
