package mosaicmakertests;

import static org.junit.Assert.*;

import mosaicmaker.BingImageFetcher;

import org.junit.Test;

public class BingImageFetcherTests {

	@Test
	public void testLoadTerms() {
		BingImageFetcher fetcher = new BingImageFetcher("test_images/terms.txt");
		assertEquals(fetcher.getTerms().size(), 3);
	}

}
