package org.autodoc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ClassLabels {
	private static HashMap<Integer,String> classLabelMap;
	
	public static void resetClassLabelMap() {
		classLabelMap = new HashMap<Integer,String>();
	}
	
	public static void addClassLabel(int classId, String classLabel) {
		if(classLabelMap == null) {
			classLabelMap = new HashMap<Integer,String>();
		}
		classLabelMap.put(new Integer(classId), classLabel);
	}
	
	public static int getClassCount() {
		return classLabelMap.size();
	}
	
	public static List<String> getClassLabels() {
		List<String> labels = new ArrayList<String>();
		Iterator<Integer> classIter = classLabelMap.keySet().iterator();
		while(classIter.hasNext()) {
			labels.add(classIter.next().toString());
		}
		return labels;
	}
	
}
