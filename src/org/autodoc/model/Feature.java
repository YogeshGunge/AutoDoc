package org.autodoc.model;

import java.util.ArrayList;

public class Feature {
	private String featureName;
	private ArrayList<String> values;
	public Feature() {
		setValues(new ArrayList<String>());
	}
	public ArrayList<String> getValues() {
		return values;
	}
	public void setValues(ArrayList<String> values) {
		this.values = values;
	}
	public String getValueAt(int index) {
		if(index >= values.size()) {
			return "0";
		}
		return values.get(index);
	}
	public void setValueAt(int index, String element) {
		if(values == null) {
			values = new ArrayList<String>();
		}
		if(values.size() == index) {
			values.add(element);
		} else if (values.size() < index) {
			while(values.size() < index) {
				values.add("");
			}
			values.add(element);
		} else {
			values.set(index, element);
		}
	}
	public int addValue(String value) {
		values.add(value);
		return values.size();
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
}