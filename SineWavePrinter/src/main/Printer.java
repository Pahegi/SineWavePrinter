package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
 * This class will create itself an instance of StdAudio
 * to print an variable sized Array out on the frequency spectrum
 */


public class Printer {

	private double startfrequency;
	private double endfrequency;
	private double pixeltime;
	private double amplitude;
	private double minPixWidth;
	
	private double frequencySpectrum;
	
	public Printer(double startfrequency, double endfrequency, double pixeltime, double amplitude, double minPixWidth) {
		this.startfrequency = startfrequency;
		this.endfrequency = endfrequency;
		this.pixeltime = pixeltime;
		this.amplitude = amplitude;
		this.minPixWidth = minPixWidth;
		
		frequencySpectrum = endfrequency - startfrequency;
	}
	
	private void checkFrequencyWidth(int imgwidth) {
		int possiblePixels = (int) Math.floor(frequencySpectrum / minPixWidth);
		if (imgwidth > possiblePixels) {
			System.err.println("Picture to big / not enough frequency width");
			System.exit(1);
		}
	}
	
	private double[] generateFreqSpectrum(int imgwidth) {
		double[] pixelfrequencies = new double[imgwidth];
		double pixelWidth = Math.floor(frequencySpectrum / (double) imgwidth);
		for (int i = 0; i < imgwidth; i++) {
			pixelfrequencies[i] = startfrequency + i*pixelWidth;
		}
		return pixelfrequencies;
	}
	
	public void playAudioAdvanced(double[][] image) {
		
		//get size of picture
		int width = image.length;
		int height = image[0].length;

		System.out.println("width " + width);
		System.out.println("height " + height);
				
		checkFrequencyWidth(width);
		
		double[] pixelfrequencies = generateFreqSpectrum(width);
		
		StdAudio audio = new StdAudio();
		
		double[] audioBuffer = audio.multiplePlayCols(pixelfrequencies, image, pixeltime, amplitude);
		
		System.out.println("Finished Buffer calculation");
		
		audio.play(audioBuffer);
		
	}
	
	public void saveAudioAdvanced(double[][] image, String path) {
		//get size of picture
		int width = image.length;
		int height = image[0].length;

		System.out.println("width " + width);
		System.out.println("height " + height);
						
		checkFrequencyWidth(width);
				
		double[] pixelfrequencies = generateFreqSpectrum(width);
				
		StdAudio audio = new StdAudio();
				
		double[] audioBuffer = audio.multiplePlayCols(pixelfrequencies, image, pixeltime, amplitude);
				
		System.out.println("Finished Buffer calculation");
		
		this.saveWave(audioBuffer, path, audio.getSampleRate());
	}
	
	public void printAudio(double[][] image) {
		
		//get size of picture
		int sizex = image.length;
		int sizey = image[0].length;

		System.out.println("sizex " + sizex);
		System.out.println("sizey " + sizey);
		
		checkFrequencyWidth(sizex);
		
		//save frequencies for x-spectrum in array
		double[] pixelfrequencies = generateFreqSpectrum(sizey);
		
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
	
	public void saveWave(double[] audioBuffer, String pathname, int sampleRate) {
    	long numFrames = audioBuffer.length;
    	try {
			WavFile wavFile = WavFile.newWavFile(new File(pathname), 1, numFrames, 16, sampleRate);
			
			wavFile.writeFrames(audioBuffer, audioBuffer.length-1);
			wavFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WavFileException e) {
			e.printStackTrace();
		};
    }
	
}
