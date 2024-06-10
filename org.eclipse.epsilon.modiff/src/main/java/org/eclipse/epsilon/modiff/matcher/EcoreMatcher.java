package org.eclipse.epsilon.modiff.matcher;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

public class EcoreMatcher extends IdMatcher {

	public static final String ID_SEP = ".";

	@Override
	protected String doGetIdentifier(EObject element) {
		String id = super.doGetIdentifier(element);
		if (id == null) {
			id = getEcoreId(element);
		}
		return id;
	}

	protected String getEcoreId(EObject element) {

		String id = null;

		if (element == null) {
		}
		else if (element instanceof ENamedElement) {
			id = getEcoreId((ENamedElement) element);
		}
		else if (element instanceof EAnnotation) {
			id = getEcoreId((EAnnotation) element);
		}
		else if (element instanceof EStringToStringMapEntryImpl) {
			id = getEcoreId((EStringToStringMapEntryImpl) element);
		}
		else {
			System.err.println("FIXME: " + element);
		}

		return id;
	}

	protected String getEcoreId(ENamedElement namedElement) {
		if (namedElement == null) {
			return null;
		}

		String result = namedElement.getName();
		
		String econtainerId = getEcoreId((ENamedElement) namedElement.eContainer());
		if (econtainerId != null) {
			result = econtainerId + ID_SEP + result;
		}
		
		return result;
	}

	protected String getEcoreId(EAnnotation annotation) {

		String econtainerId = getEcoreId(annotation.eContainer());
		if (econtainerId == null) {
			System.err.println("STRANGE: " + annotation);
		}

		return econtainerId + ID_SEP + annotation.getSource();
	}

	protected String getEcoreId(EStringToStringMapEntryImpl stringmap) {

		String econtainerId = getEcoreId(stringmap.eContainer());
		if (econtainerId == null) {
			System.err.println("STRANGE: " + stringmap);
		}

		return econtainerId + ID_SEP + stringmap.getKey();
	}
}
