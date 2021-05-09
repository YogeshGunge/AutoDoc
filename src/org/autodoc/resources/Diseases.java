package org.autodoc.resources;

import java.util.HashMap;

public class Diseases {
	
	private static HashMap<String,String> diseaseMap;
	
	static {
		diseaseMap = new HashMap<String, String>();
		diseaseMap.put("0", "Your nail are healthy.");
		diseaseMap.put("1", "Your nail indicate heart disease like anemia or diabetes mellitus.");
		diseaseMap.put("2", "Your nail show terry's nail, often a sign of liver disease like cirrosis.");
		diseaseMap.put("3", "Your nail indicate Pulmonary disease and lymphedema (swelling of the extremities).");
		diseaseMap.put("4", "Your nail show ridges, which may be caused by Arthritis, Thyroid related problems.");
		diseaseMap.put("5", "Your nail show pitting indicate psoriasis.");
		diseaseMap.put("6", "Your nail show bluish nail beds indicate lack of oxygen in blood.");
	}
	
	public static String getDescription(String diseaseId) {
		return diseaseMap.get(diseaseId);
	}

}
