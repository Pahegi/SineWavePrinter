package main;
import main.StdAudio;

public class test {

	public static void main(String args[]) {
		StdAudio audio = new StdAudio();
		final double[] hzs = {400, 500, 600};
		for (int i = 0; i < 20; i++) {
			final double[] sound = audio.multipleNotes(hzs, 0.1, 1);
			for (int j = 0; j < hzs.length; j++) {
				hzs[j] = hzs[j]+100;
			}
			audio.play(sound);
		}
	}
	
}
