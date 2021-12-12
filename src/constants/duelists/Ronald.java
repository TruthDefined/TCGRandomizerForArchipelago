package constants.duelists;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Rival Character
 * Possible future enhancement-swap with Mark (requires numerous text changes)
 */
public class Ronald extends constants.duelists.Duelist {
    
    private final int RonaldPortraitAddress = 0x93aa2;
    private final int RonaldSpriteAddress = 0x97ea6;
    private final int Ronald1 = 0x1873;
    private final int RonaldBackground = 0x0471;
    private final int Ronald2 = 0x0c72;
    private final int Ronald3 = 0x0030;
    
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
    }
    
    /**Replaces the character that appears in duels, menus, etc.
     * @throws java.io.IOException*/
    @Override
    public void ReplaceCharacterPortrait() throws IOException
    {
            byte [] portraitData = new byte[PortaitBytes];
            cartFile.seek(RonaldPortraitAddress);
            cartFile.read(portraitData, 0, PortaitBytes);
            cartFile.seek(PlayerPortraitStartAddress);
            cartFile.write(portraitData);
            
            //SGB Portait
            cartFile.seek(0x73499);
            cartFile.writeShort(Ronald1);
            cartFile.writeShort(Ronald2);
            cartFile.writeShort(RonaldBackground);
            cartFile.writeShort(Ronald3);
            
            //GBC portrait palette
            cartFile.seek(0xb3ff8);
            cartFile.writeShort(Ronald1);
            cartFile.writeShort(Ronald2);
            cartFile.writeShort(RonaldBackground);
            cartFile.writeShort(Ronald3);
    }
    /**Replaces the character that appears in overworld
     * @throws java.io.IOException*/
    @Override
    public void ReplaceOverworldSprite() throws IOException
    {
            byte [] spriteData = new byte[SpriteBytes];
            cartFile.seek(RonaldSpriteAddress);
            cartFile.read(spriteData, 0, SpriteBytes);
            cartFile.seek(PlayerOverworldSpriteStartAddress);
            cartFile.write(spriteData);
            
            //Map cursor
            cartFile.seek(0xb7b2d);

            cartFile.writeShort(Ronald2);
    }
}
