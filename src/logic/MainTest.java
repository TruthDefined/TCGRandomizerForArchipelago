package  logic;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import constants.Constants;
import containers.Card;
import utils.ByteUtils;
import utils.TextUtils;
import utils.Utils;

public class MainTest {

    public static void main(String[] args) {

        try (
            RandomAccessFile fin = new RandomAccessFile(Constants.FILE_NAME_IN,  "r" );
            FileChannel chin  = fin.getChannel();
        ){
            if (ProgramLogic.verifyRom(chin) == false) throw new FileNotFoundException();
            // Create buffers exactly long enough to hold data we need.
            ByteBuffer pointerBuffer = ByteBuffer.allocate((Constants.LAST_POKEMON_DESCRIP_TEXT_POINTER_LOCATION + 2) - Constants.FIRST_POKEMON_TEXT_POINTER_LOCATION);
            ByteBuffer textBuffer = ByteBuffer.allocate(Constants.CARD_TEXT_LAST_ID - Constants.CARD_TEXT_FIRST_ID);
            ByteBuffer pokemonCardDataBuffer = ByteBuffer.allocate(Constants.PKMN_CARD_DATA_LENGTH * Constants.NUM_POKEMON_CARDS);
            // Populate buffers with data.
            ProgramLogic.populatePointerTable(chin, pointerBuffer);
            ProgramLogic.readPokemonCardsText(chin, textBuffer);
            ProgramLogic.readPokemonCardsData(chin, pokemonCardDataBuffer);

            System.out.println("=== Function Test Harness ===");
            /*
            // // Test 1: Convert a 2-byte pointer to int
            // byte[] pointer = new byte[] {(byte) 0x02, 0x52, 0x35};
            // int offset = ByteUtils.pointerToInt(new byte[] {pointer[0], pointer[1]});
            // System.out.println("twoByteToInt: " + (byte)offset);  // Expected: 2058

            // // Test 2: Convert bank + pointer to address
            // int address = ByteUtils.pointerToFullAddress(pointer);
            // System.out.printf("bankedPointerToAddress (bank 0x%02X, pointer %02X%02X): 0x%05X\n",
            //                 pointer[0], pointer[2], pointer[1], address);

            // // Test 3: Convert address back to pointer/bank
            // int testAddress = 0x57552;
            // byte[] backPointer = ByteUtils.addressToLittleEndianPointer(testAddress);
            // System.out.printf("addressToPointer: %02X%02X, bank: 0x%02X\n", 
            //                 backPointer[1], backPointer[2], backPointer[0]);
*/
            // Test 4: Fetch real pointer from pointer index
            int textIndex = 0;
            int textPointerIndex = Constants.FIRST_POKEMON_TEXT_POINTER_CONTAINS + (textIndex * 3);
            System.out.printf("TextIndex contains: %02X \n", textPointerIndex);
            ByteUtils.Index indexContainer = new ByteUtils.Index(textPointerIndex);
            byte[] fetchedPointer = ByteUtils.getAddressFromPointerIndex(pointerBuffer, indexContainer);
            System.out.printf("addressFromPointerIndex: %02X%02X, Bank offset: %02X \n", 
                            fetchedPointer[1], fetchedPointer[2], fetchedPointer[0]);


            // Test 5: Fetch text from mock pointer and text buffer
            String result = TextUtils.returnStringFromBankAndPointer(textBuffer, fetchedPointer);
            System.out.println("returnStringFromBankAndPointer result: " + result);
            //Bulbasaur Pointer - 0a 08     Text Location - 0x57552    Pointer Location - 0x3581D    Pointer Data - 0x02 5235
            pokemonCardDataBuffer.rewind();
            
            //TODO:
            //I've got some issues here reading things in.
            //Probably related to the Index container. I should really Axe that.
            Card[] listOfCards = ProgramLogic.arrayOfCards(pokemonCardDataBuffer);
            System.out.println("Cards in List: " + listOfCards.length);
            ProgramLogic.populateCardsWithText(listOfCards,textBuffer);



        }
        catch (FileNotFoundException e) {
                Utils.print("One or more needed files are missing.\n\n"
                                            + Constants.FILE_NAME_IN + " should be in the same directory as the jar file "
                        + "and a valid Pokemon TCG ROM.\n"
                        + "Required ROM: "
                        + "Pok\u00e9mon Trading Card Game (U) [C][!].gbc md5: 219b2cc64e5a052003015d4bd4c622cd");
                                                    
                
        } catch (IOException e) {
            Utils.print("An unexpected error has occurred. Try again maybe?");
        }
    }
}
