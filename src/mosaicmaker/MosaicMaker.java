package mosaicmaker;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.lang.Math;
import javax.imageio.ImageIO;


public class MosaicMaker {
	
	private BufferedImage sourceImage;
	private ImageFetcher fetcher;
	
	public MosaicMaker(String sourceImagePath, String replacementPath) throws IOException{
		
		sourceImage = ImageIO.read(new File(sourceImagePath));
		
		// get the correct type of fetcher depending on what we were given in the replacementPath
		File dir = new File(replacementPath);
		if(dir.list() == null){
			this.fetcher = new BingImageFetcher(replacementPath);
		}
		else{
			this.fetcher = new DirectoryFetcher(replacementPath);
		}
		
	}

	public void makeMosaic(int xNumBlocks, int yNumBlocks, String outputName) throws MosaicMakerException{
		long startTime = System.currentTimeMillis();
		if(xNumBlocks < 0 || xNumBlocks > sourceImage.getWidth() || yNumBlocks < 0 || yNumBlocks > sourceImage.getHeight()){
			throw new MosaicMakerException("Invalid block size");
		}
		
		// block the image and compute the average color for each block
		ArrayList<Block> blocks = blockImage(sourceImage, xNumBlocks, yNumBlocks );
		
		// if there are no candidate blocks then there is nothing to do and just return. 
		if(blocks.size()==0){
			return;
		}
		
		// if there is more than one replacement image then we can try to continue
		// get the block width and height
		int xBlockSize = blocks.get(0).getBlockRect().width;
		int yBlockSize = blocks.get(0).getBlockRect().height;
		
		// go load the candidate images
		long startLoadCandidate = System.currentTimeMillis();
		fetcher.loadReplacementImages(xBlockSize, yBlockSize);
		long stopLoadCandidate = System.currentTimeMillis();
		
		// loop through all the blocks and find the best candidate image
		long startFindReplacements = System.currentTimeMillis();
		for(Block b : blocks){
			//System.out.println("Working on block with average color: " + b.getAverageColor()+ " block number: " + i +" Block rect = " + b.getBlockRect());
			ReplacementBlock replacement = fetcher.getBestReplacementBlock(b.getAverageColor(), true);
			
			// make sure we got a replacement block. If we ran out of replacement blocks or didn't find one 
			// we should stop
			if(replacement == null){
				throw new MosaicMakerException("Ran out of blocks. Bailing out");
			}
			
			// get the rectangle that we are currently replacing 
			Rectangle rect = b.getBlockRect(); 
			sourceImage.setRGB(rect.x, rect.y, rect.width, rect.height, replacement.getImg(), 0, rect.width);
		}
		long stopFindReplacements = System.currentTimeMillis();
		
		String extension = getExtension(outputName);
		
		try {
			ImageIO.write(sourceImage, extension, new File(outputName));
		} catch (IOException e) {
			System.out.println("Caught IO exception when writing output file");
		}
		long stopTime = System.currentTimeMillis();
		System.out.println("total time to create mosaic with parallel processing: " + String.valueOf(stopTime - startTime));
		System.out.println("total time to load candidate images with parallel processing: " + String.valueOf(stopLoadCandidate - startLoadCandidate));
		System.out.println("total time to find replacement images with sequential processing: " + String.valueOf(stopFindReplacements - startFindReplacements));
	}
	
	public ArrayList<Block> blockImage(BufferedImage img, int xNumBlocks, int yNumBlocks){
		ArrayList<Block> imgBlocks = new ArrayList<Block>();
		final int numCores = Runtime.getRuntime().availableProcessors();
		final ExecutorService threadPool = Executors.newFixedThreadPool(numCores);
		ArrayList<Callable<Block>> sourceBlocks = new ArrayList<Callable<Block>>();
		
		
		// get the number of blocks in this image 
		int blockWidth = img.getWidth() / xNumBlocks;
		int blockHeight = img.getHeight() / yNumBlocks;
		
		// if we have a image that doesn't fit well with the current blocks
		// then compute the total number of extra blocks needed to fill out the image. 
		int extraXBlocks = img.getWidth() % xNumBlocks;
		int extraYBlocks = img.getHeight() % yNumBlocks;
		
		for(int x=0; x < xNumBlocks+extraXBlocks; x++){
			for(int y=0; y < yNumBlocks+extraYBlocks; y++){
				// get the upper left coordinate of the blocks rectangle
				final int startX = x*blockWidth;
				final int startY = y*blockHeight;
				
				// compute the lower right coordinate of the blocks rectangle
				// if we are at the edge choose the min size between the block width 
				// the image width. This way at the edge we end with small blocks 
				// that fill out the rest of the image. 
				final int endX = Math.min((x+1)*blockWidth, img.getWidth());
				final int endY = Math.min((y+1)*blockHeight, img.getHeight());
				
				// get the width and height of this block
				final int width = (endX - startX);
				final int height = (endY - startY);
				
				// if the width of height is 0 then we would never see this block.
				// jump to the next iteration of the loop.
				if(width <= 0 || height <= 0 ){
					continue;
				}
				
				sourceBlocks.add(new Callable<Block>(){
					public Block call() {
						return resizeSourceImageBlock(new Rectangle(startX, startY, width, height));
					}
				});
			}
		}
		try {
			List<Future<Block>> finalBlocks = threadPool.invokeAll(sourceBlocks);
			for (Future<Block> block : finalBlocks ){
					imgBlocks.add(block.get());
				 
			}
		} catch (InterruptedException e) {
			System.out.println("Cuaght interrupted exception");
		}
		catch (ExecutionException e) {
			System.out.println("Caught execution exception");
			e.printStackTrace();
		}
		
		threadPool.shutdown();
		return imgBlocks;
	}
	
	private Block resizeSourceImageBlock(Rectangle rect){
		// allocate space for the block and extract the pixels 
		int[] pixels = new int[rect.width*rect.height];
		sourceImage.getRGB(rect.x, rect.y, rect.width, rect.height, pixels, 0, rect.width );
		
		// get the average color and add this new block to the list of blocks
		Color avgColor = getAverageColor(pixels);
		return new Block(rect, avgColor);
	}
	
	public static Color getAverageColor(int[] pixels){
		
		int r =0 ,g=0 ,b = 0; 
		for(int i : pixels){
			// and out the red channel
			r += (0x00ff0000 & i) >> 16;
			// and out the green channel
			g += (0x0000ff00 & i) >> 8;
			// and out the blue channel
			b += (0x000000ff & i);	
		}
		
		r /= pixels.length;
		g /= pixels.length;
		b /= pixels.length;
		return new Color(r,g,b);	
	}
	
	
	private String getExtension(String fileName){
		// get the file extension 
		int pos = fileName.lastIndexOf(".");
		String extension = ""; 
		if(pos > 0){
			extension = fileName.substring(pos+1);
		}
		return extension;
	}
}


