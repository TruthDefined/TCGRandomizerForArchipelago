package constants;

import constants.Fields.CardFields;
import constants.Fields.MoveFields;

public class Constants {

	public static final String FILE_NAME_IN  = "tcg.gbc";
	public static final String FILE_NAME_OUT = "tcgrandomized_";
	public static final String FILE_NAME_OUT_SUFFIX = "_.gbc";
	
    //ROM location of first card data of each type.
	//Data Goes Pokemon > Energy > Trainer
	//These have pointers that are sequential and look at the pointer table
	public static final int FIRST_POKEMON_CARD_LOCATION = 0x30e28;
	public static final int FIRST_ENERGY_CARD_LOCATION = 0x33da3; //09-0e
    public static final int FIRST_TRAINER_CARD_LOCATION = 0x33e05;    

    //Each card in the gsme is assigned a unique one-byte ID
	public static final int ENERGY_FIRST_ID  = 0x01;
	public static final int POKEMON_FIRST_ID = 0x08;
	public static final int TRAINER_FIRST_ID = 0xc3;
        
    //There are 228 unique cards, counting the two "illusion" cards
	public static final int NUM_CARDS         = 0xe4;
	public static final int NUM_POKEMON_CARDS = TRAINER_FIRST_ID - POKEMON_FIRST_ID;
	public static final int NUM_ENERGY_CARDS = POKEMON_FIRST_ID - ENERGY_FIRST_ID;
	public static final int NUM_TRAINER_CARDS = NUM_CARDS - TRAINER_FIRST_ID + 1;
	
    /*Each card type has a certain number of one-byte fields that represent 
	various attributes and behaviors. Likewise for individual moves.*/
	public static final int PKMN_CARD_DATA_LENGTH   = CardFields.END.getOffset() - CardFields.START.getOffset();
	public static final int PKMN_MOVE_DATA_LENGTH   = MoveFields.END.getOffset() - MoveFields.START.getOffset();
	public static final int TRN_CARD_DATA_LENGTH    = 0x0e;
	public static final int ENERGY_CARD_DATA_LENGTH = 0x0e;
	public static final int UNUSED_EFFECT_BEHAVIOR_START = 0x2ff03; //253 Bytes of extra effect code space
	public static final int UNUSED_EFFECT_BEHAVIOR_END = 0x2ffff;

    //ROM location of first Pokemon card text entry 
	//Text goes Energy > Pokemon > Trainers
	public static final int ENERGY_CARD_TEXT_FIRST_ID = 0x57397;   
	public static final int POKEMON_CARD_TEXT_FIRST_ID = 0x57552;
	public static final int TRAINER_CARD_TEXT_FIRST_ID = 0x6342d;
	//Last possible byte for pointer table + text to be stored
	public static final int CARD_TEXT_LAST_ID = 0x6ffff;
	//Dragonite name @ 63344

	//70000 is the next real data, i think we have plenty of buffer for longer text entries if we need them.

	public static final int COLORLESS_ENERGY_HEX = 0x0507;
	public static final int PSYCHIC_ENERGY_HEX   = 0x0506;
	public static final int FIGHTING_ENERGY_HEX  = 0x0505;
	public static final int WATER_ENERGY_HEX 	 = 0x0504;
	public static final int LIGHTNING_ENERGY_HEX = 0x0503;
	public static final int GRASS_ENERGY_HEX 	 = 0x0502;
	public static final int FIRE_ENERGY_HEX 	 = 0x0501;


	public enum EneryType {
		Fire,
		Grass,
		Lightning,
		Water,
		Fighting,
		Psychic,
		Colorless,
		Unknown,
		FireEnergy,
		GrassEnergy,
		LightningEnergy,
		WaterEnergy,
		FightingEnergy,
		PsychicEnergy,
		ColorlessEnergy,
		Unknown2,
		Trainer;
	}
	
	// 0x34000 is the first entry in the pointer table.
	//TODO: Test to see if pointer at 0x34002 points to HAND text @ 0x3630A
		//IT DOES!!! Good. That means we can increase the length of the pointer table if need be
		//and offset the text read table.
	public static final int FIRST_CARD_TEXT_POINTER_LOCATION = 0x0357F3;
	//14 entries between ^ and V. Should be Energy Name and Descrip
	public static final int FIRST_POKEMON_TEXT_POINTER_LOCATION = 0x03581D;
	public static final int LAST_POKEMON_DESCRIP_TEXT_POINTER_LOCATION = 0x036234; 
	//70 entries between ^ and V. Should be enough for 23 cards, with 2 cards that have extended desciption.
	//This is only a problem if additional cards need extended description because he do not have ectra room before strings begin
	//Would reccomend not adding additional cards of any type. Sad.
	public static final int LAST_CARD_DESCRIP_TEXT_POINTER_LOCATION = 0x036306;

	//Brute forcing this into a constant for cleaning coding. 
	public static final int FIRST_POKEMON_TEXT_POINTER_CONTAINS = 0x0a08;
	public static final int FIRST_ENERGY_TEXT_POINTER_CONTAINS = 0x0fc07;

	public static final int START_NEW_TEXT_FIELD_BYTE 	= 0x06;
	public static final int END_TEXT_FIELD_BYTE 		= 0x00;
	public static final int FILLER_TEXT_BYTE 			= 0xFF;


	/// First Text pointer 			@ 0x34002 = 00 0A 23	Points to 	0x3630A		Bank: 13
	/// 	4C000
	/// 	+230a
	/// 	4e30a
	/// 
	/// // 000000 (final 00 bank) points to 50000     00 5B							Bank: 14
	/// 
	/// //01 divider?				@0x34DA6 = 01 34 00		Points to	
	/// 	50000
	/// 	+0034
	/// 	50034
	/// 
	/// First Pokemon name pointer	@ 0x3581D = 02 52 35 	Points to 	0x57552		Bank: 15
	/// 	54000
	/// 	+3552
	/// 	57552
	/// 
}
