package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * This class reads an image and converts it into an double array
 * compatible with the AudioPrinter
 */

public class ImageReader {
	
	public double[][] readImageToArr(String path) {
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(path));
		} catch (IOException e) {
		}
		double[][] imageArr = new double[img.getHeight()][img.getWidth()];
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				int rgb = img.getRGB(i, j);
				Color c = new Color(rgb);
				imageArr[j][i] = (double)(c.getBlue() + c.getGreen() + c.getBlue()) / (double)3 / (double)255;
			}
		}
		return imageArr;
	}

}
