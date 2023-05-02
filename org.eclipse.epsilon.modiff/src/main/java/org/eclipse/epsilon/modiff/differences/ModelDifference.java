package org.eclipse.epsilon.modiff.differences;


public abstract class ModelDifference {
	protected String identifier;

	public ModelDifference(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}
}
