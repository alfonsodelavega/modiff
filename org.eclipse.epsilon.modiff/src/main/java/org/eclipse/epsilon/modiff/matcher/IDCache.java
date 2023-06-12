package org.eclipse.epsilon.modiff.matcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;

public class IDCache {

	protected Map<IDKey, String> cache = new HashMap<>();
	
	class IDKey {

		protected EObject eObj;

		public IDKey(EObject eObj) {
			this.eObj = eObj;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(eObj);
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
			return eObj == other.eObj;
		}

		private IDCache getEnclosingInstance() {
			return IDCache.this;
		}

	}

	protected IDKey getKey(EObject element) {
		return new IDKey(element);
	}

	public String getId(EObject element) {
		return cache.get(getKey(element));
	}

	public void putId(EObject element, String id) {
		cache.put(getKey(element), id);
	}
}
