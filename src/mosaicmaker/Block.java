package mosaicmaker;

import java.awt.Color;
import java.awt.Rectangle;

public class Block {
	protected Color averageColor;
	private Rectangle blockRect;
	
	public Block(int x, int y, int width, int height, Color avgColor){
		averageColor = new Color(0,0,0);
		blockRect = new Rectangle(x,y,width,height);
	}
	
	public Block(Rectangle rect, Color avgColor){
		averageColor = avgColor;
		blockRect = rect;
	}
	
	public Color getAverageColor() {
		return averageColor;
	}

	public void setAverageColor(Color newAveragColor) {
		this.averageColor = newAveragColor;
	}

	public Rectangle getBlockRect() {
		return blockRect;
	}

	public void setBlockRect(Rectangle blockRect) {
		this.blockRect = blockRect;
	}
	

}
