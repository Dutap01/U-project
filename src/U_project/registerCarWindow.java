package U_project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class registerCarWindow extends JFrame implements ActionListener
{
	private JTextField carNumberField;
	private JButton registerButton;
	private JButton cancelButton;
	private writeFile writer;

	public registerCarWindow()
	{
		setTitle("차량 등록");
		setSize(300, 150);
		setLocationRelativeTo(null);
		writer = new writeFile();
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2, 10, 10));

		panel.add(new JLabel("차량 번호 (4자리):"));
		
		carNumberField = new JTextField(10);
		panel.add(carNumberField);
		
		registerButton = new JButton("등록");
		registerButton.addActionListener(this);
		panel.add(registerButton);
		
		cancelButton = new JButton("취소");
		cancelButton.addActionListener(this);
		panel.add(cancelButton);
		
		add(panel, BorderLayout.CENTER);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == registerButton)
		{
			String carNumber = carNumberField.getText().trim();
			
			if (carNumber.matches("\\d{4}")) { 
				writer.registerNewCar(carNumber);
				JOptionPane.showMessageDialog(this, "차량 번호 " + carNumber + "가 등록되었습니다.");
				
				readFile.readRegisteredPlates();
				
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "유효한 4자리 차량 번호를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			}
		} else if (ae.getSource() == cancelButton)
		{
			dispose();
		}
	}
}