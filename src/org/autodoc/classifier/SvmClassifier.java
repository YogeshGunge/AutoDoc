package org.autodoc.classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.autodoc.AutoDoc;
import org.autodoc.model.ClassLabels;
import org.autodoc.model.CsvRecord;
import org.autodoc.model.DataSet;
import org.autodoc.model.Feature;
import org.autodoc.model.FeatureSet;
import org.autodoc.resources.Locations;
import org.autodoc.resources.Settings;
import org.autodoc.tools.ConsoleLogger;
import org.autodoc.tools.FileOperator;

import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SerializationHelper;

/**
*
* @author yogesh.gunge
*/
public class SvmClassifier implements FeatureClassPredictor {
	private LibSVM svm = new LibSVM();
	private ArrayList<Attribute> attributes;
	private String outputName;
	private String inputFsName;
	private List<String> classes;
	private int classIds[];
	private Instances instances;
	private int cacheSize;
	private boolean normalize;
	private boolean shrinking;
	private int kernelType;
	private int degree;
	private int cost;

	@Override
	public ArrayList<String> getParameterList() {
		// TODO Auto-generated method stub
		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add("Output Model Name:");
		paramList.add("Input Feature Set:");
		paramList.add("Target Classes Indices (*/comma separated):");
		paramList.add("Cache Size (512) MB:");
		paramList.add("Enable Normalization (true/false):");
		paramList.add("Enable Shrinking (true/false):");
		paramList.add("Type of Polynomial Kernel Function (0: linear, 1: polynomial, 2: radial, 3: sigmoid):");
		paramList.add("Degree of Polynomial Kernel Function:");
		paramList.add("Cost, C of C-SVC, epsilon-SVR, and nu-SVR:");
		//paramList.add("Outlier Elimination Factor (0.01 to 0.99):");
		return paramList;
	}
	
	@Override
	public void setParameters(String[] params) throws InvalidFeatureSetException {
		// TODO Auto-generated method stub
		outputName = params[0];
		inputFsName = params[1];
		if(params[2].equals("*")) {
			classes = ClassLabels.getClassLabels();
		} else {
			classes = Arrays.asList(params[2].split(","));
		}
		classIds = new int[classes.size()];
		for(int index = 0; index < classIds.length; index++) {
			try {
				classIds[index] = Integer.parseInt(classes.get(index));
			} catch (Exception e) {}
		}
		cacheSize = Integer.parseInt(params[3]);
		normalize = Boolean.parseBoolean(params[4]);
		shrinking = Boolean.parseBoolean(params[5]);
		kernelType = Integer.parseInt(params[6]);
		degree = Integer.parseInt(params[7]);
		cost = Integer.parseInt(params[8]);
	}
	
