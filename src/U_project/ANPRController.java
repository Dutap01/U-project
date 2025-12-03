package U_project;

import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.io.File;

// JavaCV/OpenCV/Tesseract Import
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class ANPRController implements Runnable
{
	mainWindow mainFrame;
	writeFile writer = new writeFile();
	Random rand = new Random();
	
	public ANPRController(mainWindow mainFrame)
	{
		this.mainFrame = mainFrame;
	}

	public void run()
	{

	}
    
    private String detectPlateFromCamera() {
        
        String detectedNumber = "0000"; 
        OpenCVFrameGrabber grabber = null;
        
        try {
            // 0번 카메라 인덱스로 노트북 웹캠 접근
            grabber = new OpenCVFrameGrabber(0); 
            grabber.start(); 
            
            // 단일 프레임 캡처
            Frame frame = grabber.grab(); 
            
            if (frame != null) {
                
                // 1. Frame을 OpenCV Mat 객체로 변환
                OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
                Mat image = converterToMat.convert(frame);
                
                // 2. OCR 초기화 및 인식 (간단한 이미지 처리 후 Tesseract 실행)
                if (image != null && !image.empty()) {
                    
                    // --- 실제 ANPR 로직 (시뮬레이션) ---
                    // 실제 구현에서는 번호판 영역을 잘라내고 전처리해야 함
                    
                    TessBaseAPI api = new TessBaseAPI();
                    // Tesseract 초기화 (언어: English, 데이터 경로 지정 필요)
                    // tessdata 폴더가 실행 경로에 있어야 함
                    if (api.Init(new File("").getAbsolutePath(), "eng") != 0) {
                        System.err.println("Tesseract 초기화 실패.");
                        api.close();
                        return "0000";
                    }
                    
                    // 이미지 처리: 흑백 변환 (OCR 정확도 향상)
                    Mat grayImage = new Mat();
                    opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_BGR2GRAY);
                    
                    // Mat을 TessBaseAPI에 전달하여 OCR 실행
                    api.SetImage(grayImage.data(), grayImage.cols(), grayImage.rows(), 1, grayImage.cols());
                    String result = api.GetUTF8Text().getString();
                    
                    api.End();
                    
                    if (result != null) {
                        // 결과에서 공백 및 특수 문자 제거 후 차량 번호 추출
                        String cleanNumber = result.replaceAll("[^0-9A-Za-z]", "").trim();
                        if (cleanNumber.length() >= 4) {
                            detectedNumber = cleanNumber.substring(0, 4); // 예시로 4자리만 사용
                            System.out.println("OCR 인식 성공 (원문: " + result.trim() + ", 추출: " + detectedNumber + ")");
                        } else {
                            detectedNumber = "0000"; // 인식된 문자열이 짧으면 실패 처리
                            System.err.println("OCR 인식 결과가 너무 짧습니다.");
                        }
                    }
                    
                    grayImage.close();
                    image.close();
                }

            } else {
                System.err.println("카메라 프레임 캡처 실패.");
            }
            
        } catch (Exception e) {
            System.err.println("카메라 접근 실패 또는 JavaCV/Tesseract 오류! [ERROR: " + e.getMessage() + "]");
            JOptionPane.showMessageDialog(null, "카메라/Tesseract 오류! 수동 처리 필요. [ERROR: " + e.getMessage() + "]");
            detectedNumber = "0000";
        } finally {
            if (grabber != null) {
                try {
                    grabber.stop();
                    grabber.release();
                } catch (FrameGrabber.Exception ex) {
                    System.err.println("카메라 자원 해제 오류: " + ex.getMessage());
                }
            }
        }
        
        return detectedNumber;
    }
	
	public void processParkSignal(int jariNumber)
	{
        String carNumber = detectPlateFromCamera(); 
		
        if (carNumber.equals("0000")) {
            JOptionPane.showMessageDialog(null, "카메라 인식 실패! 수동 처리를 이용하거나 다시 시도하세요.");
            return;
        }
        
		String carSelect = (rand.nextBoolean()) ? "경차주차" : "일반주차"; 
		
		new writeFile(String.valueOf(jariNumber), carSelect, carNumber, writer.getCurrentTime(), "-", "-", "-");

		JButton btn = mainFrame.Btn[jariNumber];
		btn.setLabel(carNumber);
		btn.setForeground(new java.awt.Color(255, 0, 0));
		mainFrame.repaint(); 
	}
	
	public void processUnparkSignal(int jariNumber, String carNumber)
	{
		writer.processUnpark(jariNumber, carNumber); 

		writer.deleteRecord(carNumber);
		
		JButton btn = mainFrame.Btn[jariNumber];
		btn.setLabel(String.valueOf(jariNumber)); 
		btn.setForeground(new java.awt.Color(0, 0, 0)); 
		mainFrame.repaint(); 
	}
}