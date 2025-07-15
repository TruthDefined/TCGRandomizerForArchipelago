package containers;

import utils.ByteUtils;
import utils.ByteUtils.Index;

public class BinaryCard {

    private byte Type;
    private byte[] GFX;
    private byte[] Name;
    private byte Rarity;
    private byte Set;
    private byte ID;
    private byte HP;
    private byte Stage;
    private byte[] PreEvolutionName;
    private BinaryMove Move1;
    private BinaryMove Move2;
    private byte Retreat;
    private byte Weakness;
    private byte Resistance;
    private byte[] Kind;
    private byte Pokedex;
    private byte Dummy;
    private byte Level;
    private byte[] Length;
    private byte[] Weight;
    private byte[] Description;
    private byte Unknown;
    private byte End;

    public BinaryCard(byte Type, byte[] GFX, byte[] Name, 
                      byte Rarity, byte Set, byte ID, 
                      byte HP, byte Stage, byte[] PreEvolutionName, 
                      BinaryMove Move1, BinaryMove Move2, 
                      byte Retreat, byte Weakness, byte Resistance, 
                      byte[] Kind, byte Pokedex, byte Dummy, 
                      byte Level, byte[] Length, byte[] Weight, 
                      byte[] Description, byte Unknown, byte End){
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
      this.End = End;
    }
    
    public BinaryCard(byte[] InputByteArray){
      if (InputByteArray.length != 65) {
            throw new IllegalArgumentException("BinaryCard requires exactly 65 bytes of data.");
        }
      Index index = new Index(0);
      
      this.Type = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.GFX = (byte[]) ByteUtils.readBytes(InputByteArray, index, 1);
      this.Name = (byte[]) ByteUtils.readBytes(InputByteArray, index, 2);
      this.Rarity = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.Set = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.ID = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.HP = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.Stage = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.PreEvolutionName = (byte[]) ByteUtils.readBytes(InputByteArray, index, 2);
      this.Move1 = new BinaryMove((byte[]) ByteUtils.readBytes(InputByteArray, index, 20));
      this.Move2 = new BinaryMove((byte[]) ByteUtils.readBytes(InputByteArray, index, 20));
      this.Retreat = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.Weakness = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.Resistance = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.Kind = (byte[]) ByteUtils.readBytes(InputByteArray, index, 2);
      this.Pokedex = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.Dummy = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.Level = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.Length = (byte[]) ByteUtils.readBytes(InputByteArray, index, 2);
      this.Weight = (byte[]) ByteUtils.readBytes(InputByteArray, index, 2);
      this.Description = (byte[]) ByteUtils.readBytes(InputByteArray, index, 2);
      this.Unknown = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
      this.End = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
    }
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
