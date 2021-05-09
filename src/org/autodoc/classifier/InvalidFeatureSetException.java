package org.autodoc.classifier;

public class InvalidFeatureSetException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public String getMessage() {
        return "Invalid Feature Set Entered.";
    }
	
}
