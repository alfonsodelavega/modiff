package org.eclipse.epsilon.modiff.matcher;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.modiff.utils.IDCache;

public class IdMatcher implements Matcher {

	protected IDCache cache = new IDCache();

	@Override
	public boolean matches(EObject element1, EObject element2) {
		String id1 = cache.getAvailableID(element1);
		return id1 != null ? id1.equals(cache.getAvailableID(element2)) : false;
	}

}
