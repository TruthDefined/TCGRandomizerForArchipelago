package containers;

import java.nio.ByteBuffer;

import constants.Cards;
import constants.Constants;
import utils.ByteUtils;
import utils.TextUtils;

public class Card {

  //Shared
  public Cards Pokemon = null;
  private byte Type;                //01
  private byte[] GFX = new byte[2];               //a7 02
  private byte[] Name = new byte[2];              //0a 08       - 0x57552         = 0x3581D
  private byte Rarity;              //00
  private byte Set;                 //10
  private int ID;                  //08
  
  //POKEMON Card
  private byte HP;                  //28
  private byte Stage;               //00
  private byte[] PreEvolutionName = new byte[2];  //00 00
  private Move Move1;         //02 00 00 00 0b 08 0c 08 00 00 14 00 11 48 00 02 00 01 59
  private Move Move2;         //00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
  private byte Retreat;             //01
  private byte Weakness;            //80
  private byte Resistance;          //00
  private byte[] Kind = new byte[2];              //0d 08
  private byte Pokedex;             //01
  private byte Dummy;               //00
  private byte Level;               //0d
  private byte[] Length = new byte[2];            //02 04
  private byte[] Weight = new byte[2];           //96 00
  private byte[] Description = new byte[2];       //0e 08
  private byte Unknown;             //10

  //ENERGY & TRAINER Card
  private byte[] EffectCommand = new byte[2];
  private byte[] Description2 = new byte[2];

  private String NameText = "";
  private String PreEvolutionNameText = "";
  private String KindText = "";
  private String DescriptionText = "";
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

  public Card(ByteBuffer inputBuffer){
    this.Type = inputBuffer.get();
    inputBuffer.get(this.GFX);
    inputBuffer.get(this.Name);
    this.Rarity = inputBuffer.get();
    this.Set = inputBuffer.get();
    this.ID = inputBuffer.get() & 0xFF;
    //System.out.printf("ID: %d \n", this.ID);

    //System.out.printf("Card Type: %d \n", getCardType().ordinal());

    switch(getCardType()){
      case CardType.Energy, CardType.Trainer -> {
        inputBuffer.get(this.EffectCommand);
        inputBuffer.get(this.Description);
        inputBuffer.get(this.Description2);
        }
      case CardType.Pokemon -> {
        this.HP = inputBuffer.get();
        this.Stage = inputBuffer.get();
        inputBuffer.get(this.PreEvolutionName);
        this.Move1 = new Move(inputBuffer);
        this.Move2 = new Move(inputBuffer);
        this.Retreat = inputBuffer.get();
        this.Weakness = inputBuffer.get();
        this.Resistance = inputBuffer.get();
        inputBuffer.get(this.Kind);
        this.Pokedex = inputBuffer.get();
        this.Dummy = inputBuffer.get();
        this.Level = inputBuffer.get();
        inputBuffer.get(this.Length);
        inputBuffer.get(this.Weight);
        inputBuffer.get(this.Description);
        this.Unknown = inputBuffer.get();
        }
    }
  }


  public boolean addTextFromPointers(ByteBuffer textBuffer, ByteBuffer pointerBuffer){
    //Converts from byte array to int
    int pointer = ByteUtils.pointerToIntFlipped(this.Name);
    //System.out.printf("Pointer ID %02X %02X \n", this.Name[0], this.Name[1]);
    //grabs address stored at byte array. NEED TO PASS POINTER BUFFER!!
    byte[] address = ByteUtils.getAddressFromPointerIndex(pointerBuffer, pointer);
    //System.out.printf("Card Address contains: %02X %02X %02X \n",address[0] , address[1], address[2]);
    this.NameText = TextUtils.returnStringFromBankAndPointer(textBuffer,address);
    System.out.println("Card name: "+ this.NameText);

    if(this.getCardType() == CardType.Pokemon){
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
    }
    
    
    //System.out.println("Kind: " + this.KindText);
    pointer = ByteUtils.pointerToIntFlipped(this.Description);
    address = ByteUtils.getAddressFromPointerIndex(pointerBuffer, pointer);
    this.DescriptionText = TextUtils.returnStringFromBankAndPointer(textBuffer,address);

    if(this.getCardType() != CardType.Pokemon){
      pointer = ByteUtils.pointerToIntFlipped(this.Description2);
      if(pointer != 0){
        address = ByteUtils.getAddressFromPointerIndex(pointerBuffer, pointer);
        this.DescriptionText += 0x0A + TextUtils.returnStringFromBankAndPointer(textBuffer,address);
      }
      
    }

    return true;
  }

