package mosaicmaker;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MosaicMaker {
	
	private BufferedImage sourceImage;
	private int xBlockSize, yBlockSize;
	private ImageFetcher fetcher;
	
	public MosaicMaker(String sourceImagePath, int xBlockSize, int yBlockSize, String replacementPath) throws IOException{
		sourceImage = ImageIO.read(new File(sourceImagePath));
		this.xBlockSize = xBlockSize;
		this.yBlockSize = yBlockSize;
		File dir = new File(replacementPath);
		if(dir.list() == null){
			this.fetcher = new GoogleImageFetcher();
		}
		else{
			this.fetcher = new DirectoryFetcher();
		}
		
	}

	private void makeMosaic(){
		
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
