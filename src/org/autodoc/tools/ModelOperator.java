package org.autodoc.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.autodoc.classifier.FeatureClassPredictor;
import org.autodoc.classifier.InvalidFeatureSetException;
import org.autodoc.classifier.LearningAlgorithms;
import org.autodoc.imageops.ImageAlgorithms;
import org.autodoc.imageops.ImageFeaturesGenerator;
import org.autodoc.imageops.ImagePreProcessor;
import org.autodoc.imageops.ImageTransformer;
import org.autodoc.interpreter.Architecture;
import org.autodoc.interpreter.InstructionDecoder;
import org.autodoc.interpreter.Model;
import org.autodoc.model.DataSet;
import org.autodoc.model.DataSetCollection;
import org.autodoc.model.Feature;
import org.autodoc.model.FeatureSet;
import org.autodoc.model.FeatureSetCollection;
import org.autodoc.model.Result;

public class ModelOperator {
	
	public static void initModel() {
		Model.initModel();
		ArrayList<String> dslist = DataSetCollection.getDataSetNameList();
		ArrayList<String> operands = new ArrayList<String>();
		operands.add(dslist.toString());
		Model.addInstruction(Architecture.load, operands);
	}
	
	public static void addOperation(String opcode, String inputs, String params) {
		ArrayList<String> operands = new ArrayList<String>();
		operands.add(inputs);
		operands.add(params);
		Model.addInstruction(opcode, operands);
	}
	
	public static void saveModel(String filepath) {
		File modelFile = new File(filepath);
		String data = "";
		InstructionDecoder idec = Model.createInstructionDecoderInstance();
		while(idec.hasNext()) {
			data += idec.getOpCode() + " " + idec.getOperandList()+"\n";
			idec.next();
		}
		FileOperator.write(modelFile, data);
	}
	
	public static void loadModel(String filepath) {
		Model.initModel();
		File modelFile = new File(filepath);
		String data = FileOperator.read(modelFile);
		StringTokenizer lineReader = new StringTokenizer(data, "\n");
		while(lineReader.hasMoreTokens()) {
			String line = lineReader.nextToken();
			String opcode = line.split(" ")[0];
			String operandsString = line.substring(opcode.length());
			ArrayList<String> operands = new ArrayList<String>();
			String datasets;
			if(operandsString.contains("], ")) {
				datasets = operandsString.substring(2, operandsString.length()-1).split("], ")[0]+"]";
			} else {
				datasets = operandsString.substring(2, operandsString.length()-1);
			}
			operands.add(datasets);
			if(operandsString.substring(2).split("], ").length == 1) {
				operands.add("");
			} else {
				String params = operandsString.substring(datasets.length()+2,operandsString.length()-1);
				if(!params.equals("")) {
					params = operandsString.substring(datasets.length()+4,operandsString.length()-1);
				}
				operands.add(params);
			}
			Model.addInstruction(opcode, operands);
		}
	}
	
	public static void executeModel() {
		InstructionDecoder idec = Model.createInstructionDecoderInstance();
		while(idec.hasNext()) {
			ArrayList<String> operandList = idec.getOperandList();
			String dsnames = operandList.get(0);
			String params = operandList.get(1);
			dsnames = dsnames.replace("[", " ").replace("]", " ").trim();
			params = params.replace("[", " ").replace("]", " ").trim();
			String datasets[] = dsnames.split(",");
			String[] paramValues = params.split(", ");
			String opcode = idec.getOpCode();
			int optype = Architecture.getOperationType(opcode);
			int opindex = Architecture.getOperationIndex(opcode);
			DataSet input, output;
			DataSet inputs[];
			switch(optype) {
			case 0:
				FileOperator.loadDataSet();
				break;
			case 1:
				ImagePreProcessor ipp = ImageAlgorithms.getImagePreProcessorAlgrithmInstance(opindex);
				ipp.setParameters(paramValues);
				input = DataSetCollection.getDataSetFromName(datasets[0]);
				output = ipp.preProcess(input);
				DataSetCollection.addDataSet(output);
				break;
			case 2:
				ImageTransformer ita = ImageAlgorithms.getImageTransformationAlgrithmInstance(opindex);
				ita.setParameters(paramValues);
				DataSet input1 = DataSetCollection.getDataSetFromName(datasets[0]);
				DataSet input2 = DataSetCollection.getDataSetFromName(datasets[1]);
				output = ita.transform(input1, input2);
				DataSetCollection.addDataSet(output);
				break;
			case 3:
				ImageFeaturesGenerator ifg = ImageAlgorithms.getFeaturesGeneratorAlgorithmInstance(opindex);
				ifg.setParameters(paramValues);
				inputs = new DataSet[datasets.length];
				for(int i=0; i<datasets.length; i++) {
					inputs[i] = DataSetCollection.getDataSetFromName(datasets[i]);
				}
				FeatureSet fs = ifg.generateFeatures(inputs);
				FeatureSetCollection.add(fs);
				FileOperator.saveFeatureSet(fs);
				break;
			case 4:
				FeatureClassPredictor fcp = LearningAlgorithms.getLearningAlgorithmInstance(opindex);
				try {
					fcp.setParameters(paramValues);
				} catch (InvalidFeatureSetException e) {
					ConsoleLogger.addLog(e.getMessage());
					return;
				}
				inputs = new DataSet[datasets.length];
				for(int i=0; i<datasets.length; i++) {
					inputs[i] = DataSetCollection.getDataSetFromName(datasets[i]);
				}
				fcp.loadModel();
				Feature result = fcp.useModel(inputs);
				Result.addResult(inputs, result);
			}
			idec.next();
		}
	}

}
