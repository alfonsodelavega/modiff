package org.eclipse.epsilon.modiff.output;

import org.eclipse.emf.ecore.EObject;

public interface LabelProvider {

	public String getLabel(EObject obj);
}
