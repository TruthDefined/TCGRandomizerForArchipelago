package utils;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import constants.Constants;
import containers.BinaryCard;


public class TextUtils {
    public TextUtils() {}



    public static List<String> extractStrings(ByteBuffer buffer) {
        List<String> strings = new ArrayList<>();
        ByteArrayOutputStream currentString = new ByteArrayOutputStream();

        while (buffer.remaining() >= 2) {
            byte b1 = buffer.get();
            byte b2 = buffer.get();
            
            if ((b1 == Constants.NEXT_ENTRY[0]) && (b2 == Constants.NEXT_ENTRY[1])) {
            strings.add(new String(currentString.toByteArray(), StandardCharsets.UTF_8));
            currentString.reset();
            } else {
                currentString.write(b1);
                buffer.position(buffer.position() - 1); // back up one byte
            }
            if ((b1 == Constants.END_DATA[0]) && (b2 == Constants.END_DATA[1])){
                strings.add(new String(currentString.toByteArray(), StandardCharsets.UTF_8));
                break;
            } 
        }

        return strings;
    }

    public static String extractStringFromCardPointer(
        ByteBuffer textData,
        BinaryCard card,
        Function<BinaryCard, byte[]> pointerGetter
        //int baseAddress 
    ) {
        byte[] pointer = pointerGetter.apply(card);

        if (pointer == null || pointer.length != 2) {
            return "Pointer Length Incorrect";
        }
        int bank = 0x15;
        // Convert pointer to full ROM address using the base address
        int targetAddress = ByteUtils.pointerToFullAddress(pointer,bank);
        
        // Adjust the target address relative to the start of textData
        //int offsetInBuffer = targetAddress - baseAddress;
        int offsetInBuffer = targetAddress;
        System.out.println(offsetInBuffer);
        if (offsetInBuffer < 0 || offsetInBuffer >= textData.capacity()) {
            
            return "Pointer Out Of Range";
        }
        
        // Prepare to read the string from the correct position
        textData.position(offsetInBuffer);
        StringBuilder sb = new StringBuilder();

        while (textData.remaining() >= 2) {
            byte b1 = textData.get();
            byte b2 = textData.get();

            if (b1 == Constants.NEXT_ENTRY[0] && b2 == Constants.NEXT_ENTRY[1]) {
                break;
            }

            sb.append((char) b1); // Replace with a custom decode function if needed
            textData.position(textData.position() - 1); // Backtrack to not skip the second byte
        }

        return sb.toString();
    }

}
