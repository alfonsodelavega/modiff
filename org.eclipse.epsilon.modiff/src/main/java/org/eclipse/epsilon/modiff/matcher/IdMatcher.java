package org.eclipse.epsilon.modiff.matcher;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.modiff.utils.IDCache;

public class IDBasedMatcher implements Matcher {

	protected IDCache cache = new IDCache();

	@Override
	public boolean matches(EObject element1, EObject element2) {
		String ID1 = cache.getAvailableID(element1);
		return ID1 != null ? ID1.equals(cache.getAvailableID(element2)) : false;
	}

}
