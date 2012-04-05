package mosaicmaker;

import java.awt.Color;

public class Block {
	protected Color averageColor;
	private int row, col;
	
	public Block(int row , int col){
		averageColor = new Color(0,0,0);
		this.row = row;
		this.col = col;
	}
	
	public Color getAverageColor() {
		return averageColor;
	}

	public void setAverageColor(Color newAveragColor) {
		this.averageColor = newAveragColor;
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
