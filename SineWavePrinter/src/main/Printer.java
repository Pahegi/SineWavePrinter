package main;

import java.util.ArrayList;

/*
 * This class will create itself an instance of StdAudio
 * to print an variable sized Array out on the frequency spectrum
 */


public class Printer {

	static final double startfrequency = 19000;
	static final double endfrequency = 21000;
	static final double pixeltime = 0.8;
	static final double amplitude = 1;
	static final double minPixWidth = 10;
	
	public void printAudio(double[][] image) {
		
		//get size of picture
		int sizex = image.length;
		int sizey = image[0].length;

		System.out.println("sizex " + sizex);
		System.out.println("sizey " + sizey);
		
		
		//check, if enough pixels are available in frequency spectrum
		double frequencySpectrum = endfrequency - startfrequency;
		int possiblePixels = (int) Math.floor(frequencySpectrum / minPixWidth);
		if (sizex > possiblePixels) {
			System.err.println("Picture to big / not enough frequency width");
			return;
		}
		
		//save frequencies for x-spectrum in array
		double[] pixelfrequencies = new double[sizey];
		double pixelWidth = Math.floor(frequencySpectrum / (double) sizey);
		for (int i = 0; i < sizey; i++) {
			pixelfrequencies[i] = startfrequency + i*pixelWidth;
		}
		
		//save all frequencies in Array of ArrayLists of doubles
		StdAudio audio = new StdAudio();
		ArrayList<double[]> freqArrays = new ArrayList<double[]>();
		for (int i = 0; i < sizex; i++) {
			ArrayList<Double> tmpRow = new ArrayList<Double>();
			for (int j = 0; j < sizey; j++) {
				if (image[i][j] > 0.5) tmpRow.add(pixelfrequencies[j]);
			}
			double[] tmpRowArr = new double[tmpRow.size()];
			for (int j = 0; j < tmpRowArr.length; j++) {
				tmpRowArr[j] = tmpRow.remove(0);
			}
			freqArrays.add(tmpRowArr);
			
		}
		
		//Play sounds
		for (int i = 0; i < freqArrays.size(); i++) {
			final double[] sound = audio.multipleNotes(freqArrays.get(i), pixeltime, amplitude);
			audio.play(sound);
		}
		
		
	}
}
