package U_project;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.text.SimpleDateFormat;

class mainWindow extends JFrame implements Runnable, ActionListener
{
	JTextField blank;
	public JButton Btn[] = new JButton[150];
	JTextArea condition;
	JButton configBtn;
	String currentDate = "";
	String currentTime = "";
	JButton history;
	int jariNumber;
	JButton notUseBtn[] = new JButton[100];
	readFile obj = new readFile();
	JPanel pan1;
	JPanel pan2;
	JPanel pan3;
    JButton manualBtn;

	public mainWindow()
	{
		setTitle("주차장 관리 프로그램");
		setSize(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pan1();
		pan2();
		pan3();
		gridInit();
		setVisible(true);
		setResizable(false);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == configBtn)
		{
			new config();
		}
		else if (e.getSource() == history)
		{
			new logWindow();
		}
		else if (e.getSource() == manualBtn)
		{
			new ManualParkWindow();
		}
		else 
		{
			String command = e.getActionCommand();
			String carNumber = command;
			int clickedJariNumber = -1;
			
			try {
				clickedJariNumber = Integer.parseInt(command);
				
				ANPRController anpr = new ANPRController(this);
				anpr.processParkSignal(clickedJariNumber); 
				
			} catch (NumberFormatException ex) {
				
				for(int i=1; i < Btn.length; i++){
				    if(Btn[i].getLabel().equals(carNumber)){
				        clickedJariNumber = i;
				        break;
				    }
				}
				
				if(clickedJariNumber != -1){
				    ANPRController anpr = new ANPRController(this);
				    anpr.processUnparkSignal(clickedJariNumber, carNumber);
				}
			}
		}
	}
    
	public void gridInit()
	{
		for (int i = 1; i <= 15; i++)
		{
			pan2.add(Btn[i] = new JButton(i + ""));
			Btn[i].addActionListener(this);
		}
		for (int i = 1; i <= 15; i++)
		{
			pan2.add(notUseBtn[i] = new JButton(""));
			notUseBtn[i].setEnabled(false);
		}
		for (int i = 16; i <= 45; i++)
		{
			pan2.add(Btn[i] = new JButton(i + ""));
			Btn[i].addActionListener(this);
		}
		for (int i = 16; i <= 30; i++)
		{
			pan2.add(notUseBtn[i] = new JButton(""));
			notUseBtn[i].setEnabled(false);
		}
		for (int i = 46; i <= 75; i++)
		{
			pan2.add(Btn[i] = new JButton(i + ""));
			Btn[i].addActionListener(this);
		}
		for (int i = 31; i <= 45; i++)
		{
			pan2.add(notUseBtn[i] = new JButton(""));
			notUseBtn[i].setEnabled(false);
		}
		for (int i = 76; i <= 90; i++)
		{
			pan2.add(Btn[i] = new JButton(i + ""));
			Btn[i].addActionListener(this);
		}
		int i = 0;
		while (i < obj.length)
		{
			int temp = Integer.parseInt(obj.jariNumber[i]); 
			Btn[temp].setLabel(obj.carNumber[i]);
			Btn[temp].setForeground(new Color(255, 0, 0));
			i++;
		}
	}
    
	public void paint(Graphics g)
	{
        super.paint(g); 
		g.setColor(Color.black);
		g.fillRect(0, 0, 1500, 100);
		g.setColor(Color.white);
		Font f1 = new Font("NanumGothicOTF", Font.BOLD, 24);
		g.setFont(f1);
		g.drawString(currentDate, 400, 50);
		g.drawString(currentTime, 410, 90);

	}

	public void pan1()
	{
		pan1 = new JPanel();
		pan1.add(blank = new JTextField());
		blank.setPreferredSize(new Dimension(900, 70));
		blank.setEnabled(false);
		add(pan1, "North");
	}
	public void pan2()
	{
		pan2 = new JPanel();
		GridLayout layout = new GridLayout(9, 1);
		pan2.setLayout(layout);
		add(pan2);
	}

	public void pan3()
	{
		pan3 = new JPanel();
		pan3.add(condition = new JTextArea());
		condition.setPreferredSize(new Dimension(700, 200));
		condition.setEditable(false);
		pan3.add(history = new JButton("내역보기"));
		pan3.add(configBtn = new JButton("요금설정"));
        pan3.add(manualBtn = new JButton("수동 처리"));
        manualBtn.addActionListener(this);
		history.addActionListener(this);
		configBtn.addActionListener(this);
		add(pan3, "South");
	}

	public void run() {
		while (true)
		{
			Date d = new Date();
			SimpleDateFormat date = new SimpleDateFormat("yyyy년 MM월 dd일");
			SimpleDateFormat time = new SimpleDateFormat("hh시간 mm분 ss초");
			currentDate = date.format(d);
			currentTime = time.format(d);
			try
			{
				Thread.sleep(100);
				repaint();
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}