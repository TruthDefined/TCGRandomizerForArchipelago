/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import constants.Fields;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pokepress
 */
public class UtilsTest {
    
    public UtilsTest() {
    }

    /**
     * Test of addNybbles method, of class Utils.
     */
    @Test
    public void testAddNybbles() {
        System.out.println("addNybbles");
        int i = 0;
        int expResult = 0;
        int result = Utils.addNybbles(i);
        assertEquals(expResult, result);
        
        i = 0x2;
        expResult = 0x2;
        result = Utils.addNybbles(i);
        assertEquals(expResult, result);
        
        i = 0x22;
        expResult = 0x4;
        result = Utils.addNybbles(i);
        assertEquals(expResult, result);
        
        i = 0x12;
        expResult = 0x3;
        result = Utils.addNybbles(i);
        assertEquals(expResult, result);
        
        i = 0x1234;
        expResult = 0xa; //10
        result = Utils.addNybbles(i);
        assertEquals(expResult, result);
        
        i = 0x12345678;
        expResult = 0x24; //36
        result = Utils.addNybbles(i);
        assertEquals(expResult, result);
        
        i = 0xffffffff;
        expResult = 0x78; //120
        result = Utils.addNybbles(i);
        assertEquals(expResult, result);
    }

    /**
     * Test of cartAddressToBank1Address method, of class Utils.
     */
    @Test
    public void testCartAddressToBank1Address() {
        System.out.println("cartAddressToBank1Address");
        int cartAddress = 0x00000000;
        short expResult = 0x4000;
        short result = Utils.cartAddressToBank1Address(cartAddress);
        assertEquals(expResult, result);
        
        cartAddress = 0x00000fff;
        expResult = 0x4fff;
        result = Utils.cartAddressToBank1Address(cartAddress);
        assertEquals(expResult, result);
        
        cartAddress = 0x00001fff;
        expResult = 0x5fff;
        result = Utils.cartAddressToBank1Address(cartAddress);
        assertEquals(expResult, result);
        
        cartAddress = 0x0007efff;
        expResult = (short) 0x6fff;
        result = Utils.cartAddressToBank1Address(cartAddress);
        assertEquals(expResult, result);
    }

    /**
     * Test of swapAddressBytes method, of class Utils.
     */
    @Test
    public void testSwapAddressBytes() {
        System.out.println("swapAddressBytes");
        short addressBytes = (short) 0xde78;
        short expResult = (short) 0x78de;
        short result = Utils.swapAddressBytes(addressBytes);
        assertEquals(expResult, result);
    }
    
}
