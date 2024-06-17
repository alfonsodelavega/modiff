package org.eclipse.epsilon.modiff.emfcompare.munidiff.transformations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.munidiff.AddedElement;
import org.eclipse.epsilon.modiff.munidiff.ChangedElement;
import org.eclipse.epsilon.modiff.munidiff.Difference;
import org.eclipse.epsilon.modiff.munidiff.Munidiff;
import org.eclipse.epsilon.modiff.munidiff.MunidiffFactory;
import org.eclipse.epsilon.modiff.munidiff.RemovedElement;

public class EmfCompare2Munidiff {

	protected Matcher matcher;
	protected MunidiffFactory munidiffFactory = MunidiffFactory.eINSTANCE;

	public EmfCompare2Munidiff(Matcher matcher) {
		this.matcher = matcher;
	}

	public Munidiff transform(Comparison comparison) {
		
		List<Difference> differences = new ArrayList<>();
		fillDifferences(differences, comparison.getMatches());

		Munidiff munidiff = munidiffFactory.createMunidiff();

		munidiff.setFromModelFile(comparison.getMatchedResources().get(0).getRightURI());
		munidiff.setToModelFile(comparison.getMatchedResources().get(0).getLeftURI());
		munidiff.addDifferences(differences);

		return munidiff;
	}

	protected AddedElement createAddedElement(String identifier, EObject element) {
		AddedElement added = munidiffFactory.createAddedElement();

		added.setIdentifier(matcher.getIdentifier(element));
		added.setElement(element);

		return added;
	}

	protected RemovedElement createRemovedElement(String identifier, EObject element) {
		RemovedElement removed = munidiffFactory.createRemovedElement();

		removed.setIdentifier(matcher.getIdentifier(element));
		removed.setElement(element);

		return removed;
	}

	protected ChangedElement createChangedElement(EObject removedElement, EObject addedElement, Matcher matcher) {
		ChangedElement changed = munidiffFactory.createChangedElement();

		changed.setIdentifier(matcher.getIdentifier(removedElement));
		changed.setFromElement(removedElement);
		changed.setToElement(addedElement);
		changed.setMatcher(matcher);

		return changed;
	}

	protected void fillDifferences(List<Difference> differences, EList<Match> matches) {
		for (Match m : matches) {
			if (m.getLeft() == null) {
				differences.add(createRemovedElement(matcher.getIdentifier(m.getRight()), m.getRight()));
			}
			else if (m.getRight() == null) {
				differences.add(createAddedElement(matcher.getIdentifier(m.getLeft()), m.getLeft()));
			}
			else {
				// changedElement
				ChangedElement changedElement = createChangedElement(m.getRight(), m.getLeft(), matcher);
				if (changedElement.hasDifferences()) {
					differences.add(changedElement);
				}

			}
			fillDifferences(differences, m.getSubmatches());
		}
	}
}
