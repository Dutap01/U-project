import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

public class logWindow extends JFrame implements ActionListener, ItemListener
{
	Checkbox cb0;
	Checkbox cb1;
	Checkbox cb2;
	DefaultTableColumnModel cModel;
	Label label1;
	Label label10;
	Label label11;
	Label label12;
	Label label13;
	Label label14;
	Label label15;
	Label label16;
	Label label2;
	Label label3;
	Label label4;
	Label label5;
	Label label6;
	Label label7;
	Label label8;
	Label label9;
	DefaultTableModel model;
	DefaultTableModel model2;
	readLog obj = new readLog();
	JPanel pan1;
	JPanel pan2;
	JPanel pan3;
	JPanel pan4;
	JPanel pan5;
	JScrollPane scrollpane;
	JScrollPane scrollpane2;
	JButton search;
	JTable table;
	JTable table2;
	String[] temp = new String[7];
	JTextField tf1;
	JTextField tf2;
	JTextField tf3;
	JTextField tf4;
	static int fast_speedCnt = 0;
	static int slow_speedCnt = 0;
	static int normalCnt = 0;
	static int totalCar = 0;
	static int totalCharge = 0;
	static int smallCnt = 0;
	String[] historyCol = { "자리 번호", "주차,충전유무", "차량번호", "입차시간", "출차시간", "주차시간(분)","요금(원)" };
	String[] totalCol = { "통계", "경차", "일반차량", "완속충전", "고속충전", "차량댓수", "요금합계(원)" };

