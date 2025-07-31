package containers;

import java.nio.ByteBuffer;

import constants.Constants;
import utils.ByteUtils;
import utils.TextUtils;

public class Card {

    private byte Type;                //01
    private byte[] GFX;               //a7 02
    private byte[] Name;              //0a 08       - 0x57552         = 0x3581D
    private byte Rarity;              //00
    private byte Set;                 //10
    private byte ID;                  //08
    private byte HP;                  //28
    private byte Stage;               //00
    private byte[] PreEvolutionName;  //00 00
    private Move Move1;         //02 00 00 00 0b 08 0c 08 00 00 14 00 11 48 00 02 00 01 59
    private Move Move2;         //00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
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

    private String NameText;
    private String PreEvolutionNameText;
    private String KindText;
    private String DescriptionText;
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


    public Card(byte Type, byte[] GFX, byte[] Name, 
                      byte Rarity, byte Set, byte ID, 
                      byte HP, byte Stage, byte[] PreEvolutionName, 
                      Move Move1, Move Move2, 
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
    
    public Card(byte[] InputByteArray){
      if (InputByteArray.length != Constants.PKMN_CARD_DATA_LENGTH) {
            throw new IllegalArgumentException("BinaryCard requires exactly 65 bytes of data.");
        }
      int index = 0;
      
      // For single-byte fields, extract byte from byte[] returned by readBytes
        this.Type = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.GFX = ByteUtils.readBytes(InputByteArray, index, 2);
        index += 2;
        this.Name = ByteUtils.readBytes(InputByteArray, index, 2);
        index += 2;
        this.Rarity = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.Set = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.ID = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.HP = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.Stage = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.PreEvolutionName = ByteUtils.readBytes(InputByteArray, index, 2);
        index += 2;
        this.Move1 = new Move(ByteUtils.readBytes(InputByteArray, index, 19));
        index += 19;
        this.Move2 = new Move(ByteUtils.readBytes(InputByteArray, index, 19));
        index += 19;
        this.Retreat = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.Weakness = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.Resistance = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.Kind = ByteUtils.readBytes(InputByteArray, index, 2);
        index += 2;
        this.Pokedex = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.Dummy = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.Level = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index += 1;
        this.Length = ByteUtils.readBytes(InputByteArray, index, 2);
        index += 2;
        this.Weight = ByteUtils.readBytes(InputByteArray, index, 2);
        index += 2;
        this.Description = ByteUtils.readBytes(InputByteArray, index, 2);
        index += 2;
        this.Unknown = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        //this.End = ByteUtils.readBytes(InputByteArray, index, 1)[0];
    }
    public byte[] getName() {
      return this.Name;  // Name is declared as byte[] in your class
    }

    public boolean addTextFromPointers(ByteBuffer textBuffer, ByteBuffer pointerBuffer){
      //bb = Text buffer filled with all text strings back to back
      //This. contains pointer to an index pointing to the correct address


      
      //System.out.printf("Card Name Pointer: %02X %02X \n", this.Name[0], this.Name[1]);
      //Converts from byte array to int
      int pointer = ByteUtils.pointerToIntFlipped(this.Name);
      //grabs address stored at byte array. NEED TO PASS POINTER BUFFER!!
      byte[] address = ByteUtils.getAddressFromPointerIndex(pointerBuffer, pointer);
      //System.out.printf("Card Address contains: %02X %02X %02X \n",address[0] , address[1], address[2]);
      this.NameText = TextUtils.returnStringFromBankAndPointer(textBuffer,address);
      System.out.println("Card Name: " + this.NameText);




      pointer = ByteUtils.pointerToIntFlipped(this.PreEvolutionName);
      if (pointer!=0){
        address = ByteUtils.getAddressFromPointerIndex(pointerBuffer, pointer);
        this.PreEvolutionNameText = TextUtils.returnStringFromBankAndPointer(textBuffer,address);
        //System.out.println("Prevolution Name: " + this.PreEvolutionNameText);
      }
      this.Move1.SetTextFromPointer(textBuffer,pointerBuffer);
      
      this.Move2.SetTextFromPointer(textBuffer,pointerBuffer);
      
      pointer = ByteUtils.pointerToIntFlipped(this.Kind);
      address = ByteUtils.getAddressFromPointerIndex(pointerBuffer, pointer);
      this.KindText = TextUtils.returnStringFromBankAndPointer(textBuffer,address);
      //System.out.println("Kind: " + this.KindText);
      pointer = ByteUtils.pointerToIntFlipped(this.Description);
      address = ByteUtils.getAddressFromPointerIndex(pointerBuffer, pointer);
      this.DescriptionText = TextUtils.returnStringFromBankAndPointer(textBuffer,address);
      //System.out.println("Description: " + this.DescriptionText);



      return true;
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
