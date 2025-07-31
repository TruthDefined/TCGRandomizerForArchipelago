package containers;

import java.nio.ByteBuffer;

import constants.Constants;
import utils.ByteUtils;
import utils.TextUtils;

public class Move {
    
    private byte[] Energy;      //02 00 00 00
    private byte[] Name;        //0b 08
    private byte[] Description; //0c 08 00 00
    private byte Damage;        //14
    private byte Category;      //00
    private byte[] Effect_cmds; //11 48
    private byte Flags1;        //00
    private byte Flags2;        //02
    private byte Flags3;        //00
    private byte Unknown;       //01
    private byte End;         //59???
    //02 00 00 00 0b 08 0c 08 00 00 14 00 11 48 00 02 00 01 59

    private String NameText;
    private String DescriptionText;
    
    public Move(byte[] Energy, byte[] Name, byte[] Description, byte Damage, byte Category, byte[] Effect_cmds, byte Flags1, byte Flags2, byte Flags3, byte Unknown, byte End){
        this.Energy = Energy;
        this.Name = Name;
        this.Description = Description;
        this.Damage = Damage;
        this.Category = Category;
        this.Effect_cmds = Effect_cmds;
        this.Flags1 = Flags1;
        this.Flags2 = Flags2;
        this.Flags3 = Flags3;
        this.Unknown = Unknown;
        this.End = End;

    }
    
    public Move(byte[] InputByteArray) {
        if (InputByteArray.length != Constants.PKMN_MOVE_DATA_LENGTH) {
            throw new IllegalArgumentException("BinaryMove requires exactly 19 bytes of data.");
        }
        int index = 0;

        this.Energy = ByteUtils.readBytes(InputByteArray, index, 4);
        index+=4;
        this.Name = ByteUtils.readBytes(InputByteArray, index, 2);
        index+=2;
        this.Description = ByteUtils.readBytes(InputByteArray, index, 4);
        index+=4;
        // For single bytes, extract the first element of the returned array:
        this.Damage = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index+=1;
        this.Category = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index+=1;
        this.Effect_cmds = ByteUtils.readBytes(InputByteArray, index, 2);
        index+=2;
        this.Flags1 = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index+=1;
        this.Flags2 = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index+=1;
        this.Flags3 = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index+=1;
        this.Unknown = ByteUtils.readBytes(InputByteArray, index, 1)[0];
        index+=1;
        this.End = ByteUtils.readBytes(InputByteArray, index, 1)[0];
    }

    public boolean SetTextFromPointer(ByteBuffer textBuffer, ByteBuffer pointerBuffer){

        //System.out.printf("Move Name Pointer: %02X %02X \n", this.Name[0], this.Name[1]);
        //Converts from byte array to int
        int pointer = ByteUtils.pointerToIntFlipped(this.Name );
        if(pointer != 0){
            //grabs address stored at byte array. NEED TO PASS POINTER BUFFER!!
            byte[] address = ByteUtils.getAddressFromPointerIndex(pointerBuffer, pointer);
            //System.out.printf("Move Address contains: %02X %02X %02X \n",address[0] , address[1], address[2]);
            this.NameText = TextUtils.returnStringFromBankAndPointer(textBuffer,address);
            //System.out.println("Move Name: " + this.NameText);

            pointer = ByteUtils.pointerToIntFlipped(new byte[] {this.Description[0],this.Description[1]});
            if(pointer != 0){
            address = ByteUtils.getAddressFromPointerIndex(pointerBuffer, pointer);
            this.DescriptionText = TextUtils.returnStringFromBankAndPointer(textBuffer,address);
            }
            pointer = ByteUtils.pointerToIntFlipped(new byte[] {this.Description[2],this.Description[3]});
            if(pointer != 0){
                address = ByteUtils.getAddressFromPointerIndex(pointerBuffer, pointer);
                this.DescriptionText = this.DescriptionText + TextUtils.returnStringFromBankAndPointer(textBuffer,address);
            }
            

            return true;
        }
        return false;
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