package mosaicmaker;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class MosaicMaker {
	
	private BufferedImage sourceImage;
	private int xBlockNumber, yBlockNumber;
	private ImageFetcher fetcher;
	
	public MosaicMaker(String sourceImagePath, int xBlockNumber, int yBlockNumber, String replacementPath) throws IOException{
		sourceImage = ImageIO.read(new File(sourceImagePath));
		this.xBlockNumber = xBlockNumber;
		this.yBlockNumber = yBlockNumber;
		File dir = new File(replacementPath);
		if(dir.list() == null){
			this.fetcher = new GoogleImageFetcher();
		}
		else{
			this.fetcher = new DirectoryFetcher();
		}
		
	}

	private void makeMosaic(){
		int xOffset= sourceImage.getWidth()/xBlockNumber;
		int xLastBlock= sourceImage.getWidth()%xBlockNumber;
		int yOffset = sourceImage.getHeight()/yBlockNumber;
		int yLastBlock = sourceImage.getWidth()%yBlockNumber;
		
		ArrayList<Block> source= new ArrayList<Block>();
		int[] rgb = new int[xOffset*yOffset];
		for(int i=0; i<xBlockNumber; i++){
			for(int j = 0; j<yBlockNumber; j++){
				Block toAdd = new Block(i,j);
				if(j == yBlockNumber - 1 && i == xBlockNumber - 1 ){
					int[] rgbCorner = new int[xLastBlock*yLastBlock];
					sourceImage.getRGB(i*xLastBlock, j*yLastBlock, xLastBlock, yLastBlock, rgbCorner, 0, xOffset);
					//not sure if this will be thread safe
					new Block(i,j).setAverageColor(new Color(rgbCorner[0],rgbCorner[1],rgbCorner[2]));
					source.add(toAdd);
					
				}
				else if(j == yBlockNumber - 1 ){
					int[] rgbBottom = new int[xOffset*yLastBlock];
					sourceImage.getRGB(i*xOffset, j*yLastBlock, xOffset, yLastBlock, rgbBottom, 0, xOffset); 
					new Block(i,j).setAverageColor(new Color(rgbBottom[0],rgbBottom[1],rgbBottom[2]));
					source.add(toAdd);
				}
				else if(i == xBlockNumber - 1 ){
					int[] rgbSide = new int[xLastBlock*yOffset];
					sourceImage.getRGB(i*xLastBlock, j*yOffset, xLastBlock, yOffset, rgbSide, 0, xOffset); 
					new Block(i,j).setAverageColor(new Color(rgbSide[0],rgbSide[1],rgbSide[2]));
					source.add(toAdd);
				}
				else{
					sourceImage.getRGB(i*xOffset, j*yOffset, xOffset, yOffset, rgb, 0, xOffset); 
					new Block(i,j).setAverageColor(new Color(rgb[0],rgb[1],rgb[2]));
					source.add(toAdd);
				}
				
			}
			
		}
		
		
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
