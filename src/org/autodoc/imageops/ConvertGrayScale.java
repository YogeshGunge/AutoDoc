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

public class ConvertGrayScale implements ImagePreProcessor {
	private String outputName;
	
	@Override
	public ArrayList<String> getParameterList() {
		// TODO Auto-generated method stub
		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add("Output Name:");
		return paramList;
	}

	@Override
	public void setParameters(String[] params) {
		// TODO Auto-generated method stub
		outputName = params[0];
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
		bi.getRGB(0, 0, W, H, pixels, 0, W);
		double rootThree = Math.sqrt(3);
		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H; j++) {
				int position = j * W + i;
				int p = pixels[position];
				int r = (p >> 16) & 0xFF;
				int g = (p >> 8) & 0xFF;
				int b = p & 0xFF;
				int gray = (int) ( Math.sqrt((double)(r*r+g*g+b*b)) / rootThree );
				gray = Math.max(0, Math.min(255, gray));
				r = g = b = gray;
				p = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
				pixels[position] = p; 
			}
		}
		BufferedImage out = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
		out.setRGB(0, 0, W, H, pixels, 0, W);
		File outFile = new File(imgDestPath);
		ImageIO.write(out, Settings.imageExt.replace(".", " ").trim() , outFile);
	}
	

}
