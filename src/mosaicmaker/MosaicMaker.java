package mosaicmaker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MosaicMaker {
	
	private BufferedImage sourceImage;
	private int xBlockSize, yBlockSize;
	
	public MosaicMaker(String sourceImagePath, int xBlockSize, int yBlockSize, String replacementPath) throws IOException{
		sourceImage = ImageIO.read(new File(sourceImagePath));
		this.xBlockSize = xBlockSize;
		this.yBlockSize = yBlockSize;
	}
	
	private void makeMosaic(){
		
	}
}
