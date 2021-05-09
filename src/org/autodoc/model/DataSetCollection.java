package org.autodoc.model;

import java.util.ArrayList;
import java.util.HashMap;

public class DataSetCollection {
	private static ArrayList<DataSet> datasets;
	private static HashMap<String,DataSet> nameDataSetMap;
	
	public static DataSet getDataSetFromName(String dataSetName) {
		if(nameDataSetMap == null) {
			nameDataSetMap = new HashMap<String,DataSet>();
			for(DataSet d : datasets) {
				nameDataSetMap.put(d.getName(),d);
			}
		}
		return nameDataSetMap.get(dataSetName);
	}
	
	public static void addDataSet(DataSet ds) {
		nameDataSetMap.put(ds.getName(),ds);
		datasets.add(ds);
	}
	
	public static void init(int size) {
		datasets = new ArrayList<DataSet>();
		for(int index = 0; index < size; index++) {
			datasets.add(new DataSet());
		}
	}
	
	public static DataSet getDataSet(int index) {
		return datasets.get(index);
	}
	
	public static ArrayList<String> getDataSetNameList() {
		ArrayList<String> dslist = new ArrayList<String>();
		for(DataSet d : datasets) {
			dslist.add(d.getName());
		}
		return dslist;
	}
	
	public static DataSet[] getDataSets() {
		DataSet[] dsarr = (DataSet[])(datasets.toArray());
		return dsarr;
	}
	
}