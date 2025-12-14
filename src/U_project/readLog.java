package U_project;

import java.io.*;
import java.util.*;

public class readLog {
	public String[] jariNumber;
	public String[] carSelect;
	public String[] carNumber;
	public String[] parkTime;
	public String[] unparkTime;
	public String[] inTime;
	public String[] charge;
	public String[] printParkTime;
	public String[] printUnparkTime;
	public int length;

	public readLog() {
		try {
			File logFile = new File("log.csv");
			if (!logFile.exists()) {
				logFile.createNewFile();
				System.out.println("log.csv 파일이 생성됨. 기록 길이 0.");
				length = 0;
				return;
			}

			FileReader fileReader = new FileReader(logFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			StringBuilder csvStr = new StringBuilder();

			while ((line = bufferedReader.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					csvStr.append(line).append("\n");
				}
			}
			bufferedReader.close();

			if (csvStr.length() == 0) {
				length = 0;
				return;
			}

			StringTokenizer tokenizer = new StringTokenizer(csvStr.toString(), ",\n");
			int totalTokens = tokenizer.countTokens();
			int expectedFields = 7;
			
			if (totalTokens % expectedFields != 0) {
				System.err.println("log.csv 파일 오류: 총 토큰 수가 7의 배수가 아닙니다. 토큰 수: " + totalTokens);
				length = 0;
				return;
			}

			length = totalTokens / expectedFields;
			
			jariNumber = new String[length];
			carSelect = new String[length];
			carNumber = new String[length];
			parkTime = new String[length];
			unparkTime = new String[length];
			inTime = new String[length];
			charge = new String[length];
			printParkTime = new String[length];
			printUnparkTime = new String[length];

			for (int i = 0; i < length; i++) {
				jariNumber[i] = tokenizer.nextToken().trim();
				carSelect[i] = tokenizer.nextToken().trim();
				carNumber[i] = tokenizer.nextToken().trim();
				parkTime[i] = tokenizer.nextToken().trim();
				unparkTime[i] = tokenizer.nextToken().trim();
				inTime[i] = tokenizer.nextToken().trim();
				charge[i] = tokenizer.nextToken().trim();

				printParkTime[i] = ConvertTime(parkTime[i]);
				printUnparkTime[i] = ConvertTime(unparkTime[i]);
			}
			
		} catch (Exception e) {
			System.err.println("readLog 중 오류 발생: " + e.getMessage());
			length = 0;
		}
	}

	private String ConvertTime(String time) {
		if (time == null || time.isEmpty() || time.equals("-") || time.split("/").length < 5) {
			return time;
		}

		String[] parts = time.split("/"); 
		
		if (parts.length < 5) {
			return time;
		}

		try {
			String year = parts[0];
			String month = parts[1];
			String day = parts[2];
			String hour = parts[3];
			String minute = parts[4];
			
			return String.format("%s년 %s월 %s일 %s시 %s분", year, month, day, hour, minute);
			
		} catch (Exception e) {
			return time;
		}
	}
}