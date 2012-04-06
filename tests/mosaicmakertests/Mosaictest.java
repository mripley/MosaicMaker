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
			
			assertEquals("Number of blocks not 4", blocks.size(), 6);
			
			for(Block b : blocks){
				System.out.println(b.getAverageColor());
			}

			
		} catch (IOException e) {
			System.out.println("Caught IO exception in MosaicMaker constructor");
			e.printStackTrace();
		}
		
	}


}
