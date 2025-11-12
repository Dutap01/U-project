public class project
{

	public static void main(String[] args)
	{
		mainWindow cl = new mainWindow();
		Thread th = new Thread(cl);
		th.start();
	}
}