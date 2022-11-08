package org.eclipse.epsilon.modiff.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;

public class IDCache {

	protected Map<IDKey, String> cache = new HashMap<>();
	
	class IDKey {

		// TODO: we might be able to simplify this to use only eobjects
		protected XMLResource resource;
		protected EObject eObj;

		public IDKey(XMLResource resource, EObject eObj) {
			this.resource = resource;
			this.eObj = eObj;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(eObj, resource);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			IDKey other = (IDKey) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return eObj == other.eObj && resource == other.resource;
		}

		private IDCache getEnclosingInstance() {
			return IDCache.this;
		}

	}

	public String getAvailableID(EObject eObj) {

		XMLResource resource = (XMLResource) eObj.eResource();
		IDKey key = new IDKey(resource, eObj);
		String ID = null;

		if (cache.containsKey(key)) {
			return cache.get(key);
		}
		else {
			ID = resource.getID(eObj); // XMI id
			if (ID == null) {
				ID = EcoreUtil.getID(eObj); // Ecore's id attribute
			}
			if (ID != null) {
				cache.put(key, ID);
			}
		}
		return ID;
	}
}
