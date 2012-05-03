package mosaicmaker;

import java.io.IOException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 5){
			System.out.println("Not enough args to MosaicMaker");
			return;
		}
		
		String sourceImage = args[0];
		String outputImage = args[1];
		String replacementPath = args[2];
		int numXBlocks = Integer.valueOf(args[3]);
		int numYBlocks = Integer.valueOf(args[4]);
		
		MosaicMaker maker;
		try {
			maker = new MosaicMaker(sourceImage, replacementPath);
			maker.makeMosaic(numXBlocks, numYBlocks, outputImage);
		} catch (IOException e) {
			System.out.println("caught io exception in constructor of MosaicMaker");
		}catch (MosaicMakerException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Exiting");
		return;
	}

}
