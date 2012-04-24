package mosaicmaker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class DirectoryFetcher extends ImageFetcher {

	private File directory;
	
	public DirectoryFetcher(String dir){
		// open the directory
		directory = new File(dir);
		
		// preallocate space in the array list
		this.replacements = new ArrayList<ReplacementBlock>(directory.list().length);
	}
	
	@Override
	public void loadReplacementImages(int blockWidth, int blockHeight) {
		
		// loop through all the files in the directory and load them as required 
		for(File fileName : directory.listFiles()){
			try {
				BufferedImage img = loadAndScaleImage(fileName, blockWidth, blockHeight); 
				replacements.add(this.buildReplacement(img));
	
			} catch (IOException e) {
				System.out.println("Caught IO exception while loading image " + fileName);
			}
			
		}
		
	}
	
}
