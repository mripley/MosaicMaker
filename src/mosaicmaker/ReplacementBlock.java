package mosaicmaker;

import java.awt.Color;
import java.awt.Rectangle;


public class ReplacementBlock extends Block {
	
	private int[] img;

	public ReplacementBlock(int[] pixels, Rectangle blockRect, Color avgColor) {
		super(blockRect, avgColor);
		this.img = pixels;
	}
	
	public ReplacementBlock(int[] pixels){
		super(new Rectangle(0,0,0,0), new Color(0,0,0));
		this.img = pixels;
	}
	
	public int[] getImg() {
		return img;
	}

	public void setImg(int[] img) {
		this.img = img;
	}
	

}
