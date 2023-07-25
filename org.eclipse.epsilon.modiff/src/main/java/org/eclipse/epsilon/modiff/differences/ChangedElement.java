package org.eclipse.epsilon.modiff.differences;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.output.UnifiedDiffFormatter;

public class ChangedElement extends ModelDifference {

	protected EObject fromElement;
	protected EObject toElement;
	protected List<EStructuralFeature> changedFeatures;
	protected Matcher matcher;

	public ChangedElement(Matcher matcher, EObject fromElement, EObject toElement) {
		super(matcher.getIdentifier(fromElement));
		this.fromElement = fromElement;
		this.toElement = toElement;
		this.matcher = matcher;
	}

	public EObject getFromElement() {
		return fromElement;
	}

	public EObject getToElement() {
		return toElement;
	}

	public List<EStructuralFeature> getChangedFeatures() {
		if (changedFeatures == null) {
			changedFeatures = new DifferencesFinder(matcher).getChangedFeatures(fromElement, toElement);
		}
		return changedFeatures;
	}

	public boolean hasDifferences() {
		return !getChangedFeatures().isEmpty();
	}

	@Override
	public String format(UnifiedDiffFormatter formatter) {
		return formatter.format(this);
	}
}
