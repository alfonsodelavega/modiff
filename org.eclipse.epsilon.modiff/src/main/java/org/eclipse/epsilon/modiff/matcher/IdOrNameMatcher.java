package org.eclipse.epsilon.modiff.matcher;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class IdOrNameMatcher extends IdMatcher {

	@Override
	public boolean matches(EObject element1, EObject element2) {
		if (super.matches(element1, element2)) {
			return true;
		}
		EStructuralFeature fName = element1.eClass().getEStructuralFeature("name");
		
		if (fName != null) {
			Object name1 = element1.eGet(fName);
			Object name2 = element2.eGet(fName);
			
			return name1 != null && name2 != null ? name1.equals(name2) : false;
		}
		return false;
	}
}
