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

public class ConvertUpScale implements ImagePreProcessor {
	private String outputName;
	private int outputScale;
	
	@Override
	public ArrayList<String> getParameterList() {
		// TODO Auto-generated method stub
		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add("Output Name:");
		paramList.add("Output Scale (Integer):");
		return paramList;
	}

	@Override
	public void setParameters(String[] params) {
		// TODO Auto-generated method stub
		outputName = params[0];
		outputScale = Integer.parseInt(params[1]);
	}

	@Override
	public DataSet preProcess(DataSet input) {
		// TODO Auto-generated method stub
		DataSet ds = new DataSet();
		String inputName = input.getName();
		ds.setName(outputName);
		try {
			File newFolder = new File(Locations.dataPath + "/" + AutoDoc.mode + "/" + AutoDoc.collection + "/" + outputName + "/");
			newFolder.mkdir();
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
			String newFilePath = Locations.dataPath + "/" + AutoDoc.mode + "/"
					+ AutoDoc.collection + "/" + outputName + "/"
					+ imgFile + Settings.imageExt;
			try {
				convertToUpScale(imgFilePath, newFilePath);
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
	
	private void convertToUpScale(String imgFilePath, String imgDestPath) throws IOException {
		File imgFile = new File(imgFilePath);
		BufferedImage bi = ImageIO.read(imgFile);
		int W = bi.getWidth();
		int H = bi.getHeight();
		int totalpixels = W * H;
		int[] pixels = new int[totalpixels];
		int W2 = outputScale;
		int H2 = outputScale;
		int totalpixels2 = W2 * H2;
		int[] upScaled = new int[totalpixels2];
		bi.getRGB(0, 0, W, H, pixels, 0, W);
		for (double i = 0; i < W2; i++) {
			for (double j = 0; j < H2; j++) {
				int position = (int)(j * W2 + i);
				int x = Math.min((int)(i/W2 * W), W);
				int y = Math.min((int)(j/H2 * H), H);
				int r = getImageChannel(x, y, H, W, pixels, 1);
				int g = getImageChannel(x, y, H, W, pixels, 2);
				int b = getImageChannel(x, y, H, W, pixels, 3);
				int p = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
				upScaled[position] = p;
			}
		}
		BufferedImage out = new BufferedImage(W2, H2, BufferedImage.TYPE_INT_RGB);
		out.setRGB(0, 0, W2, H2, upScaled, 0, W2);
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
