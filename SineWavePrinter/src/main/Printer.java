package main;

import java.util.ArrayList;

/*
 * This class will create itself an instance of StdAudio
 * to print an variable sized Array out on the frequency spectrum
 */


public class Printer {

	static final double startfrequency = 20000;
	static final double endfrequency = 20200;
	static final double pixeltime = 2;
	static final double amplitude = 1;
	static final double minPixWidth = 20;
	
	public void printAudio(double[][] image) throws Exception {
		
		//get size of picture
		int sizex = image.length;
		int sizey = image[0].length;

		//check, if enough pixels are available in frequency spectrum
		double frequencySpectrum = endfrequency - startfrequency;
		int possiblePixels = (int) Math.floor(frequencySpectrum / minPixWidth);
		if (sizex > possiblePixels) throw new Exception("Picture to big / not enough frequency width");
		
		//save frequencies for x-spectrum in array
		double[] pixelfrequencies = new double[sizex];
		double pixelWidth = Math.floor(frequencySpectrum / (double) sizex);
		for (int i = 0; i < sizex; i++) {
			pixelfrequencies[i] = startfrequency + i*pixelWidth;
		}
		
		//print out all x-rows
		StdAudio audio = new StdAudio();
		ArrayList<Double> tmpFreq = new ArrayList<Double>();
		for (int i = 0; i < sizey; i++) {
			tmpFreq.clear();
			for (int j = 0; j < sizex; j++) {
				if (image[i][j] > 0.5) tmpFreq.add(pixelfrequencies[j]);
			}
			double[] freqs = new double[tmpFreq.size()];
			for (int j = 0; j < freqs.length; j++) {
				freqs[j] = tmpFreq.remove(0);
			}
			final double[] sound = audio.multipleNotes(freqs, 2, 1);
			audio.play(sound);
		}
		
		
	}
}
