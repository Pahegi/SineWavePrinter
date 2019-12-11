package main;

public class test {

	public static void main(String args[]) {
		ImageReader imageReader = new ImageReader();
		Printer audioPrinter = new Printer(19000, 21000, 0.5, 1, 10);
		double[][] imageArr = imageReader.readImageToArr("chaos.png");
		audioPrinter.printAudio(imageArr);
	}
}