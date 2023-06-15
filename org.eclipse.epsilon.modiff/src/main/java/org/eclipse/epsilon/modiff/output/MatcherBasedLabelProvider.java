package org.eclipse.epsilon.modiff.output;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.modiff.matcher.Matcher;

public class MatcherBasedLabelProvider implements LabelProvider {

	protected Matcher matcher;

	public MatcherBasedLabelProvider(Matcher matcher) {
		this.matcher = matcher;
	}

	@Override
	public String getLabel(EObject obj) {
		return String.format("%s \"%s\"",
				obj.eClass().getName(), matcher.getIdentifier(obj));
	}
}
