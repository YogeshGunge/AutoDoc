package org.autodoc.classifier;

import java.util.ArrayList;

import org.autodoc.model.DataSet;
import org.autodoc.model.Feature;

/**
*
* @author yogesh.gunge
*/
public interface FeatureClassPredictor {
	ArrayList<String> getParameterList();
	void setParameters(String params[]) throws InvalidFeatureSetException;
	void trainModel(DataSet inputs[]);
	void saveModel();
	void loadModel();
	Feature useModel(DataSet inputs[]);
}
