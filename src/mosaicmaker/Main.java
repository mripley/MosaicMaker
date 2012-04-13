package mosaicmaker;

import java.io.IOException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 6){
			System.out.println("Not enough args to MosaicMaker");
			return;
		}
		
		String sourceImage = args[1];
		String outputImage = args[2];
		String replacementPath = args[3];
		int numXBlocks = Integer.valueOf(args[4]);
		int numYBlocks = Integer.valueOf(args[5]);
		
		MosaicMaker maker;
		try {
			maker = new MosaicMaker(sourceImage, replacementPath);
			maker.makeMosaic(numXBlocks, numYBlocks, outputImage);
		} catch (IOException e) {
			System.out.println("caught io exception in constructor of MosaicMaker");
		}catch (MosaicMakerException e) {
			System.out.println(e.getMessage());
		}
		
		
	}

}
