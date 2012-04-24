package mosaicmaker;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.code.bing.search.client.BingSearchClient;
import com.google.code.bing.search.client.BingSearchServiceClientFactory;
import com.google.code.bing.search.client.BingSearchClient.SearchRequestBuilder;
import com.google.code.bing.search.schema.AdultOption;
import com.google.code.bing.search.schema.SearchOption;
import com.google.code.bing.search.schema.SearchResponse;
import com.google.code.bing.search.schema.SourceType;
import com.google.code.bing.search.schema.multimedia.ImageResult;
import com.google.code.bing.search.schema.web.WebSearchOption;

public class BingImageFetcher extends ImageFetcher {

	
	private final static long resultsPerPage = 30;
	private ArrayList<String> terms; 
	public BingImageFetcher(String filePath){
		terms = getWordList(filePath);
	}
	
	private ArrayList<String> getWordList(String filePath){
		ArrayList<String> words = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			
			String line;
			while((line = reader.readLine()) != null){
				line = line.trim();
				words.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file: " + filePath);
		} catch (IOException e) {
			System.out.println("Caught exception while reading file: " + filePath);
		}
		return words;
	}
	
	@Override
	public void loadReplacementImages(int xBlockSize, int yBlockSize) {
		
		BingSearchServiceClientFactory factory = BingSearchServiceClientFactory.newInstance();
		BingSearchClient client = factory.createBingSearchClient();

		SearchRequestBuilder builder = client.newSearchRequestBuilder();
		builder.withAppId("39F09985E5E66CA0126004E4E980349D61AA445F");
		builder.withSourceType(SourceType.IMAGE);
		builder.withVersion("2.0");
		builder.withMarket("en-us");
		builder.withAdultOption(AdultOption.MODERATE);
		builder.withSearchOption(SearchOption.ENABLE_HIGHLIGHTING);

		builder.withImageRequestCount(resultsPerPage);

		builder.withWebRequestSearchOption(WebSearchOption.DISABLE_HOST_COLLAPSING);
		builder.withWebRequestSearchOption(WebSearchOption.DISABLE_QUERY_ALTERATIONS);

		for(int i=0; i<terms.size(); i++){
			builder.withQuery(terms.get(i));
			builder.withImageRequestOffset((long) (i * resultsPerPage));
			SearchResponse response = client.search(builder.getResult());
			this. replacements = new ArrayList<ReplacementBlock> (response.getImage().getResults().size());
			
			for (ImageResult result : response.getImage().getResults()) {
				if(result != null){
					try {
						BufferedImage img = this.loadAndScaleImage(result.getMediaUrl(), xBlockSize, yBlockSize);				
						replacements.add(this.buildReplacement(img));
					} catch (IOException e) {
						
						System.out.println("Caught io exception in loadReplacementImages");
					}
				}
				else{
					System.out.println("Error fetching page: "+i);
				}        
			}	
		}
	}
	
	public ArrayList<String> getTerms() {
		return terms;
	}

}
