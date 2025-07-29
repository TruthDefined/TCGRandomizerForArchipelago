package utils;

import java.nio.ByteBuffer;

import constants.Constants;


public class TextUtils {
    public TextUtils() {}
     /**
     * Extracts a string from the text buffer using a 2-byte pointer and a specified bank.
     * It searches forward from the resolved address until it finds a 0x06 start marker before reading text.
     *
     * @param textBuffer    ByteBuffer containing all text data across banks.
     * @param bank          The bank number (e.g., 0x15–0x19) where the pointer's target lies.
     * @param pointer       The 2-byte little-endian pointer value (e.g., {0x5E, 0x09} for 0x495E).
     * @return              The decoded string from the ROM's text encoding.
     */
    public static String returnStringFromBankAndPointer(ByteBuffer textBuffer, byte[] pointer) {
        if (pointer == null || pointer.length != 3) {
            return "Invalid pointer";
        }

        // Convert little-endian 2-byte pointer to offset
        int offset = ((pointer[2] & 0xFF) << 8) | (pointer[1] & 0xFF);
        int bank = pointer[0] + 0x13;

        // Compute the absolute ROM address
        int address = ((bank & 0xFF) * 0x4000) + (offset);

        address = address - Constants.CARD_TEXT_FIRST_ID;

        return retrieveString(textBuffer, address);  
    }

    private static String retrieveString(ByteBuffer textBuffer,int address){
        // Advance to the start marker 0x06
        textBuffer.position(address);
        
        byte marker = textBuffer.get();
        if ((marker & 0xFF) == Constants.START_NEW_TEXT_FIELD_BYTE) {
            // Read characters until 0x00 terminator is found
            StringBuilder result = new StringBuilder();
            while (textBuffer.hasRemaining()) {
                byte b = textBuffer.get();

                if ((b & 0xFF) == Constants.END_TEXT_FIELD_BYTE) {
                    break;
                }

                result.append(decodeChar(b)); // Replace with your decoder logic
            }
            return result.toString();   
            }
        else{
            return "Pointer position not set to start of string.";
        }

    }

    private static char decodeChar(byte b) {
        // Replace with your actual character map
        return (char) (b & 0xFF);
    }
}
