package org.autodoc.model;

import java.util.ArrayList;

public class DataSet {
	private String name;
	private ArrayList<CsvRecord> trainLabels;
	
	public DataSet() {
		trainLabels = new ArrayList<CsvRecord>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addTrainRecord(String imageFileName, String imageClassName) {
		CsvRecord trainRecord = new CsvRecord();
		trainRecord.setImageFileName(imageFileName);
		trainRecord.setImageClassName(imageClassName);
		trainLabels.add(trainRecord);
	}
	
	public ArrayList<CsvRecord> getTrainLabels() {
		return trainLabels;
	}
	
}
