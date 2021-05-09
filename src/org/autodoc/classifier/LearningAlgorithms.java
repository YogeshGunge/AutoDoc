package org.autodoc.classifier;

import org.autodoc.interpreter.Architecture;

public class LearningAlgorithms {
	public static String[] featureClassificationAlgos;
	
	static {
		featureClassificationAlgos = new String[4];
		featureClassificationAlgos[0] = Architecture.dtreeJ48;
		featureClassificationAlgos[1] = Architecture.merge;
		featureClassificationAlgos[2] = Architecture.mlpnet;
		featureClassificationAlgos[3] = Architecture.svm;
	}
	
	public static FeatureClassPredictor getLearningAlgorithmInstance(int index) {
		FeatureClassPredictor fcp = null;
		switch(index) {
			case 0:
				fcp = new J48Classifier();
				break;
			case 1:
				fcp = new FeatureMerger();
				break;
			case 2:
				fcp = new AnnClassifier();
				break;
			case 3:
				fcp = new SvmClassifier();
				break;
		}
		return fcp;
	}
	
}
