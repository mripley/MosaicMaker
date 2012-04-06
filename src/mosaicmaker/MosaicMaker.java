package mosaicmaker;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;
import javax.imageio.ImageIO;

public class MosaicMaker {
	
	private BufferedImage sourceImage;
	private int xNumBlocks, yNumBlocks;
	private ImageFetcher fetcher;
	
	public MosaicMaker(String sourceImagePath, String replacementPath) throws IOException{
		sourceImage = ImageIO.read(new File(sourceImagePath));

		File dir = new File(replacementPath);
		if(dir.list() == null){
			this.fetcher = new GoogleImageFetcher();
		}
		else{
			this.fetcher = new DirectoryFetcher();
		}
		
	}

	public void makeMosaic(int xNumBlocks, int yNumBlocks){
		if(xNumBlocks < 0 || xNumBlocks > sourceImage.getWidth() || yNumBlocks < 0 || yNumBlocks > sourceImage.getHeight()){
			System.out.println("Invalid block Size. Bailing out");
			return;
		}
		
	}
	
	public ArrayList<Block> blockImage(BufferedImage img, int xNumBlocks, int yNumBlocks){
		ArrayList<Block> imgBlocks = new ArrayList<Block>();
		
		this.xNumBlocks = xNumBlocks;
		this.yNumBlocks = yNumBlocks;
		
		int blockWidth = img.getWidth() / xNumBlocks;
		int blockHeight = img.getHeight() / yNumBlocks;
		
		for(int x=0; x < xNumBlocks; x++){
			for(int y=0; y < yNumBlocks; y++){
				int startX = x*blockWidth;
				int startY = y*blockHeight;
				int endX = Math.min((x+1)*blockWidth, img.getWidth());
				int endY = Math.min((y+1)*blockHeight, img.getHeight());
				
				int width = (endX - startX);
				int height = (endY - startY);
				
				if(width ==0 || height == 0 ){
					continue;
				}
				
				int[] pixels = new int[width*height];
				img.getRGB(startX, startY, width, height, pixels, 0, width ); 
				Color avgColor = getAverageColor(pixels);
				Rectangle blockRect = new Rectangle(startX, startY, width, height);
				imgBlocks.add(new Block(blockRect, avgColor));
			
			}
		}
		
		return imgBlocks;
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
}
