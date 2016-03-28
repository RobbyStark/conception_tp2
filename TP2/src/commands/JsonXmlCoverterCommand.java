
package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * This command creates a JSON file from XML content,
 * or XML file from JSON content
 * it returns either a success message or a failure message
 */

public class JsonXmlCoverterCommand implements ICommand {

	@Override
	public String run(File file) {
		
		String fileContent = readStringFromFile(file);
		String convertString = "";
		String name = file.getAbsoluteFile().toString();
		
		if (getExtension(file).equals("json")) {	
			name = name.replace(".json",".xml");	
			File newFile = new File(name);			
			
			JSONObject json = null;
			try {
				json = new JSONObject(fileContent);
			} catch (JSONException e) {
				return ("Invalid JSON");
			}
			try {
				convertString = XML.toString(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			writeStringToFile(convertString, newFile);			
			return ("XML file created");
		} else if (getExtension(file).equals("xml")) {
			name = name.replace(".xml",".json");	
			File newFile = new File(name);			
			
			try {
				JSONObject xmlJSONObj = XML.toJSONObject(fileContent);
				convertString = xmlJSONObj.toString(4);
			} catch (JSONException je) {
				return ("Error");
			}
			
			writeStringToFile(convertString, newFile);			
			return ("JSON file created");

		} else {
			return ("Invalid file.");
		}
	}

	@Override
	public boolean getSupportFolder() {
		return false;
	}

	@Override
	public boolean getSupportFile() {
		return true;
	}

	String getExtension(File file) {
		String extension = "";

		int i = file.getName().lastIndexOf('.');
		if (i > 0) {
			extension = file.getName().substring(i + 1);
		}

		return extension;
	}
	
	String readStringFromFile(File file){		
		Scanner scanner;
		String name = "";
		try {
			scanner = new Scanner(file);
			name = scanner.useDelimiter("\\A").next();
			scanner.close();
			return name;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Error";
		}		
	}
	
	void writeStringToFile(String string, File file){
		try(  PrintWriter out = new PrintWriter(file)  ){
		    out.println( string );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}