package org.autodoc.imageops;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.imageio.ImageIO;

import org.autodoc.AutoDoc;
import org.autodoc.model.CsvRecord;
import org.autodoc.model.DataSet;
import org.autodoc.resources.Locations;
import org.autodoc.resources.Settings;

public class ConvertImageBlur implements ImagePreProcessor {
	private String outputName;
	private int filterSize;
	
	@Override
	public ArrayList<String> getParameterList() {
		// TODO Auto-generated method stub
		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add("Output Name:");
		paramList.add("Size of Blur Filter ( 3, 5, 7,..) :");
		return paramList;
	}

	@Override
	public void setParameters(String[] params) {
		// TODO Auto-generated method stub
		outputName = params[0];
		filterSize = Integer.parseInt(params[1]);
	}

	@Override
	public DataSet preProcess(DataSet input) {
		// TODO Auto-generated method stub
		DataSet ds = new DataSet();
		String inputName = input.getName();
		ds.setName(outputName);
		try {
			File grayFolder = new File(Locations.dataPath + "/" + AutoDoc.mode + "/" + AutoDoc.collection + "/" + outputName + "/");
			grayFolder.mkdir();
			copy(Locations.dataPath + "/" + AutoDoc.mode + "/" + AutoDoc.collection + "/" + inputName + ".csv"
					, Locations.dataPath + "/" + AutoDoc.mode + "/" + AutoDoc.collection + "/" + outputName + ".csv" );
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		ArrayList<CsvRecord> csvRows = input.getTrainLabels();
		ListIterator<CsvRecord> csvIterator = csvRows.listIterator();
		while (csvIterator.hasNext()) {
			CsvRecord csvRow = (CsvRecord) csvIterator.next();
			String imgFile = csvRow.getImageFileName();
			String imgFilePath = Locations.dataPath + "/" + AutoDoc.mode + "/"
					+ AutoDoc.collection + "/" + inputName + "/"
					+ imgFile + Settings.imageExt;
			String grayFilePath = Locations.dataPath + "/" + AutoDoc.mode + "/"
					+ AutoDoc.collection + "/" + outputName + "/"
					+ imgFile + Settings.imageExt;
			try {
				convertToGrayScale(imgFilePath, grayFilePath);
				ds.addTrainRecord(csvRow.getImageFileName(), csvRow.getImageClassName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ds;
	}
	
	public static void copy(String sourcePath, String destinationPath) throws IOException {
	    Files.copy(Paths.get(sourcePath), new FileOutputStream(destinationPath));
	}
	
	private void convertToGrayScale(String imgFilePath, String imgDestPath) throws IOException {
		File imgFile = new File(imgFilePath);
		BufferedImage bi = ImageIO.read(imgFile);
		int W = bi.getWidth();
		int H = bi.getHeight();
		int totalpixels = W * H;
		int[] pixels = new int[totalpixels];
		int[] blur = new int[totalpixels];
		bi.getRGB(0, 0, W, H, pixels, 0, W);
		int d = filterSize / 2;
		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H; j++) {
				int position = j * W + i;
				int r = 0;
				int g = 0;
				int b = 0;
				for(int x = i-d; x<=i+d; x++) {
					for(int y = j-d; y<=j+d; y++) {
						r += getImageChannel(x, y, H, W, pixels, 1);
						g += getImageChannel(x, y, H, W, pixels, 2);
						b += getImageChannel(x, y, H, W, pixels, 3);
					}
				}
				r /= (2*d + 1)*(2*d + 1);
				g /= (2*d + 1)*(2*d + 1);
				b /= (2*d + 1)*(2*d + 1);
				int p = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
				blur[position] = p; 
			}
		}
		BufferedImage out = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
		out.setRGB(0, 0, W, H, blur, 0, W);
		File outFile = new File(imgDestPath);
		ImageIO.write(out, Settings.imageExt.replace(".", " ").trim() , outFile);
	}
	
	private int getImageChannel(int x, int y, int H, int W, int pixels[], int channel) {
		if(x >= 0 && y >= 0 && x < W && y < H) {
			int position = y * W + x;
			int p = pixels[position];
			int offset = ( 3 - channel ) * 8;
			return (p >> offset) & 0xFF;
		}
		return 0;
	}
	

}
