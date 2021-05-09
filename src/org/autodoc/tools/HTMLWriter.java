package org.autodoc.tools;

import java.io.*;

public class HTMLWriter {

	boolean resetFlag;
	String htmlFileName;

	public HTMLWriter(String filename) {
		htmlFileName = filename;
		resetCode();
	}

	public void generateCode(int tool, int arg1, int arg2, int arg3, int arg4) {
		resetFlag = false;
		switch (tool) {
		case 0:
			writeToFile("ctx.moveTo(" + arg1 + "," + arg2 + ");");
			writeToFile("ctx.lineTo(" + arg3 + "," + arg4 + ");");
			writeToFile("ctx.stroke();");
			break;
		case 1:
			int R = Math.abs(arg2 - arg4) / 2;
			writeToFile("ctx.moveTo(" + (arg1 + 2 * R) + "," + (arg2 + R)
					+ ");");
			writeToFile("ctx.arc(" + (arg1 + R) + "," + (arg2 + R) + "," + R
					+ ",0,2*Math.PI);");
			break;
		case 2:
			writeToFile("ctx.moveTo(" + arg1 + "," + arg2 + ");");
			writeToFile("ctx.lineTo(" + arg1 + "," + arg4 + ");");
			writeToFile("ctx.lineTo(" + arg3 + "," + arg4 + ");");
			writeToFile("ctx.lineTo(" + arg3 + "," + arg2 + ");");
			writeToFile("ctx.lineTo(" + arg1 + "," + arg2 + ");");
			writeToFile("ctx.stroke();");
			break;
		}
	}

	public void generateCode(int arg1, int arg2, int arg3, String arg5) {
		resetFlag = false;
		writeToFile("ctx.font='"+arg3+"px Arial';");
		writeToFile("ctx.fillText(\"" + arg5 + "\"," + arg1 + "," + arg2 + ");");
	}

	public void generateCode() {
		writeToFile("</script>");
		writeToFile("</body>");
		writeToFile("</html>");
	}

	private void writeToFile(String text) {
		try {
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(
							new File(htmlFileName)
							, true));
			bw.write(text);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			System.out.println("File Writing Exception!! : " + e);
		}
	}

	public void resetCode() {
		if (!resetFlag) {
			File f1 = new File(htmlFileName);
			if (f1.exists())
				f1.delete();
			writeToFile("<!DOCTYPE html>");
			writeToFile("<html>");
			writeToFile("<body>");
			writeToFile("<canvas id=\"myCanvas\" width=\"500\" height=\"500\" style=\"border:1px solid #d3d3d3;\">Your browser does not support the HTML5 canvas tag.</canvas>");
			writeToFile("<script>");
			writeToFile("var c=document.getElementById(\"myCanvas\");");
			writeToFile("var ctx=c.getContext(\"2d\");");
			resetFlag = true;
		}
	}
}