	public logWindow() 
	{
		setTitle("주차내역");
		setSize(1000, 700);
		model = new DefaultTableModel(historyCol, 0);
		table = new JTable(model);
		model2 = new DefaultTableModel(totalCol, 0);
		table2 = new JTable(model2);
		pan1 = new JPanel();
		pan2 = new JPanel();
		pan3 = new JPanel();
		defaultTable();
		pan1();
		pan3();
		setVisible(true);
		setResizable(false);
	}
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == search)
		{
			String temp1 = tf1.getText();
			String temp2 = tf2.getText();
			String temp3 = tf3.getText();
			String temp4 = tf4.getText();
			removeTable();

			if (!temp1.isEmpty() && temp2.isEmpty() && temp3.isEmpty()&& temp4.isEmpty())
			{
				carNumberSearch(tf1.getText());
			}
			else if (temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()&& !temp4.isEmpty())
			{
				dateSearch(tf2.getText(), tf3.getText(), tf4.getText());
			}
			else
			{
				defaultTable();
			}
		}
	}
	public void carNumberSearch(String carNumber)
	{
		totalCar = 0;
		smallCnt = 0;
		normalCnt = 0;
		slow_speedCnt = 0;
		fast_speedCnt = 0;
		totalCharge = 0;
		for (int i = 0; i < obj.length; i++)
		{
			if (obj.carNumber[i].equals(carNumber))
			{
				temp[0] = obj.jariNumber[i];
				temp[1] = obj.carSelect[i];
				temp[2] = obj.carNumber[i];
				temp[3] = obj.printParkTime[i];
				temp[4] = obj.printUnparkTime[i];
				temp[5] = obj.inTime[i];
				temp[6] = obj.charge[i];
				model.addRow(temp);
				total(obj.carSelect[i], obj.inTime[i], obj.charge[i]);
				totalCar++;
			}
		}
		temp[0] = "";
		temp[1] = Integer.toString(smallCnt);
		temp[2] = Integer.toString(normalCnt);
		temp[3] = Integer.toString(slow_speedCnt);
		temp[4] = Integer.toString(fast_speedCnt);
		temp[5] = Integer.toString(totalCar);
		temp[6] = Integer.toString(totalCharge);
		model2.addRow(temp); 
	}

	public void dateSearch(String year, String month, String day)
	{
		totalCar = 0;
		smallCnt = 0;
		normalCnt = 0;
		slow_speedCnt = 0;
		fast_speedCnt = 0;
		totalCharge = 0;
		for (int i = 0; i < obj.length; i++)
		{
			String s = obj.printUnparkTime[i];
			if (s.matches(year + "년.*"))
				if (s.matches(".*" + month + "월.*"))
					if (s.matches(".*" + day + "일.*"))
					{
						temp[0] = obj.jariNumber[i];
						temp[1] = obj.carSelect[i];
						temp[2] = obj.carNumber[i];
						temp[3] = obj.printParkTime[i];
						temp[4] = obj.printUnparkTime[i];
						temp[5] = obj.inTime[i];
						temp[6] = obj.charge[i];
						model.addRow(temp);
						total(obj.carSelect[i], obj.inTime[i], obj.charge[i]);
						totalCar++;
					}

		}
		temp[0] = "";
		temp[1] = Integer.toString(smallCnt);
		temp[2] = Integer.toString(normalCnt);
		temp[3] = Integer.toString(slow_speedCnt);
		temp[4] = Integer.toString(fast_speedCnt);
		temp[5] = Integer.toString(totalCar);
		temp[6] = Integer.toString(totalCharge);
		model2.addRow(temp);
	}

	public void defaultTable()
	{
		totalCar = 0;
		smallCnt = 0;
		normalCnt = 0;
		slow_speedCnt = 0;
		fast_speedCnt = 0;
		totalCharge = 0;
		for (int i = 0; i < obj.length; i++)
		{
			temp[0] = obj.jariNumber[i];
			temp[1] = obj.carSelect[i];
			temp[2] = obj.carNumber[i];
			temp[3] = obj.printParkTime[i];
			temp[4] = obj.printUnparkTime[i];
			temp[5] = obj.inTime[i];
			temp[6] = obj.charge[i];
			model.addRow(temp);
			total(obj.carSelect[i], obj.inTime[i], obj.charge[i]);
			totalCar++;
		}

		temp[0] = "";
		temp[1] = Integer.toString(smallCnt);
		temp[2] = Integer.toString(normalCnt);
		temp[3] = Integer.toString(slow_speedCnt);
		temp[4] = Integer.toString(fast_speedCnt);
		temp[5] = Integer.toString(totalCar);
		temp[6] = Integer.toString(totalCharge);
		model2.addRow(temp);
	}

	public void itemStateChanged(ItemEvent ie)
	{
		if (ie.getSource() == cb1)
		{
			tf2.setEnabled(false);
			tf3.setEnabled(false);
			tf4.setEnabled(false);
			tf2.setText("");
			tf3.setText("");
			tf4.setText("");
			tf1.setEnabled(true);

		}
		else if (ie.getSource() == cb2)
		{
			tf1.setEnabled(false);
			tf1.setText("");
			tf2.setEnabled(true);
			tf3.setEnabled(true);
			tf4.setEnabled(true);

		}
	}
	public void pan1()
	{
		Font f1 = new Font("NanumGothicOTF", Font.BOLD, 24);
		BoxLayout blayout = new BoxLayout(pan1, BoxLayout.Y_AXIS);
		pan1.setLayout(blayout);
		label1 = new Label("주차내역");
		label1.setFont(f1);
		pan1.add(label1);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(250);
		table.getColumnModel().getColumn(4).setPreferredWidth(250);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		scrollpane = new JScrollPane(table);
		pan1.add(scrollpane);
		label2 = new Label("통계");
		label2.setFont(f1);
		pan1.add(label2);
		scrollpane2 = new JScrollPane(table2);
		pan1.add(scrollpane2);
		table2.setPreferredScrollableViewportSize(new Dimension(300, 30));
		add(pan1, "North");
		table.setEnabled(false);
		table2.setEnabled(false);
	}

	public void pan3()
	{
		CheckboxGroup cbg = new CheckboxGroup();
		cb1 = new Checkbox("차량번호", cbg, true);
		cb2 = new Checkbox("날짜", cbg, false);
		label3 = new Label("               ");
		label4 = new Label("년");
		label5 = new Label("월");
		label6 = new Label("일");
		tf1 = new JTextField(10);
		tf2 = new JTextField(10);
		tf3 = new JTextField(10);
		tf4 = new JTextField(10);
		pan3.add(label3);
		pan3.add(cb1);
		pan3.add(tf1);
		label7 = new Label("               ");
		pan3.add(label7);
		pan3.add(cb2);
		pan3.add(tf2);
		pan3.add(label4);
		pan3.add(tf3);
		pan3.add(label5);
		pan3.add(tf4);
		pan3.add(label6);
		cb1.addItemListener(this);
		cb2.addItemListener(this);
		search = new JButton("검색");
		search.addActionListener(this);
		pan3.add(search);
		tf2.setEnabled(false);
		tf3.setEnabled(false);
		tf4.setEnabled(false);
		add(pan3, "South");

	}

	public void removeTable()
	{
		int temp = table.getRowCount();
		for (int i = 0; i < temp; i++)
		{
			model.removeRow(0);
		}
		temp = table2.getRowCount();
		for (int i = 0; i < temp; i++)
		{
			model2.removeRow(0);
		}
	}
	public void total(String carSelect, String inTime, String charge)
	{

		if (carSelect.equals("경차주차"))
			smallCnt++;
		else if (carSelect.equals("일반주차"))
			normalCnt++;
		else if (carSelect.equals("완속충전"))
			slow_speedCnt++;
		else if (carSelect.equals("고속충전"))
			fast_speedCnt++;
		totalCharge += Integer.parseInt(charge);
	}
};
