package U_project;

public class project
{
	public static void main(String[] args)
	{
		mainWindow cl = new mainWindow();
		Thread th = new Thread(cl);
		th.start();
		
		ANPRController anpr = new ANPRController(cl);
		Thread anprTh = new Thread(anpr);
		anprTh.start();
	}
}