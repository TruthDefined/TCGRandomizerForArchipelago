package  logic;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import constants.Constants;
import containers.Card;
import settings.Settings;
import utils.ByteUtils;
import utils.TextUtils;
import utils.Utils;

public class MainTest {

    public static void main(String[] args) {

        try (
            RandomAccessFile fin = new RandomAccessFile(Constants.FILE_NAME_IN,  "r" );
            FileChannel chin  = fin.getChannel();
        ){
            // Settings settings = new Settings(0,0,0,0);
            // settings.Options.HP;
            
            if (ProgramLogic.verifyRom(chin) == false) throw new FileNotFoundException();
            // Create buffers exactly long enough to hold data we need.
            ByteBuffer pointerBuffer = ByteBuffer.allocate((Constants.LAST_POKEMON_DESCRIP_TEXT_POINTER_LOCATION + 3) - Constants.FIRST_POKEMON_TEXT_POINTER_LOCATION);
            ByteBuffer textBuffer = ByteBuffer.allocate(Constants.CARD_TEXT_LAST_ID - Constants.CARD_TEXT_FIRST_ID);
            ByteBuffer pokemonCardDataBuffer = ByteBuffer.allocate(Constants.PKMN_CARD_DATA_LENGTH * Constants.NUM_POKEMON_CARDS);
            // Populate buffers with data.
            ProgramLogic.populatePointerTable(chin, pointerBuffer);
            ProgramLogic.readPokemonCardsText(chin, textBuffer);
            ProgramLogic.readPokemonCardsData(chin, pokemonCardDataBuffer);

            System.out.println("=== Function Test Harness ===");

            // Test 4: Fetch real pointer from pointer index
            int textIndex = 0;
            int textPointerIndex = Constants.FIRST_POKEMON_TEXT_POINTER_CONTAINS + (textIndex * 3);
            //System.out.printf("TextIndex contains: %02X \n", textPointerIndex);
            byte[] fetchedPointer = ByteUtils.getAddressFromPointerIndex(pointerBuffer, textPointerIndex);
            //System.out.printf("addressFromPointerIndex: %02X%02X, Bank offset: %02X \n", 
            //               fetchedPointer[1], fetchedPointer[2], fetchedPointer[0]);


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
            ProgramLogic.populateCardsWithText(listOfCards,textBuffer,pointerBuffer);
            ProgramLogic.replaceNameInMovesWithPlaceholder(listOfCards);
            System.out.println("*****Pre-randomization*****");
            for(Card c : listOfCards){
                System.out.printf("Name: %s Type: %s HP: %d WR: %02X %02X Retreat: %d \n",c.getName() , c.getType(), c.getHP(), c.getWeaknessAndResistance()[0], c.getWeaknessAndResistance()[1], c.getRetreat());
                System.out.printf("Move 1: %s, Move 2: %s \n", c.getMove1().getNameText(), c.getMove2().getNameText());
                //System.out.printf("Move 2: %s, Descrip: %s \n", c.getMove2().getNameText(), c.getMove2().getDescriptionText());
                //System.out.println("WR Combo: " + c.getWeaknessAndResistance()[0] + c.getWeaknessAndResistance()[1]  );
            }
            Settings.settings.setWRRandomizationType(Settings.wrRandomType.None);
            ProgramLogic.doRandomization(listOfCards);


            System.out.println("*****Post-randomization*****");
            for(Card c : listOfCards){
                System.out.printf("Name: %s Type: %s HP: %d WR: %02X %02X Retreat: %d \n",c.getName() , c.getType(), c.getHP(), c.getWeaknessAndResistance()[0], c.getWeaknessAndResistance()[1], c.getRetreat());
                System.out.printf("Move 1: %s, Move 2: %s \n", c.getMove1().getNameText(), c.getMove2().getNameText());
                //System.out.printf("Move 2: %s, Descrip: %s \n", c.getMove2().getNameText(), c.getMove2().getDescriptionText());
            }

            //RANDOMIZE based on CARD objects


            //Replace Placeholder with NAME


            //Write data back to ROM


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
