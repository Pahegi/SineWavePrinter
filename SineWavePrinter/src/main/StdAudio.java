package main;

import java.io.File;
import java.io.IOException;

/*************************************************************************
 *  Compilation:  javac this.java
 *  Execution:    java StdAudio
 *  
 *  Simple library for reading, writing, and manipulating .wav files.

 *
 *  Limitations
 *  -----------
 *    - Does not seem to work properly when reading .wav files from a .jar file.
 *    - Assumes the audio is monaural, with sampling rate of 44,100.
 *
 *************************************************************************/

import javax.sound.sampled.*;

/**
 * <i>Standard audio</i>. This class provides a basic capability for creating,
 * reading, and saving audio.
 * <p>
 * The audio format uses a sampling rate of 44,100 (CD quality audio), 16-bit,
 * monaural.
 * 
 * <p>
 * For additional documentation, see <a
 * href="http://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 * <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 * Robert Sedgewick and Kevin Wayne.
 */
public final class StdAudio {

    /**
     * The sample rate - 44,100 Hz for CD quality audio.
     */
    public final int SAMPLE_RATE = 44100;

    private final int BYTES_PER_SAMPLE = 2; // 16-bit audio
    private final int BITS_PER_SAMPLE = 16; // 16-bit audio
    private final double MAX_16_BIT = Short.MAX_VALUE; // 32,767
    private final int SAMPLE_BUFFER_SIZE = 4096;

    private SourceDataLine line; // to play the sound
    private byte[] buffer; // our internal buffer
    private int bufferSize = 0; // number of samples currently in internal
                                // buffer

    // initializer
    {
        init();
    }

    // open up an audio stream
    private void init() {
        try {
            // 44,100 samples per second, 16-bit audio, mono, signed PCM, little
            // Endian
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE,
                    BITS_PER_SAMPLE, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);

            // the internal buffer is a fraction of the actual buffer size, this
            // choice is arbitrary
            // it gets divided because we can't expect the buffered data to line
            // up exactly with when
            // the sound card decides to push out its samples.
            buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE / 3];
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // no sound gets made before this call
        line.start();
    }

    /**
     * Close standard audio.
     */
    public void close() {
        line.drain();
        line.stop();
    }

    /**
     * Write one sample (between -1.0 and +1.0) to standard audio. If the sample
     * is outside the range, it will be clipped.
     */
    public void play(double in) {

        // clip if outside [-1, +1]
        if (in < -1.0)
            in = -1.0;
        if (in > +1.0)
            in = +1.0;

        // convert to bytes
        short s = (short) (MAX_16_BIT * in);
        buffer[bufferSize++] = (byte) s;
        buffer[bufferSize++] = (byte) (s >> 8); // little Endian

        // send to sound card if buffer is full
        if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
        }
    }

    /**
     * Write an array of samples (between -1.0 and +1.0) to standard audio. If a
     * sample is outside the range, it will be clipped.
     */
    public void play(double[] input) {
        for (int i = 0; i < input.length; i++) {
            play(input[i]);
        }
    }

    /**
     * Create a note (sine wave) of the given frequency (Hz), for the given
     * duration (seconds) scaled to the given volume (amplitude).
     */
    public double[] note(double hz, double duration, double amplitude) {
        int N = (int) (this.SAMPLE_RATE * duration);
        double[] a = new double[N + 1];
        for (int i = 0; i <= N; i++)
            a[i] = amplitude * Math.sin(2 * Math.PI * i * hz / this.SAMPLE_RATE);
        return a;
    }
    
    public double[] multipleNotes(double[] hzs, double duration, double amplitude) {
        amplitude = amplitude / hzs.length;
        int N = (int) (SAMPLE_RATE * duration);
        double[] a = new double[N + 1];
        for (int i = 0; i <= N; i++) {
            a[i] = 0;
            for (int j = 0; j < hzs.length; j++)
                a[i] += amplitude * Math.sin(2 * Math.PI * i * hzs[j] / SAMPLE_RATE);
        }
        return a;
    }
    
    public void multiplePlay(double[] hzs, double duration, double amplitude) {
        amplitude = amplitude / hzs.length;
        int N = (int) (SAMPLE_RATE * duration);
        double sum;
        for (int i = 0; i <= N; i++) {
            sum = 0;
            for (int j = 0; j < hzs.length; j++)
                sum += amplitude * Math.sin(2 * Math.PI * i * hzs[j] / SAMPLE_RATE);
            this.play(sum);
        }
    }
    
    /**
     * 
     * @param hzs : array of frequencies to be played
     * @param opacitys : twodimensional array of opacitys of frequencies
     * @param pixelduration : duration of single opacity-rows
     * @param amplitude : general amplitude
     * @return Array of Audiosamples
     */
    public double[] multiplePlayCols(double[] hzs, double[][] opacitys, double pixelduration, double amplitude) {
        System.out.println("Opacity x: " + opacitys.length + " Opacity y: " + opacitys[0].length);
        System.out.println("SAMPLE_RATE: " + SAMPLE_RATE + " hzs.length: " + hzs.length);
    	int N = (int) (SAMPLE_RATE * pixelduration * opacitys.length);
    	System.out.println("N: " + N);
    	amplitude = amplitude / hzs.length;
        double[] a = new double[N + 1];
        double samplesum = 0;
        int row = 0;
        for (int i = 0; i <= N; i++) {
            samplesum = 0;
            for (int j = 0; j < hzs.length; j++) {
            	samplesum += amplitude * opacitys[row][j] * Math.sin((2 * Math.PI * i * hzs[j] / SAMPLE_RATE) % (2 * Math.PI));
            }
            a[i] = samplesum;
            if ((i % (pixelduration * SAMPLE_RATE)) == 0 && i != 0) {
            	row++;
            	System.out.println("Calculated row " + row + " of " + hzs.length);
            }
        }
        return a;
    }
    
    public int getSampleRate() {
    	return SAMPLE_RATE;
    }

}