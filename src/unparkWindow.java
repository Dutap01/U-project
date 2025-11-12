import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class unparkWindow extends JFrame implements ActionListener, time
{
	GridBagConstraints c;
	String carNumber;
	String carSelect;
	CheckboxGroup cbg;
	String charge;
	String inTime;
	String jariNumber;
	readFile obj = new readFile();
	JPanel pan1;
	JPanel pan2;
	JPanel pan3;
	String parkTime;
	StringTokenizer parse;
	String printParkTime;
	String printUnparkTime;
	String pt[];
	int ptInt[];
	String temp[] = new String[6];
	int temp1;
	int temp2;
	int temp3;
	JTextField tf12;
	JTextField tf13;
	JTextField tf14;
	JTextField tf15;
	JTextField tf16;
	JTextField tf17;
	JTextField tf18;
	String unparkTime;
	String upt[];
	int uptInt[];
	JButton confirm;
	JButton cancel;
	Checkbox small;
	Checkbox normal;
	Checkbox fast_speed;
	Checkbox slow_speed;

	public unparkWindow(int jariNumber)
	{
		for (int i = 0; i < obj.length; i++)
		{
			if (jariNumber == Integer.parseInt(obj.jariNumber[i]))
			{
				temp1 = i;
				break;
			}
		}
		this.carSelect = obj.carSelect[temp1];
		this.carNumber = obj.carNumber[temp1];
		this.jariNumber = "" + jariNumber;
		this.parkTime = obj.parkTime[temp1];
		this.printParkTime = ConvertTime(parkTime);
		this.unparkTime = "" + year + "/" + month + "/" + day + "/" + hour + "/" + min;
		this.printUnparkTime = ConvertTime(unparkTime);
		this.inTime = "" + intime();
		this.charge = "" + charge();
		setTitle("출차");
		setSize(250, 300);
		setLocation(350, 250);
		setVisible(true);
		setResizable(false);
		pan1();
	}
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == confirm)
		{
			new writeFile(jariNumber, carSelect, carNumber, parkTime,unparkTime, inTime, charge);
			dispose();
		}
		else if (ae.getSource() == cancel)
		{
			dispose();
		}
		mainWindow cl = new mainWindow();
		Thread th = new Thread(cl);
		th.start();
	}
	public int charge()
	{
		if (carSelect.equals("경차주차"))
		{
			if (intime() < 60)
			{
				return obj.c[0];
			}
			else if (intime() <= 60)
			{
				if (((((intime() - 60) / 30 + 1) * obj.c[1]) + obj.c[0]) < obj.c[2])
				{
					return (((intime() - 60) / 30 + 1) * obj.c[1]) + obj.c[0];
				}
				else if (((((intime() - 60) / 30 + 1) * obj.c[1]) + obj.c[0]) > obj.c[2])
				{
					return obj.c[2];
				}
			}

		}
		else if (carSelect.equals("일반주차"))
		{
			if (intime() < 60)
			{
				return obj.c[3];
			}
			else if (intime() <= 60)
			{
				if ((((intime() - 60) / 30 + 1) * obj.c[4]) + obj.c[3] < obj.c[5])
				{
					return (((intime() - 60) / 30 + 1) * obj.c[4]) + obj.c[3];
				}
				else if ((((intime() - 60) / 30 + 1) * obj.c[4]) + obj.c[3] > obj.c[5])
				{
					return obj.c[5];
				}
			}
		}
		else if (carSelect.equals("완속충전"))
		{
			if (intime() < 60)
			{
				return obj.c[6];
			} else if (intime() <= 60)
			{
				if ((((intime() - 60) / 30 + 1) * obj.c[7]) + obj.c[6] < obj.c[8])
				{
					return (((intime() - 60) / 30 + 1) * obj.c[7]) + obj.c[6];
				}
				else if ((((intime() - 60) / 30 + 1) * obj.c[7]) + obj.c[6] > obj.c[8])
				{
					return obj.c[8];
				}
			}
		}
		else if (carSelect.equals("고속충전"))
		{
			if (intime() < 60)
			{
				return obj.c[9];
			}
			else if (intime() <= 60)
			{
				if ((((intime() - 60) / 30 + 1) * obj.c[10]) + obj.c[9] < obj.c[11])
				{
					return (((intime() - 60) / 30 + 1) * obj.c[10]) + obj.c[9];
				}
				else if ((((intime() - 60) / 30 + 1) * obj.c[10]) + obj.c[9] > obj.c[11])
				{
					return obj.c[11];
				}
			}
		}
		return 0;
	}

	private String ConvertTime(String time)
	{
		StringTokenizer parse = new StringTokenizer(time, "/");
		temp[0] = parse.nextToken();
		temp[1] = parse.nextToken();
		temp[2] = parse.nextToken();
		temp[3] = parse.nextToken();
		temp[4] = parse.nextToken();
		String printTime = temp[0] + "년 " + temp[1] + "월 " + temp[2] + "일 " + temp[3] + "시 " + temp[4] + "분";
		return printTime;
	}

	public int intime()
	{
		pt = new String[5];
		ptInt = new int[5];
		upt = new String[5];
		uptInt = new int[5];
		StringTokenizer parse1 = new StringTokenizer(parkTime, "/");
		StringTokenizer parse2 = new StringTokenizer(unparkTime, "/");
		for (int i = 0; i < 5; i++)
		{
			pt[i] = parse1.nextToken();
			ptInt[i] = Integer.parseInt(pt[i]);
			upt[i] = parse2.nextToken();
			uptInt[i] = Integer.parseInt(upt[i]);
		}
		temp2 = (ptInt[2] * 1440) + (ptInt[3] * 60) + (ptInt[4]);
		temp3 = (uptInt[2] * 1440) + (uptInt[3] * 60) + (uptInt[4]);
		return temp3 - temp2;
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
		tf12 = new JTextField(carSelect);
		tf13 = new JTextField(carNumber);
		tf14 = new JTextField(jariNumber);
		tf15 = new JTextField(printParkTime);
		tf16 = new JTextField(printUnparkTime);
		tf17 = new JTextField(inTime);
		tf18 = new JTextField(charge);
		confirm = new JButton("확인");
		cancel = new JButton("취소");
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		layout(new Label("전기차유무"), 0, 0, 1, 2);
		layout(tf12, 1, 1, 4, 1);
		layout(new Label("차량번호"), 0, 2, 1, 1);
		layout(tf13, 1, 2, 4, 1);
		layout(new Label("자리번호"), 0, 3, 1, 1);
		layout(tf14, 1, 3, 4, 1);
		layout(new Label("입차시간"), 0, 4, 1, 1);
		layout(tf15, 1, 4, 4, 1);
		layout(new Label("출차시간"), 0, 5, 1, 1);
		layout(tf16, 1, 5, 4, 1);
		layout(new Label("주차시간(분)"), 0, 6, 1, 1);
		layout(tf17, 1, 6, 4, 1);
		layout(new Label("요금"), 0, 7, 1, 1);
		layout(tf18, 1, 7, 4, 1);
		layout(new Label(""), 1, 8, 1, 1);
		layout(new Label("출차하시겠습니까?"), 1, 9, 4, 1);
		layout(confirm, 1, 10, 2, 1);
		layout(cancel, 3, 10, 2, 1);
		tf12.setEditable(false);
		tf13.setEditable(false);
		tf14.setEditable(false);
		tf15.setEditable(false);
		tf16.setEditable(false);
		tf17.setEditable(false);
		tf18.setEditable(false);

	}
}