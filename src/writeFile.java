import java.io.*;

public class writeFile
{

	public String carNumber;
	public String carSelect;
	public String charge;
	public String inTime;
	public String jariNumber;
	readFile obj = new readFile();
	public String parkTime;
	public String unparkTime;

	public writeFile(String jariNumber, String carSelect, String carNumber,String parkTime, String unparkTime, String inTime, String charge)
	{
		this.carSelect = carSelect;
		this.jariNumber = jariNumber;
		this.carNumber = carNumber;
		this.parkTime = parkTime;
		this.unparkTime = unparkTime;
		this.inTime = inTime;
		this.charge = charge;
		write();
	}

	public void write()
	{
		try
		{
			if (charge == "-")
			{
				BufferedWriter w = new BufferedWriter(new FileWriter("data.csv", true));
				w.write(jariNumber);
				w.write(",");
				w.write(carSelect);
				w.write(",");
				w.write(carNumber);
				w.write(",");
				w.write(parkTime);
				w.write(",");
				w.write(unparkTime);
				w.write(",");
				w.write(inTime);
				w.write(",");
				w.write(charge);
				w.newLine();
				w.close();
			}
			else
			{
				FileWriter w = new FileWriter("data.csv");
				BufferedWriter w1 = new BufferedWriter(new FileWriter("log.csv", true));
				int temp1, temp2;
				temp1 = Integer.parseInt(jariNumber);
				int temp = -1;
				for (int i = 0; i < obj.length; i++)
				{
					temp2 = Integer.parseInt(obj.jariNumber[i]);
					if (temp1 == temp2)
					{
						temp = i;
						w1.write(jariNumber);
						w1.write(",");
						w1.write(carSelect);
						w1.write(",");
						w1.write(carNumber);
						w1.write(",");
						w1.write(parkTime);
						w1.write(",");
						w1.write(unparkTime);
						w1.write(",");
						w1.write(inTime);
						w1.write(",");
						w1.write(charge);
						w1.newLine();
						break;
					}
				}
				for (int i = 0; i < obj.length; i++)
				{
					if (i == temp)
						continue;
					w.write(obj.jariNumber[i]);
					w.write(",");
					w.write(obj.carSelect[i]);
					w.write(",");
					w.write(obj.carNumber[i]);
					w.write(",");
					w.write(obj.parkTime[i]);
					w.write(",");
					w.write(obj.unparkTime[i]);
					w.write(",");
					w.write(obj.inTime[i]);
					w.write(",");
					w.write(obj.charge[i]);
					w.write("\r\n");
				}
				w.close();
				w1.close();
			}

		}
		catch (IOException e)
		{
		}
	}
}
