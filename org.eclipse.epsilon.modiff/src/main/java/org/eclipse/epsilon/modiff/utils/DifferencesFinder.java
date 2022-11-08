package org.eclipse.epsilon.modiff.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;

/**
 * Equality helper that compares the EObject features that are serialised into
 * the starting XML tag, i.e., EAttributes and non-containment EReferences
 */
public class DifferencesFinder extends EqualityHelper {

	private static final long serialVersionUID = 2L;

	public class TwoWayComparison {

		protected EObject fromObject;
		protected EObject toObject;

		protected List<EStructuralFeature> changedFeatures = new ArrayList<>();

		public TwoWayComparison(EObject fromObject, EObject toObject) {
			this.fromObject = fromObject;
			this.toObject = toObject;

			compare();
		}

		protected void compare() {
			EClass eClass = fromObject.eClass();

			for (int i = 0, size = eClass.getFeatureCount(); i < size; ++i) {
				EStructuralFeature feature = eClass.getEStructuralFeature(i);
				// Ignore derived features and containment references
				// TODO: decide what to do with cont. features ordering
				if (!feature.isDerived() &&
						!(feature instanceof EReference && ((EReference) feature).isContainment())) {

					if (!haveEqualFeature(fromObject, toObject, feature)) {
						changedFeatures.add(feature);
					}
				}
			}
		}

		public List<EStructuralFeature> getChangedFeatures() {
			return changedFeatures;
		}
	}

	public TwoWayComparison compare(EObject fromObject, EObject toObject) {
		return new TwoWayComparison(fromObject, toObject);
	}


	public boolean equals(EObject eObject1, EObject eObject2) {

		// If the first object is null, the second object must be null.
		if (eObject1 == null) {
			return eObject2 == null;
		}

		// We know the first object isn't null, so if the second one is, it can't be equal.
		if (eObject2 == null) {
			return false;
		}

		// If eObject1 and eObject2 are the same instance...
		if (eObject1 == eObject2) {
			return true;
		}

		// If eObject1 is a proxy...
		if (eObject1.eIsProxy()) {
			// Then the other object must be a proxy with the same URI.
			if (((InternalEObject) eObject1).eProxyURI().equals(((InternalEObject) eObject2).eProxyURI())) {
				return true;
			}
			else {
				return false;
			}
		}
		// If eObject1 isn't a proxy but eObject2 is, they can't be equal.
		else if (eObject2.eIsProxy()) {
			return false;
		}

		// If they don't have the same class, they can't be equal.
		EClass eClass = eObject1.eClass();
		if (eClass != eObject2.eClass()) {
			return false;
		}

		// Check attributes and non-containment references
		for (int i = 0, size = eClass.getFeatureCount(); i < size; ++i) {
			EStructuralFeature feature = eClass.getEStructuralFeature(i);
			// Ignore derived features and containment references
			if (!feature.isDerived() &&
					!(feature instanceof EReference && ((EReference) feature).isContainment())) {

				if (!haveEqualFeature(eObject1, eObject2, feature)) {
					return false;
				}
			}
		}

		// There's no reason they aren't equal, so they are.
		return true;
	}

	/**
	 * Returns whether the two objects have equal values for the
	 * reference (does not check referenced objects)
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
				if (values1.get(i) != values2.get(i)) {
					return false;
				}
			}
		}
		else {
			return value1 == value2;
		}

		return true;
	}
}
