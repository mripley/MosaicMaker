package mosaicmaker;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	
	private BingSearchServiceClientFactory factory;
	private BingSearchClient client; 
	private SearchRequestBuilder builder;
	
	public BingImageFetcher(String filePath){
		terms = getWordList(filePath);
		initBing();
	}
	
	// load the list of terms from a file.
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
	
	// init bing and setup all image searching parameter. 
	private void initBing(){

		factory = BingSearchServiceClientFactory.newInstance();
		client = factory.createBingSearchClient();
		builder = client.newSearchRequestBuilder();
		
		builder.withAppId("39F09985E5E66CA0126004E4E980349D61AA445F");
		builder.withSourceType(SourceType.IMAGE);
		builder.withVersion("2.0");
		builder.withMarket("en-us");
		builder.withAdultOption(AdultOption.MODERATE);
		builder.withSearchOption(SearchOption.ENABLE_HIGHLIGHTING);

		builder.withImageRequestCount(resultsPerPage);
		builder.withWebRequestSearchOption(WebSearchOption.DISABLE_HOST_COLLAPSING);
		builder.withWebRequestSearchOption(WebSearchOption.DISABLE_QUERY_ALTERATIONS);
		
	}
	
	// Loads the replacement images from the links returned by bing. 
	@Override
	public void loadReplacementImages(final int xBlockSize, final int yBlockSize) {

		replacements = new ArrayList<ReplacementBlock>((int) (terms.size() * resultsPerPage) );
		ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		ArrayList<Callable<ReplacementBlock>> replacementTasks = new ArrayList<Callable<ReplacementBlock>>();

		// loop through all the terms 
		for(int i=0; i<terms.size(); i++){
			builder.withQuery(terms.get(i));
			
			// adjust this if we decide to get more than one page of results 
			builder.withImageRequestOffset(0L);
			
			// execute the query
			SearchResponse response = client.search(builder.getResult());
			// loop through all all the responses
			for (final ImageResult result : response.getImage().getResults()) {
				
				if(result.getFileSize() > 2500000){
					System.out.println("skipping image due to size" + result.getFileSize());
					continue;
				}
				
				if(result != null){
					replacementTasks.add( new Callable<ReplacementBlock>(){
						
					@Override
					public ReplacementBlock call() throws Exception {
						BufferedImage img = null;
						try {
							
							String url = result.getMediaUrl();
							if(url != null){
								// load and scale the image from the url
								img = loadAndScaleImage(result.getMediaUrl(), xBlockSize, yBlockSize);	
							}	
						} catch (IOException e) {
							System.out.println("Caught io exception in loadReplacementImages: " +  e.getMessage());
						}
						return buildReplacement(img);
					} 
				});
				}
			}
		}
		try {
			List<Future<ReplacementBlock>> blocks = threadPool.invokeAll(replacementTasks);
			for(Future<ReplacementBlock> b : blocks){
				if(b.get() != null){
					replacements.add(b.get());
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Caught interrupted exception when copying futures to blocks: " +  e.getMessage());
		} catch (ExecutionException e) {
			System.out.println("Caught execution exception when copying futures to blocks: " +  e.getMessage());
		}		
		threadPool.shutdown();
	}

	
	public ArrayList<String> getTerms() {
		return terms;
	}

}
