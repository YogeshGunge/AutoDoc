package org.autodoc.model;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Result {
	
	private static HashMap<String,String> resultMap;
	
	static {
		resultMap = new HashMap<String,String>();
	}
	
	public static void initResults() {
		resultMap = new HashMap<String,String>();
	}
	
	public static void addResult(DataSet[] inputs, Feature f) {
		int fRow = 0;
		for(int index = 0; index < inputs.length; index++) {
    		DataSet ds = inputs[index];
    		ArrayList<CsvRecord> trList = ds.getTrainLabels();
    		ListIterator<CsvRecord> trIter = trList.listIterator();
    		while(trIter.hasNext()) {
    			CsvRecord row = trIter.next();
    			int newClass = 0;
    			String imageFileName = row.getImageFileName();
    			if(f.getValueAt(fRow) != null) {
    				newClass = Integer.parseInt(f.getValueAt(fRow));
    			}
    			if(resultMap.get(imageFileName) == null) {
    				resultMap.put(imageFileName, ""+newClass);
    			} else {
    				int oldClass = Integer.parseInt(resultMap.get(imageFileName));
    				int finalClass = (newClass > oldClass) ? newClass : oldClass;
    				resultMap.put(imageFileName, ""+finalClass);
    			}
    			fRow++;
    		}
		}
	}
	
	public static int getResultSize() {
		return resultMap.size();
	}
	
	public static int getCorrect() {
		int correctCount = 0;
		Iterator<String> imageLabels = resultMap.keySet().iterator();
		while(imageLabels.hasNext()) {
			String label = imageLabels.next();
			double d = Double.parseDouble(label);
			int classId1 = (int)d;
			int classId2 = (int) Double.parseDouble(resultMap.get(label));
			if(classId1 == classId2) {
				correctCount ++;
			}
		}
		return correctCount;
	}
	
	public static HashMap<String,String> getResult() {
		return resultMap;
	}
	
}