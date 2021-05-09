package org.autodoc.interfaces.graphics;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;

import javax.swing.JFrame;

class FullScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	//private DisplayMode originalDM;
	private GraphicsDevice device;
	private boolean isFullScreen = false;

	public FullScreen(GraphicsDevice device) {
		super(device.getDefaultConfiguration());
		this.device = device;
		//originalDM = device.getDisplayMode();
		setLayout(null);
	}

	public void onFullScreen() {
		isFullScreen = device.isFullScreenSupported();
		setUndecorated(isFullScreen);
		setResizable(!isFullScreen);
		if (isFullScreen) {
			// Full-screen mode
			device.setFullScreenWindow(this);
			validate();
		} else {
			// Windowed mode
			pack();
			setVisible(true);
		}
	}

	public void changeDM(int width, int height, int bitdepth, int refreshrate) {
		if (device.isDisplayChangeSupported()) {
			DisplayMode dm = new DisplayMode(width, height, bitdepth,
					refreshrate);
			device.setDisplayMode(dm);
		}
	}
	
}
