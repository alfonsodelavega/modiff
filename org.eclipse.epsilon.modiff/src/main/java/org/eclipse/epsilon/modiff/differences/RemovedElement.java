package org.eclipse.epsilon.modiff.differences;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.modiff.output.UnifiedDiffFormatter;

public class RemovedElement extends ModelDifference {

	protected EObject element;

	public RemovedElement(String identifier, EObject element) {
		super(identifier);
		this.element = element;
	}

	public EObject getElement() {
		return element;
	}

	@Override
	public String format(UnifiedDiffFormatter formatter) {
		return formatter.format(this);
	}
}
