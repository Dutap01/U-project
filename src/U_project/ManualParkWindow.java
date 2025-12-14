package U_project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

public class ManualParkWindow extends JFrame implements ActionListener, ItemListener, time
{
	GridBagConstraints c;
	JButton cancel;
	JButton confirm;
	String carNumber = "";
	String carSelect = "";
	CheckboxGroup cbgMode;
	CheckboxGroup cbgType; 
	
	String jariNumber = "";
	Label labelCarNum;
	Label labelJariNum;
	Label labelType;
	
	readFile obj = new readFile(); 
	
	JTextField tfCarNumber;
	JTextField tfJariNumber;
	
	Checkbox cbParkMode;
	Checkbox cbUnparkMode;
    Checkbox cbRegisterMode;
	
	Checkbox small;
	Checkbox normal;
	
	private mainWindow mainFrame; 
	
	public ManualParkWindow(mainWindow mainFrame)
	{
		this.mainFrame = mainFrame; 
		setTitle("수동 주차 관리 및 등록");
		setSize(500, 300);
		setLocation(350, 250);
		setVisible(true);
		setResizable(false);
		pan1();
	}

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == confirm)
		{
			if (cbParkMode.getState()) {
				processManualPark(); 
			} else if (cbUnparkMode.getState()) {
				processManualUnpark(); 
			} else if (cbRegisterMode.getState()) {
                processManualRegister();
            }
		} 
		else if (ae.getSource() == cancel)
		{
			dispose();
		}
	}
	
	public void itemStateChanged(ItemEvent ie)
	{
		if (cbParkMode.getState()) {
			tfCarNumber.setEnabled(true);
			tfJariNumber.setEnabled(true);
			small.setEnabled(true);
			normal.setEnabled(true);
			if (small.getState()) carSelect = "경차주차";
			else if (normal.getState()) carSelect = "일반주차";
			
		} else if (cbUnparkMode.getState()) {
			tfCarNumber.setEnabled(true);
			tfJariNumber.setEnabled(false);
			small.setEnabled(false);
			normal.setEnabled(false);
			carSelect = "";
		} else if (cbRegisterMode.getState()) {
            tfCarNumber.setEnabled(true);
			tfJariNumber.setEnabled(false);
			small.setEnabled(false);
			normal.setEnabled(false);
            carSelect = "";
        }
	}
	
	private void processManualPark() {
		jariNumber = tfJariNumber.getText();
		carNumber = tfCarNumber.getText().trim(); 
		
		if (jariNumber.isEmpty() || carNumber.isEmpty()) {
			JOptionPane.showMessageDialog(null, "자리 번호와 차량 번호를 모두 입력해주세요.");
			return;
		}
        
        readFile.readRegisteredPlates();
        boolean isRegistered = readFile.registeredPlates.contains(carNumber);

		if (isRegistered) {
			carSelect = "등록차량"; 
		} else {
            if (!small.getState() && !normal.getState()) {
                JOptionPane.showMessageDialog(null, "차량 종류(경차/일반)를 선택해주세요.");
                return;
            }
            carSelect = small.getState() ? "경차주차" : "일반주차";
		}

		int jariIndex;
		try {
			jariIndex = Integer.parseInt(jariNumber);
			if (jariIndex <= 0 || jariIndex >= mainFrame.Btn.length) {
				JOptionPane.showMessageDialog(null, "유효한 자리 번호를 입력해주세요.");
				return;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "자리번호는 숫자로 입력해주세요.");
			return;
		}
		
		obj.readData(); 
		for (int i = 0; i < obj.length; i++) {
			try {
				if (carNumber.equals(obj.carNumber[i])) {
					JOptionPane.showMessageDialog(null, "이미 입차되어 있는 차량입니다.");
					return;
				}
			} catch (java.lang.NullPointerException e1) {
			}
		}
		
		writeFile writer = new writeFile();
		String parkTime = "" + writer.year + "/" + writer.month + "/" + writer.day + "/" + writer.hour + "/" + writer.min; 
		new writeFile(jariNumber, carSelect, carNumber, parkTime, "-", "-", "-");
		
		JButton btn = mainFrame.Btn[jariIndex];
		btn.setLabel(carNumber); 
		btn.setForeground(new java.awt.Color(255, 0, 0)); 
		
		mainFrame.repaint(); 
		mainFrame.pan2.revalidate();
        
        String message = isRegistered ? "등록차량으로 " : "";
		JOptionPane.showMessageDialog(null, jariNumber + "번에 " + carNumber + " 차량이 " + message + "수동 입차되었습니다.");
		dispose();
	}
	
	private void processManualUnpark() {
		carNumber = tfCarNumber.getText().trim();
		if (carNumber.isEmpty()) {
			JOptionPane.showMessageDialog(null, "차량 번호를 입력해주세요.");
			return;
		}

		obj.readData(); 
		
		writeFile writer = new writeFile();
		
		String currentJariNumber = null;
		for(int i = 0; i < obj.length; i++) {
			if(obj.carNumber[i] != null && obj.carNumber[i].equals(carNumber)) {
				currentJariNumber = obj.jariNumber[i];
				break;
			}
		}
		
		if (currentJariNumber == null) {
			JOptionPane.showMessageDialog(null, "주차된 차량 목록에서 " + carNumber + "를 찾을 수 없습니다.");
			return;
		}
		
		writer.processUnpark(Integer.parseInt(currentJariNumber), carNumber); 

		try {
			int jariIndex = Integer.parseInt(currentJariNumber);
			JButton btn = mainFrame.Btn[jariIndex];
			btn.setLabel("  " + jariIndex + "번"); 
			btn.setForeground(new java.awt.Color(0, 0, 0)); 

			mainFrame.repaint();
			mainFrame.pan2.revalidate();
		} catch (NumberFormatException e) {
		}
		
		JOptionPane.showMessageDialog(null, carNumber + " 차량이 " + currentJariNumber + "번에서 수동 출차되었습니다.");
		dispose();
	}
    
    private void processManualRegister() {
        carNumber = tfCarNumber.getText().trim();
        
        if (!carNumber.matches("\\d{4}")) { 
            JOptionPane.showMessageDialog(null, "유효한 4자리 차량 번호를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        writeFile writer = new writeFile();
        writer.registerNewCar(carNumber); 
        
        readFile.readRegisteredPlates();
        
        dispose();
    }


	public void layout(Component obj, int x, int y, int width, int height)
	{
		c.gridx = x; 
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		add(obj, c);
	}
	
	public void pan1()
	{
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH; 
		
		cbgMode = new CheckboxGroup();
		cbParkMode = new Checkbox("수동 입차", cbgMode, true);
		cbUnparkMode = new Checkbox("수동 출차", cbgMode, false);
        cbRegisterMode = new Checkbox("차량 등록", cbgMode, false);
		cbParkMode.addItemListener(this);
		cbUnparkMode.addItemListener(this);
        cbRegisterMode.addItemListener(this);
		
		layout(new Label("모드 선택:"), 0, 0, 1, 1);
		layout(cbParkMode, 1, 0, 1, 1);
		layout(cbUnparkMode, 2, 0, 1, 1);
        layout(cbRegisterMode, 3, 0, 1, 1);
		
        cbgType = new CheckboxGroup();
		small = new Checkbox("경차주차", cbgType, false); 
		normal = new Checkbox("일반주차", cbgType, false);
		small.addItemListener(this);
		normal.addItemListener(this);
		
		layout(new Label("차량 구분:"), 0, 1, 1, 1);
		layout(small, 1, 1, 1, 1);
		layout(normal, 2, 1, 1, 1);
		
		labelJariNum = new Label("자리 번호:");
		tfJariNumber = new JTextField(10);
		layout(labelJariNum, 0, 2, 1, 1);
		layout(tfJariNumber, 1, 2, 3, 1);
		
		labelCarNum = new Label("차량 번호 (4자리):");
		tfCarNumber = new JTextField(10);
		layout(labelCarNum, 0, 3, 1, 1);
		layout(tfCarNumber, 1, 3, 3, 1);
		
		confirm = new JButton("확인");
		cancel = new JButton("취소");
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		
		layout(confirm, 1, 4, 1, 1);
		layout(cancel, 2, 4, 1, 1);
        
        small.setEnabled(true);
        normal.setEnabled(true);
	}
}