package U_project;

import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set; 

import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.tesseract.global.tesseract.PSM_SINGLE_LINE; 

public class ANPRController implements Runnable
{
	mainWindow mainFrame;
	writeFile writer = new writeFile(); 
	Random rand = new Random();
	private static final int MAX_CAMERA_INDEX_TRIES = 5; 
	
	private volatile boolean running = true; 
	private volatile String lastDetectedPlate = "0000"; 
	private final String tessdataPath; 
	
	private Set<String> registeredPlates; 

	public ANPRController(mainWindow mainFrame)
	{
		this.mainFrame = mainFrame;
		
		tessdataPath = "C:\\parking"; 
		
		System.out.println("Tesseract 탐색 경로 초기 설정 완료: " + tessdataPath);
		
		readFile.readRegisteredPlates();
		this.registeredPlates = readFile.registeredPlates;
	}
	
	private void releaseGrabber(OpenCVFrameGrabber grabber) {
		if (grabber != null) {
			try {
				grabber.stop();
				grabber.release();
			} catch (FrameGrabber.Exception ex) {
				System.err.println("카메라 자원 해제 오류: " + ex.getMessage());
			}
		}
	}
	
	public void run()
	{
		System.out.println("ANPR Controller 스레드 시작됨. (카메라 및 자동 인식 기능 비활성화 - 수동 모드)");
		
		while (running) {
			try {
				Thread.sleep(1000); 
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				running = false;
			}
		}
		
		System.out.println("ANPR Controller 스레드 종료됨.");
	}

	private String processFrameForPlate(Frame frame) {
		
		String detectedNumber = "0000";
		TessBaseAPI api = null;
		
		Mat image = null;
		Mat grayImage = null;
		Mat blurredImage = null;
		Mat threshImage = null;
		
		try (OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat()) {
			
			image = converterToMat.convert(frame);
			
			if (image == null || image.empty()) {
				return "0000"; 
			}
			
			api = new TessBaseAPI();
			if (api.Init(tessdataPath, "eng") != 0) { 
				System.err.println("Tesseract 초기화 실패. (경로: " + tessdataPath + ")");
				return "0000"; 
			}
			api.SetPageSegMode(PSM_SINGLE_LINE); 
			
			grayImage = new Mat();
			blurredImage = new Mat();
			threshImage = new Mat();
			
			opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_BGR2GRAY);
			opencv_imgproc.GaussianBlur(grayImage, blurredImage, new Size(5, 5), 0);
			opencv_imgproc.threshold(blurredImage, threshImage, 0, 255, opencv_imgproc.THRESH_BINARY + opencv_imgproc.THRESH_OTSU);
			
			api.SetImage(threshImage.data(), threshImage.cols(), threshImage.rows(), 1, threshImage.cols());
			String result = api.GetUTF8Text().getString();
			
			if (result != null) {
				String cleanNumber = result.replaceAll("[^0-9A-Za-z]", "").trim();
				Pattern pattern = Pattern.compile("\\d{4}"); 
				Matcher matcher = pattern.matcher(cleanNumber);
				
				if (matcher.find()) {
					detectedNumber = matcher.group(0); 
				}
			}
			
		} catch (Exception e) {
			detectedNumber = "0000";
		} finally {
			if (threshImage != null) threshImage.close();
			if (blurredImage != null) blurredImage.close();
			if (grayImage != null) grayImage.close();
			if (image != null) image.close(); 
			
			if (api != null) {
				api.End();
				api.close();
			}
		}
		
		return detectedNumber;
	}
	
	public void processParkSignal(int jariNumber)
	{
		String carNumber = this.lastDetectedPlate; 
		
		if (carNumber.equals("0000")) {
			JOptionPane.showMessageDialog(null, "감지된 유효한 차량 번호가 없습니다! 수동 처리를 이용하세요.");
			return;
		}
		
		this.lastDetectedPlate = "0000";
		
		boolean isRegistered = this.registeredPlates.contains(carNumber);
		
		String carSelect;
		
		if (isRegistered) {
			carSelect = "등록차량"; 
			System.out.println(carNumber + " 차량: 등록 차량으로 확인됨 -> 주차 처리 시작 (요금 면제).");
		} else {
			carSelect = isCompactCar(carNumber) ? "경차주차" : "일반주차";
			System.out.println(carNumber + " 차량 종류 판별: " + carSelect + " -> 주차 처리 시작.");
		}
		
		new writeFile(String.valueOf(jariNumber), carSelect, carNumber, writer.getCurrentTime(), "-", "-", "-");

		JButton btn = mainFrame.Btn[jariNumber];
		btn.setLabel(carNumber);
		btn.setForeground(new java.awt.Color(255, 0, 0)); 
		mainFrame.repaint(); 
	}
	
	private boolean isCompactCar(String number) {
		try {
			int lastFourDigits = Integer.parseInt(number);
			return lastFourDigits < 1000; 
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public void processUnparkSignal(int jariNumber, String carNumber)
	{
		writer.processUnpark(jariNumber, carNumber); 
		writer.deleteRecord(carNumber);
		
		JButton btn = mainFrame.Btn[jariNumber];
		btn.setLabel(String.valueOf(jariNumber)); 
		btn.setForeground(new java.awt.Color(0, 0, 0)); 
		
		btn.repaint();
		mainFrame.revalidate();
		mainFrame.repaint(); 
	}

	public void stopRunning() {
		this.running = false;
	}
}