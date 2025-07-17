package constants;

import constants.Fields.CardFields;
import constants.Fields.MoveFields;

public class Constants {

	public static final String FILE_NAME_IN  = "tcg.gbc";
	public static final String FILE_NAME_OUT = "tcgrandomized_";
	public static final String FILE_NAME_OUT_SUFFIX = "_.gbc";
	
        //ROM location of first Pokemon card following energy cards
	public static final int POKEMON_CARDS = 0x30e28;
        
        //Each card in the gsme is assigned a unique one-byte ID
	public static final int ENERGY_FIRST_ID  = 0x01;
	public static final int POKEMON_FIRST_ID = 0x08;
	public static final int TRAINER_FIRST_ID = 0xc3;
        
        //There are 228 unique cards, counting the two "illusion" cards
	public static final int NUM_CARDS         = 0xe4;
	public static final int NUM_POKEMON_CARDS = TRAINER_FIRST_ID - POKEMON_FIRST_ID;
	
        /*Each card type has a certain number of one-byte fields that represent 
          various attributes and behaviors. Likewise for individual moves.*/
	public static final int PKMN_CARD_DATA_LENGTH   = CardFields.END.getOffset() - CardFields.START.getOffset();
	public static final int PKMN_MOVE_DATA_LENGTH   = MoveFields.END.getOffset() - MoveFields.START.getOffset();
	public static final int TRN_CARD_DATA_LENGTH    = 0x0e;
	public static final int ENERGY_CARD_DATA_LENGTH = 0x0e;
	public static final int UNUSED_EFFECT_BEHAVIOR_START = 0x2ff03; //253 Bytes of extra effect code space
	public static final int UNUSED_EFFECT_BEHAVIOR_END = 0x2ffff;

    	//ROM location of first Pokemon card text entry    
	public static final int CARD_TEXT_FIRST_ID = 0x57553;
	public static final int CARD_TEXT_LAST_ID = 0x6fff0;
	//Dragonite name @ 63344

	//70000 is the next real data, i think we have plenty of buffer for longer text entries if we need them.

	public static final int COLORLESS_ENERGY_HEX = 0x0507;
	public static final int PSYCHIC_ENERGY_HEX   = 0x0506;
	public static final int FIGHTING_ENERGY_HEX  = 0x0505;
	public static final int WATER_ENERGY_HEX 	 = 0x0504;
	public static final int LIGHTNING_ENERGY_HEX = 0x0503;
	public static final int GRASS_ENERGY_HEX 	 = 0x0502;
	public static final int FIRE_ENERGY_HEX 	 = 0x0501;

	//Banks start at 0x015 for 0x54000 and incriment whenever a pointer would point about the next 4000 mark
	// 0x54000, 0x58000, 0x5C000, 0x60000, 0x64000
	// 0x15,	0x16,	 0x17,	  0x18,	   0x19
	public static final int FIRST_CARD_TEXT_POINTER_LOCATION = 0x0357F3;
	public static final int FIRST_POKEMON_TEXT_POINTER_LOCATION = 0x03581D;
	public static final int LAST_POKEMON_DESCRIP_TEXT_POINTER_LOCATION = 0x036234; 
	public static final int LAST_CARD_DESCRIP_TEXT_POINTER_LOCATION = 0x036309;
	//All text pointers at and after this address use 03 between pointers;
	//All text pointers before this address use 02 between pointers;
	public static final int TEXT_DELINIATOR_CHANGE_ADDRESS = 0x0362AC;
	//Bank 0x15, 0x16, 0x17, 0x18, 0x19 starting at FIRST_CARD_TEXT_POINTER_LOCATION

	public static final byte[] NEXT_ENTRY 	= { 0x00, 0x06 };
	public static final byte[] END_DATA 	= { 0x00, (byte) 0xFF };
}
