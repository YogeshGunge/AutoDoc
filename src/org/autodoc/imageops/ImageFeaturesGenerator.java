package org.autodoc.imageops;

import java.util.ArrayList;

import org.autodoc.model.DataSet;
import org.autodoc.model.FeatureSet;

/*
 * Interface for Image Feature Generation Algorithms:
 * 1. Extract Average Color
 * 2. Extract Shape Signal From A Direction
 * 3. Extract Texture of Image
 * 4. Extract Binary Stripes Texture
 * 5. Extract Local Spots Texture
 */

/**
*
* @author yogesh.gunge
*/
public interface ImageFeaturesGenerator {
	ArrayList<String> getParameterList();
	void setParameters( String params[] );
	FeatureSet generateFeatures( DataSet inputs[] );
}
