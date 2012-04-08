package mosaicmaker;

import java.awt.Color;
import java.util.ArrayList;

public abstract class ImageFetcher {
	
	protected ArrayList<ReplacementBlock> replacements;
	
	private double getDist(Color c1, Color c2){
		return Math.sqrt(Math.pow((c1.getRed()-c2.getRed()),2) + 
								Math.pow((c1.getBlue()-c2.getBlue()), 2) + 
								Math.pow((c1.getGreen()-c2.getGreen()),2));
	}
	
	public abstract void loadReplacementImages(int xBlockSize, int yBlockSize);
	
	public ReplacementBlock getBestReplacementBlock(Color color){
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
		
		return retval;
	}
}
