package mosaicmaker;

import java.awt.image.BufferedImage;

public class Block {
	protected int averageColor[];
	private int row, col;
	
	public Block(int row , int col){
		averageColor = new int[3];
		this.row = row;
		this.col = col;
	}
	
	public int[] getAverageColor() {
		return averageColor;
	}

	public void setAverageColor(int[] averageColor) {
		this.averageColor = averageColor;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
}
