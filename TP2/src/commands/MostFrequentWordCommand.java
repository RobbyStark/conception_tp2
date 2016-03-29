
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
 * algorithme inspiré de
 * http://stackoverflow.com/questions/8545590/java-find-the-most-popular-element-in-int-array
 */

public class MostFrequentWordCommand implements ICommand {

	@Override
	public String run(File file) {
		
		String string = readStringFromFile(file);
		String [] tokens = string.split("[\\p{Punct}\\s]+");	
		return getPopularElement(tokens);
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
	
	
	public String getPopularElement(String[] a)
	{
		if (a.length==0){
			return "";
		}
	  int count = 1, tempCount;
	  String popular = a[0];
	  String temp = "";
	  for (int i = 0; i < (a.length - 1); i++)
	  {
	    temp = a[i];
	    tempCount = 0;
	    for (int j = 1; j < a.length; j++)
	    {
	      if (temp.equals(a[j]))
	        tempCount++;
	    }
	    if (tempCount > count)
	    {
	      popular = temp;
	      count = tempCount;
	    }
	  }
	  return popular;
	}
	
}