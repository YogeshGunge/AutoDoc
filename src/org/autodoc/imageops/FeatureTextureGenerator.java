package org.autodoc.imageops;

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
* Texture : Rippled, Cracked, Pitting, Fungal, Dark Lines, ...
* Texture : GLCM
* 		GLCM contrast (GC)
* 	,	GLCM homogeneity(GH)
* 	,	GLCM mean (GM)
* 	and	GLCM variance (GV)
* 
* A co-occurrence matrix C is defined over an n × m image I, parameterized by an offset (Δx,Δy), as: 
* 	Cij<dx,dy>(i, j) = SumOver(x = 1 to N) SumOver(y = 1 to M)
* 						1 if I(x,y) == i and I(x+dx,y+dy) == j
* 						0 otherwise
* 
* G(i,j) = four directional normalized symmetrical GLCM: 0, 45, 90, and 135 degrees
* G(i,j) - represents the element (i,j) of a normalized symmetrical GLCM,
* 			and N the number of grey levels
* 
* Gc = SumOver(i = 1 to N) SumOver(j = 1 to N) G(i,j) * (i - j)^2
* 
* Gh = SumOver(i = 1 to N) SumOver(j = 1 to N) G(i,j) / ( 1 + (i - j)^2 )
* 
* Gm = Mi = SumOver(i = 1 to N) SumOver(j = 1 to N) i * G(i,j)
* 
* Gv = SumOver(i = 1 to N) SumOver(j = 1 to N) G(i,j)* ( i - Mi ) ^ 2
* 
* @author yogesh.gunge
*/
public class FeatureTextureGenerator implements ImageFeaturesGenerator {
	private int offset;
	private int levels;
	private String angles;
	private String outputName;

	@Override
	public ArrayList<String> getParameterList() {
		// TODO Auto-generated method stub
		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add("Output Name:");
		paramList.add("Co-occurrence distance paramter:");
		paramList.add("Normalized along directions (0, 45, 90,.. degrees):");
		paramList.add("Number of grey levels (10 - 300):");
		return paramList;
	}

