import java.io.*;
import java.util.*;

public class readFile
{
	public int c[];
	public String carNumber[];
	public String carSelect[];
	public String charge[];
	public String inTime[];
	public String jariNumber[];
	public int length;
	public String parkTime[];
	public String printParkTime[];
	public String printUnparkTime[];
	public String temp[] = new String[5];
	public String temp1 = "";
	public String unparkTime[];

	public readFile()
	{
		readdata();
		readconfig();
	}

	public void readconfig()
	{
		try
		{
			FileReader file = new FileReader(new File("config.cfg"));
			BufferedReader r = new BufferedReader(file);
			temp1 = r.readLine();
			c = new int[12];
			StringTokenizer parse = new StringTokenizer(temp1, ",");
			String temp[] = new String[15];
			for (int i = 0; i < 12; i++)
			{
				temp[i] = parse.nextToken();
				c[i] = Integer.parseInt(temp[i]);
			}
		}
		catch (IOException e)
		{
			String temp = "500,250,10000,1000,1000,15000,1500,1500,15000,2000,2000,20000";
			try
			{
				FileWriter w = new FileWriter("config.cfg");
				w.write(temp);
				w.close();
			} catch (IOException e1)
			{
			}
		}
	}

	public void readdata()
	{
		try
		{
			FileReader file = new FileReader(new File("data.csv"));
			BufferedReader r = new BufferedReader(file);
			String csvStr = "";
			String tmpStr = "";
			while (tmpStr != null)
			{
				tmpStr = r.readLine();
				if (tmpStr != null)
				{
					csvStr = csvStr + tmpStr + ",";
				}
			}
			StringTokenizer parse = new StringTokenizer(csvStr, ",");
			length = (parse.countTokens() / 7);
			jariNumber = new String[length];
			carSelect = new String[length];
			carNumber = new String[length];
			parkTime = new String[length];
			unparkTime = new String[length];
			inTime = new String[length];
			charge = new String[length];
			for (int i = 0; i < length; i++)
			{
				jariNumber[i] = parse.nextToken();
				carSelect[i] = parse.nextToken();
				carNumber[i] = parse.nextToken();
				parkTime[i] = parse.nextToken();
				unparkTime[i] = parse.nextToken();
				inTime[i] = parse.nextToken();
				charge[i] = parse.nextToken();
			}
		}
		catch (IOException e)
		{
		}
	}

}
