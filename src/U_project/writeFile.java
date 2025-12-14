package U_project;

import java.util.Calendar;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class writeFile implements time 
{
	private String getCurrentTimeFormat(String format) {
		return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
	}
    
	public writeFile() {
	}
	
	public void registerNewCar(String carNumber) {
		if (readFile.registeredPlates.contains(carNumber)) {
			JOptionPane.showMessageDialog(null, "차량 번호 " + carNumber + "는 이미 등록되어 있습니다.");
			return;
		}
		
		try (FileWriter w = new FileWriter("registered_plates.csv", true)) {
			w.write(carNumber + "\n");
			JOptionPane.showMessageDialog(null, "차량 번호 " + carNumber + "가 등록되었습니다. 재시작 없이 즉시 적용됩니다.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "차량 등록 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}
	
	public writeFile(String jariNumber, String carSelect, String carNumber,
			String parkTime, String unparkTime, String inTime, String charge)
	{
		try
		{
			File dataFile = new File("data.csv");
			if (!dataFile.exists()) {
				dataFile.createNewFile();
			}
			
			FileWriter w = new FileWriter(dataFile, true);
			w.write(jariNumber + "," + carSelect + "," + carNumber + ","
					+ parkTime + "," + unparkTime + "," + inTime + "," + charge
					+ "\n");
			w.close();
		} catch (IOException e)
		{
			System.err.println("Error writing to data.csv during parking:");
			e.printStackTrace();
		}
	}

	public String getCurrentTime() {
		return getCurrentTimeFormat("yyyy/MM/dd/HH/mm");
	}

	public void processUnpark(int jariNumber, String carNumber) {
		
		readFile r = new readFile();
		int[] config = r.c != null ? r.c : new int[]{0,0,0,0,0,0}; 
		
		String parkTimeStr = "";
		String carSelect = "";
		
		r.readData(); 
		
		for (int i = 0; i < r.length; i++) {
			if (r.carNumber[i] != null && r.carNumber[i].equals(carNumber)) {
				parkTimeStr = r.parkTime[i];
				carSelect = r.carSelect[i];
				break;
			}
		}
		
		if (parkTimeStr.isEmpty() || parkTimeStr.equals("-")) {
			System.err.println("Error: Parking record not found in data.csv for car number " + carNumber);
			return;
		}
        
        long parkingMinutes = 0;
        int charge = 0;

        readFile.readRegisteredPlates();
        boolean isCurrentlyRegistered = readFile.registeredPlates.contains(carNumber);

        System.out.println("--- 출차 처리 디버그 ---");
        System.out.println("차량번호: " + carNumber);
        System.out.println("data.csv에서 읽은 carSelect: [" + carSelect + "]");
        System.out.println("현재 등록 목록에 포함 여부: " + isCurrentlyRegistered);
        if (carSelect.equals("등록차량") || isCurrentlyRegistered) {
            parkingMinutes = calculateParkingTime(parkTimeStr, getCurrentTime());
            charge = 0; 
            System.out.println("결과: 요금 면제 확정 (Charge=0).");
        } else {
            parkingMinutes = calculateParkingTime(parkTimeStr, getCurrentTime());
            charge = calculateCharge(carSelect, parkingMinutes, config);
            System.out.println("결과: 일반 요금 계산 적용 (Charge=" + charge + ").");
        }
		
		String logRecord = jariNumber + "," + carSelect + "," + carNumber + "," + parkTimeStr + "," + getCurrentTime() + "," + parkingMinutes + "," + charge;
		try {
			File logFile = new File("log.csv");
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			
			FileWriter w = new FileWriter(logFile, true); 
			w.write(logRecord + "\n");
			w.close();
			System.out.println("LOG SUCCESS: Car " + carNumber + " written to log.csv.");
		} catch (IOException e)
		{
			System.err.println("Error writing to log.csv during unparking:");
			e.printStackTrace();
		}
		
		deleteRecord(carNumber);
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

		if (baseIndex != -1 && c.length >= baseIndex + 3) {
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
			File dataFile = new File("data.csv");
			
			if (!dataFile.exists()) return;

			FileReader file = new FileReader(dataFile);
			BufferedReader r = new BufferedReader(file);
			String line;
			StringBuilder newContent = new StringBuilder();

			while ((line = r.readLine()) != null)
			{
				if (line.trim().isEmpty()) continue;
				
				String[] fields = line.split(",");
				if (fields.length > 2 && !fields[2].trim().equals(carNumber))
				{
					newContent.append(line).append("\n");
				}
			}
			r.close();

			FileWriter w = new FileWriter(dataFile, false); 
			w.write(newContent.toString());
			w.close();
		}
		catch (IOException e)
		{
			System.err.println("Error deleting record from data.csv:");
			e.printStackTrace();
		}
	}
}