	@Override
	public void trainModel(DataSet inputs[]) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		// Setting Parameters
		svm.setCacheSize(cacheSize); // MB
        svm.setNormalize(normalize);
        svm.setShrinking(shrinking);
        switch(kernelType) {
        case 0:
        	svm.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_LINEAR, LibSVM.TAGS_KERNELTYPE));
        	break;
        case 1:
        	svm.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_POLYNOMIAL, LibSVM.TAGS_KERNELTYPE));
        	break;
        case 2:
        	svm.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_RBF, LibSVM.TAGS_KERNELTYPE));
        	break;
        case 3:
        	svm.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_SIGMOID, LibSVM.TAGS_KERNELTYPE));
        	break;
        }
        svm.setDegree(degree);
        svm.setSVMType(new SelectedTag(LibSVM.SVMTYPE_C_SVC, LibSVM.TAGS_SVMTYPE));
		svm.setCost(cost);
        
		// Creating Training Instance
		FeatureSet fs = FileOperator.loadFeatureSet(inputFsName);
		List<Feature> fList = fs.getFeaturelist();
		attributes = new ArrayList<Attribute>();
		for(int index = 0; index < fList.size(); index++) {
			attributes.add(
					new Attribute(fList.get(index)
							.getFeatureName()
					)
			);
		}
		attributes.add(new Attribute(outputName, classes));
        instances = new Instances(outputName, attributes, 500);
        instances.setClassIndex(fList.size());
        
        int fsRow = 0;
        for(int index = 0; index < inputs.length; index++) {
    		DataSet ds = inputs[index];
    		ArrayList<CsvRecord> trList = ds.getTrainLabels();
    		ListIterator<CsvRecord> trIter = trList.listIterator();
    		while(trIter.hasNext()) {
    			CsvRecord row = trIter.next();
    			String imageFileName = row.getImageFileName();
    			try {
					float imageId = Float.parseFloat(imageFileName);
					int imageClass = (int) imageId;
					boolean imageAllowed = false;
					for(int id = 0; id < classIds.length; id++) {
						if(classIds[id] == imageClass) {
							imageAllowed = true;
						}
					}
					if(imageAllowed) {
						Instance i = new DenseInstance(fList.size()+1);
				        i.setDataset(instances);
				        // Loop to set all features of featureset
				        for(int fsCol = 0; fsCol < fList.size(); fsCol++) {
				        	double featureVal = Double.parseDouble(fList.get(fsCol).getValueAt(fsRow));
				        	i.setValue(fsCol, featureVal);
						}
				        i.setValue(fList.size(), ""+imageClass);
				        instances.add(i);
					}
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			fsRow ++;
    		}
        }
        try {
			svm.buildClassifier(instances);
	        // System.out.println(classifier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveModel() {
		// TODO Auto-generated method stub
		try {
			String outFilePath = Locations.dataPath + "/" + AutoDoc.mode + "/"
					+ AutoDoc.collection + "/"
					+ outputName + Settings.modelExt;
			SerializationHelper.write(outFilePath, svm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConsoleLogger.addLog("Saved new model "+outputName + Settings.modelExt+".");
	}
	
	@Override
	public void loadModel() {
		// TODO Auto-generated method stub
		try {
			String outFilePath = Locations.dataPath + "/" + AutoDoc.mode + "/"
					+ AutoDoc.collection + "/"
					+ outputName + Settings.modelExt;
			svm = (LibSVM) SerializationHelper.read(outFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Feature useModel(DataSet inputs[]) {
		// TODO Auto-generated method stub
		Feature result = new Feature();
		result.setFeatureName(outputName);
		FeatureSet fs = FileOperator.loadFeatureSet(inputFsName);
		List<Feature> fList = fs.getFeaturelist();
		//attributes = new ArrayList<Attribute>();
		//for(int index = 0; index < fList.size(); index++) {
		//	attributes.add(
		//			new Attribute(fList.get(index)
		//					.getFeatureName()
		//			)
		//	);
		//}
		//attributes.add(new Attribute(outputName, classes));
        //instances = new Instances(outputName, attributes, 500);
        //instances.setClassIndex(classes.size());
        int fsRow = 0;
        for(int index = 0; index < inputs.length; index++) {
    		DataSet ds = inputs[index];
    		ArrayList<CsvRecord> trList = ds.getTrainLabels();
    		ListIterator<CsvRecord> trIter = trList.listIterator();
    		while(trIter.hasNext()) {
    			CsvRecord row = trIter.next();
    			String imageFileName = row.getImageFileName();
    			try {
					float imageId = Float.parseFloat(imageFileName);
					int imageClass = (int) imageId;
					boolean imageAllowed = false;
					for(int id = 0; id < classIds.length; id++) {
						if(classIds[id] == imageClass) {
							imageAllowed = true;
						}
					}
					if(imageAllowed) {
						Instance i = new DenseInstance(fList.size()+1);
				        i.setDataset(instances);
				        // Loop to set all features of featureset
				        for(int fsCol = 0; fsCol < fList.size(); fsCol++) {
				        	double featureVal = Double.parseDouble(fList.get(fsCol).getValueAt(fsRow));
				        	i.setValue(fsCol, featureVal);
						}
				        double[] classRes = svm.distributionForInstance(i);
				        int classLabel = 0;
				        for(int x = 0; x < classRes.length; x++) {
				        	if(classRes[x] > classRes[classLabel]) {
				        		classLabel = x;
				        	}
				        }
				        result.addValue(""+(classIds[classLabel]));
					} else {
						result.addValue("0");
					}
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			fsRow ++;
    		}
        }
        System.out.println(result.getValues());
		return result;
	}

}
