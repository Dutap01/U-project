package U_project;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class writeFile implements time 
{
	public writeFile() {
        // 기본 생성자
    }
	
	public writeFile(String jariNumber, String carSelect, String carNumber,
			String parkTime, String unparkTime, String inTime, String charge)
	{
		try
		{
			FileWriter w = new FileWriter("data.csv", true);
			w.write(jariNumber + "," + carSelect + "," + carNumber + ","
					+ parkTime + "," + unparkTime + "," + inTime + "," + charge
					+ "\n");
			w.close();
		} catch (IOException e)
		{
		}
	}

    public String getCurrentTime() {
        return "" + year + "/" + month + "/" + day + "/" + hour + "/" + min;
    }

    public void processUnpark(int jariNumber, String carNumber) {
        
        readFile r = new readFile();
        int[] config = r.c; 
        
        String parkTimeStr = "";
        String carSelect = "";
        for (int i = 0; i < r.length; i++) {
            if (r.carNumber[i].equals(carNumber)) {
                parkTimeStr = r.parkTime[i];
                carSelect = r.carSelect[i];
                break;
            }
        }
        
        long parkingMinutes = calculateParkingTime(parkTimeStr, getCurrentTime());
        int charge = calculateCharge(carSelect, parkingMinutes, config);
        
        String logRecord = jariNumber + "," + carSelect + "," + carNumber + "," + parkTimeStr + "," + getCurrentTime() + "," + parkingMinutes + "," + charge;
        try {
            FileWriter w = new FileWriter("log.csv", true); 
            w.write(logRecord + "\n");
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private long calculateParkingTime(String parkTimeStr, String unparkTimeStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
            Date parkDate = format.parse(parkTimeStr);
            Date unparkDate = format.parse(unparkTimeStr);
            
            long diff = unparkDate.getTime() - parkDate.getTime();
            return diff / (60 * 1000); 
        } catch (Exception e) {
            return 0;
        }
    }
    
    private int calculateCharge(String carSelect, long minutes, int[] c) {
        int baseMin = 60;
        int intervalMin = 30;
        int baseFee = 0;
        int intervalFee = 0;
        int maxFee = 0;
        
        int baseIndex = -1; 
        
        if (carSelect.equals("경차주차")) baseIndex = 0;
        else if (carSelect.equals("일반주차")) baseIndex = 3;

        if (baseIndex != -1) {
            baseFee = c[baseIndex];     
            intervalFee = c[baseIndex + 1]; 
            maxFee = c[baseIndex + 2];  
        } else {
            return 0; 
        }
        
        if (minutes <= 0) return baseFee; 

        int totalFee = baseFee;
        long remainingMinutes = minutes - baseMin;

        if (remainingMinutes > 0) {
            int intervals = (int) Math.ceil((double) remainingMinutes / intervalMin);
            totalFee += intervals * intervalFee;
        }
        
        return Math.min(totalFee, maxFee); 
    }
    
	public void deleteRecord(String carNumber)
	{
		try
		{
			FileReader file = new FileReader(new File("data.csv"));
			BufferedReader r = new BufferedReader(file);
			String line;
			StringBuilder newContent = new StringBuilder();

			while ((line = r.readLine()) != null)
			{
				String[] fields = line.split(",");
				if (fields.length > 2 && !fields[2].trim().equals(carNumber))
				{
					newContent.append(line).append("\n");
				}
			}
			r.close();

			FileWriter w = new FileWriter("data.csv", false); 
			w.write(newContent.toString());
			w.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}