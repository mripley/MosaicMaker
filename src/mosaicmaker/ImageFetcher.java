package mosaicmaker;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public abstract class ImageFetcher {
	
	protected ArrayList<ReplacementBlock> replacements;
	
	// get the distance between 2 colors 
	private double getDist(Color c1, Color c2){
		return Math.sqrt(Math.pow((c1.getRed()-c2.getRed()),2) + 
								Math.pow((c1.getBlue()-c2.getBlue()), 2) + 
								Math.pow((c1.getGreen()-c2.getGreen()),2));
	}
	
	public abstract void loadReplacementImages(int xBlockSize, int yBlockSize);
	
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
	
	protected BufferedImage loadAndScaleImage(File filename, int blockWidth, int blockHeight) throws IOException{
		return Scalr.resize(ImageIO.read(filename), Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT,
	               blockWidth, blockHeight, Scalr.OP_ANTIALIAS);
	} 
	
	
	protected BufferedImage loadAndScaleImage(String url, int blockWidth, int blockHeight) throws IOException{
		return Scalr.resize(ImageIO.read(new URL(url)), Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT,
	               blockWidth, blockHeight, Scalr.OP_ANTIALIAS);
	} 
	
	protected ReplacementBlock buildReplacement(BufferedImage scaledImage){
		int width = scaledImage.getWidth();
		int height = scaledImage.getHeight();
		int[] pixels = new int[width * height];
		scaledImage.getRGB(0, 0, width, height, pixels, 0, width);
		ReplacementBlock newBlock = new ReplacementBlock(pixels);
		newBlock.setAverageColor(MosaicMaker.getAverageColor(pixels));
		return newBlock;
		
	}
}
