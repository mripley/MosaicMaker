package mosaicmakertests;

import static org.junit.Assert.*;

import mosaicmaker.DirectoryFetcher;

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
	}

}
