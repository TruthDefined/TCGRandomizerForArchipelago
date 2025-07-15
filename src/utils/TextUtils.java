package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import constants.Constants;


public class TextUtils {
    public TextUtils() {}


    /** Sets FileChannel position to start of Pokemon card text data */
	public static void init (FileChannel ch) throws IOException {
		
		ch.position(Constants.CARD_TEXT_FIRST_ID);
	}
    
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

}
