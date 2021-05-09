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

public class BlendImages implements ImageTransformer {
	private String outputName;
	private int operId;

	@Override
	public ArrayList<String> getParameterList() {
		// TODO Auto-generated method stub
		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add("Output Name:");
		paramList.add("Operation Name ( add, sub ):");
		return paramList;
	}

	@Override
	public void setParameters(String[] params) {
		// TODO Auto-generated method stub
		outputName = params[0];
		String operationName = params[1];
		if( operationName.equals("add") ) {
			operId = 1;
		} else if( operationName.equals("sub") ) {
			operId = -1;
		} else {
			operId = 0;
		}
	}

	@Override
	public DataSet transform(DataSet input1, DataSet input2) {
		// TODO Auto-generated method stub
		DataSet ds = new DataSet();
		String inputName1 = input1.getName();
		String inputName2 = input2.getName();
		ds.setName(outputName);
		try {
			File newFolder = new File(Locations.dataPath + "/" + AutoDoc.mode + "/" + AutoDoc.collection + "/" + outputName + "/");
			newFolder.mkdir();
			copy(Locations.dataPath + "/" + AutoDoc.mode + "/" + AutoDoc.collection + "/" + inputName1 + ".csv"
					, Locations.dataPath + "/" + AutoDoc.mode + "/" + AutoDoc.collection + "/" + outputName + ".csv" );
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		ArrayList<CsvRecord> csvRows1 = input1.getTrainLabels();
		ArrayList<CsvRecord> csvRows2 = input2.getTrainLabels();
		ListIterator<CsvRecord> csvIterator1 = csvRows1.listIterator();
		ListIterator<CsvRecord> csvIterator2 = csvRows2.listIterator();
		while (csvIterator1.hasNext() && csvIterator2.hasNext()) {
			CsvRecord csvRow1 = (CsvRecord) csvIterator1.next();
			CsvRecord csvRow2 = (CsvRecord) csvIterator2.next();
			String imgFile1 = csvRow1.getImageFileName();
			String imgFile2 = csvRow2.getImageFileName();
			String imgFilePath1 = Locations.dataPath + "/" + AutoDoc.mode + "/"
					+ AutoDoc.collection + "/" + inputName1 + "/"
					+ imgFile1 + Settings.imageExt;
			String imgFilePath2 = Locations.dataPath + "/" + AutoDoc.mode + "/"
					+ AutoDoc.collection + "/" + inputName2 + "/"
					+ imgFile2 + Settings.imageExt;
			String newFilePath = Locations.dataPath + "/" + AutoDoc.mode + "/"
					+ AutoDoc.collection + "/" + outputName + "/"
					+ imgFile1 + Settings.imageExt;
			try {
				addImages(imgFilePath1, imgFilePath2, newFilePath);
				ds.addTrainRecord(csvRow1.getImageFileName(), csvRow1.getImageClassName());
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
	
	private void addImages(String imgFilePath1, String imgFilePath2, String imgDestPath) throws IOException {
		File imgFile1 = new File(imgFilePath1);
		File imgFile2 = new File(imgFilePath2);
		BufferedImage bi1 = ImageIO.read(imgFile1);
		int W1 = bi1.getWidth();
		int H1 = bi1.getHeight();
		int totalpixels1 = W1 * H1;
		int[] pixels1 = new int[totalpixels1];
		bi1.getRGB(0, 0, W1, H1, pixels1, 0, W1);
		BufferedImage bi2 = ImageIO.read(imgFile2);
		int W2 = bi2.getWidth();
		int H2 = bi2.getHeight();
		int totalpixels2 = W2 * H2;
		int[] pixels2 = new int[totalpixels2];
		bi2.getRGB(0, 0, W2, H2, pixels2, 0, W2);
		for (int i = 0; i < W1; i++) {
			for (int j = 0; j < H1; j++) {
				int position = j * W1 + i;
				int p = pixels1[position];
				int r2 = getImageChannel(i, j, H2, W2, pixels2, 1);
				int g2 = getImageChannel(i, j, H2, W2, pixels2, 2);
				int b2 = getImageChannel(i, j, H2, W2, pixels2, 3);
				int r = (p >> 16) & 0xFF;
				int g = (p >> 8) & 0xFF;
				int b = p & 0xFF;
				r = Math.max(Math.min(r+r2*operId, 255), 0);
				g = Math.max(Math.min(g+g2*operId, 255), 0);
				b = Math.max(Math.min(b+b2*operId, 255), 0);
				p = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
				pixels1[position] = p; 
			}
		}
		BufferedImage out = new BufferedImage(W1, H1, BufferedImage.TYPE_INT_RGB);
		out.setRGB(0, 0, W1, H1, pixels1, 0, W1);
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
