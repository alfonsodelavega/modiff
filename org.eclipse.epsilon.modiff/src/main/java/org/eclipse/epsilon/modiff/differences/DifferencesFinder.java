package org.eclipse.epsilon.modiff.differences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;
import org.eclipse.epsilon.modiff.matcher.Matcher;

/**
 * Equality helper that compares the EObject features that are serialised into
 * the starting XML tag, i.e., EAttributes and non-containment EReferences
 */
public class DifferencesFinder extends EqualityHelper {

	private static final long serialVersionUID = 2L;
	protected Matcher matcher;

	public DifferencesFinder(Matcher matcher) {
		super();
		this.matcher = matcher;
	}

	public List<EStructuralFeature> getChangedFeatures(EObject fromElement, EObject toElement) {
		EClass eClass = fromElement.eClass();
		List<EStructuralFeature> changedFeatures = new ArrayList<>();

		for (int i = 0, size = eClass.getFeatureCount(); i < size; ++i) {
			EStructuralFeature feature = eClass.getEStructuralFeature(i);
			// Ignore derived features
			if (!feature.isDerived()) {
				if (!haveEqualFeature(fromElement, toElement, feature)) {
					changedFeatures.add(feature);
				}
			}
		}

		return changedFeatures;
	}

	/**
	 * Returns whether the two objects have equal matcher values for each position
	 * (does not check referenced objects)
	 */
	@SuppressWarnings("unchecked")
	protected boolean haveEqualReference(EObject eObject1, EObject eObject2, EReference reference) {
		Object value1 = eObject1.eGet(reference);
		Object value2 = eObject2.eGet(reference);

		if (reference.isMany()) {
			List<EObject> values1 = (List<EObject>) value1;
			List<EObject> values2 = (List<EObject>) value2;

			if (values1.size() != values2.size()) {
				return false;
			}
			for (int i = 0; i < values1.size(); i++) {
				if (!matcher.matches(values1.get(i), values2.get(i))) {
					return false;
				}
			}
		}
		else {
			return matcher.matches((EObject) value1, (EObject) value2);
		}

		return true;
	}
}
