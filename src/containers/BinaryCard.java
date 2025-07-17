package containers;

import constants.Constants;
import utils.ByteUtils;
import utils.ByteUtils.Index;

public class BinaryCard {

    private byte Type;                //01
    private byte[] GFX;               //a7 02
    private byte[] Name;              //0a 08
    private byte Rarity;              //00
    private byte Set;                 //10
    private byte ID;                  //08
    private byte HP;                  //28
    private byte Stage;               //00
    private byte[] PreEvolutionName;  //00 00
    private BinaryMove Move1;         //02 00 00 00 0b 08 0c 08 00 00 14 00 11 48 00 02 00 01 59
    private BinaryMove Move2;         //00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
    private byte Retreat;             //01
    private byte Weakness;            //80
    private byte Resistance;          //00
    private byte[] Kind;              //0d 08
    private byte Pokedex;             //01
    private byte Dummy;               //00
    private byte Level;               //0d
    private byte[] Length;            //02 04
    private byte[] Weight;            //96 00
    private byte[] Description;       //0e 08
    private byte Unknown;             //10

    
/*
 * 01 a7 02 0a 08 00 10 08 28 00 00 00 02 00 00 00 
 * 0b 08 0c 08 00 00 14 00 11 48 00 02 00 01 59 00 
 * 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
 * 00 00 01 80 00 0d 08 01 00 0d 02 04 96 00 0e 08 
 * 10
 */
/*
 * 01 08 03 0f 08 01 10 09 3c 01 0a 08 01 00 00 20 
 * 10 08 00 00 00 00 1e 00 00 00 00 00 00 00 27 03 
 * 00 00 00 11 08 12 08 00 00 14 00 0a 48 01 00 00
 * 00 38 01 80 00 0d 08 02 00 14 03 03 22 01 13 08 
 * 10 
 */


    public BinaryCard(byte Type, byte[] GFX, byte[] Name, 
                      byte Rarity, byte Set, byte ID, 
                      byte HP, byte Stage, byte[] PreEvolutionName, 
                      BinaryMove Move1, BinaryMove Move2, 
                      byte Retreat, byte Weakness, byte Resistance, 
                      byte[] Kind, byte Pokedex, byte Dummy, 
                      byte Level, byte[] Length, byte[] Weight, 
                      byte[] Description, byte Unknown){
      this.Type = Type;
      this.GFX = GFX;
      this.Name = Name;
      this.Rarity = Rarity;
      this.Set = Set;
      this.ID = ID;
      this.HP = HP;
      this.Stage = Stage;
      this.PreEvolutionName = PreEvolutionName;
      this.Move1 = Move1;
      this.Move2 = Move2;
      this.Retreat = Retreat;
      this.Weakness = Weakness;
      this.Resistance = Resistance;
      this.Kind = Kind;
      this.Pokedex = Pokedex;
      this.Dummy = Dummy;
      this.Level = Level;
      this.Length = Length;
      this.Weight = Weight;
      this.Description = Description;
      this.Unknown = Unknown;
      //this.End = End;
    }
    
    public BinaryCard(byte[] InputByteArray){
      if (InputByteArray.length != Constants.PKMN_CARD_DATA_LENGTH) {
            throw new IllegalArgumentException("BinaryCard requires exactly 65 bytes of data.");
        }
      Index index = new Index(0);
      
      // For single-byte fields, extract byte from byte[] returned by readBytes
        this.Type = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.GFX = ByteUtils.readBytes(InputByteArray, index, 2);
        this.Name = ByteUtils.readBytes(InputByteArray, index, 2);
        this.Rarity = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.Set = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.ID = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.HP = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.Stage = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.PreEvolutionName = ByteUtils.readBytes(InputByteArray, index, 2);
        this.Move1 = new BinaryMove(ByteUtils.readBytes(InputByteArray, index, 19));
        this.Move2 = new BinaryMove(ByteUtils.readBytes(InputByteArray, index, 19));
        this.Retreat = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.Weakness = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.Resistance = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.Kind = ByteUtils.readBytes(InputByteArray, index, 2);
        this.Pokedex = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.Dummy = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.Level = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        this.Length = ByteUtils.readBytes(InputByteArray, index, 2);
        this.Weight = ByteUtils.readBytes(InputByteArray, index, 2);
        this.Description = ByteUtils.readBytes(InputByteArray, index, 2);
        this.Unknown = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        //this.End = ByteUtils.readBytes(InputByteArray, index, 1)[0];
    }
    public byte[] getName() {
      return this.Name;  // Name is declared as byte[] in your class
    }

    // public String getName(){
    //   return "";
    //   }
    
}




    //  START         (0),
    // 	TYPE          (0),
    // 	GFX           (1),
    // 	NAME          (3), //(0A08), 0F08, 1408  57554, 5763C, 576EF
    // 	RARITY        (5),
      //             /** Set uses the upper nybble to represent the in-game set and 
      //                the lower nybble to represent the real-world set:
      //                 Low Bits: 0-Base 1-Jungle 2-Fossil 7-GB
      //                 High Bits: 0-Colosseum 1-Evolution 2-Mystery 3-Laboratory
      //                 Grand Master Birds/Illusion Cards: 0x47
      //                 Promos: 0x48 (Rarity 0xff) */
    // 	SET           (6), 
    // 	ID            (7),
      //             //Multiple of 10 between 10 and 120
    // 	HP            (8),
    // 	STAGE         (9),
    // 	PRE_EVO_NAME (10),
    // 	MOVE1        (12),
    // 	MOVE2        (31),
    // 	RETREAT_COST (50),
    // 	WEAKNESS     (51),
    // 	RESISTANCE   (52),
    // 	KIND         (53),
    // 	POKEDEX      (55),
    // 	DUMMY        (56),
    // 	LEVEL        (57),
    // 	LENGTH       (58),
    // 	WEIGHT       (60),
    // 	DESCRIPTION  (62),
    // 	UNKNOWN      (64),
    // 	END          (65);
