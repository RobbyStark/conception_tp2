
package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This commands returns the most frequent word in a file
 */

public class MostFrequentWordCommand implements ICommand {

	@Override
	public String run(File file) {
		
		String string = readStringFromFile(file);
		StringTokenizer tokenizer = new StringTokenizer(string);
		List<String> words = null;
		int wordCount = 0;
		String frequentWord = "";
		while(tokenizer.hasMoreTokens()) {
			String currentWord = tokenizer.nextToken();
			
	        if (currentWord.equals(frequentWord)) {
	        	wordCount++;
	        } else if (wordCount == 0) {
	        	frequentWord = currentWord;
	        	wordCount = 1;
	        } else {
	        	wordCount--;
	        }			
		}		
		
		return frequentWord;
	}

	@Override
	public boolean getSupportFolder() {
		return false;
	}

	@Override
	public boolean getSupportFile() {
		return true;
	}
	
	String readStringFromFile(File file){		
		Scanner scanner;
		String name = "";
		try {
			scanner = new Scanner(file);
			if(scanner.hasNext()) {
			name = scanner.useDelimiter("\\Z").next();
			}
			scanner.close();
			return name;
		}
		catch (NoSuchElementException e) {
			e.printStackTrace();
			return "Error";
		}	catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Error";
		}		
	}
}