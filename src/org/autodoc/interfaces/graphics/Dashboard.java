package org.autodoc.interfaces.graphics;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.autodoc.AutoDoc;
import org.autodoc.classifier.FeatureClassPredictor;
import org.autodoc.classifier.InvalidFeatureSetException;
import org.autodoc.classifier.LearningAlgorithms;
import org.autodoc.imageops.ImageAlgorithms;
import org.autodoc.imageops.ImageFeaturesGenerator;
import org.autodoc.imageops.ImagePreProcessor;
import org.autodoc.imageops.ImageTransformer;
import org.autodoc.interpreter.Architecture;
import org.autodoc.model.DataSet;
import org.autodoc.model.DataSetCollection;
import org.autodoc.model.Feature;
import org.autodoc.model.FeatureSet;
import org.autodoc.model.FeatureSetCollection;
import org.autodoc.model.Result;
import org.autodoc.resources.Colors;
import org.autodoc.resources.Locations;
import org.autodoc.resources.Strings;
import org.autodoc.tools.ConsoleLogger;
import org.autodoc.tools.FileOperator;
import org.autodoc.tools.GraphGenerator;
import org.autodoc.tools.ModelOperator;

/**
*
* @author yogesh.gunge
*/
public class Dashboard extends FullScreen implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	Container c;
	GraphView dagView;
	// PadDraw drawPad;
	Label title = new Label(Strings.trainDashboard);
	Choice datasetChoice = new Choice();
	Label datasetLbl = new Label(Strings.trainMsg);
	TextArea console = ConsoleLogger.getConsole();
	Button load = new Button("Load Dataset");
	Button preprocess = new Button("Pre-Process Images");
	Button transform = new Button("Transform Images");
	Button features = new Button("Generate Features");
	Button classify = new Button("Classify Dataset");
	Button validate = new Button("Validate Results");
	Button save = new Button("Save Model");
	Button exit = new Button("Exit");
	Icon iconC = new ImageIcon("./res/c.png");
	Icon iconD = new ImageIcon("./res/d.png");
	Icon iconF = new ImageIcon("./res/f.png");
	Icon iconE = new ImageIcon("./res/e.png");
	Icon iconI = new ImageIcon("./res/i.png");
	
	public Dashboard(GraphicsDevice gd) {
		super(gd);
		c = getContentPane();
		datasetLbl.setBounds(30, 60, 320, 20);
		datasetChoice.setBounds(300, 60, 150, 20);
		datasetChoice.addItem("nails");
		AutoDoc.collection = datasetChoice.getSelectedItem();
		title.setBounds(0, 0, 1270, 45);
		title.setBackground(Colors.background);
		title.setForeground(Color.WHITE);
		title.setAlignment(Label.CENTER);
		exit.setBounds(30, 100, 260, 45);
		exit.setBackground(Colors.btnColor);
		exit.setForeground(Color.WHITE);
		console.setBounds(30, 610, 1200, 120);
		console.setEditable(false);
		load.setBounds(30, 150, 260, 45);
		load.setBackground(Colors.btnColor);
		load.setForeground(Color.WHITE);
		preprocess.setBounds(30, 200, 260, 45);
		preprocess.setBackground(Colors.btnColor);
		preprocess.setForeground(Color.WHITE);
		preprocess.setEnabled(false);
		transform.setBounds(30, 250, 260, 45);
		transform.setBackground(Colors.btnColor);
		transform.setForeground(Color.WHITE);
		transform.setEnabled(false);
		features.setBounds(30, 300, 260, 45);
		features.setBackground(Colors.btnColor);
		features.setForeground(Color.WHITE);
		features.setEnabled(false);
		classify.setBounds(30, 350, 260, 45);
		classify.setBackground(Colors.btnColor);
		classify.setForeground(Color.WHITE);
		classify.setEnabled(false);
		validate.setBounds(30, 400, 260, 45);
		validate.setBackground(Colors.btnColor);
		validate.setForeground(Color.WHITE);
		validate.setEnabled(false);
		save.setBounds(30, 450, 260, 45);
		save.setBackground(Colors.btnColor);
		save.setForeground(Color.WHITE);
		save.setEnabled(false);
		// drawPad = new PadDraw(Paths.dataPath+"/"+AutoDoc.mode+"/"+AutoDoc.collection+"/report.html");
		dagView = GraphGenerator.getGraphView(Locations.dataPath + "/" + AutoDoc.mode + "/" + AutoDoc.collection + "/report.html");
		dagView.setBounds(300, 100, 500, 500);
		//creates a new padDraw, which is pretty much the Build Canvas program
		
		c.add(dagView);
		//sets the padDraw in the center
		
/*		Panel panel = new Panel();
		panel.setBounds(800, 100, 100, 100);
		//creates a Panel
		panel.setPreferredSize(new Dimension(32, 68));
		panel.setMinimumSize(new Dimension(32, 68));
		panel.setMaximumSize(new Dimension(32, 68));
		//This sets the size of the panel
		
		Button clearButton = new Button("Clear");
		//creates the clear button and sets the text as "Clear"
		Button buildButton = new Button("Build");
		//creates the clear button and sets the text as "Build"
		
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.clear();
			}
		});
		//this is the clear button, which clears the screen.  This pretty
		//much attaches an action listener to the button and when the
		//button is pressed it calls the clear() method
		
		buildButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.finish();
			}
		});
		//this is the build button, which builds the HTML code.
		Icon iconL = new ImageIcon("./res/line.GIF");
		Icon iconE = new ImageIcon("./res/ellipse.GIF");
		Icon iconR = new ImageIcon("./res/rectangle.GIF");
		Icon iconT = new ImageIcon("./res/text.GIF");

		JButton lineButton = new JButton(iconL);
		lineButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.setTool(0);
			}
		});
		
		JButton circleButton = new JButton(iconE);
		circleButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.setTool(1);
			}
		});
		
		JButton rectangleButton = new JButton(iconR);
		rectangleButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.setTool(2);
			}
		});
		
		JButton textButton = new JButton(iconT);
		textButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.setTool(3);
			}
		});
		
		lineButton.setPreferredSize(new Dimension(16, 16));
		circleButton.setPreferredSize(new Dimension(16, 16));
		rectangleButton.setPreferredSize(new Dimension(16, 16));
		textButton.setPreferredSize(new Dimension(16, 16));
		//sets the sizes of the buttons
		
		panel.add(lineButton);
		panel.add(circleButton);
		panel.add(rectangleButton);
		panel.add(textButton);
		panel.add(clearButton);
		panel.add(buildButton);
		//adds the buttons to the panel
		
		c.add(panel);
		//sets the panel to the left
*/		
		
		c.add(datasetChoice);
		c.add(datasetLbl);
		c.add(load);
		c.add(preprocess);
		c.add(transform);
		c.add(features);
		c.add(classify);
		c.add(validate);
		c.add(exit);
		c.add(title);
		c.add(console);
		c.add(save);
		exit.addActionListener(this);
		load.addActionListener(this);
		preprocess.addActionListener(this);
		transform.addActionListener(this);
		features.addActionListener(this);
		classify.addActionListener(this);
		validate.addActionListener(this);
		datasetChoice.addItemListener(this);
		save.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource() == exit) {
			GraphGenerator.finishGraph();
			dispose();
		}
		
		if (ae.getSource() == load) {
			FileOperator.loadDataSet();
			ModelOperator.initModel();
			GraphGenerator.refreshGraph();
			load.setEnabled(false);
			preprocess.setEnabled(true);
			transform.setEnabled(true);
			features.setEnabled(true);
			classify.setEnabled(true);
		}
		
		if (ae.getSource() == preprocess) {
			if(ImageAlgorithms.preProcessingAlgos.length == 0) {
            	JOptionPane.showMessageDialog(this, Strings.noPreProcessMsg);
            	return;
			}
			int selection = JOptionPane.showOptionDialog(this
					, Strings.imgPreProcessMsg
					, Strings.project + " : " + Strings.trainDashboard
					, JOptionPane.DEFAULT_OPTION
					, JOptionPane.QUESTION_MESSAGE
					, iconI
					, ImageAlgorithms.preProcessingAlgos
					, ImageAlgorithms.preProcessingAlgos[0]);
			if(selection < 0)
				return;
			String opcode = ImageAlgorithms.preProcessingAlgos[selection];
			ArrayList<String> paramValuesList = new ArrayList<String>();
			ImagePreProcessor ipp = ImageAlgorithms.getImagePreProcessorAlgrithmInstance(selection);
			ArrayList<String> params = ipp.getParameterList();
			String[] paramValues = new String[params.size()];
			ListIterator<String> paramsIter = params.listIterator();
			int index = 0;
			while(paramsIter.hasNext()) {
				String param = paramsIter.next();
				String value = JOptionPane.showInputDialog(this
						, Strings.enterAlgoParams + param
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.QUESTION_MESSAGE);
				paramValues[index++] = value;
				paramValuesList.add(value);
			}
			ipp.setParameters(paramValues);
			ArrayList<String> dsnames = DataSetCollection.getDataSetNameList();
			String datasets[] = new String[dsnames.size()];
			for(int i = 0; i < dsnames.size(); i ++) {
				datasets[i] = dsnames.get(i);
			}
			selection = JOptionPane.showOptionDialog(this
					, Strings.dataSetSelectionMsg
					, Strings.project + " : " + Strings.trainDashboard
					, JOptionPane.DEFAULT_OPTION
					, JOptionPane.QUESTION_MESSAGE
					, iconD
					, datasets
					, datasets[0]);
			if(selection < 0)
				return;
			String dsname = datasets[selection];
			DataSet input = DataSetCollection.getDataSetFromName(dsname);
			DataSet output = ipp.preProcess(input);
			DataSetCollection.addDataSet(output);
			ModelOperator.addOperation(opcode, "[" + dsname + "]", paramValuesList.toString());
			GraphGenerator.refreshGraph();
		}
		
		if (ae.getSource() == transform) {
			if(ImageAlgorithms.transformAlgos.length == 0) {
            	JOptionPane.showMessageDialog(this, Strings.noImageTransformMsg);
            	return;
			}
			int selection = JOptionPane.showOptionDialog(this
					, Strings.imgTransformMsg
					, Strings.project + " : " + Strings.trainDashboard
					, JOptionPane.DEFAULT_OPTION
					, JOptionPane.QUESTION_MESSAGE
					, iconI
					, ImageAlgorithms.transformAlgos
					, ImageAlgorithms.transformAlgos[0]);
			if(selection < 0)
				return;
			String opcode = ImageAlgorithms.transformAlgos[selection];
			
			ArrayList<String> dsnamesList = new ArrayList<String>();
			ArrayList<String> paramValuesList = new ArrayList<String>();
			
			ImageTransformer ita = ImageAlgorithms.getImageTransformationAlgrithmInstance(selection);
			ArrayList<String> params = ita.getParameterList();
			String[] paramValues = new String[params.size()];
			ListIterator<String> paramsIter = params.listIterator();
			int index = 0;
			while(paramsIter.hasNext()) {
				String param = paramsIter.next();
				String value = JOptionPane.showInputDialog(this
						, Strings.enterAlgoParams + param
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.QUESTION_MESSAGE);
				paramValues[index++] = value;
				paramValuesList.add(value);
			}
			ita.setParameters(paramValues);
			
			ArrayList<DataSet> dsList = new ArrayList<DataSet>();
			selection = 1;
			int lastSelection = 0;
			ArrayList<String> dsnames = DataSetCollection.getDataSetNameList();
			String datasets[] = new String[dsnames.size()+1];
			datasets[0] = "Done";
			for(int i =0; i < dsnames.size(); i ++) {
				datasets[i+1] = dsnames.get(i);
			}
			while(selection != 0 && selection != lastSelection) {
				lastSelection = selection;
				selection = JOptionPane.showOptionDialog(this
						, Strings.dataSetSelectionMsg
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.DEFAULT_OPTION
						, JOptionPane.QUESTION_MESSAGE
						, iconD
						, datasets
						, datasets[0]);
				if(selection < 0)
					return;
				if(selection != 0) {
					String dsname = datasets[selection];
					dsnamesList.add(dsname);
					DataSet ds = DataSetCollection.getDataSetFromName(dsname);
					if(!dsList.contains(ds)) {
						dsList.add(ds);
					}
				}
			}
			if(dsList.size() == 0) {
				JOptionPane.showMessageDialog(this
						, Strings.errorNoDatasetSelected
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.ERROR_MESSAGE
						, iconE);
				return;
			}
			if(dsList.size() == 1) {
				JOptionPane.showMessageDialog(this
						, Strings.errorOnlyOneDatasetSelected
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.ERROR_MESSAGE
						, iconE);
				return;
			}
			DataSet inputs[] = new DataSet[dsList.size()];
			dsList.toArray(inputs);
			DataSet input1 = inputs[0];
			DataSet input2 = inputs[1];
			DataSet output = ita.transform(input1, input2);
			DataSetCollection.addDataSet(output);
			ModelOperator.addOperation(opcode, "[" + input1.getName() + "," + input2.getName() + "]", paramValuesList.toString());
			GraphGenerator.refreshGraph();
		}
		
		if (ae.getSource() == features) {
			if(ImageAlgorithms.featureGenerateAlgos.length == 0) {
            	JOptionPane.showMessageDialog(this, Strings.noFeatureGenerateMsg);
            	return;
			}
			int selection = JOptionPane.showOptionDialog(this
					, Strings.featureGenerateMsg
					, Strings.project + " : " + Strings.trainDashboard
					, JOptionPane.DEFAULT_OPTION
					, JOptionPane.QUESTION_MESSAGE
					, iconF
					, ImageAlgorithms.featureGenerateAlgos
					, ImageAlgorithms.featureGenerateAlgos[0]);
			if(selection < 0)
				return;
			String opcode = ImageAlgorithms.featureGenerateAlgos[selection];
			
			ArrayList<String> dsnamesList = new ArrayList<String>();
			ArrayList<String> paramValuesList = new ArrayList<String>();
			
			ImageFeaturesGenerator ifg = ImageAlgorithms.getFeaturesGeneratorAlgorithmInstance(selection);
			ArrayList<String> params = ifg.getParameterList();
			String[] paramValues = new String[params.size()];
			ListIterator<String> paramsIter = params.listIterator();
			int index = 0;
			while(paramsIter.hasNext()) {
				String param = paramsIter.next();
				String value = JOptionPane.showInputDialog(this
						, Strings.enterAlgoParams + param
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.QUESTION_MESSAGE);
				paramValues[index++] = value;
				paramValuesList.add(value);
			}
			ifg.setParameters(paramValues);

			ArrayList<DataSet> dsList = new ArrayList<DataSet>();
			selection = 1;
			int lastSelection = 0;
			ArrayList<String> dsnames = DataSetCollection.getDataSetNameList();
			String datasets[] = new String[dsnames.size()+1];
			datasets[0] = "Done";
			for(int i =0; i < dsnames.size(); i ++) {
				datasets[i+1] = dsnames.get(i);
			}
			while(selection != 0 && selection != lastSelection) {
				lastSelection = selection;
				selection = JOptionPane.showOptionDialog(this
						, Strings.dataSetSelectionMsg
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.DEFAULT_OPTION
						, JOptionPane.QUESTION_MESSAGE
						, iconD
						, datasets
						, datasets[0]);
				if(selection < 0)
					return;
				if(selection != 0) {
					String dsname = datasets[selection];
					dsnamesList.add(dsname);
					DataSet ds = DataSetCollection.getDataSetFromName(dsname);
					if(!dsList.contains(ds)) {
						dsList.add(ds);
					}
				}
			}
			if(dsList.size() == 0) {
				JOptionPane.showMessageDialog(this
						, Strings.errorNoDatasetSelected
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.ERROR_MESSAGE
						, iconE);
				return;
			}
			DataSet inputs[] = new DataSet[dsList.size()];
			dsList.toArray(inputs);
			FeatureSet fs = ifg.generateFeatures(inputs);
			FeatureSetCollection.add(fs);
			ModelOperator.addOperation(opcode, dsnamesList.toString(), paramValuesList.toString());
			FileOperator.saveFeatureSet(fs);
			GraphGenerator.refreshGraph();
			classify.setEnabled(true);
			preprocess.setEnabled(false);
			transform.setEnabled(false);
		}
		
		if (ae.getSource() == classify) {
			if(LearningAlgorithms.featureClassificationAlgos.length == 0) {
            	JOptionPane.showMessageDialog(this, Strings.noFeatureClassifierMsg);
            	return;
			}
			int selection = JOptionPane.showOptionDialog(this
					, Strings.featureClassifyMsg
					, Strings.project + " : " + Strings.trainDashboard
					, JOptionPane.DEFAULT_OPTION
					, JOptionPane.QUESTION_MESSAGE
					, iconC
					, LearningAlgorithms.featureClassificationAlgos
					, LearningAlgorithms.featureClassificationAlgos[0]);
			if(selection < 0)
				return;
			String opcode = LearningAlgorithms.featureClassificationAlgos[selection];
			
			ArrayList<String> dsnamesList = new ArrayList<String>();
			ArrayList<String> paramValuesList = new ArrayList<String>();
			FeatureClassPredictor fcp = LearningAlgorithms.getLearningAlgorithmInstance(selection);
			ArrayList<String> params = fcp.getParameterList();
			String[] paramValues = new String[params.size()];
			ListIterator<String> paramsIter = params.listIterator();
			int index = 0;
			while(paramsIter.hasNext()) {
				String param = paramsIter.next();
				String value = JOptionPane.showInputDialog(this
						, Strings.enterAlgoParams + param
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.QUESTION_MESSAGE);
				paramValues[index++] = value;
				paramValuesList.add(value);
			}
			try {
				fcp.setParameters(paramValues);
			} catch (InvalidFeatureSetException e) {
				JOptionPane.showMessageDialog(this
						, e.getMessage()
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.ERROR_MESSAGE
						, iconE);
				return;
			}
			ArrayList<DataSet> dsList = new ArrayList<DataSet>();
			selection = 1;
			int lastSelection = 0;
			ArrayList<String> dsnames = DataSetCollection.getDataSetNameList();
			String datasets[] = new String[dsnames.size()+1];
			datasets[0] = "Done";
			for(int i =0; i < dsnames.size(); i ++) {
				datasets[i+1] = dsnames.get(i);
			}
			while(selection != 0 && selection != lastSelection) {
				lastSelection = selection;
				selection = JOptionPane.showOptionDialog(this
						, Strings.dataSetSelectionMsg
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.DEFAULT_OPTION
						, JOptionPane.QUESTION_MESSAGE
						, iconD
						, datasets
						, datasets[0]);
				if(selection != 0) {
					String dsname = datasets[selection];
					dsnamesList.add(dsname);
					DataSet ds = DataSetCollection.getDataSetFromName(dsname);
					if(!dsList.contains(ds)) {
						dsList.add(ds);
					}
				}
			}
			if(dsList.size() == 0) {
				JOptionPane.showMessageDialog(this
						, Strings.errorNoDatasetSelected
						, Strings.project + " : " + Strings.trainDashboard
						, JOptionPane.ERROR_MESSAGE
						, iconE);
				return;
			}
			DataSet inputs[] = new DataSet[dsList.size()];
			dsList.toArray(inputs);
			try {
				fcp.trainModel(inputs);
			} catch(IllegalArgumentException e) {
				if(opcode.equals(Architecture.dtreeJ48)) {
					JOptionPane.showMessageDialog(this
							, Strings.errorMoreClassesThanAttributes
							, Strings.project + " : " + Strings.trainDashboard
							, JOptionPane.ERROR_MESSAGE
							, iconE);
				}
				return;
			}
			ModelOperator.addOperation(opcode, dsnamesList.toString(), paramValuesList.toString());
			fcp.saveModel();
			Feature result = fcp.useModel(inputs);
			Result.addResult(inputs, result);
			GraphGenerator.refreshGraph();
			validate.setEnabled(true);
			features.setEnabled(false);
		}
		
		if (ae.getSource() == validate) {
			int total = Result.getResultSize();
			int correct = Result.getCorrect();
			double accuracy = ((double) correct / (double) total) * 100;
			ConsoleLogger.addLog("Result has "+correct+" images correctly classified out of "+total+".");
			ConsoleLogger.addLog("Accuracy = "+accuracy);
			save.setEnabled(true);
		}
		
		if (ae.getSource() == save) {
			String modelFile = JOptionPane.showInputDialog(this
					, Strings.enterModelName
					, Strings.project + " : " + Strings.trainDashboard
					, JOptionPane.QUESTION_MESSAGE);
			ModelOperator.saveModel(Locations.dataPath + "/" + AutoDoc.mode + "/" + AutoDoc.collection + "/" + modelFile + ".model");
			ConsoleLogger.addLog("Model saved to file successfully.");
		}
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == datasetChoice) {
			AutoDoc.collection = datasetChoice.getSelectedItem();
		}
	}
}