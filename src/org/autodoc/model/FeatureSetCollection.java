package org.autodoc.model;

import java.util.ArrayList;
import java.util.HashMap;

public class FeatureSetCollection {
	private static ArrayList<FeatureSet> fSets;
	private static HashMap<String,FeatureSet> nameFsetMap;
	
	public static FeatureSet getDataSetFromName(String featureSetName) {
		if(nameFsetMap == null) {
			nameFsetMap = new HashMap<String,FeatureSet>();
			for(FeatureSet f : fSets) {
				nameFsetMap.put(f.getName(),f);
			}
		}
		return nameFsetMap.get(featureSetName);
	}
	
	public static void init() {
		if(fSets == null) {
			fSets = new ArrayList<FeatureSet>();
		}
	}
	
	public static void add(FeatureSet fs) {
		init();
		fSets.add(fs);
	}
	
	public static FeatureSet getFeatureSet(int index) {
		return fSets.get(index);
	}
	
	public static ArrayList<String> getFeatuerSetNameList() {
		ArrayList<String> fslist = new ArrayList<String>();
		for(FeatureSet f : fSets) {
			fslist.add(f.getName());
		}
		return fslist;
	}
	
}