  public void replaceNameInMovesWithPlaceholder(){
      Move1.ReplaceNameInDescriptionWithPlaceholder(this.NameText);
      Move2.ReplaceNameInDescriptionWithPlaceholder(this.NameText);
  }

  public void replacePlaceholderInMovesWithName(){
      Move1.ReplacePlaceholderInDescriptionWithName(this.NameText);
      Move2.ReplacePlaceholderInDescriptionWithName(this.NameText);
  }

  public String getNameText(){
    return this.NameText;
  }

  public boolean setWeaknessAndResistance(byte[] WR){
    this.Weakness = WR[0];
    this.Resistance = WR[1];
    return true;
  }

  public byte[] getWeaknessAndResistance(){
    return new byte[] {this.Weakness, this.Resistance};
  }

  public boolean setHP(byte hp){
    this.HP = hp;
    return true;
  }

  public boolean setRetreatCost(byte cost){
    this.Retreat = cost;
    return true;
  }

  public int getRetreat(){
    return this.Retreat;
  }

  public int getHP(){
    return this.HP;
  }

  
  public boolean isIllusionCard() {
      return (this.Pokemon == Cards.Venusaur1 || this.Pokemon == Cards.Mew2);
  }

  public byte getSet(){
    return this.Set;
  }
  public String getKindText(){
    return this.KindText;
  }

  public boolean setSet(byte set){
    this.Set = set;
    return true;
  }

  public byte getRarity(){
    return this.Rarity;
  }

  public boolean setRarity(byte rarity){
    this.Rarity = rarity;
    return true;
  }

  public Move getMove1(){
    return this.Move1;
  }
  public Move getMove2(){
    return this.Move2;
  }

  public boolean setMove1(Move move){
    this.Move1 = move;
    return true;
  }
  public boolean setMove2(Move move){
    this.Move2 = move;
    return true;
  }

  public Constants.EneryType getType(){
    return Constants.EneryType.values()[this.Type];
  }

  public String getDescText(){
    return this.DescriptionText;
  }

  public final CardType getCardType(){
    if(this.ID<Constants.POKEMON_FIRST_ID) return CardType.Energy;
    else if(this.ID<Constants.TRAINER_FIRST_ID) return CardType.Pokemon;
    else return CardType.Trainer;
  }

  // public String getName(){
  //   return "";
  //   }
  public ByteBuffer dataToByteBuffer() {
    //IF POKEMON DO THIS
    ByteBuffer buffer = ByteBuffer.allocate(Constants.PKMN_CARD_DATA_LENGTH);
    //IF NOT POKEMON DO SMALLER BUFFER

    buffer.put(Type);
    buffer.put(GFX);
    buffer.put(Name);
    buffer.put(Rarity);
    buffer.put(Set);
    buffer.put((byte)ID);
    buffer.put(HP);
    buffer.put(Stage);
    buffer.put(PreEvolutionName);
    buffer.put(Move1.dataToByteBuffer());
    buffer.put(Move2.dataToByteBuffer());
    buffer.put(Retreat);
    buffer.put(Weakness);
    buffer.put(Resistance);
    buffer.put(Kind);
    buffer.put(Pokedex);
    buffer.put(Dummy);
    buffer.put(Level);
    buffer.put(Length);
    buffer.put(Weight);
    buffer.put(Description);
    buffer.put(Unknown);

    buffer.flip(); // Prepare buffer for reading
    return buffer;
} 

    public int getID() {
      return this.ID;
    }

  public enum CardType{
    Energy,
    Pokemon,
    Trainer;
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
