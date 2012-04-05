package mosaicmaker;


public class ReplacementBlock extends Block {
	
	private int[] img;

	public ReplacementBlock(int[] pixels, int row, int col) {
		super(row, col);
		this.img = pixels;
	}
	
	public ReplacementBlock(int[] pixels){
		super(0,0);
		this.img = pixels;
	}
	
	public int[] getImg() {
		return img;
	}

	public void setImg(int[] img) {
		this.img = img;
	}
	

}
