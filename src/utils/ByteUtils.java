package utils;
import java.nio.ByteBuffer;

import constants.Constants;
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

    /**
     * Reads a specified number of bytes from a ByteBuffer starting at a tracked index.
     *
     * @param buffer The ByteBuffer to read from.
     * @param index  The mutable Index object used to track the current position.
     * @param length The number of bytes to read.
     * @return A byte array containing the read bytes.
     * @throws IndexOutOfBoundsException if there are not enough bytes left in the buffer.
     */
    private static byte[] readBytes(ByteBuffer buffer, Index index, int length) {
        if (index.value + length > buffer.capacity()) {
            throw new IndexOutOfBoundsException("Not enough bytes to read " + length + " bytes.");
        }

        byte[] result = new byte[length];
        buffer.position(index.value);
        buffer.get(result, 0, length);
        index.value += length;

        return result;
    }

    /**
     * Converts a 2-byte little-endian pointer and a given bank number to a full 3-byte ROM address.
     *
     * @param pointer A 2-byte array in little-endian format representing the pointer (e.g., {0x5E, 0x09}).
     * @param bank The bank number that this pointer belongs to (e.g., 0x06).
     * @return The full 3-byte address in the ROM (e.g., 0x06495E).
     * @throws IllegalArgumentException if the pointer is not exactly 2 bytes.
     */
    public static int pointerToFullAddress(byte[] pointer) {
        if (pointer.length != 3) {
            throw new IllegalArgumentException("Pointer must be exactly 3 bytes.");
        }

        int offset = (pointer[2] & 0xFF) << 8 | (pointer[1] & 0xFF);
        return (pointer[0] * 0x4000) + offset;
    }
       
    /**
     * Converts a full 3-byte Game Boy address (e.g., 0x06495E) to a 2-byte little-endian pointer.
     * Assumes the address is in a switchable ROM bank and follows standard bank mapping rules.
     *
     * @param fullAddress The full 3-byte ROM address to convert (e.g., 0x06495E).
     * @return A 3-byte array. 1 Byte of Bank information, and 2-byte in little-endian format (e.g., {0x15, 0x5E, 0x09} for address 0x06495E).
     * @throws IllegalArgumentException if the address does not fall within the valid banked ROM range.
     */
    public static byte[] addressToLittleEndianPointer(int fullAddress) {
        int bank = (fullAddress >> 14) & 0xFF; // Approximate bank number
        int offset = fullAddress - (bank * 0x4000);

        if (offset < 0 || offset > 0x3FFF) {
            throw new IllegalArgumentException("Address does not fall within valid banked ROM range.");
        }

        return new byte[] {
            (byte) (bank),
            (byte) (offset & 0xFF),        // Low byte
            (byte) ((offset >> 8) & 0xFF)  // High byte1
        };
    }
    
    
    /**
     * Retrieves a 2-byte pointer from the pointer table given a pointer index from a card.
     *
     * The pointer stored in the card is not a direct address, but an index into the pointer table
     * which starts at Constants.FIRST_POKEMON_TEXT_POINTER_CONTAINS. This function adjusts the index
     * accordingly, seeks to the correct offset in the ByteBuffer, and reads the 2-byte pointer stored there.
     *
     * @param pointerBuffer     The ByteBuffer containing the pointer table.
     * @param index             The Index object representing the current pointer index from the card data.
     *                          This will be modified to point to the actual location within the pointer table.
     * @return                  A 2-byte array representing the pointer to the actual text data.
     */
    public static byte[] getAddressFromPointerIndex(ByteBuffer pointerBuffer, Index index) {
        // Adjust index from card-relative to pointer table-relative
        System.out.println("Card Index: "  + index.value);
        int pointerTableOffset = index.value - Constants.FIRST_POKEMON_TEXT_POINTER_CONTAINS;
        
        // Update buffer's read index to where the actual 2-byte pointer lives
        // Adding 1 targets the actual data and not the buffer byte
        index.value = pointerTableOffset;

        // Read the 2-byte pointer from the buffer
        return ByteUtils.readBytes(pointerBuffer, index, 3);
    }

    /**
     * Converts a 2-byte little-endian byte array to an unsigned int.
     *
     * @param pointer A byte array of length 2, in little-endian order (e.g., {0x5E, 0x09}).
     * @return An int representing the 16-bit unsigned value (0 to 65535).
     * @throws IllegalArgumentException if the array is not exactly 2 bytes long.
     */
    public static int pointerToInt(byte[] pointer) {
        if (pointer == null || pointer.length != 2) {
            throw new IllegalArgumentException("Pointer must be exactly 2 bytes.");
        }

        return (pointer[1] & 0xFF) << 8 | (pointer[0] & 0xFF);
    }

    // Simple wrapper for passing index by reference
    public static class Index {
        public int value;
        public Index(int value) {
            this.value = value;
        }
    }

}
