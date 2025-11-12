import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;


public class config extends JFrame implements ActionListener
{
	GridBagConstraints c;
	CheckboxGroup cbg;
	Label label1;
	Label label2;
	Label label3;
	readFile obj = new readFile();
	JPanel pan1;
	JPanel pan2;
	JPanel pan3;
	JTextField tf0;
	JTextField tf1;
	JTextField tf2;
	JTextField tf3;
	JTextField tf4;
	JTextField tf5;
	JTextField tf6;
	JTextField tf7;
	JTextField tf8;
	JTextField tf9;
	JTextField tf10;
	JTextField tf11;
	JTextField tf12;
	JTextField tf13;
	JTextField tf14;
	JTextField tf15;
	JTextField tf16;
	JTextField tf17;
	JTextField tf18;

	Checkbox small;
	Checkbox normal;
	Checkbox slow_speed;
	Checkbox fast_speed;
	JButton confirm;
	JButton cancel;

	public config()
	{
		setTitle("요금 설정"); 
		setSize(500, 180);
		setLocation(350, 250);
		pan1(); 
		setVisible(true); 
		setResizable(true);

	}

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == confirm)
		{ //temp에 저장
			String temp = tf0.getText() + "," + tf1.getText() + ","+ tf2.getText() + "," +
			tf3.getText() + "," + tf4.getText() + "," + tf5.getText() + "," + tf6.getText() + ","+
			tf7.getText() + "," + tf8.getText() + "," + tf9.getText()+ "," + tf10.getText() + "," + tf11.getText();
			try
			{
				FileWriter w = new FileWriter("config.cfg");
				w.write(temp);
				w.close();
			} catch (IOException e)
			{
			}
			dispose();
		} else if (ae.getSource() == cancel)
		{
			dispose();
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
		tf0 = new JTextField("" + obj.c[0]);
		tf1 = new JTextField("" + obj.c[1]);
		tf2 = new JTextField("" + obj.c[2]);
		tf3 = new JTextField("" + obj.c[3]);
		tf4 = new JTextField("" + obj.c[4]);
		tf5 = new JTextField("" + obj.c[5]);
		tf6 = new JTextField("" + obj.c[6]);
		tf7 = new JTextField("" + obj.c[7]);
		tf8 = new JTextField("" + obj.c[8]);
		tf9 = new JTextField("" + obj.c[9]);
		tf10 = new JTextField("" + obj.c[10]);
		tf11 = new JTextField("" + obj.c[11]);
		confirm = new JButton("확인");
		cancel = new JButton("취소");
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		layout(new Label("경차주차"), 1, 0, 2, 1);
		layout(new Label("일반주차"), 3, 0, 2, 1);
		layout(new Label("완속충전"), 5, 0, 2, 1);
		layout(new Label("급속충전"), 7, 0, 2, 1);
		layout(new Label("1시간 기본요금"), 0, 1, 1, 1);
		layout(tf0, 1, 1, 2, 1);
		layout(tf3, 3, 1, 2, 1);
		layout(tf6, 5, 1, 2, 1);
		layout(tf9, 7, 1, 2, 1);
		layout(new Label("이후 30분당 주차요금"), 0, 2, 1, 1);
		layout(tf1, 1, 2, 2, 1);
		layout(tf4, 3, 2, 2, 1);
		layout(tf7, 5, 2, 2, 1);
		layout(tf10, 7, 2, 2, 1);
		layout(new Label("최대요금"), 0, 3, 1, 1);
		layout(tf2, 1, 3, 2, 1);
		layout(tf5, 3, 3, 2, 1);
		layout(tf8, 5, 3, 2, 1);
		layout(tf11, 7, 3, 2, 1);
		layout(confirm, 1, 6, 2, 1);
		layout(cancel, 3, 6, 2, 1);

	}
}