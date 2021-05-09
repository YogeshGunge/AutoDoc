package org.autodoc.imageops;

import org.autodoc.interpreter.Architecture;

/**
*
* @author yogesh.gunge
*/
public class ImageAlgorithms {
	
	public static String[] preProcessingAlgos;
	public static String[] transformAlgos;
	public static String[] featureGenerateAlgos;
	
	static {
		preProcessingAlgos = new String[3];
		preProcessingAlgos[0] = Architecture.gray;
		preProcessingAlgos[1] = Architecture.blur;
		preProcessingAlgos[2] = Architecture.upscale;
		transformAlgos = new String[1];
		transformAlgos[0] = Architecture.blend;
		featureGenerateAlgos = new String[3];
		featureGenerateAlgos[0] = Architecture.average;
		featureGenerateAlgos[1] = Architecture.bicluster;
		featureGenerateAlgos[2] = Architecture.glcm;
	}
	
	public static ImageFeaturesGenerator getFeaturesGeneratorAlgorithmInstance(int index) {
		ImageFeaturesGenerator ifg = null;
		switch(index) {
		case 0:
			ifg = new FeatureAverageColorGenerator();
			break;
		case 1:
			ifg = new FeatureBiClusterColorGenerator();
			break;
		case 2:
			ifg = new FeatureTextureGenerator();
			break;
		}
		return ifg;
	}
	
	public static ImagePreProcessor getImagePreProcessorAlgrithmInstance(int index) {
		ImagePreProcessor ipp = null;
		switch(index) {
		case 0:
			ipp = new ConvertGrayScale();
			break;
		case 1:
			ipp = new ConvertImageBlur();
			break;
		case 2:
			ipp = new ConvertUpScale();
			break;
		}
		return ipp;
	}
	
	public static ImageTransformer getImageTransformationAlgrithmInstance(int index) {
		ImageTransformer ita = null;
		switch(index) {
		case 0:
			ita = new BlendImages();
			break;
		}
		return ita;
	}

}