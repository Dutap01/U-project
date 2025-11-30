package U_project;

import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;


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
        
        boolean cameraSuccess = false;
        
        try {
            cameraSuccess = true;
            
        } catch (Exception e) {
            System.err.println("카메라 접근 또는 JavaCV 에러 발생! [ERROR: " + e.getMessage() + "]");
        }
        
        if (cameraSuccess) {
            System.out.println("✅ 카메라 접근 성공: 번호판 인식 시뮬레이션.");
            return String.format("%04d", rand.nextInt(10000));
        } else {
            return "0000";
        }
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