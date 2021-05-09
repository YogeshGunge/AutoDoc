package org.autodoc.model;

import java.util.ArrayList;

public class FeatureSet {
	private String name;
	private ArrayList<Feature> featurelist;
	public FeatureSet(String name) {
		setName(name);
		setFeaturelist(new ArrayList<Feature>());
	}
	public FeatureSet(String name, ArrayList<Feature> fList) {
		setName(name);
		setFeaturelist(fList);
	}
	public ArrayList<Feature> getFeaturelist() {
		return featurelist;
	}
	public void setFeaturelist(ArrayList<Feature> featurelist) {
		this.featurelist = featurelist;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addFeature(Feature f) {
		featurelist.add(f);
	}
}
