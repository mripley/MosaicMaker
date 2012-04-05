package mosaicmaker;

import java.awt.image.BufferedImage;

public class ReplacementBlock extends Block {
	
	private BufferedImage img;

	public ReplacementBlock(BufferedImage img, int row, int col) {
		super(row, col);
		this.img = img;
	}
	
	public ReplacementBlock(BufferedImage img){
		super(0,0);
		this.img = img;
	}
	
	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

}
