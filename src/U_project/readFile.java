package U_project;

import java.io.*;
import java.util.*;

public class readFile 
{
	public static Set<String> registeredPlates = new HashSet<>();
	
	private static final String REGISTERED_FILE_NAME = "registered_plates.csv";
	
	public int c[] = new int[6];
	public String jariNumber[] = new String[100];
	public String carSelect[] = new String[100];
	public String carNumber[] = new String[100];
	public String parkTime[] = new String[100];
	public String unparkTime[] = new String[100];
	public String inTime[] = new String[100];
	public String charge[] = new String[100];
	public int length;

	public readFile()
	{
		readConfig();
		readData();
	}
	
	public static void readRegisteredPlates() {
		registeredPlates.clear();
		try (BufferedReader reader = new BufferedReader(new FileReader(REGISTERED_FILE_NAME))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String plate = line.trim(); 
				if (!plate.isEmpty()) {
					registeredPlates.add(plate);
				}
			}
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readConfig()
	{
		try
		{
			File configFile = new File("config.cfg");
			if (!configFile.exists()) {
				writeDefaultConfig(configFile);
			}
			
			FileReader r = new FileReader(configFile);
			int i = 0;
			int j = 0;
			String temp = "";
			
			while ((i = r.read()) != -1)
			{
				if (((char) i) == ',')
				{
					c[j] = Integer.parseInt(temp);
					j++;
					temp = "";
				} else
				{
					temp = temp + (char) i;
				}
			}
			c[j] = Integer.parseInt(temp);
			r.close();
			
		} catch (IOException e)
		{
			
		}
	}
	
	private void writeDefaultConfig(File configFile) {
		try (FileWriter w = new FileWriter(configFile)) {
			w.write("1000,500,10000,2000,1000,20000"); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readData()
	{
		try
		{
			File dataFile = new File("data.csv");
			if (!dataFile.exists()) {
				dataFile.createNewFile();
			}

			BufferedReader r = new BufferedReader(new FileReader(dataFile));
			String line;
			int i = 0;
			
			while ((line = r.readLine()) != null && i < 100)
			{
				if (line.trim().isEmpty()) continue;
				
				String[] fields = line.split(",");
				if (fields.length == 7) 
				{
					jariNumber[i] = fields[0];
					carSelect[i] = fields[1];
					carNumber[i] = fields[2];
					parkTime[i] = fields[3];
					unparkTime[i] = fields[4];
					inTime[i] = fields[5];
					charge[i] = fields[6];
					i++;
				}
			}
			r.close();
			length = i;
		} catch (IOException e)
		{
			
		}
	}
}