package org.autodoc.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.autodoc.AutoDoc;
import org.autodoc.model.ClassLabels;
import org.autodoc.model.DataSetCollection;
import org.autodoc.model.Feature;
import org.autodoc.model.FeatureSet;
import org.autodoc.resources.Locations;

public class FileOperator {
	
	public static void loadDataSet() {
		String datasetNames[] = Locations.testDataSets.split(",");
		int size = datasetNames.length;
		DataSetCollection.init(size);
		for(int i=0; i<size; i++) {
			String dsname = datasetNames[i].replace('\n', ' ').trim();
			DataSetCollection.getDataSet(i).setName(dsname);
			File csvFile = new File(Locations.dataPath+"/"+AutoDoc.mode+"/"+AutoDoc.collection+"/"+dsname+".csv");
			String csvRows[] = read(csvFile).split("\n");
			int numTuples = csvRows.length;
			String imageFileName = "";
			String imageClassName = "";
			for(int j=0; j<numTuples; j++) {
				String row = csvRows[j];
				String cols[] = row.split(",");
				imageFileName = cols[0];
				if(cols.length > 1) {
					imageClassName = cols[1];
					try {
						float imageId = Float.parseFloat(imageFileName);
						int imageClass = (int) imageId;
						ClassLabels.addClassLabel(imageClass, imageClassName);
					} catch (Exception e) {	}
				}
				DataSetCollection.getDataSet(i).addTrainRecord(imageFileName, imageClassName);
			}
			ConsoleLogger.addLog("Loaded DataSet '"+dsname+"' with "+numTuples+" tuples and "+ClassLabels.getClassCount()+" classes.");
		}
	}
	
	public static FeatureSet loadFeatureSet(String fsName) {
		String filePath = Locations.dataPath+"/"+AutoDoc.mode+"/"+AutoDoc.collection+"/"+fsName+".csv";
		File f = new File(filePath);
		if(!f.exists() || !f.canRead()) {
			return null;
		}
		FeatureSet fs = new FeatureSet(fsName);
		fs.setFeaturelist(new ArrayList<Feature>());
		try {
			FileReader fr = new FileReader(f);
			int ch = 1, rowId = -1, colId;
			char chars;
			String row = "";
			while(ch != -1) {
				ch = fr.read();
				chars = (char) ch;
				if(chars != '\n')
					row += chars;
				else if (ch == -1 || chars == '\n'){
					String features[] = row.split(",");
					while(fs.getFeaturelist().size() < features.length) {
						fs.getFeaturelist().add(new Feature());
					}
					if(rowId == -1) {
						colId = 0;
						while(colId < features.length) {
							fs.getFeaturelist().get(colId).setFeatureName(features[colId]);
							colId ++;
						}
					} else {
						colId = 0;
						while(colId < features.length) {
							fs.getFeaturelist().get(colId).setValueAt(rowId, features[colId]);
							colId ++;
						}
					}
					rowId ++;
					row = "";
				}
			}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fs;
	}
	
	public static void saveFeatureSet(FeatureSet fs) {
		String filePath = Locations.dataPath+"/"+AutoDoc.mode+"/"+AutoDoc.collection+"/"+fs.getName()+".csv";
		File f = new File(filePath);
		if(f.exists()) {
			ConsoleLogger.addLog("FeatureSet '"+fs.getName()+"' already exists, deleting old file..");
			f.delete();
			f = new File(filePath);
		}
		try {
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			ArrayList<Feature> fList = fs.getFeaturelist();
			int rowCount = fList.get(0).getValues().size();
			int colCount = fList.size();
			for(int col = 0; col<colCount-1; col++) {
				fw.write(fList.get(col).getFeatureName()+",");
			}
			fw.write(fList.get(colCount-1).getFeatureName()+"\n");
			for(int row = 0; row<rowCount; row++) {
				for(int col = 0; col<colCount-1; col++) {
					fw.write(fList.get(col).getValueAt(row)+",");
				}
				fw.write(fList.get(colCount-1).getValueAt(row)+"\n");
			}
			fw.close();
			ConsoleLogger.addLog("Generated FeatureSet '"+fs.getName()+"'.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String read(File f) {
		String data = "";
		try {
			FileInputStream fis = new FileInputStream(f);
			int dataByte;
			while ((dataByte = fis.read()) != -1) {
				data += (char) dataByte;
			}
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static void append(File f, String data) {
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String oldData = read(f);
		write(f, oldData+data);
	}
	
	public static void write(File f, String data) {
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		try {
			byte[] dataBytes = data.getBytes();
			FileOutputStream fis = new FileOutputStream(f);
			fis.write(dataBytes);
			fis.flush();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String uploadTestImage(String imagePath, String fname) {
		File fin = new File(imagePath);
		Path finPath = fin.toPath();
		File fout = new File(Locations.dataPath+"/"+AutoDoc.mode+"/"+AutoDoc.collection+"/"+fname);
		if(fin.exists()) {
			OutputStream os;
			try {
				fout.createNewFile();
				os = new FileOutputStream(fout);
				Files.copy(finPath, os);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fout.getAbsolutePath();
	}
	
}