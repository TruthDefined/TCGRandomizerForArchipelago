package utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import constants.Constants;

public class ByteUtils {
    
    public static byte[] readBytes(byte[] source, int index, int length) {
        if (index + length > source.length) {
            throw new IndexOutOfBoundsException("Not enough bytes to read " + length + " bytes. " + (source.length - index) + " bytes left.");
        }

        byte[] result = new byte[length];
        System.arraycopy(source, index, result, 0, length);
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
    public static byte[] readBytes(ByteBuffer buffer, int index, int length) {
        if (index + length > buffer.capacity()) {
            throw new IndexOutOfBoundsException("Not enough bytes to read " + length + " bytes."+ (buffer.capacity() - index) + " bytes left.");
        }

        byte[] result = new byte[length];
        buffer.position(index);
        buffer.get(result, 0, length);

        return result;
    }
    
    /**
     * Retrieves a 3-byte pointer from the pointer table given a pointer index from a card.
     *
     * The pointer stored in the card is not a direct address, but an index into the pointer table
     * which starts at Constants.FIRST_POKEMON_TEXT_POINTER_CONTAINS. This function adjusts the index
     * accordingly, seeks to the correct offset in the ByteBuffer, and reads the 2-byte pointer stored there.
     *
     * @param pointerBuffer     The ByteBuffer containing the pointer table.
     * @param index             The Index object representing the current pointer index from the card data.
     *                          This will be modified to point to the actual location within the pointer table.
     * @return                  A 3-byte array representing the pointer to the actual text data.
     */
    public static byte[] getAddressFromPointerIndex(ByteBuffer pointerBuffer, int index) {
        // Adjust index from card-relative to pointer table-relative
        //System.out.printf("Index: %d %02X \n" , index, index);
        int pointerTableOffset = index - pointerToIntFlipped(new byte[]{(byte)0xfc, 0x07});
        //System.out.println("Index Offset: "  + pointerTableOffset);
        pointerBuffer.rewind();
        // Read the 3-byte pointer from the buffer
        return ByteUtils.readBytes(pointerBuffer, (pointerTableOffset*3), 3);
    }

    /**
     * Converts a 2-byte little-endian byte array to an unsigned int.
     *
     * @param pointer A byte array of length 2, in little-endian order (e.g., {0x5E, 0x09}).
     * @return An int representing the 16-bit unsigned value (0 to 65535).
     * @throws IllegalArgumentException if the array is not exactly 2 bytes long.
     */
    public static int pointerToIntFlipped(byte[] pointer) {
        if (pointer == null || pointer.length != 2) {
            throw new IllegalArgumentException("Pointer must be exactly 2 bytes.");
        }
        return (pointer[1] & 0xFF) << 8 | (pointer[0] & 0xFF);
    }

    public static int writeStringToStream(String text, ByteArrayOutputStream stream)throws IOException{
        //Be sure to split up Description text by 36 symbols per line, 7 lines per desc page.

        if(text.length()<=36){
            //Name or single line description
            stream.write(Constants.START_NEW_TEXT_FIELD_BYTE);
            stream.write(text.getBytes(StandardCharsets.UTF_8));
            stream.write(Constants.END_TEXT_FIELD_BYTE);
            return 1;
        } else{
            //Description
            //TODO: Check "0x0A" newline segment length in case new pokemon name makes them too long
            // 0x20 is space, 0x0A is newline
            byte[] charArray = text.getBytes(StandardCharsets.UTF_8);
            int lastNewLine = 0;
            int numLines = 1;
            int panel2Index = 0;
            for (int i = 0; i < charArray.length; i++) {
                if(charArray[i]==0x0A){
                    if(i-lastNewLine>36){
                        for(int j = i; j>lastNewLine; j--){
                            if(charArray[j] == 0x20){
                                charArray[j] = 0x0A;
                                lastNewLine = j;
                                break;
                            }
                        }
                        charArray[i] = 0x20;
                    } else lastNewLine = i;
                    numLines++;
                    if(numLines == 8){ 
                        panel2Index = i;
                    }
                }
            }
            if(numLines <=7){
                //Single Panel Description
                stream.write(Constants.START_NEW_TEXT_FIELD_BYTE);
                stream.write(charArray);
                stream.write(Constants.END_TEXT_FIELD_BYTE);
                return 1;
            } else{
                //Double Panel Description
                byte[] desc1 = Arrays.copyOfRange(charArray, 0, panel2Index-1);
                byte[] desc2 = Arrays.copyOfRange(charArray, panel2Index, charArray.length);
                stream.write(Constants.START_NEW_TEXT_FIELD_BYTE);
                stream.write(desc1);
                stream.write(Constants.END_TEXT_FIELD_BYTE);
                stream.write(Constants.START_NEW_TEXT_FIELD_BYTE);
                stream.write(desc2);
                stream.write(Constants.END_TEXT_FIELD_BYTE);
                return 2;
            }
            
        }
    }
    /**
     * Converts a 3-byte array to an int, assuming big-endian order.
     * The most significant byte is at index 0.
     * This method effectively left-shifts the bytes and combines them.
     */
    public static int convertBytesToInt(byte[] bytes) {
        if (bytes == null || bytes.length != 3) {
            throw new IllegalArgumentException("Byte array must be of length 3.");
        }

        int value = 0;
        // Shift the first byte to the most significant position
        value |= (bytes[0] & 0xFF) << 16; 
        // Shift the second byte to the middle position
        value |= (bytes[2] & 0xFF) << 8; 
        // Add the third byte to the least significant position
        value |= (bytes[1] & 0xFF); 

        return value;
    }
    public static byte[] intToThreeBytes(int value) {
        byte[] bytes = new byte[3];
        //Switch 1 and 2s
        // Extract the most significant byte (bits 16-23)
        bytes[0] = (byte) ((value >> 16) & 0xFF); 
        // Extract the middle byte (bits 8-15)
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        // Extract the least significant byte (bits 0-7)
        bytes[1] = (byte) (value & 0xFF);

        return bytes;
    }
    public static void printByteBufferBytes(ByteBuffer buffer) {
        // Prepare the buffer for reading by setting position to 0 and limit to current position
        // This ensures all data written to the buffer is available for reading.
        buffer.flip(); 

        System.out.print("Bytes in ByteBuffer: [");
        while (buffer.hasRemaining()) {
            // Get the next byte
            byte b = buffer.get();
            // Print the byte, potentially formatted as a hexadecimal value for clarity
            System.out.printf("0x%02X", b); 
            if (buffer.hasRemaining()) {
                System.out.print(", ");
            }
        }
        System.out.println("]");

        // Rewind the buffer if you need to read from it again later
    }
    public static void printByteBufferBytes(ByteBuffer buffer, int position, int length) {
        // Prepare the buffer for reading by setting position to 0 and limit to current position
        // This ensures all data written to the buffer is available for reading.
        buffer.position(position);
        
        System.out.print("Bytes in ByteBuffer: [");
        while (buffer.hasRemaining() && length>0) {
            // Get the next byte
            byte b = buffer.get();
            // Print the byte, potentially formatted as a hexadecimal value for clarity
            System.out.printf("0x%02X", b); 
            if (buffer.hasRemaining()) {
                System.out.print(", ");
            }
            length--;
        }
        System.out.println("]");

        // Rewind the buffer if you need to read from it again later
    }
    public static void printBytes(byte[] bytes) {
        if (bytes == null) {
            System.out.println("Byte array is null.");
            return;
        }

        System.out.print("[ ");
        for (byte b : bytes) {
            // Format each byte as a two-digit hexadecimal number, padded with a leading zero if necessary
            System.out.printf("0x%02X ", b); 
        }
        System.out.println("]");
    }

}
