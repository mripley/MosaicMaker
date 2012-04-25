package mosaicmaker;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public abstract class ImageFetcher {
	
	protected ArrayList<ReplacementBlock> replacements;
	
	// get the distance between 2 colors in RGB space
	private double getDist(Color c1, Color c2){
		return Math.sqrt(Math.pow((c1.getRed()-c2.getRed()),2) + 
								Math.pow((c1.getBlue()-c2.getBlue()), 2) + 
								Math.pow((c1.getGreen()-c2.getGreen()),2));
	}
	
	
	// override this method to make new types of image fetchers. 
	public abstract void loadReplacementImages(int xBlockSize, int yBlockSize);
	
	// gets the best replacement block. For now uses the min distance between the 
	// query block and a block in the replacements array
	// at some point in the future replace this with a better data structure. 
	public ReplacementBlock getBestReplacementBlock(Color color, boolean duplicateImages){
		ReplacementBlock retval = null; 
		
		double curMin = Double.MAX_VALUE;
		
		for(ReplacementBlock block : replacements){
			Color avgBlockColor = MosaicMaker.getAverageColor(block.getImg());
			double newMin = this.getDist(color, avgBlockColor);
			if( newMin < curMin){
				curMin = newMin;
				retval = block;
			}
		}
		
		if(!duplicateImages){
			replacements.remove(retval);
		}
		
		return retval;
	}
	
	public int getNumReplacementImages(){
		return replacements.size();
	}
	
	
	// load and scale an image from a file. 
	protected BufferedImage loadAndScaleImage(File filename, int blockWidth, int blockHeight) throws IOException{
		return Scalr.resize(ImageIO.read(filename), Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT,
	               blockWidth, blockHeight, Scalr.OP_ANTIALIAS);
	} 
	
	// load and scale an image from a URL. the ImageIO class takes care of all the HTTP stuff. 
	protected BufferedImage loadAndScaleImage(String url, int blockWidth, int blockHeight) throws IOException{
		return Scalr.resize(ImageIO.read(new URL(url)), Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT,
	               blockWidth, blockHeight, Scalr.OP_ANTIALIAS);
	} 
	
	// build a replacement blockk from a buffered image. 
	protected ReplacementBlock buildReplacement(BufferedImage scaledImage){
		if(scaledImage == null){
			return null;
		}
		
		// grab the width and height of the incoming image. 
		int width = scaledImage.getWidth();
		int height = scaledImage.getHeight();
		
		// allocate enough space to hold the incoming pixels
		int[] pixels = new int[width * height];
		
		// copy the pixels out to the arrya. 
		scaledImage.getRGB(0, 0, width, height, pixels, 0, width);
		ReplacementBlock newBlock = new ReplacementBlock(pixels);
		newBlock.setAverageColor(MosaicMaker.getAverageColor(pixels));
		return newBlock;
		
	}
}
