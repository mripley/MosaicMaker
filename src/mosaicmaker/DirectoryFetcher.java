package mosaicmaker;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class DirectoryFetcher extends ImageFetcher {

	private File directory;
	private int blockWidth;
	private int blockHeight;
	
	public DirectoryFetcher(String dir){
		// open the directory
		directory = new File(dir);
		
		// set the default block size to just to be safe
		blockWidth = 0;
		blockHeight = 0;
		
		// preallocate space in the array list
		this.replacements = new ArrayList<ReplacementBlock>(directory.list().length);
	}
	
	@Override
	public void loadReplacementImages(int xBlockSize, int yBlockSize) {
		// loop through all the files in the directory and load them as required 
		for(String fileName : directory.list()){
			try {
				BufferedImage img = (BufferedImage)ImageIO.read(new File(fileName)).getScaledInstance(xBlockSize, yBlockSize, Image.SCALE_DEFAULT);
				
				int[] pixels = new int[blockWidth*blockHeight];
				img.getRGB(0, 0, blockWidth, blockHeight, pixels, 0, blockWidth);
				ReplacementBlock newBlock = new ReplacementBlock(pixels);
				replacements.add(newBlock);
	
			} catch (IOException e) {
				System.out.println("Caught IO exception while loading image " + fileName);
			}
		}
		
	}


}
