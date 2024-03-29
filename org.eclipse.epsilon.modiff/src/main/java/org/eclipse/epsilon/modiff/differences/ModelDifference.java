package org.eclipse.epsilon.modiff.differences;

import org.eclipse.epsilon.modiff.output.UnifiedDiffFormatter;

public abstract class ModelDifference {
	protected String identifier;

	public ModelDifference(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public abstract String format(UnifiedDiffFormatter formatter);
}
