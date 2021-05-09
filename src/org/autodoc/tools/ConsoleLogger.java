package org.autodoc.tools;

import java.awt.TextArea;

public class ConsoleLogger {
	
	private static TextArea console;
	
	public static TextArea getConsole() {
		if(console == null) {
			console = new TextArea();
		}
		return console;
	}
	
	public static void addLog(String lines) {
		String oldLog = console.getText();
		String newLog = oldLog + lines + "\n";
		console.setText(newLog);
	}

}
