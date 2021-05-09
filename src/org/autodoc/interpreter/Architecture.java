package org.autodoc.interpreter;

import java.util.HashMap;

/*
 * This class defines the architecture of AutoDoc Virtual Doctor
 * It has instruction set that includes:
 * 1. Input: Load Dataset
 * 2. primitives of image processing, machine learning and reasoning
 */
/**
*
* @author yogesh.gunge
*/
public class Architecture {
	
	private static HashMap<String,Integer> opcodeMap;
	private static HashMap<String,Integer> optypeMap;
	private static HashMap<String,Integer> opindexMap;
	
	public static String load = "Load"; // Instruction to load dataset
	public static String average = "ExtractAverageColor"; // Instruction to calculate average color from Images
	public static String dtreeJ48 = "ClassifyDtreeJ48"; // Instruction to classify features from FeatureSet using J48 Algorithm
	public static String bicluster = "ExtractBiClusterColor"; // Instruction to calculate average of two clusters of colors from Images
	public static String glcm = "ExtractCHMVbyGLCM"; // Instruction to calculate CHMV from Images using GLCM
	public static String gray = "ConvertGrayScale";
	public static String blur = "ConvertImageBlur";
	public static String upscale = "ConvertImageUpScale";
	public static String blend = "BlendImages";
	public static String merge = "MergeFeatures";
	public static String mlpnet = "MlpNeuralNetwork";
	public static String svm = "PolynomialSvm";
	
	static {
		opcodeMap = new HashMap<String, Integer>();
		opcodeMap.put(load,1);
		opcodeMap.put(average,2);
		opcodeMap.put(dtreeJ48, 3);
		opcodeMap.put(bicluster, 4);
		opcodeMap.put(glcm, 5);
		opcodeMap.put(gray, 6);
		opcodeMap.put(blur, 7);
		opcodeMap.put(upscale, 8);
		opcodeMap.put(blend, 9);
		opcodeMap.put(merge, 10);
		opcodeMap.put(mlpnet, 11);
		opcodeMap.put(svm, 12);
		
		optypeMap = new HashMap<String, Integer>();
		optypeMap.put(load,0); // 0 - Load Type
		optypeMap.put(average,3); // 3 - FeatureGenerator
		optypeMap.put(dtreeJ48, 4); // 4 - Classifier
		optypeMap.put(bicluster, 3);
		optypeMap.put(glcm, 3);
		optypeMap.put(gray, 1); // 1 - Preprocess
		optypeMap.put(blur, 1);
		optypeMap.put(upscale, 1);
		optypeMap.put(blend, 2); // 2 - Transform
		optypeMap.put(merge, 4);
		optypeMap.put(mlpnet, 4);
		optypeMap.put(svm, 4);
		
		opindexMap = new HashMap<String, Integer>();
		opindexMap.put(load,0);
		opindexMap.put(average,0);
		opindexMap.put(dtreeJ48, 0);
		opindexMap.put(bicluster, 1);
		opindexMap.put(glcm, 2);
		opindexMap.put(gray, 0);
		opindexMap.put(blur, 1);
		opindexMap.put(upscale, 2);
		opindexMap.put(blend, 0);
		opindexMap.put(merge, 1);
		opindexMap.put(mlpnet, 2);
		opindexMap.put(svm, 3);
		
	}
	
	public static int getOpCodeId(String Operation) {
		return opcodeMap.get(Operation);
	}
	
	public static int getOperationType(String Operation) {
		return optypeMap.get(Operation);
	}
	
	public static int getOperationIndex(String operation) {
		return opindexMap.get(operation);
	}
	
}
