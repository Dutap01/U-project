package U_project;

import java.io.*;
import java.util.*;

public class readLog
{
	public String carNumber[];
	public String carSelect[];
	public String charge[];
	public String inTime[];
	public String jariNumber[];
	public int length;
	public String parkTime[];
	StringTokenizer parse;
	public String printParkTime[];
	public String printUnparkTime[];
	public String temp[] = new String[5];
	public String unparkTime[];

	public readLog()
	{
		try 
		{
			FileReader file = new FileReader(new File("log.csv"));
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
			parse = new StringTokenizer(csvStr, ",");
			length = (parse.countTokens() / 7);
			jariNumber = new String[length];
			carSelect = new String[length];
			carNumber = new String[length];
			parkTime = new String[length];
			unparkTime = new String[length];
			printParkTime = new String[length];
			printUnparkTime = new String[length];
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
			for (int i = 0; i < length; i++)
			{
				printParkTime[i] = ConvertTime(parkTime[i]);
				printUnparkTime[i] = ConvertTime(unparkTime[i]);
			}
		}
		catch (IOException e)
		{
		}
	}
	private String ConvertTime(String time)
	{
		StringTokenizer parse = new StringTokenizer(time, "/");
		temp[0] = parse.nextToken();
		temp[1] = parse.nextToken();
		temp[2] = parse.nextToken();
		temp[3] = parse.nextToken();
		temp[4] = parse.nextToken();
		String printTime = temp[0] + "년 " + temp[1] + "월 " + temp[2] + "일 "+ temp[3] + "시 " + temp[4] + "분";
		return printTime;
	}
}