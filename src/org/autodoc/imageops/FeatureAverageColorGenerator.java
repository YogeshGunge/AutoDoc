package org.autodoc.imageops;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.imageio.ImageIO;

import org.autodoc.AutoDoc;
import org.autodoc.model.CsvRecord;
import org.autodoc.model.DataSet;
import org.autodoc.model.Feature;
import org.autodoc.model.FeatureSet;
import org.autodoc.resources.Locations;
import org.autodoc.resources.Settings;

/**
*
* @author yogesh.gunge
*/
public class FeatureAverageColorGenerator implements ImageFeaturesGenerator {
	private float outlierFact;
	private String outputFormat;
	private String outputName;

	@Override
	public ArrayList<String> getParameterList() {
		// TODO Auto-generated method stub
		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add("Output Name:");
		paramList.add("Output Format (RGB/HSV):");
		paramList.add("Outlier Elimination Factor (0.01 to 0.99):");
		return paramList;
	}

	@Override
	public void setParameters(String[] params) {
		// TODO Auto-generated method stub
		outputName = params[0];
		outputFormat = params[1];
		outlierFact = Float.parseFloat(params[2]);
	}

	@Override
	public FeatureSet generateFeatures(DataSet[] inputs) {
		// TODO Auto-generated method stub
		FeatureSet fs = new FeatureSet(outputName);
		for (DataSet ds : inputs) {
			ArrayList<CsvRecord> csvRows = ds.getTrainLabels();
			Feature col1 = new Feature();
			Feature col2 = new Feature();
			Feature col3 = new Feature();
			if(outputFormat.equals("HSV")) {
				col1.setFeatureName("Hue");
				col2.setFeatureName("Saturation");
				col3.setFeatureName("Value");
			} else {
				col1.setFeatureName("Red");
				col2.setFeatureName("Green");
				col3.setFeatureName("Blue");
			}
			fs.addFeature(col1);
			fs.addFeature(col2);
			fs.addFeature(col3);
			ListIterator<CsvRecord> csvIterator = csvRows.listIterator();
			while (csvIterator.hasNext()) {
				CsvRecord csvRow = (CsvRecord) csvIterator.next();
				String imgFile = csvRow.getImageFileName();
				String imgFilePath = Locations.dataPath + "/" + AutoDoc.mode + "/"
						+ AutoDoc.collection + "/" + ds.getName() + "/"
						+ imgFile + Settings.imageExt;
				try {
					Color c = getAverageColor(imgFilePath);
					int r = c.getRed();
					int g = c.getBlue();
					int b = c.getGreen();
					if(outputFormat.equals("HSV")) {
						float hsv[] = new float[3];
						Color.RGBtoHSB(r, g, b, hsv);
						col1.addValue(""+hsv[0]);
						col2.addValue(""+hsv[1]);
						col3.addValue(""+hsv[2]);
					} else {
						col1.addValue(""+r);
						col2.addValue(""+g);
						col3.addValue(""+b);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		}
		return fs;
	}

	/*
	 * This feature extraction generates features by:
	 * step 1: Find average leaving 1/6th margin around the border of image by RGB
	 * step 2: Consider only specific pixels within +/-outlier range of HSV of above value
	 * step 3: Find average for whole image
	 * @see org.autodoc.imageops.ImageFeaturesGenerator#generateFeatures(org.autodoc.model.DataSet[])
	 */
	private Color getAverageColor(String imgFilePath) throws IOException {
		//System.out.println("imgFilePath: "+imgFilePath);
		File imgFile = new File(imgFilePath);
		BufferedImage bi = ImageIO.read(imgFile);
		int W = bi.getWidth();
		int H = bi.getHeight();
		int totalpixels = W * H;
		int[] pixels = new int[totalpixels];
		bi.getRGB(0, 0, W, H, pixels, 0, W);
		int count = 0;
		long ar = 0, ag = 0, ab = 0;
		for (int i = W / 6; i < 5 * W / 6; i++) {
			for (int j = H / 6; j < 5 * H / 6; j++) {
				int position = j * W + i;
				int p = pixels[position];
				int r = (p >> 16) & 0xFF;
				int g = (p >> 8) & 0xFF;
				int b = p & 0xFF;
				count++;
				ar += r;
				ag += g;
				ab += b;
			}
		}
		if (count == 0) {
			count = 1;
		}
		ar /= count;
		ag /= count;
		ab /= count;

		float[] hsv = new float[3];
		Color.RGBtoHSB((int) ar, (int) ag, (int) ab, hsv);
		double minhue = hsv[0] - outlierFact;
		double maxhue = hsv[0] + outlierFact;
		double minsat = hsv[1] - outlierFact;
		double maxsat = hsv[1] + outlierFact;
		double minval = hsv[2] - outlierFact;
		double maxval = hsv[2] + outlierFact;

		long nr = 0, ng = 0, nb = 0;
		count = 0;
		for (int i = 0; i < totalpixels; i++) {
			int p = pixels[i];
			int r = (p >> 16) & 0xFF;
			int g = (p >> 8) & 0xFF;
			int b = p & 0xFF;
			Color.RGBtoHSB(r, g, b, hsv);
			if ((hsv[0] >= minhue && hsv[0] <= maxhue 
					&& hsv[1] >= minsat && hsv[1] <= maxsat
					&& hsv[2] >= minval && hsv[2] <= maxval
					)
				|| (hsv[0] >= minhue / 2 && hsv[0] <= maxhue / 2
						&& hsv[1] >= minsat * 3 && hsv[1] <= maxsat * 3
						&& hsv[2] >= minval  * 3 && hsv[2] <= maxval * 3
						)
				|| (hsv[0] >= minhue * 3 && hsv[0] <= maxhue * 3
						&& hsv[1] >= minsat / 2 && hsv[1] <= maxsat / 2
						&& hsv[2] >= minval  * 3 && hsv[2] <= maxval * 3
						)
				|| (hsv[0] >= minhue * 3 && hsv[0] <= maxhue * 3
						&& hsv[1] >= minsat * 3 && hsv[1] <= maxsat * 3
						&& hsv[2] >= minval  / 2 && hsv[2] <= maxval / 2
						)
				) {
				nr += r;
				ng += g;
				nb += b;
				count++;
			} else {
				r = g = b = 0;
			}
			r = Math.min(255, Math.max(0, r));
			g = Math.min(255, Math.max(0, g));
			b = Math.min(255, Math.max(0, b));
			p = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
			pixels[i] = p;
		}
		if (count == 0) {
			count = 1;
		}
		nr /= count;
		ng /= count;
		nb /= count;
		/*Color.RGBtoHSB((int) nr, (int) ng, (int) nb, hsv);
		System.out.println(nr + ":" + ng + ":" + nb + ":" + hsv[0] + ":" + hsv[1] + ":" + hsv[2]);
		BufferedImage out = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
		out.setRGB(0, 0, W, H, pixels, 0, W);
		File outFile = new File("./out.png");
		ImageIO.write(out,"png" , outFile);*/
		int avgRed = (int)Math.min(255, Math.max(0, nr));
		int avgGreen = (int)Math.min(255, Math.max(0, ng));
		int avgBlue = (int)Math.min(255, Math.max(0, nb));
		return new Color(avgRed, avgGreen, avgBlue);
	}

	public static void main(String cp[]) {
		FeatureAverageColorGenerator fag = new FeatureAverageColorGenerator();
		String params[] = { "avgRGB", "RGB", "0.08" };
		fag.setParameters(params);
		Color avgColor;
		try {
			avgColor = fag
					.getAverageColor("./data/train/nails/nails_front/1.3.png");
			System.out.println("RGB: (" + avgColor.getRed() + ","
					+ avgColor.getGreen() + "," + avgColor.getBlue() + ")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
