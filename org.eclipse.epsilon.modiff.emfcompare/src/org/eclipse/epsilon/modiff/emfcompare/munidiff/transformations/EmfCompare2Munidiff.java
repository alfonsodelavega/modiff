package org.eclipse.epsilon.modiff.emfcompare.munidiff.transformations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.epsilon.modiff.differences.AddedElement;
import org.eclipse.epsilon.modiff.differences.ChangedElement;
import org.eclipse.epsilon.modiff.differences.ModelDifference;
import org.eclipse.epsilon.modiff.differences.Munidiff;
import org.eclipse.epsilon.modiff.differences.RemovedElement;
import org.eclipse.epsilon.modiff.matcher.IdMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.output.LabelProvider;
import org.eclipse.epsilon.modiff.output.MatcherBasedLabelProvider;

public class EmfCompare2Munidiff {

	protected Matcher matcher;
	protected LabelProvider labelProvider;

	public EmfCompare2Munidiff() {
		matcher = new IdMatcher();
		labelProvider = new MatcherBasedLabelProvider(matcher);
	}

	public Munidiff transform(Comparison comparison) {
		
		List<ModelDifference> differences = new ArrayList<>();
		fillDifferences(differences, comparison.getMatches());

		Munidiff munidiff = new Munidiff(differences, labelProvider);
		munidiff.setFromModelFile(comparison.getMatchedResources().get(0).getRightURI());
		munidiff.setToModelFile(comparison.getMatchedResources().get(0).getLeftURI());

		return munidiff;
	}

	protected void fillDifferences(List<ModelDifference> differences, EList<Match> matches) {
		for (Match m : matches) {
			if (m.getLeft() == null) {
				differences.add(new RemovedElement(matcher.getIdentifier(m.getRight()), m.getRight()));
			}
			else if (m.getRight() == null) {
				differences.add(new AddedElement(matcher.getIdentifier(m.getRight()), m.getRight()));
			}
			else {
				// changedElement
				ChangedElement changedElement = new ChangedElement(matcher, m.getRight(), m.getLeft());
				if (changedElement.hasDifferences()) {
					differences.add(changedElement);
				}

			}
			fillDifferences(differences, m.getSubmatches());
		}
	}
}
