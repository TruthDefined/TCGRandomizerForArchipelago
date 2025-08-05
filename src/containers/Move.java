package containers;

import constants.Constants;
import java.nio.ByteBuffer;
import utils.ByteUtils;
import utils.TextUtils;

public class Move {
    
    private byte[] Energy = new byte[4];        //02 00 00 00
    private byte[] Name = new byte[2];          //0b 08
    private byte[] Description = new byte[4];   //0c 08 00 00
    private byte Damage;                        //14
    private byte Category;                      //00
    private byte[] Effect_cmds = new byte[2];   //11 48
    private byte Flags1;                        //00
    private byte Flags2;                        //02
    private byte Flags3;                        //00
    private byte Unknown;                       //01
    private byte End;                           //59???
    //02 00 00 00 0b 08 0c 08 00 00 14 00 11 48 00 02 00 01 59

    private String NameText = "";
    private String DescriptionText = "";

    private String PokemonNamePlaceholder = "**";

    public Move(ByteBuffer inputBuffer){
        inputBuffer.get(this.Energy);
        inputBuffer.get(this.Name);
        inputBuffer.get(this.Description);
        this.Damage = inputBuffer.get();
        this.Category = inputBuffer.get();
        inputBuffer.get(this.Effect_cmds);
        this.Flags1 = inputBuffer.get();
        this.Flags2 = inputBuffer.get();
        this.Flags3 = inputBuffer.get();
        this.Unknown = inputBuffer.get();
        this.End = inputBuffer.get();
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

    public boolean ReplaceNameInDescriptionWithPlaceholder(String pokemonName){
        if(DescriptionText.contains(pokemonName)){
            this.DescriptionText = this.DescriptionText.replace(pokemonName,PokemonNamePlaceholder);
            return true;
        }
        return false;   
    }
    public boolean ReplacePlaceholderInDescriptionWithName(String pokemonName){
        if(DescriptionText.contains(PokemonNamePlaceholder)){
            this.DescriptionText = this.DescriptionText.replace(PokemonNamePlaceholder,pokemonName);
            return true;
        }
        return false;   
    }

    public String getNameText(){
        return this.NameText;
    }

    public String getDescriptionText(){
        return this.DescriptionText;
    }

    public byte[] getEnergy(){
        return this.Energy;
    }

    public ByteBuffer dataToByteBuffer() {

        ByteBuffer buffer = ByteBuffer.allocate(Constants.PKMN_MOVE_DATA_LENGTH);

        buffer.put(Energy);
        buffer.put(Name); 
        buffer.put(Description);
        buffer.put(Damage);
        buffer.put(Category);
        buffer.put(Effect_cmds);
        buffer.put(Flags1);
        buffer.put(Flags2);
        buffer.put(Flags3);
        buffer.put(Unknown);
        buffer.put(End);        

        buffer.flip(); // Prepare buffer for reading
      return buffer;
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