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
public class FeatureBiClusterColorGenerator implements ImageFeaturesGenerator {
	private float outlierFact;
	private String outputFormat;
	private String outputName;

	@Override
	public ArrayList<String> getParameterList() {
		// TODO Auto-generated method stub
		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add("Output Name:");
		paramList.add("Output Format (RGB/CMY/HSV):");
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

	/*
	 * This feature extraction generates features by 
	 * @see org.autodoc.imageops.ImageFeaturesGenerator#generateFeatures(org.autodoc.model.DataSet[])
	 */
	@Override
	public FeatureSet generateFeatures(DataSet[] inputs) {
		// TODO Auto-generated method stub
		FeatureSet fs = new FeatureSet(outputName);
		for (DataSet ds : inputs) {
			ArrayList<CsvRecord> csvRows = ds.getTrainLabels();
			Feature col1 = new Feature();
			Feature col2 = new Feature();
			Feature col3 = new Feature();
			Feature col4 = new Feature();
			Feature col5 = new Feature();
			Feature col6 = new Feature();
			
			if(outputFormat.equals("HSV")) {
				col1.setFeatureName("Hue1");
				col2.setFeatureName("Saturation1");
				col3.setFeatureName("Value1");
				col4.setFeatureName("Hue2");
				col5.setFeatureName("Saturation3");
				col6.setFeatureName("Value2");
			} else if(outputFormat.equals("CMY")) {
				col1.setFeatureName("Cyan1");
				col2.setFeatureName("Magenta1");
				col3.setFeatureName("Yellow1");
				col4.setFeatureName("Cyan2");
				col5.setFeatureName("Magenta2");
				col6.setFeatureName("Yellow2");
			} {
				col1.setFeatureName("Red1");
				col2.setFeatureName("Green1");
				col3.setFeatureName("Blue1");
				col4.setFeatureName("Red2");
				col5.setFeatureName("Green2");
				col6.setFeatureName("Blue2");
			}
			fs.addFeature(col1);
			fs.addFeature(col2);
			fs.addFeature(col3);
			fs.addFeature(col4);
			fs.addFeature(col5);
			fs.addFeature(col6);
			
			ListIterator<CsvRecord> csvIterator = csvRows.listIterator();
			while (csvIterator.hasNext()) {
				CsvRecord csvRow = (CsvRecord) csvIterator.next();
				String imgFile = csvRow.getImageFileName();
				String imgFilePath = Locations.dataPath + "/" + AutoDoc.mode + "/"
						+ AutoDoc.collection + "/" + ds.getName() + "/"
						+ imgFile + Settings.imageExt;
				try {
					Color cArr[] = getBiClusterColor(imgFilePath);
					Color c1 = cArr[0];
					Color c2 = cArr[0];
					int r1 = c1.getRed();
					int g1 = c1.getBlue();
					int b1 = c1.getGreen();
					int r2 = c2.getRed();
					int g2 = c2.getBlue();
					int b2 = c2.getGreen();
					if(outputFormat.equals("HSV")) {
						float hsv1[] = new float[3];
						float hsv2[] = new float[3];
						hsv1 = Color.RGBtoHSB(r1, g1, b1, hsv1);
						hsv2 = Color.RGBtoHSB(r2, g2, b2, hsv2);
						col1.addValue(""+hsv1[0]);
						col2.addValue(""+hsv1[1]);
						col3.addValue(""+hsv1[2]);
						col4.addValue(""+hsv2[0]);
						col5.addValue(""+hsv2[1]);
						col6.addValue(""+hsv2[2]);
					} else if(outputFormat.equals("CMY")) {
						float cmy1[] = new float[3];
						float cmy2[] = new float[3];
						cmy1 = RGBtoCMY(r1, g1, b1, cmy1);
						cmy2 = RGBtoCMY(r2, g2, b2, cmy2);
						col1.addValue(""+cmy1[0]);
						col2.addValue(""+cmy1[1]);
						col3.addValue(""+cmy1[2]);
						col4.addValue(""+cmy2[0]);
						col5.addValue(""+cmy2[1]);
						col6.addValue(""+cmy2[2]);
					} else {
						col1.addValue(""+r1);
						col2.addValue(""+g1);
						col3.addValue(""+b1);
						col4.addValue(""+r2);
						col5.addValue(""+g2);
						col6.addValue(""+b2);
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
	 * step 1: Find average leaving 1/6th margin around the border of image by RGB and for upper half
	 * step 2: Find average leaving 1/6th margin around the border of image by RGB and for lower half
	 * step 3: Apply 2NN algorithm and get maximum occurrence 2 colors from the image
	 * @see org.autodoc.imageops.ImageFeaturesGenerator#generateFeatures(org.autodoc.model.DataSet[])
	 */
	private Color[] getBiClusterColor(String imgFilePath) throws IOException {
		//System.out.println("imgFilePath: "+imgFilePath);
		File imgFile = new File(imgFilePath);
		System.out.println(imgFilePath);
		BufferedImage bi = ImageIO.read(imgFile);
		int W = bi.getWidth();
		int H = bi.getHeight();
		int totalpixels = W * H;
		int[] pixels = new int[totalpixels];
		bi.getRGB(0, 0, W, H, pixels, 0, W);
		int count = 0;
		long ar1 = 0, ag1 = 0, ab1 = 0;
		for (int i = W / 6; i < 5 * W / 6; i++) {
			for (int j = H / 8; j < H / 2; j++) {
				int position = j * W + i;
				int p = pixels[position];
				int r = (p >> 16) & 0xFF;
				int g = (p >> 8) & 0xFF;
				int b = p & 0xFF;
				count++;
				ar1 += r;
				ag1 += g;
				ab1 += b;
			}
		}
		if (count == 0) {
			count = 1;
		}
		ar1 /= count;
		ag1 /= count;
		ab1 /= count;
		
		count = 0;
		long ar2 = 0, ag2 = 0, ab2 = 0;
		for (int i = W / 6; i < 5 * W / 6; i++) {
			for (int j = H / 2; j < 7 * H / 8; j++) {
				int position = j * W + i;
				int p = pixels[position];
				int r = (p >> 16) & 0xFF;
				int g = (p >> 8) & 0xFF;
				int b = p & 0xFF;
				count++;
				ar2 += r;
				ag2 += g;
				ab2 += b;
			}
		}
		if (count == 0) {
			count = 1;
		}
		ar2 /= count;
		ag2 /= count;
		ab2 /= count;

		float[] hsv1 = new float[3];
		Color.RGBtoHSB((int) ar1, (int) ag1, (int) ab1, hsv1);
		float[] hsv2 = new float[3];
		Color.RGBtoHSB((int) ar2, (int) ag2, (int) ab2, hsv2);
		
		double minhue1 = hsv1[0] - outlierFact;
		double maxhue1 = hsv1[0] + outlierFact;
		double minsat1 = hsv1[1] - outlierFact;
		double maxsat1 = hsv1[1] + outlierFact;
		double minval1 = hsv1[2] - outlierFact;
		double maxval1 = hsv1[2] + outlierFact;
		
		double minhue2 = hsv2[0] - outlierFact;
		double maxhue2 = hsv2[0] + outlierFact;
		double minsat2 = hsv2[1] - outlierFact;
		double maxsat2 = hsv2[1] + outlierFact;
		double minval2 = hsv2[2] - outlierFact;
		double maxval2 = hsv2[2] + outlierFact;
		
		long nr1 = 0, ng1 = 0, nb1 = 0;
		long nr2 = 0, ng2 = 0, nb2 = 0;
		int n1 = 0, n2 = 0;
		float[] hsv = new float[3];
		for (int i = 0; i < totalpixels; i++) {
			int p = pixels[i];
			int r = (p >> 16) & 0xFF;
			int g = (p >> 8) & 0xFF;
			int b = p & 0xFF;
			Color.RGBtoHSB(r, g, b, hsv);
			boolean cond1 = isWithinLimits(hsv, minhue1, maxhue1, minsat1, maxsat1, minval1, maxval1);
			boolean cond2 = isWithinLimits(hsv, minhue2, maxhue2, minsat2, maxsat2, minval2, maxval2);
			if (cond1 && cond2) {
				double dist1 = getColorDifference(hsv, hsv1);
				double dist2 = getColorDifference(hsv, hsv2);
				if(dist1 < dist2) {
					nr1 += r;
					ng1 += g;
					nb1 += b;
					n1++;
				} else {
					nr2 += r;
					ng2 += g;
					nb2 += b;
					n2++;
				}
			} else if (cond1){
				nr1 += r;
				ng1 += g;
				nb1 += b;
				n1++;
			} else if (cond2){
				nr2 += r;
				ng2 += g;
				nb2 += b;
				n2++;
			} else {
				r = g = b = 0;
			}
			r = Math.min(255, Math.max(0, r));
			g = Math.min(255, Math.max(0, g));
			b = Math.min(255, Math.max(0, b));
			p = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
			pixels[i] = p;
		}
		if (n1 == 0) {
			n1 = 1;
		}
		if (n2 == 0) {
			n2 = 1;
		}
		nr1 /= n1;
		ng1 /= n1;
		nb1 /= n1;
		nr2 /= n2;
		ng2 /= n2;
		nb2 /= n2;
		/*Color.RGBtoHSB((int) nr, (int) ng, (int) nb, hsv);
		System.out.println(nr + ":" + ng + ":" + nb + ":" + hsv[0] + ":" + hsv[1] + ":" + hsv[2]);
		BufferedImage out = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
		out.setRGB(0, 0, W, H, pixels, 0, W);
		File outFile = new File("./out.png");
		ImageIO.write(out,"png" , outFile);*/
		int red1 = (int)Math.min(255, Math.max(0, nr1));
		int green1 = (int)Math.min(255, Math.max(0, ng1));
		int blue1 = (int)Math.min(255, Math.max(0, nb1));
		int red2 = (int)Math.min(255, Math.max(0, nr2));
		int green2 = (int)Math.min(255, Math.max(0, ng2));
		int blue2 = (int)Math.min(255, Math.max(0, nb2));
		Color arr[] = new Color[2];
		arr[0] = new Color(red1, green1, blue1);
		arr[1] = new Color(red2, green2, blue2);
		return arr;
	}

	public static void main(String cp[]) {
		FeatureBiClusterColorGenerator fag = new FeatureBiClusterColorGenerator();
		String params[] = { "avgRGB", "RGB", "0.08" };
		fag.setParameters(params);
		Color avgColor[];
		try {
			avgColor = fag
					.getBiClusterColor("./data/train/nails/nails_front/1.3.png");
			System.out.println("RGB1: (" + avgColor[0].getRed() + ","
					+ avgColor[0].getGreen() + "," + avgColor[0].getBlue() + ")");
			System.out.println("RGB2: (" + avgColor[1].getRed() + ","
					+ avgColor[1].getGreen() + "," + avgColor[1].getBlue() + ")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isWithinLimits(float hsv[], double minhue, double maxhue, double minsat, double maxsat, double minval, double maxval) {
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
			return true;
		}
		return false;
	}
	
	private double getColorDifference(float hsv1[], float hsv2[]) {
		double hueDiff = Math.abs(hsv1[0] - hsv2[0]);
		double satDiff = Math.abs(hsv1[1] - hsv2[1]);
		double valDiff = Math.abs(hsv1[2] - hsv2[2]);
		return Math.sqrt(hueDiff*hueDiff+satDiff*satDiff+valDiff*valDiff);
	}
	
	private float[] RGBtoCMY(int r, int g, int b, float[] cmyvals) {
        if (cmyvals == null) {
        	cmyvals = new float[3];
        }
        // R' = R/255
        // G' = G/255
        // B' = B/255
        float rf = r/255;
        float gf = g/255;
        float bf = b/255;
        
        // K = 1-max(R', G', B')
        float k = 1 - Math.max(Math.max(rf, gf),bf);
        // The cyan color (C) is calculated from the red (R') and black (K) colors:
        // C = (1-R'-K) / (1-K)
        float c = (1 - rf - k)/(1 - k);
        // The magenta color (M) is calculated from the green (G') and black (K) colors:
        // M = (1-G'-K) / (1-K)
        float m = (1 - gf - k)/(1 - k);
        // The yellow color (Y) is calculated from the blue (B') and black (K) colors:
        // Y = (1-B'-K) / (1-K)
        float y = (1 - bf - k)/(1 - k);
        cmyvals[0] = c;
        cmyvals[1] = m;
        cmyvals[2] = y;
        return cmyvals;
    }

}
