package main;

public class test {

	public static void main(String args[]) {
		ImageReader imageReader = new ImageReader();
		Printer audioPrinter = new Printer();
		double[][] imageArr = imageReader.readImageToArr("chaos.png");
		audioPrinter.printAudio(imageArr);
	}
}