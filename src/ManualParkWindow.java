package U_project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.StringTokenizer;
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
	
	Checkbox small;
	Checkbox normal;
	
	public ManualParkWindow()
	{
		setTitle("수동 주차 관리");
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
			}
		}
		else if (ae.getSource() == cancel)
		{
			dispose();
		}
	}
	
	private void processManualPark() {
		jariNumber = tfJariNumber.getText();
		carNumber = tfCarNumber.getText();
		
		try {
			Integer.parseInt(carNumber);
		} catch (java.lang.NumberFormatException e2) {
			JOptionPane.showMessageDialog(null, "차량번호는 4가지 이상의 숫자여야 합니다.");
			return;
		}

		if (carSelect.equals("")) {
			JOptionPane.showMessageDialog(null, "주차유무를 선택하세요.");
			return;
		} else if (carNumber.length() < 4) {
			JOptionPane.showMessageDialog(null, "차량 번호는 4자리 이상이여야 합니다.");
			return;
		}
		
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
		JOptionPane.showMessageDialog(null, jariNumber + "번에 " + carNumber + " 차량이 수동 입차되었습니다.");
		dispose();
	}
	
	private void processManualUnpark() {
		carNumber = tfCarNumber.getText();
		int targetJariNumber = -1;
		
		for (int i = 0; i < obj.length; i++) {
			try {
				if (carNumber.equals(obj.carNumber[i])) {
					targetJariNumber = Integer.parseInt(obj.jariNumber[i]);
					break;
				}
			} catch (java.lang.NullPointerException e1) {
			}
		}
		
		if (targetJariNumber == -1) {
			JOptionPane.showMessageDialog(null, "입차 기록이 없는 차량입니다.");
			return;
		}
		
		writeFile writer = new writeFile();
		writer.processUnpark(targetJariNumber, carNumber);
		writer.deleteRecord(carNumber);
		
		JOptionPane.showMessageDialog(null, carNumber + " 차량의 출차 및 정산이 완료되었습니다. 요금은 log.csv에 기록됩니다.");
		dispose();
	}
	

	public void itemStateChanged(ItemEvent ie)
	{
		if (ie.getSource() == cbParkMode) {
			tfJariNumber.setEnabled(true);
			tfCarNumber.setEnabled(true);
			small.setEnabled(true);
			normal.setEnabled(true);
		} else if (ie.getSource() == cbUnparkMode) {
			tfJariNumber.setEnabled(false);
			tfCarNumber.setEnabled(true);
			small.setEnabled(false);
			normal.setEnabled(false);
			small.setState(false);
			normal.setState(false);
			carSelect = "";
		}
		
		if (ie.getSource() == small)
		{
			carSelect = "경차주차";
		}
		else if (ie.getSource() == normal)
		{
			carSelect = "일반주차";
		}
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
		cbParkMode.addItemListener(this);
		cbUnparkMode.addItemListener(this);
		
		layout(new Label("모드 선택:"), 0, 0, 1, 1);
		layout(cbParkMode, 1, 0, 2, 1);
		layout(cbUnparkMode, 3, 0, 2, 1);
		
		cbgType = new CheckboxGroup();
		small = new Checkbox("경차주차", cbgType, false);
		normal = new Checkbox("일반주차", cbgType, false);
		small.addItemListener(this);
		normal.addItemListener(this);
		
		layout(new Label("주차유무"), 0, 1, 1, 1);
		layout(small, 1, 1, 2, 1);
		layout(normal, 3, 1, 2, 1);
		
		labelCarNum = new Label("차량번호");
		tfCarNumber = new JTextField();
		labelJariNum = new Label("자리번호");
		tfJariNumber = new JTextField();
		
		layout(labelCarNum, 0, 2, 1, 1);
		layout(tfCarNumber, 1, 2, 4, 1);
		layout(labelJariNum, 0, 3, 1, 1);
		layout(tfJariNumber, 1, 3, 4, 1);
		
		confirm = new JButton("확인");
		cancel = new JButton("취소");
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		
		layout(new Label(""), 1, 4, 1, 1); 
		layout(confirm, 1, 5, 2, 1);
		layout(cancel, 3, 5, 2, 1);

	}
}