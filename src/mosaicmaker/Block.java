package mosaicmaker;

import java.awt.image.BufferedImage;

public class Block {
	private int averageColor[];
	private BufferedImage downSampledImage;
	
	public Block(){
		averageColor = new int[3];
		
	}
	
	public int[] getAverageColor() {
		return averageColor;
	}

	public void setAverageColor(int[] averageColor) {
		this.averageColor = averageColor;
	}

	public BufferedImage getDownSampledImage() {
		return downSampledImage;
	}

	public void setDownSampledImage(BufferedImage downSampledImage) {
		this.downSampledImage = downSampledImage;
	}

	
}
