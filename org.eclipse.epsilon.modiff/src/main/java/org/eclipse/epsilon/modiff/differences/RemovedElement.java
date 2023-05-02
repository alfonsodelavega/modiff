package org.eclipse.epsilon.modiff.differences;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.modiff.utils.PrettyPrint;

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
	public String toString() {
		return PrettyPrint.featuresMap(element, identifier, "- ");
	}
}
