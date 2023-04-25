package org.eclipse.epsilon.modiff.matcher;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class IdOrNameMatcher extends IdMatcher {

	@Override
	public String doGetIdentifier(EObject element) {
		String id = super.doGetIdentifier(element);
		if (id == null) {
			EStructuralFeature fName = element.eClass().getEStructuralFeature("name");

			if (fName != null) {
				id = (String) element.eGet(fName);
			}
		}
		return id;
	}
}
