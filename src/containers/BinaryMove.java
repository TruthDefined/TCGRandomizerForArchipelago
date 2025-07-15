package containers;

import utils.ByteUtils;
import utils.ByteUtils.Index;

public class BinaryMove {
    
    private byte[] Energy;
    private byte[] Name;
    private byte[] Description;
    private byte Damage;
    private byte Catagory;
    private byte[] Effect_cmds;
    private byte Flags1;
    private byte Flags2;
    private byte Flags3;
    private byte Unknown;
    private byte End;
    
    
    public BinaryMove(byte[] Energy, byte[] Name, byte[] Description, byte Damage, byte Catagory, byte[] Effect_cmds, byte Flags1, byte Flags2, byte Flags3, byte Unknown, byte End){
        this.Energy = Energy;
        this.Name = Name;
        this.Description = Description;
        this.Damage = Damage;
        this.Catagory = Catagory;
        this.Effect_cmds = Effect_cmds;
        this.Flags1 = Flags1;
        this.Flags2 = Flags2;
        this.Flags3 = Flags3;
        this.Unknown = Unknown;
        this.End = End;

    }
    
    public BinaryMove(byte[] InputByteArray){
        if (InputByteArray.length != 20) {
                throw new IllegalArgumentException("BinaryMove requires exactly 20 bytes of data.");
            }
        Index index = new Index(0);
        
        this.Energy = (byte[]) ByteUtils.readBytes(InputByteArray, index, 4);
        this.Name = (byte[]) ByteUtils.readBytes(InputByteArray, index, 2);
        this.Description = (byte[]) ByteUtils.readBytes(InputByteArray, index, 4);
        this.Damage = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
        this.Catagory = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
        this.Effect_cmds = (byte[]) ByteUtils.readBytes(InputByteArray, index, 2);
        this.Flags1 = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
        this.Flags2 = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
        this.Flags3 = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
        this.Unknown = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
        this.End = (byte) ByteUtils.readBytes(InputByteArray, index, 1);
    
    
    
    }
}

//      START         (0),
// 		ENERGY        (0),
// 		NAME          (4), //0013
// 		DESCRIPTION   (6),
// 		DAMAGE       (10),
// 		CATEGORY     (11),
// 		EFFECT_CMDS  (12),
// 		FLAGS1       (14),
// 		FLAGS2       (15),
// 		FLAGS3       (16),
// 		UNKNOWN1     (17),
// 		ANIMATION    (18),
// 		END          (19);