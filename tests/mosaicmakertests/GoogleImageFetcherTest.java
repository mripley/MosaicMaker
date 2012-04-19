package mosaicmakertests;

import static org.junit.Assert.*;

import mosaicmaker.GoogleImageFetcher;

import org.junit.Test;

public class GoogleImageFetcherTest {

	@Test
	public void testLoadTerms() {
		GoogleImageFetcher fetcher = new GoogleImageFetcher("test_images/terms.txt");
		assertEquals(fetcher.getTerms().size(), 3);
	}

}
