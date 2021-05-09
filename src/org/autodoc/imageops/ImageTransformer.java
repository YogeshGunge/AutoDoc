package org.autodoc.imageops;

import java.util.ArrayList;

import org.autodoc.model.DataSet;

/*
 * Interface for Image Transformation Algorithms:
 * 1. Add / Subtract Images
 * 2. Multiply Images
 * 3. Convolution of Images
 * 4. Max-Pooling of Images
 * 5. Min-Pooling of Images
 * 6. Average-Pooling of Images
 */
/**
*
* @author yogesh.gunge
*/
public interface ImageTransformer {
	ArrayList<String> getParameterList();
	void setParameters( String params[] );
	DataSet transform( DataSet input1, DataSet input2 );
}
