package org.autodoc.interfaces.graphics;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import org.autodoc.AutoDoc;
import org.autodoc.database.DBMetaData;
import org.autodoc.database.JDBC;
import org.autodoc.model.Result;
import org.autodoc.model.UserInfo;
import org.autodoc.resources.Colors;
import org.autodoc.resources.Locations;
import org.autodoc.resources.Settings;
import org.autodoc.resources.Strings;
import org.autodoc.resources.Diseases;
import org.autodoc.tools.ConsoleLogger;
import org.autodoc.tools.FileOperator;
import org.autodoc.tools.ModelOperator;


/**
*
* @author yogesh.gunge
*/
public class CheckupScreen extends FullScreen implements ActionListener, ItemListener {

    private static final long serialVersionUID = 1L;
    Container c;
	Label title = new Label(Strings.checkupScreen);
	Choice datasetChoice = new Choice();
	Choice dataChoice = new Choice();
	Label datasetLbl = new Label(Strings.testMsg);
	TextArea console = ConsoleLogger.getConsole();
	Button exit = new Button("Exit");
	Button upload = new Button("Upload Test Image");
	Button predict = new Button("Predict Diseases");
	boolean firstTime = false;
	JLabel testImageIcon = new JLabel();
	private static Connection con;
	private static Statement stmt;
    
	public CheckupScreen(GraphicsDevice gd) {
		super(gd);
		c = getContentPane();
		datasetLbl.setBounds(30, 60, 320, 20);
		datasetChoice.setBounds(300, 60, 150, 20);
		datasetChoice.addItem("nails");
		dataChoice.setBounds(500, 60, 150, 20);
		AutoDoc.collection = UserInfo.username + "/" + datasetChoice.getSelectedItem();
		File file1 = new File(Locations.dataPath + "/" + AutoDoc.mode + "/" + UserInfo.username);
		if(!file1.exists()) {
			firstTime = true;
			file1.mkdir();
			File file2 = new File(Locations.dataPath+"/"+AutoDoc.mode+"/"+AutoDoc.collection);
			if(!file2.exists()) {
				file2.mkdir();
			}
			String testFolders[] = Locations.testDataSets.split(",");
			for(String folderName : testFolders) {
				File testFolder = new File(Locations.dataPath+"/"+AutoDoc.mode+"/"+AutoDoc.collection+"/"+folderName);
				dataChoice.addItem(folderName);
				testFolder.mkdir();
			}
		}
		String testFolders[] = Locations.testDataSets.split(",");
		for(String folderName : testFolders) {
			dataChoice.addItem(folderName);
		}
		title.setBounds(0, 0, 1270, 45);
		title.setBackground(Colors.background);
		title.setForeground(Color.WHITE);
		title.setAlignment(Label.CENTER);
		exit.setBounds(30, 100, 260, 45);
		exit.setBackground(Colors.btnColor);
		exit.setForeground(Color.WHITE);
		upload.setBounds(30, 150, 260, 45);
		upload.setBackground(Colors.btnColor);
		upload.setForeground(Color.WHITE);
		predict.setBounds(30, 200, 260, 45);
		predict.setBackground(Colors.btnColor);
		predict.setForeground(Color.WHITE);
		testImageIcon.setBounds(300, 100, 500, 500);
		testImageIcon.setBackground(Color.gray);
		console.setBounds(30, 610, 1200, 120);
		console.setEditable(false);
				
		c.add(title);
		c.add(datasetLbl);
		c.add(datasetChoice);
		c.add(dataChoice);
		c.add(exit);
		c.add(upload);
		c.add(testImageIcon);
		c.add(console);
		c.add(predict);
		exit.addActionListener(this);
		upload.addActionListener(this);
		predict.addActionListener(this);
		
		UserInfo.uploadImageCount = 0;
		try {
			ResultSet rs;
			int userId;
			con = JDBC.connect();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from " + DBMetaData.imageTable + ";");
			while (rs.next()) {
				userId = rs.getInt(DBMetaData.loginUserId);
				if (userId == UserInfo.userId) {
					UserInfo.uploadImageCount ++;
				}
			}
			stmt.close();
		} catch (Exception e) {
			System.out.println("Error=" + e);
		}
		ConsoleLogger.addLog("Welcome, "+UserInfo.username+", you have given "+UserInfo.uploadImageCount+" test images");
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if (ae.getSource() == exit) {
			dispose();
		}
		if (ae.getSource() == upload) {
			if( dataChoice.getSelectedItem() == null ) {
				ConsoleLogger.addLog("Please select dataset first.");
				return;
			}
			JFileChooser chooser = new JFileChooser();
	    	int opt = chooser.showOpenDialog(this);
	    	if(opt == JFileChooser.APPROVE_OPTION)
	    	{
	    		File f = chooser.getSelectedFile();
	    		String path = f.toString();
	    		testImageIcon.setIcon(new ImageIcon(path));
	    		String newImageName = UserInfo.userId
	    				+ "." + (UserInfo.uploadImageCount+1);
	    		String newPath = FileOperator.uploadTestImage(path, dataChoice.getSelectedItem() + "/" + newImageName + Settings.imageExt);
	    		String csvFileName = Locations.dataPath+"/"+AutoDoc.mode+"/"+AutoDoc.collection+"/"+dataChoice.getSelectedItem()+".csv";
	    		FileOperator.append(new File(csvFileName), newImageName+",0\n");
	    		UserInfo.uploadImageCount ++;
	    		try {
					con = JDBC.connect();
					stmt = con.createStatement();
					String Query = "INSERT INTO " + DBMetaData.imageTable + " (USER_ID, IMAGE_PATH) VALUES (";
					Query += UserInfo.userId + ",";
					Query += "\'" + newPath + "\')";
					System.out.println(Query);
					stmt.executeUpdate(Query);
					stmt.close();
		    		ConsoleLogger.addLog("Uploaded Image "+path+" to your account.");
				} catch (Exception e) {
					System.out.println("Error=" + e);
				}
	    	}
		}
		if(ae.getSource() == predict) {
			if( dataChoice.getSelectedItem() == null ) {
				ConsoleLogger.addLog("Please select dataset first.");
				return;
			}
			JFileChooser chooser = new JFileChooser();
	    	int opt = chooser.showOpenDialog(this);
	    	if(opt == JFileChooser.APPROVE_OPTION)
	    	{
	    		File f = chooser.getSelectedFile();
	    		Result.initResults();
	    		String path = f.toString();
	    		ModelOperator.loadModel(path);
	    		ModelOperator.executeModel();
	    		String imageId = UserInfo.userId + "." + UserInfo.uploadImageCount;
	    		String classId = Result.getResult().get(imageId);
	    		ConsoleLogger.addLog("Result: \n"+Diseases.getDescription(classId));
	    	}
		}
	}

}