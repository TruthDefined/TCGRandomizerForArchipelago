package constants.duelists;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Rival Character
 */
public class Ronald extends constants.duelists.Duelist {
    
    private final int RonaldPortraitAddress = 0x93aa2;
    private final int RonaldSpriteAddress = 0x97ea6;
    private final int RonaldPaletteAddress = 0x734e3;
    private final int RonaldPaletteAddressCGB = 0xb8311;
    private final int Ronald1 = 0x1873;
    private final int RonaldBackground = 0x0471;
    private final int Ronald2 = 0x0c72;
    private final int Ronald3 = 0x0030;
    private final String SubstituteRivalName = "Markus"; //Same letter count
    
    public Ronald(RandomAccessFile f) {
        super(f);
    }
    
    /**Alters text for default name, pronouns, etc
     * @throws java.io.IOException*/
    @Override
    public void AdjustGameText() throws IOException
    {
            //Adjust default player name
            cartFile.seek(0x128ec);
            cartFile.writeByte(0x41); //R
            cartFile.seek(0x128ee);
            cartFile.writeByte(0x3e); //O
            cartFile.seek(0x128f0);
            cartFile.writeByte(0x3d); //N
            cartFile.seek(0x128f2);
            cartFile.writeByte(0x30); //A
            cartFile.writeShort(0x033b); //L
            cartFile.writeShort(0x0333); //D
            
            //Update game script references
            cartFile.seek(0x3b597);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x3b5a9);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x3b5bc);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x3b5ce);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x3f67b);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x48c46);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x49f76);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x4a27b);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x4a329);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x4a36e);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x4a3d7);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x4a472);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x4a6e5);
            cartFile.writeBytes(SubstituteRivalName);
            cartFile.seek(0x5425d);
            cartFile.writeBytes(SubstituteRivalName);
    }
    
    /**Replaces the character that appears in duels, menus, etc.
     * @throws java.io.IOException*/
    @Override
    public void ReplaceCharacterPortrait() throws IOException
    {
            //Get default portrait
            byte [] playerPortraitData = new byte[PortaitBytes];
            cartFile.seek(PlayerPortraitStartAddress);
            cartFile.read(playerPortraitData, 0, PortaitBytes);
            
            //Get Ronald's portrait
            byte [] portraitData = new byte[PortaitBytes];
            cartFile.seek(RonaldPortraitAddress);
            cartFile.read(portraitData, 0, PortaitBytes);
            
            //Swap portraits
            cartFile.seek(RonaldPortraitAddress);
            cartFile.write(playerPortraitData);
            cartFile.seek(PlayerPortraitStartAddress);
            cartFile.write(portraitData);
            
            //Get default palette
            byte [] playerPaletteData = new byte[8];
            cartFile.seek(0x73499);
            cartFile.read(playerPaletteData, 0, 8);
            
            //Swap SGB portait palettes
            cartFile.seek(0x73499);
            cartFile.writeShort(Ronald1);
            cartFile.writeShort(Ronald2);
            cartFile.writeShort(RonaldBackground);
            cartFile.writeShort(Ronald3);
            
            cartFile.seek(RonaldPaletteAddress);
            cartFile.write(playerPaletteData);
            
            //Swap GBC portrait palettes
            cartFile.seek(0xb3ff8);
            cartFile.writeShort(Ronald1);
            cartFile.writeShort(Ronald2);
            cartFile.writeShort(RonaldBackground);
            cartFile.writeShort(Ronald3);
            
            cartFile.seek(RonaldPaletteAddressCGB);
            cartFile.write(playerPaletteData);
    }
    /**Replaces the character that appears in overworld
     * @throws java.io.IOException*/
    @Override
    public void ReplaceOverworldSprite() throws IOException
    {
            //Get default overworld sprite
            byte [] playerSpriteData = new byte[SpriteBytes];
            cartFile.seek(PlayerOverworldSpriteStartAddress);
            cartFile.read(playerSpriteData, 0, SpriteBytes);
            
            //Get Ronald's overworld sprite
            byte [] spriteData = new byte[SpriteBytes];
            cartFile.seek(RonaldSpriteAddress);
            cartFile.read(spriteData, 0, SpriteBytes);
            
            //Swap overworld sprites
            cartFile.seek(RonaldSpriteAddress);
            cartFile.write(playerSpriteData);
            cartFile.seek(PlayerOverworldSpriteStartAddress);
            cartFile.write(spriteData);
            
            //Map cursor
            cartFile.seek(0xb7b2d);
            cartFile.writeShort(RonaldBackground);
            
           
            /*Adjust rival OW sprite palette. Should technically be red 0x1e,
             but only pink 0x12 seems to work.
            */
            cartFile.seek(0x119ed);
            cartFile.writeByte(0x12);
            cartFile.seek(0x119fa);
            cartFile.writeByte(0x12);
            cartFile.seek(0x11a07);
            cartFile.writeByte(0x12);
    }
}
