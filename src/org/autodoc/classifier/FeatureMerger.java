package org.autodoc.classifier;

import java.util.ArrayList;

import org.autodoc.model.DataSet;
import org.autodoc.model.Feature;
import org.autodoc.model.FeatureSet;
import org.autodoc.tools.FileOperator;

/**
*
* @author yogesh.gunge
*/
public class FeatureMerger implements FeatureClassPredictor {
	private String outputName;
	private String inputFsNames;

	@Override
	public ArrayList<String> getParameterList() {
		// TODO Auto-generated method stub
		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add("Output Merged FeatureSet:");
		paramList.add("Input Feature Sets:");
		return paramList;
	}
	
	@Override
	public void setParameters(String[] params) throws InvalidFeatureSetException {
		// TODO Auto-generated method stub
		outputName = params[0];
		inputFsNames = params[1];
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void trainModel(DataSet inputs[]) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		String fsNamesArray[] = inputFsNames.split(",");
		FeatureSet fsInput, fsOutput;
		String fname;
		ArrayList<Feature> targetList = new ArrayList<Feature>();
		for(int i=0; i<fsNamesArray.length; i++) {
			fsInput = FileOperator.loadFeatureSet(fsNamesArray[i]);
			Object obj = fsInput.getFeaturelist().clone();
			if(obj instanceof ArrayList) {
				ArrayList<Feature> sourceList = (ArrayList<Feature>) obj;
				int j = 0;
				for(Feature f : sourceList) {
					fname = f.getFeatureName() + "-" + (i+1);
					f.setFeatureName(fname);
					sourceList.set(j, f);
					j++;
				}
				targetList.addAll(sourceList);
			}
		}
		fsOutput = new FeatureSet(outputName, targetList);
		FileOperator.saveFeatureSet(fsOutput);
	}
	
	@Override
	public void saveModel() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void loadModel() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Feature useModel(DataSet inputs[]) {
		// TODO Auto-generated method stub
		Feature result = new Feature();
		String fsNamesArray[] = inputFsNames.split(",");
		FeatureSet fsInput, fsOutput;
		String fname;
		ArrayList<Feature> targetList = new ArrayList<Feature>();
		for(int i=0; i<fsNamesArray.length; i++) {
			fsInput = FileOperator.loadFeatureSet(fsNamesArray[i]);
			Object obj = fsInput.getFeaturelist().clone();
			if(obj instanceof ArrayList) {
				ArrayList<Feature> sourceList = (ArrayList<Feature>) obj;
				int j = 0;
				for(Feature f : sourceList) {
					fname = f.getFeatureName() + "-" + (i+1);
					f.setFeatureName(fname);
					sourceList.set(j, f);
					j++;
				}
				targetList.addAll(sourceList);
			}
		}
		fsOutput = new FeatureSet(outputName, targetList);
		FileOperator.saveFeatureSet(fsOutput);
		return result;
	}

}
