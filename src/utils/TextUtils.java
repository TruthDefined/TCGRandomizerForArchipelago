package utils;

import java.nio.ByteBuffer;

import constants.Constants;
import containers.Card;


public class TextUtils {
    public TextUtils() {}

    public static String returnStringFromBankAndPointer(ByteBuffer textBuffer, byte[] pointer) {
        if (pointer == null || pointer.length != 3) {
            return "Invalid pointer";
        }

        // Convert little-endian 2-byte pointer to offset
        int offset = ((pointer[2] & 0xFF) << 8) | (pointer[1] & 0xFF);
        int bank = pointer[0] + 0x13;
        if(pointer[1] == 0x00 && pointer[2] == 0x00){
            bank = (bank & 0xFF) + 0x01;
        }
        //Jumps from bank 15 to 19???
        if (bank==0x16) bank = 0x19;
        //System.out.printf("Bank: %02X, Ptr: %02X \n", bank, offset);
        //TODO: URGENT FIX. pointer issue at 0x64000 bank change
        // Compute the absolute ROM address
        int address;
        address = ((bank & 0xFF) * (0x4000 & 0xFFFF)) + (offset & 0xFFFF);
        int adjustedAddress = (address & 0xFFFFFF) - (Constants.ENERGY_CARD_TEXT_FIRST_ID & 0xFFFFFF);
        //System.out.printf("Original Address: %02X, Adjusted Address: %02X \n", address, adjustedAddress);

        return retrieveString(textBuffer, adjustedAddress);  
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

    // public static String extractTextField(ByteBuffer buffer, int location) {
    //     // Make a copy of the buffer to avoid mutating the original
    //     ByteBuffer bb = buffer.duplicate();
    //     bb.position(location - 0x0a08);

    //     // Check for start byte
    //     byte startByte = bb.get();
    //     if ((startByte & 0xFF) != Constants.START_NEW_TEXT_FIELD_BYTE) {
    //         throw new IllegalArgumentException(String.format("Start byte mismatch at %X. Expected: %02X, Found: %02X", location, Constants.START_NEW_TEXT_FIELD_BYTE, startByte));
    //     }

    //     StringBuilder result = new StringBuilder();

    //     // Read until END_TEXT_FIELD_BYTE or end of buffer
    //     while (bb.hasRemaining()) {
    //         byte current = bb.get();
    //         if ((current & 0xFF) == Constants.END_TEXT_FIELD_BYTE) {
    //             break;
    //         }
    //         result.append((char) (current & 0xFF));
    //     }

    //     // If we reached the end of buffer without finding END_TEXT_FIELD_BYTE
    //     if ((result.length() == 0 || bb.position() == buffer.limit()) && (result.length() > 0 && result.charAt(result.length() - 1) != Constants.END_TEXT_FIELD_BYTE)) {
    //         throw new IllegalStateException("End byte not found for text field.");
    //     }

    //     return result.toString();
    // }

    public static void printCardData(Card card){
        if(card.getCardType()==Card.CardType.Pokemon){
            System.out.printf("%s, %s: %s \n", card.getNameText(), card.getKindText(), card.getDescText());
            System.out.printf("%s: %s  \n", card.getMove1().getNameText(), card.getMove1().getDescriptionText());
            System.out.printf("%s: %s  \n", card.getMove2().getNameText(), card.getMove2().getDescriptionText());

        }
        else{
            System.out.printf("%s: %s \n", card.getNameText(), card.getDescText());
        }

    }


    private static char decodeChar(byte b) {
        // Replace with your actual character map
        return (char) (b & 0xFF);
    }

}
