package org.autodoc.tools;

import java.util.ArrayList;
import java.util.HashMap;

import org.autodoc.interfaces.graphics.GraphView;
import org.autodoc.interpreter.InstructionDecoder;
import org.autodoc.interpreter.Model;

/*
 * Generates Graph For Given Model
 */

public class GraphGenerator {
	private static GraphView gv;
	
	public static GraphView getGraphView(String htmlFileName) {
		if(gv == null) {
			gv = new GraphView(htmlFileName);
		}
		return gv;
	}
	
	public static void initGraph() {
		gv.clear();
	}
	
	public static void refreshGraph() {
		int drawY = 0;
		HashMap<String,Point> dsPtMap = new HashMap<String,Point>();
		InstructionDecoder idec = Model.createInstructionDecoderInstance();
		gv.clear();
		String dsname, fsname;
		Point dsPoint;
		while(idec.hasNext()) {
			ArrayList<String> operandList = idec.getOperandList();
			String inputs = operandList.get(0);
			inputs = inputs.replace("[", " ").replace("]", " ").trim();
			String datasets[] = inputs.split(",");
			int opid = idec.getOpCodeId();
			int drawX = 0;
			switch(opid) {
				case 1:
					// Load Datasets
					drawX = 0;
					for(int index = 0; index< datasets.length; index++) {
						dsname = datasets[index];
						gv.drawShape(2, drawX+5, drawY+5, drawX+95, drawY+25); // Rectangle
						gv.drawShape(3, drawX+10, drawY+18, 10, 18, dsname); // Text
						dsPtMap.put(dsname, new Point(drawX+50,drawY+25));
						drawX += 100;
					}
					drawY += 50;
					break;
				case 2:
					// Extract Average Color From Image
					drawX = 0;
					for(int index = 0; index< datasets.length; index++) {
						dsname = datasets[index];
						dsPoint = dsPtMap.get(dsname);
						if(dsPoint != null) {
							gv.drawShape(0, dsPoint.x, dsPoint.y, drawX+50, drawY); // Line
						}
					}
					gv.drawShape(1, drawX+40, drawY, drawX+65, drawY+20); // Circle
					gv.drawShape(3, drawX+43, drawY+13, 10, 18, "col"); // Text
					gv.drawShape(0, drawX+50, drawY+20, drawX+50, drawY+40); // Line
					gv.drawShape(2, drawX+5, drawY+40, drawX+95, drawY+60); // Rectangle
					fsname = operandList.get(1).replace("["," ").trim().split(",")[0];
					gv.drawShape(3, drawX+10, drawY+53, 10, 18, fsname); // Text
					dsPtMap.put(fsname, new Point(drawX+50,drawY+60));
					drawY += 80;
					break;
				case 3:
					// Classify using J48 classifier
					drawX = 0;
					dsname = operandList.get(1);
					dsname = dsname.replace("[", " ").replace("]", " ")
							.trim().split(",")[1].trim();
					dsPoint = dsPtMap.get(dsname);
					if(dsPoint != null) {
						gv.drawShape(0, dsPoint.x, dsPoint.y, drawX+50, drawY); // Line
					}
					gv.drawShape(1, drawX+40, drawY, drawX+65, drawY+20); // Circle
					gv.drawShape(3, drawX+43, drawY+13, 10, 18, "j48"); // Text
					gv.drawShape(0, drawX+50, drawY+20, drawX+50, drawY+40); // Line
					gv.drawShape(2, drawX+5, drawY+40, drawX+95, drawY+60); // Rectangle
					String result = operandList.get(1).replace("["," ").trim().split(",")[0];
					gv.drawShape(3, drawX+10, drawY+53, 10, 18, result); // Text
					dsPtMap.put(result, new Point(drawX+50,drawY+60));
					drawY += 80;
					break;
				case 4:
					// Extract Two Cluster of Colors From Image
					drawX = 0;
					for(int index = 0; index< datasets.length; index++) {
						dsname = datasets[index];
						dsPoint = dsPtMap.get(dsname);
						if(dsPoint != null) {
							gv.drawShape(0, dsPoint.x, dsPoint.y, drawX+50, drawY); // Line
						}
					}
					gv.drawShape(1, drawX+40, drawY, drawX+65, drawY+20); // Circle
					gv.drawShape(3, drawX+43, drawY+13, 10, 18, "2_c"); // Text
					gv.drawShape(0, drawX+50, drawY+20, drawX+50, drawY+40); // Line
					gv.drawShape(2, drawX+5, drawY+40, drawX+95, drawY+60); // Rectangle
					fsname = operandList.get(1).replace("["," ").trim().split(",")[0];
					gv.drawShape(3, drawX+10, drawY+53, 10, 18, fsname); // Text
					dsPtMap.put(fsname, new Point(drawX+50,drawY+60));
					drawY += 80;
					break;
				case 5:
					// Extract GLCM Texture From Image
					drawX = 0;
					for(int index = 0; index< datasets.length; index++) {
						dsname = datasets[index];
						dsPoint = dsPtMap.get(dsname);
						if(dsPoint != null) {
							gv.drawShape(0, dsPoint.x, dsPoint.y, drawX+50, drawY); // Line
						}
					}
					gv.drawShape(1, drawX+40, drawY, drawX+65, drawY+20); // Circle
					gv.drawShape(3, drawX+43, drawY+13, 10, 18, "txt"); // Text
					gv.drawShape(0, drawX+50, drawY+20, drawX+50, drawY+40); // Line
					gv.drawShape(2, drawX+5, drawY+40, drawX+95, drawY+60); // Rectangle
					fsname = operandList.get(1).replace("["," ").replace("]"," ").trim().split(",")[0];
					gv.drawShape(3, drawX+10, drawY+53, 10, 18, fsname); // Text
					dsPtMap.put(fsname, new Point(drawX+50,drawY+60));
					drawY += 80;
					break;
				case 6:
					// Convert to GrayScale
					drawX = 0;
					for(int index = 0; index< datasets.length; index++) {
						dsname = datasets[index];
						dsPoint = dsPtMap.get(dsname);
						if(dsPoint != null) {
							gv.drawShape(0, dsPoint.x, dsPoint.y, drawX+50, drawY); // Line
						}
					}
					gv.drawShape(1, drawX+40, drawY, drawX+65, drawY+20); // Circle
					gv.drawShape(3, drawX+43, drawY+13, 10, 18, "gry"); // Text
					gv.drawShape(0, drawX+50, drawY+20, drawX+50, drawY+40); // Line
					gv.drawShape(2, drawX+5, drawY+40, drawX+95, drawY+60); // Rectangle
					fsname = operandList.get(1).replace("["," ").replace("]"," ").trim().split(",")[0];
					gv.drawShape(3, drawX+10, drawY+53, 10, 18, fsname); // Text
					dsPtMap.put(fsname, new Point(drawX+50,drawY+60));
					drawY += 80;
					break;
				case 7:
					// Convert to Blur
					drawX = 0;
					for(int index = 0; index< datasets.length; index++) {
						dsname = datasets[index];
						dsPoint = dsPtMap.get(dsname);
						if(dsPoint != null) {
							gv.drawShape(0, dsPoint.x, dsPoint.y, drawX+50, drawY); // Line
						}
					}
					gv.drawShape(1, drawX+40, drawY, drawX+65, drawY+20); // Circle
					gv.drawShape(3, drawX+43, drawY+13, 10, 18, "blr"); // Text
					gv.drawShape(0, drawX+50, drawY+20, drawX+50, drawY+40); // Line
					gv.drawShape(2, drawX+5, drawY+40, drawX+95, drawY+60); // Rectangle
					fsname = operandList.get(1).replace("["," ").replace("]"," ").trim().split(",")[0];
					gv.drawShape(3, drawX+10, drawY+53, 10, 18, fsname); // Text
					dsPtMap.put(fsname, new Point(drawX+50,drawY+60));
					drawY += 80;
					break;
				case 8:
					// Code To UpScale Images
					drawX = 0;
					for(int index = 0; index< datasets.length; index++) {
						dsname = datasets[index];
						dsPoint = dsPtMap.get(dsname);
						if(dsPoint != null) {
							gv.drawShape(0, dsPoint.x, dsPoint.y, drawX+50, drawY); // Line
						}
					}
					gv.drawShape(1, drawX+40, drawY, drawX+65, drawY+20); // Circle
					gv.drawShape(3, drawX+43, drawY+13, 10, 18, "res"); // Text
					gv.drawShape(0, drawX+50, drawY+20, drawX+50, drawY+40); // Line
					gv.drawShape(2, drawX+5, drawY+40, drawX+95, drawY+60); // Rectangle
					fsname = operandList.get(1).replace("["," ").replace("]"," ").trim().split(",")[0];
					gv.drawShape(3, drawX+10, drawY+53, 10, 18, fsname); // Text
					dsPtMap.put(fsname, new Point(drawX+50,drawY+60));
					drawY += 80;
					break;
			}
			idec.next();
		}
	}
	
	public static void finishGraph() {
		gv.finish();
	}
	
}

class Point {
	public int x;
	public int y;
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}