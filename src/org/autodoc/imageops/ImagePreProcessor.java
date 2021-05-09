package org.autodoc.imageops;

import java.util.ArrayList;

import org.autodoc.model.DataSet;

/*
 * Interface for Image Pre-processing Algorithms:
 * 1. Resize Image
 * 2. GrayScale Image
 * 3. Blur Image
 * 4. Sharpen Image
 * 5. Rotate Image
 * 6. Crop Image
 * 7. Threshold/Cluster Image
 * 8. Apply Filter on Image
 */
/**
*
* @author yogesh.gunge
*/
public interface ImagePreProcessor {
	ArrayList<String> getParameterList();
	void setParameters(String params[]);
	DataSet preProcess( DataSet input );
}
