package org.eclipse.epsilon.modiff.matcher;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.NamedElement;

public class UmlMatcher extends IdMatcher implements Matcher {


	@Override
	protected String doGetIdentifier(EObject element) {

		String result = super.doGetIdentifier(element);
		if (result == null) {
			// Uses fully qualified name.
			if (element instanceof NamedElement) {
				NamedElement namedElement = (NamedElement) element;

				StringBuilder qualifiedName = new StringBuilder();
				org.eclipse.uml2.uml.Package packageElement = namedElement.getNearestPackage();

				while (packageElement != null) {
					qualifiedName.insert(0, packageElement.getName() + ".");
					packageElement = packageElement.getNestingPackage();
				}

				qualifiedName.append(namedElement.getName());
				result = qualifiedName.toString();
			}
		}
		return result;
	}
}
