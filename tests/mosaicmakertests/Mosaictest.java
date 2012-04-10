package mosaicmakertests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import mosaicmaker.Block;
import mosaicmaker.MosaicMaker;
import mosaicmaker.MosaicMakerException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Mosaictest {

	private BufferedImage testImage;
	@Before
	public void setUp() throws Exception {
		testImage = ImageIO.read(new File("test_images/testImage.bmp"));
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInvalidBlockSize(){
		MosaicMaker mm = null;
		try {
			mm = new MosaicMaker("test_images/testImage.bmp", "./test_images/dir_test/");
		

		} catch (IOException e) {
			System.out.println("Caught IO exception in Mosaic Maker constructor");
			fail("Caught IO exception");
		}

		
		try {
			mm.makeMosaic(-4, -2);
		}
		catch (MosaicMakerException e) {
			assertEquals("Wrong exception thrown", "Invalid block size", e.getMessage());
		}
		
		try {
			mm.makeMosaic(4000, 20000);
		}
		catch (MosaicMakerException e) {
			assertEquals("Wrong exception thrown", "Invalid block size", e.getMessage());
		}
		
	}
	
	@Test
	public void testBlockingEven() {
		
		try {
			MosaicMaker mm = new MosaicMaker("test_images/testImage.bmp", "./");
			ArrayList<Block> blocks = mm.blockImage(testImage, 2, 2);
			
			assertEquals("Number of blocks not 4", blocks.size(), 4);
			
			assertEquals("Average Color of block 0 not red", blocks.get(0).getAverageColor(), new Color(255,0,0));
			assertEquals("Average Color of block 1 not blue", blocks.get(1).getAverageColor(), new Color(0,0,255));
			assertEquals("Average Color of block 2 not green", blocks.get(2).getAverageColor(), new Color(0,255,0));
			assertEquals("Average Color of block 3 not black", blocks.get(3).getAverageColor(), new Color(0,0,0));
			
		} catch (IOException e) {
			System.out.println("Caught IO exception in MosaicMaker constructor");
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void testBlockingNonEven(){
		try {
			MosaicMaker mm = new MosaicMaker("test_images/testImage.bmp", "./");
			ArrayList<Block> blocks = mm.blockImage(testImage, 2, 3);
			
			assertEquals("Number of blocks not 6", blocks.size(), 6);
			
			assertEquals("Average Color of block 0 not matching", blocks.get(0).getAverageColor(), new Color(255,0,0));
			assertEquals("Average Color of block 1 not matching", blocks.get(1).getAverageColor(), new Color(131,0,123));
			assertEquals("Average Color of block 2 not matching", blocks.get(2).getAverageColor(), new Color(0,0,255));
			assertEquals("Average Color of block 3 not matching", blocks.get(3).getAverageColor(), new Color(0,255,0));
			assertEquals("Average Color of block 4 not matching", blocks.get(4).getAverageColor(), new Color(0,131,0));
			assertEquals("Average Color of block 5 not matching", blocks.get(5).getAverageColor(), new Color(0,0,0));
			
		} catch (IOException e) {
			System.out.println("Caught IO exception in MosaicMaker constructor");
			e.printStackTrace();
		}
		
	}


}
