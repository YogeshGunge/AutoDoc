package org.autodoc.interfaces.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

import org.autodoc.tools.HTMLWriter;

/**
*
* @author yogesh.gunge
*/
public class GraphView extends JComponent  {

	private static final long serialVersionUID = 1L;
	Image image;
	Graphics2D graphics2D;
	HTMLWriter codeGen;
	String text;
	
	public GraphView(String htmlFileName){
		setDoubleBuffered(false);
		codeGen = new HTMLWriter(htmlFileName);	
	}
	
	/*
	 * tool:			args:
	 * 		0 -> Line		X1, Y1, X2, Y2
	 * 		1 -> circle		X1, Y1, -, Y2
	 * 		2 -> Rectangle	X1, Y1, X2, Y2
	 * 		3 -> Text		X1, Y1, fontsize, maxChars, textContent
	 */
	public void drawShape(int tool, int x1, int y1, int x2, int y2) {
		drawShape(tool, x1, y1, x2, y2, "");
	}
	public void drawShape(int tool, int x1, int y1, int x2, int y2, String text) {
		if(graphics2D != null) {
			if(tool==0) {
					graphics2D.drawLine(x1, y1, x2, y2);
				}
			if(tool==1) {
					int H = Math.abs(y2-y1);
					graphics2D.draw(new Ellipse2D.Double(x1, y1, H, H));
				}
			if(tool==2) {
					int W = Math.abs(x2-x1);
					int H = Math.abs(y2-y1);
					graphics2D.drawRect(x1, y1, W, H);
				}
			if(tool==3) {
					graphics2D.setFont( new Font("Arial", Font.PLAIN, x2) );
					graphics2D.drawString(text,x1,y1);
					if(text.length()>y2) {
						text = text.substring(0, y2);
					}
					codeGen.generateCode(x1, y1, x2, text);
				}
			}
		repaint();
		codeGen.generateCode(tool,x1, y1, x2, y2);
	}
	
	public void paintComponent(Graphics g){
		if(image == null){
			image = createImage(getSize().width, getSize().height);
			graphics2D = (Graphics2D)image.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		g.drawImage(image, 0, 0, null);
	}
	
	public void clear(){
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setPaint(Color.black);
		repaint();
		codeGen.resetCode();
	}
	public void finish() {
		codeGen.generateCode();
	}
}
