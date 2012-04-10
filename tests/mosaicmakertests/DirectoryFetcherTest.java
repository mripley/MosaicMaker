package mosaicmakertests;

import static org.junit.Assert.*;

import java.awt.Color;

import mosaicmaker.DirectoryFetcher;
import mosaicmaker.ReplacementBlock;

import org.junit.Before;
import org.junit.Test;

public class DirectoryFetcherTest {

	public DirectoryFetcher dir;
	@Before
	public void setUp() throws Exception {
		dir = new DirectoryFetcher("test_images/dir_test");
	}

	@Test
	public void testDirectoryLoad() {
		dir.loadReplacementImages(200, 200);
		assertTrue(dir.getNumReplacementImages() == 8);
		
		ReplacementBlock block = dir.getBestReplacementBlock(new Color(255,255,255), false);
		assertEquals("Average color not equal for test image 0", block.getAverageColor(), new Color(247,246,246));
	}

}
