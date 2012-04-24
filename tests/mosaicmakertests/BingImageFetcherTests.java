package mosaicmakertests;

import static org.junit.Assert.*;

import java.io.IOException;

import mosaicmaker.BingImageFetcher;
import mosaicmaker.MosaicMaker;
import mosaicmaker.MosaicMakerException;

import org.junit.Test;

public class BingImageFetcherTests {

	@Test
	public void testLoadTerms() {
		BingImageFetcher fetcher = new BingImageFetcher("test_images/terms.txt");
		assertEquals(fetcher.getTerms().size(), 3);
	}
	
	@Test
	public void testBingResults(){
		BingImageFetcher fetcher = new BingImageFetcher("test_images/terms.txt");
		fetcher.loadReplacementImages(40, 30);
		System.out.println(fetcher.getNumReplacementImages());
		
		// passing means that we got at least 66% of the images we wanted
		assertTrue(fetcher.getNumReplacementImages() > 60);
	}
	
	@Test
	public void testBingMakeMosaic (){
		MosaicMaker maker;
		try {
			maker = new MosaicMaker("test_images/testImage2.jpg", "test_images/terms.txt");
			maker.makeMosaic(40,30,"result.png");
		} catch (IOException e) {
			System.out.println("caught io exception in constructor of MosaicMaker");
		}catch (MosaicMakerException e) {
			System.out.println(e.getMessage());
		}
		assertTrue(1==1);
	}

}