	@Override
	public void setParameters(String[] params) {
		// TODO Auto-generated method stub
		outputName = params[0];
		offset = (int )Double.parseDouble(params[1]);
		angles = params[2];
		levels = Integer.parseInt(params[3]);
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
			col1.setFeatureName("Contrast");
			col2.setFeatureName("Homogeneity");
			col3.setFeatureName("Mean");
			col4.setFeatureName("Variance");			
			fs.addFeature(col1);
			fs.addFeature(col2);
			fs.addFeature(col3);
			fs.addFeature(col4);
			
			ListIterator<CsvRecord> csvIterator = csvRows.listIterator();
			while (csvIterator.hasNext()) {
				CsvRecord csvRow = (CsvRecord) csvIterator.next();
				String imgFile = csvRow.getImageFileName();
				String imgFilePath = Locations.dataPath + "/" + AutoDoc.mode + "/"
						+ AutoDoc.collection + "/" + ds.getName() + "/"
						+ imgFile + Settings.imageExt;
				try {
					double chmv[] = getCHMV(imgFilePath);
					col1.addValue(""+chmv[0]);
					col2.addValue(""+chmv[1]);
					col3.addValue(""+chmv[2]);
					col4.addValue(""+chmv[3]);
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
	private double[] getCHMV(String imgFilePath) throws IOException {
		//System.out.println("imgFilePath: "+imgFilePath);
		File imgFile = new File(imgFilePath);
		BufferedImage bi = ImageIO.read(imgFile);
		int W = bi.getWidth();
		int H = bi.getHeight();
		int gray[][] = new int[W/2+1][H/2+1];
		String angle[] = angles.split(",");
		int totalpixels = W * H;
		int[] pixels = new int[totalpixels];
		bi.getRGB(0, 0, W, H, pixels, 0, W);
		double rootThree = Math.sqrt(3);
		for (int i = W/4; i < W * 3/4; i++) {
			for (int j = H/4 ; j < H * 3/4; j++) {
				int position = j * W + i;
				int p = pixels[position];
				int r = (p >> 16) & 0xFF;
				int g = (p >> 8) & 0xFF;
				int b = p & 0xFF;
				gray[i-W/4][j-H/4] = (int) ( Math.sqrt((double)(r*r+g*g+b*b)) * (levels - 1) / ( rootThree * 255) );
			}
		}
		double glcm[][] = getGLCM(angle, levels, gray, W/2, H/2, offset);
		double chmv[] = new double[4];
		chmv[0] = 0.0;
		for(int i=0; i<levels; i++) {
			for(int j=0; j<levels; j++) {
				chmv[0] += glcm[i][j] * ((i-j)*(i-j));
			}
		}
		chmv[0] = chmv[0] / (W * H);
		chmv[1] = 0.0;
		for(int i=0; i<levels; i++) {
			for(int j=0; j<levels; j++) {
				chmv[1] += glcm[i][j] / ( 1 + (i-j)*(i-j) );
			}
		}
		chmv[1] = chmv[1] * 100 / (W * H);
		chmv[2] = 0.0;
		for(int i=0; i<levels; i++) {
			for(int j=0; j<levels; j++) {
				chmv[2] += glcm[i][j] * i;
			}
		}
		chmv[2] = chmv[2] / (W * H);
		double mean = chmv[2];
		chmv[3] = 0.0;
		for(int i=0; i<levels; i++) {
			for(int j=0; j<levels; j++) {
				chmv[3] += glcm[i][j] * ((i - mean)*(i - mean));
			}
		}
		chmv[3] = chmv[3] / (W * H);
		return chmv;
	}

	public static void main(String cp[]) {
		FeatureTextureGenerator fag = new FeatureTextureGenerator();
		String params[] = { "texture", "3", "0,90,180,270", "200" };
		fag.setParameters(params);
		double chmv[];
		try {
			chmv = fag
					.getCHMV("./data/train/nails/nails_front/1.3.png");
			System.out.println("CHMV: (" + chmv[0] + ","
					+ chmv[1] + "," + chmv[2] + "," + chmv[3] + ")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private double[][] getGLCM(String angle[], int levels, int gray[][], int w, int h, int d) {
		int c[][][] = new int[angle.length][levels][levels];
		for(int k=0; k<angle.length;k++) {
			for(int i=0; i<levels; i++) {
				for(int j=0; j<levels; j++) {
					c[k][i][j] = 0;
				}
			}
		}
		for(int i=0; i<w; i++) {
			for(int j=0; j<h; j++) {
				for(int k=0; k<angle.length;k++) {
					if(angle[k].equals("0")) {
						if(j+d < h) {
							int l1 = gray[i][j];
							int l2 = gray[i][j+d];
							c[k][l1][l2] ++;
						}
					}
					if(angle[k].equals("45")) {
						if(j+d < h && i+d < w) {
							int l1 = gray[i][j];
							int l2 = gray[i+d][j+d];
							c[k][l1][l2] ++;
						}
					}
					if(angle[k].equals("90")) {
						if(i+d < w) {
							int l1 = gray[i][j];
							int l2 = gray[i+d][j];
							c[k][l1][l2] ++;
						}
					}
					if(angle[k].equals("135")) {
						if(j-d > 0 && i+d < w) {
							int l1 = gray[i][j];
							int l2 = gray[i+d][j-d];
							c[k][l1][l2] ++;
						}
					}
					if(angle[k].equals("180")) {
						if(j-d > 0) {
							int l1 = gray[i][j];
							int l2 = gray[i][j-d];
							c[k][l1][l2] ++;
						}
					}
					if(angle[k].equals("225")) {
						if(j-d > 0 && i-d > 0) {
							int l1 = gray[i][j];
							int l2 = gray[i-d][j-d];
							c[k][l1][l2] ++;
						}
					}
					if(angle[k].equals("270")) {
						if(i-d > 0) {
							int l1 = gray[i][j];
							int l2 = gray[i-d][j];
							c[k][l1][l2] ++;
						}
					}
					if(angle[k].equals("315")) {
						if(j+d < h && i-d > 0) {
							int l1 = gray[i][j];
							int l2 = gray[i-d][j+d];
							c[k][l1][l2] ++;
						}
					}
				}
			}
		}
		double g[][] = new double[levels][levels];
		for(int i=0; i<levels; i++) {
			for(int j=0; j<levels; j++) {
				g[i][j] = 0;
				for(int k=0; k<angle.length; k++) {
					g[i][j] += c[k][i][j];
				}
				g[i][j] /= angle.length;
			}
		}
		return g;
	}

}
