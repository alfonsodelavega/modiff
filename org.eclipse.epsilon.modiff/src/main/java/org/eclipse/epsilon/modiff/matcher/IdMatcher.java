package org.eclipse.epsilon.modiff.matcher;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.epsilon.modiff.utils.IDCache;

public class IdMatcher implements Matcher {

	protected IDCache cache = new IDCache();

	@Override
	public boolean matches(EObject element1, EObject element2) {
		String id1 = getIdentifier(element1);
		return id1 != null ? id1.equals(getIdentifier(element2)) : false;
	}

	@Override
	public String getIdentifier(EObject element) {
		String id = cache.getId(element);
		if (id == null) {
			id = doGetIdentifier(element);
			if (id != null) {
				cache.putId(element, id);
			}
		}
		return id;
	}

	protected String doGetIdentifier(EObject element) {
		XMLResource resource = (XMLResource) element.eResource();
		String id = resource.getID(element); // XMI id
		if (id == null) {
			id = EcoreUtil.getID(element); // Ecore's id attribute
		}
		return id;
	}
}
