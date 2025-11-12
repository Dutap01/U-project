import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.StringTokenizer;
import javax.swing.*;

public class parkWindow extends JFrame implements ActionListener, ItemListener,time
{
	GridBagConstraints c;
	JButton cancel;
	String carNumber = "";
	String carSelect = "";
	CheckboxGroup cbg;
	JButton confirm;
	String jariNumber = "";
	Label label1;
	Label label2;
	Label label3;
	Checkbox fast_speed;
	Checkbox slow_speed;
	readFile obj = new readFile();
	JPanel pan1;
	JPanel pan2;
	JPanel pan3;
	String parkTime = "";
	String printParkTime = "";
	Checkbox normal;
	String[] temp = new String[5];
	JTextField tf1;
	JTextField tf2;
	Checkbox small;

	public parkWindow(int jariNumber)
	{
		this.jariNumber = "" + jariNumber;
		setTitle("입차");
		setSize(400, 250);
		setLocation(350, 250);
		setVisible(true);
		setResizable(false);
		pan1();

	}

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == confirm)
		{
			carNumber = tf1.getText();
			jariNumber = tf2.getText();
			try
			{
				Integer.parseInt(carNumber);
			}
			catch (java.lang.NumberFormatException e2)
			{
				JOptionPane.showMessageDialog(null, "차량번호 4가지 이상의 숫자여야 합니다.");
				return;
			}
			if (carSelect.equals(""))
			{
				JOptionPane.showMessageDialog(null, "주차,충전유무을 선택하세요.");
				return;
			}
			else if (carNumber.length() < 4)
			{
				JOptionPane.showMessageDialog(null, "차량 번호는 4자리 이상이여야 합니다.");
				return;
			}
			else
			{
				for (int i = 0; i < 120; i++)
				{
					try
					{
						if (carNumber.equals(obj.carNumber[i]))
						{
							JOptionPane.showMessageDialog(null, "이미 입차되어 있는 차량입니다.");
							return;
						}
					}
					catch (java.lang.ArrayIndexOutOfBoundsException e)
					{
					}
					catch (java.lang.NullPointerException e1)
					{
					}
				}
			}
			parkTime = "" + year + "/" + month + "/" + day + "/" + hour + "/" + min;
			printParkTime = ConvertTime(parkTime);
			new writeFile(jariNumber, carSelect, carNumber, parkTime, "-", "-","-");
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
	public void itemStateChanged(ItemEvent ie)
	{
		if (ie.getSource() == small)
		{
			carSelect = "경차주차";
		}
		else if (ie.getSource() == normal)
		{
			carSelect = "일반주차";
		}
		else if (ie.getSource() == slow_speed)
		{
			carSelect = "완속충전";
		}
		else if (ie.getSource() == fast_speed)
		{
			carSelect = "고속충전";
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
		CheckboxGroup cbg = new CheckboxGroup();
		small = new Checkbox("경차주차", cbg, false);
		normal = new Checkbox("일반주차", cbg, false);
		slow_speed = new Checkbox("완속충전", cbg, false);
		fast_speed = new Checkbox("고속충전", cbg, false);
		tf1 = new JTextField();
		tf2 = new JTextField(jariNumber);
		confirm = new JButton("확인");
		cancel = new JButton("취소");
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		layout(new Label("주차,충전유무"), 0, 0, 1, 2);
		layout(small, 1, 1, 1, 1);
		layout(normal, 2, 1, 1, 1);
		layout(slow_speed, 3, 1, 1, 1);
		layout(fast_speed, 4, 1, 1, 1);
		small.addItemListener(this);
		normal.addItemListener(this);
		slow_speed.addItemListener(this);
		fast_speed.addItemListener(this);
		layout(new Label("차량번호"), 0, 2, 1, 1);
		layout(tf1, 1, 2, 4, 1);
		layout(new Label("자리번호"), 0, 3, 1, 1);
		layout(tf2, 1, 3, 4, 1);
		layout(new Label(""), 1, 4, 1, 1);
		layout(new Label("입차하시겠습니까?"), 1, 5, 4, 1);
		layout(confirm, 1, 6, 2, 1);
		layout(cancel, 3, 6, 2, 1);
		tf2.setEditable(false);

	}
}