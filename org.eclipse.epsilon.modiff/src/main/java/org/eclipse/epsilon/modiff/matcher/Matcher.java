package org.eclipse.epsilon.modiff.matcher;

import org.eclipse.emf.ecore.EObject;

public interface Matcher {

	public boolean matches(EObject element1, EObject element2);

	public String getIdentifier(EObject element);
}
