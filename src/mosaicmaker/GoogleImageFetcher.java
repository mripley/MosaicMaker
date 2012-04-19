package mosaicmaker;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GoogleImageFetcher extends ImageFetcher {

	private ArrayList<String> terms; 
	public GoogleImageFetcher(String filePath){
		terms = getWordList(filePath);
	}
	
	private ArrayList<String> getWordList(String filePath){
		ArrayList<String> words = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			
			String line;
			while((line = reader.readLine()) != null){
				line = line.trim();
				words.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file: " + filePath);
		} catch (IOException e) {
			System.out.println("Caught exception while reading file: " + filePath);
		}
		return words;
	}
	
	@Override
	public void loadReplacementImages(int xBlockSize, int yBlockSize) {
		
		
	}
	
	public ArrayList<String> getTerms() {
		return terms;
	}

}
