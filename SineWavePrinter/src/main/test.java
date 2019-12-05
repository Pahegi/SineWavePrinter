package main;
import main.StdAudio;

public class test {

	public static void main(String args[]) {
		Printer audioPrinter = new Printer();
		double[][] picture =
		{
			{1,0,0,1,0,0,1,0},
			{1,0,0,1,0,0,1,0},
			{1,0,0,1,0,0,1,0},
			{1,1,1,1,0,0,1,0},
			{1,0,0,1,0,0,1,0},
			{1,0,0,1,0,0,1,0},
			{1,0,0,1,0,0,1,0},
			{1,0,0,1,0,0,1,0}
		};
		try {
			audioPrinter.printAudio(picture);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
