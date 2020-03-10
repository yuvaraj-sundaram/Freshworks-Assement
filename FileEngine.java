import java.util.*;
import java.io.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalTime;

@SuppressWarnings({ "serial", "unused" })
class DuplicateKey extends Exception {

}

@SuppressWarnings("serial")
class InvalidKey extends Exception {

}

@SuppressWarnings("serial")
class TimeExceeded extends Exception {

}

@SuppressWarnings("serial")
class KeySizeExceeded extends Exception {

}

@SuppressWarnings("serial")
class ValueSizeExceeded extends Exception {

}

public class FileEngine {
	private final String FilePath;

	FileEngine(String path) throws JSONException {
		FilePath = path;
		JSONObject fill = new JSONObject();
		fill.put(" ", " "); 
		try (FileWriter file = new FileWriter(FilePath, false)) {
			file.write(fill.toString()); 
			file.close();
		} catch (IOException E) {
			System.out.println("Caught IOException");
		}

	}

	FileEngine() throws JSONException {
		FilePath = "D://FileEngine.JSON"; // Windows default location
//		FilePath = "FileEngine.JSON"; // Ubuntu default location
		JSONObject fill = new JSONObject();
		fill.put(" ", " ");
		try (FileWriter file = new FileWriter(FilePath, false)) {
			file.write(fill.toString()); 
			file.close();
		} catch (IOException E) {
			System.out.println("Caught IOException");
		}

	}

	public void CreateEntry(String Key, JSONObject Value, int TimeToLive) throws Exception 
	{
		double length = (Value.toString().getBytes("utf-8").length)/1000.0;
		try {
			if (Key.length() > 32)
				throw new KeySizeExceeded();
			else if ((length) > 16) 
				throw new ValueSizeExceeded();
		} catch (KeySizeExceeded e) {
			System.out.println(" Key size exceeds maximum size, Enter Valid Key");
		} catch (ValueSizeExceeded e) {
			System.out.println(" Value size exceeds maximum size ");
		}

		try (FileReader reader = new FileReader(FilePath)) {
			JSONTokener tokener = new JSONTokener(reader);
			JSONObject temp = new JSONObject(tokener);
			if (temp.has(Key)) 
				throw new DuplicateKey();
			JSONArray tempArray = new JSONArray();
			tempArray.put(Value); 
			tempArray.put(TimeToLive); 
			LocalTime time = LocalTime.now();
			int TimeStamp = time.toSecondOfDay();
			tempArray.put(TimeStamp); 
			temp.put(Key, tempArray);
			try (FileWriter file = new FileWriter(FilePath, false)) 
			{

				file.write(temp.toString());
				file.close();

			} catch (IOException e) {
				System.out.println("Caught IO Exception");
			}

		} catch (DuplicateKey e) {
			System.out.println("KEY already exists. Duplicate keys not allowed");
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		} catch (IOException e) {
			System.out.println("Caught IO Exception");
		}
	}

	public void CreateEntry(String Key, JSONObject Value) throws Exception 
	{
		double length = (Value.toString().getBytes("utf-8").length) / 1000.0;
		try {
			if (Key.length() > 32)
				throw new KeySizeExceeded();
			else if ((length) > 16) 
				throw new ValueSizeExceeded();
		} catch (KeySizeExceeded e) {
			System.out.println(" Key size exceeds maximum size, Enter Valid Key");
		} catch (ValueSizeExceeded e) {
			System.out.println(" Value size exceeds maximum size ");
		}
		try (FileReader reader = new FileReader(FilePath)) {

			JSONTokener tokener = new JSONTokener(reader);
			JSONObject temp = new JSONObject(tokener);
			if (temp.has(Key)) 
				throw new DuplicateKey();
			JSONArray tempArray = new JSONArray();
			tempArray.put(Value); 
			tempArray.put(Integer.MAX_VALUE); 

			LocalTime time = LocalTime.now();
			int TimeStamp = time.toSecondOfDay();
			tempArray.put(TimeStamp); 
			temp.put(Key, tempArray);
			try (FileWriter file = new FileWriter(FilePath, false))
			{
				file.write(temp.toString());
				file.close();
			} catch (IOException e) {
				System.out.println("Caught IO Exception");
			}

		} catch (DuplicateKey e) {
			System.out.println("KEY already exists. Duplicate keys not allowed");
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		} catch (IOException e) {
			System.out.println("Caught IO Exception");
		}
	}

	public JSONObject ReadEntry(String Key) throws Exception 
	{

		try (FileReader reader = new FileReader(FilePath)) {
			JSONTokener tokener = new JSONTokener(reader);
			JSONObject temp = new JSONObject(tokener);
			if (temp.has(Key)) 
			{
				JSONArray tempArray = new JSONArray();
				tempArray = temp.getJSONArray(Key);
				LocalTime time = LocalTime.now();
				int CurrentTime = time.toSecondOfDay();
				if ((CurrentTime - tempArray.getInt(2)) < tempArray.getInt(1)) 
					return tempArray.getJSONObject(0);
				else
					throw new TimeExceeded();

			} else
				throw new InvalidKey();

		} catch (TimeExceeded e) {
			System.out.println("Key Exceeded Time To Live");
		} catch (InvalidKey e) {
			System.out.println("Invalid Key.Enter a valid key to continue");
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		} catch (IOException e) {
			System.out.println("Caught IO Exception");
		}
		return null;
	}

	public void DeleteEntry(String Key) throws Exception 
	{

		try (FileReader reader = new FileReader(FilePath)) 
		{
			JSONTokener tokener = new JSONTokener(reader);
			JSONObject temp = new JSONObject(tokener);
			if (temp.has(Key)) 
			{
				JSONArray tempArray = new JSONArray();
				tempArray = temp.getJSONArray(Key);
				LocalTime time = LocalTime.now();
				int CurrentTime = time.toSecondOfDay();
				if ((CurrentTime - tempArray.getInt(2)) < tempArray.getInt(1)) 
					temp.remove(Key);
				else
					throw new TimeExceeded();

				try (FileWriter file = new FileWriter(FilePath, false)) 
				{

					file.write(temp.toString());
					file.close();
				}
			} else
				throw new InvalidKey();

		} catch (InvalidKey e) {
			System.out.println("Invalid Key.Enter a valid key to continue");
		} catch (IOException e) {
			System.out.println("Caught IO Exception");
		}

		catch (TimeExceeded e) {
			System.out.println("Key Exceeded Time To Live");
		}
	}

}