package org.eclipse.epsilon.modiff.differences;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.modiff.utils.DifferencesFinder;
import org.eclipse.epsilon.modiff.utils.PrettyPrint;

public class ChangedElement extends ModelDifference {

	protected EObject fromElement;
	protected EObject toElement;

	public ChangedElement(String identifier, EObject fromElement, EObject toElement) {
		super(identifier);
		this.fromElement = fromElement;
		this.toElement = toElement;
	}

	public EObject getFromElement() {
		return fromElement;
	}

	public EObject getToElement() {
		return toElement;
	}

	public List<EStructuralFeature> getChangedFeatures() {
		return new DifferencesFinder().compare(fromElement, toElement).getChangedFeatures();
	}

	public String toString() {
		return PrettyPrint.featureDiferences(
				identifier,
				toElement, "+ ",
				fromElement, "- ",
				getChangedFeatures(), "~ ");
	}
}
