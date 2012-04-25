package mosaicmaker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class DirectoryFetcher extends ImageFetcher {

	private File directory;
	
	public DirectoryFetcher(String dir){
		// open the directory
		directory = new File(dir);
		
		// preallocate space in the array list
		this.replacements = new ArrayList<ReplacementBlock>(directory.list().length);
	}
	
	@Override
	public void loadReplacementImages(final int blockWidth, final int blockHeight) {
		
		ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		ArrayList<Callable<ReplacementBlock>> replacementTasks = new ArrayList<Callable<ReplacementBlock>>();
		
		// loop through all the files in the directory and load them as required 
		for(final File fileName : directory.listFiles()){
			
			replacementTasks.add(new Callable<ReplacementBlock>(){
				@Override
				public ReplacementBlock call() throws Exception {
					
					BufferedImage img = null;
					try {
						img = loadAndScaleImage(fileName, blockWidth, blockHeight); 
					} catch (IOException e) {
						System.out.println("Caught IO exception while loading image " + fileName);
					}
					return buildReplacement(img);
				}
			});
			
		}
		
		try {
			List<Future<ReplacementBlock>> blocks = threadPool.invokeAll(replacementTasks);
			for(Future<ReplacementBlock> b : blocks){	
				replacements.add(b.get());
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
