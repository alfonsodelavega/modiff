package org.eclipse.epsilon.modiff.output;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.differences.AddedElement;
import org.eclipse.epsilon.modiff.differences.ChangedElement;
import org.eclipse.epsilon.modiff.differences.ModelDifference;
import org.eclipse.epsilon.modiff.differences.RemovedElement;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

/**
 * Model differences formatter that mimicks GNU's Unified Format
 */
public class UnifiedFormatter {

	protected Modiff modiff;

	public UnifiedFormatter(Modiff modiff) {
		this.modiff = modiff;
	}

	public String format() {
		StringBuilder s = new StringBuilder();

		s.append("--- ").append(modiff.getFromModelFile()).append("\n");
		s.append("+++ ").append(modiff.getToModelFile()).append("\n");

		for (ModelDifference d : modiff.getDifferences()) {
			s.append("@@").append("\n");
			s.append(d.format(this)).append("\n");
		}

		return s.toString();
	}

	public String format(AddedElement addedElement) {
		return featuresMap(addedElement.getElement(), addedElement.getIdentifier(), "+ ");
	}

	public String format(RemovedElement removedElement) {
		return featuresMap(removedElement.getElement(), removedElement.getIdentifier(), "- ");
	}

	public String format(ChangedElement changedElement) {

		StringBuilder s = new StringBuilder();
		EClass eclass = changedElement.getFromElement().eClass();
		
		String addedPrefix = "+ ";
		String commonPrefix = "  ";
		String removedPrefix = "- ";

		s.append(commonPrefix)
				.append(eclass.getName())
				.append(" ")
				.append(changedElement.getIdentifier())
				.append(" {");

		for (EStructuralFeature feat : changedElement.getChangedFeatures()) {
			if (feat.isMany()) {
				s.append("\n\t");
				appendMultiFeatureDifferences(s, feat, changedElement);
			}
			else {
				s.append("\n\t");
				appendSingleFeature(s, addedPrefix, feat,
						changedElement.getToElement(), changedElement.getIdentifier());
				s.append("\n\t");
				appendSingleFeature(s, removedPrefix, feat,
						changedElement.getFromElement(), changedElement.getIdentifier());
			}
		}

		s.append("\n").append(commonPrefix).append("}");

		return s.toString();
	}

	public String featuresMap(EObject obj, String objId) {
		return featuresMap(obj, objId, "");
	}

	public String featuresMap(EObject obj, String objId, String prefix) {
		return featuresMap(obj, objId,
				obj.eClass().getEAllStructuralFeatures()
						.stream()
						.filter(feat -> obj.eIsSet(feat))
						.collect(Collectors.toList()),
				prefix);
	}

	public String featuresMap(EObject obj, String objId,
			List<EStructuralFeature> features, String prefix) {
		StringBuilder s = new StringBuilder();

		EClass eclass = obj.eClass();

		s.append(prefix)
				.append(eclass.getName()).append(" ").append(objId)
				.append(" {\n");

		for (EStructuralFeature feat : features) {
			if (obj.eIsSet(feat)) {
				s.append("\t");
				appendFeature(s, prefix, feat, obj, objId);
				s.append("\n");
			}
		}

		s.append(prefix + "}");
		return s.toString();
	}


	protected void appendFeature(StringBuilder s, String prefix,
			EStructuralFeature feat, EObject obj, String objId) {
		if (feat.isMany()) {
			appendMultiFeature(s, prefix, feat, obj, objId);
		}
		else {
			appendSingleFeature(s, prefix, feat, obj, objId);
		}
	}

	protected void appendMultiFeature(StringBuilder s, String prefix,
			EStructuralFeature feat, EObject obj, String objId) {

		List<String> values = getValues(obj, feat);

		s.append(prefix).append(feat.getName()).append(getFeatureSeparator(feat))
				.append("[")
				.append(values.stream().collect(Collectors.joining(", ")))
				.append("]");
	}

	protected String getFeatureSeparator(EStructuralFeature feat) {
		if (feat instanceof EAttribute) {
			return ": ";
		}
		else {
			return " -> ";
		}
	}
	protected List<String> getValues(EObject obj, EStructuralFeature feat) {
		@SuppressWarnings("unchecked")
		List<EObject> values = (List<EObject>) obj.eGet(feat);
		
		if (feat instanceof EAttribute) {
			return values.stream()
					.map(value -> String.valueOf(value))
					.collect(Collectors.toList());
		}
		else {
			return values.stream()
					.map(value -> typeAndId(value, modiff.getMatcher().getIdentifier(value)))
					.collect(Collectors.toList());
		}
	}

	protected void appendMultiFeatureDifferences(StringBuilder s,
			EStructuralFeature feat, ChangedElement changedElement) {

		List<String> fromValues = getValues(changedElement.getFromElement(), feat);
		List<String> toValues = getValues(changedElement.getToElement(), feat);

		Patch<String> patch = DiffUtils.diff(fromValues, toValues);

		s.append(feat.getName()).append(getFeatureSeparator(feat)).append("[\n");
		
		for (AbstractDelta<String> delta : patch.getDeltas()) {
			switch(delta.getType()) {
			case EQUAL:
				s.append(delta.getTarget().getLines().stream().map(line -> "\t\t  " + line + ",").collect(Collectors.joining("\n")));
				break;
			case INSERT:
				s.append(delta.getTarget().getLines().stream().map(line -> "\t\t+ " + line + ",").collect(Collectors.joining("\n")));
				break;
			case DELETE:
				s.append(delta.getSource().getLines().stream().map(line -> "\t\t- " + line + ",").collect(Collectors.joining("\n")));
				break;
			case CHANGE:
				s.append(delta.getTarget().getLines().stream().map(line -> "\t\t+ " + line + ",").collect(Collectors.joining("\n")));
				s.append("\n");
				s.append(delta.getSource().getLines().stream().map(line -> "\t\t- " + line + ",").collect(Collectors.joining("\n")));
				break;
			}
		}

		s.append("\n\t]");

	}

	protected void appendSingleFeature(StringBuilder s, String addedPrefix,
			EStructuralFeature feat, EObject obj, String objId) {

		if (feat instanceof EAttribute) {
			appendSingleAttribute(s, addedPrefix, feat, obj);
		}
		else {
			appendSingleReference(s, addedPrefix, feat, obj, objId);
		}
	}

	protected void appendSingleAttribute(StringBuilder s, String prefix,
			EStructuralFeature feat, EObject obj) {
		s.append(prefix).append(feat.getName()).append(": ").append(obj.eGet(feat));
	}

	protected void appendSingleReference(StringBuilder s, String prefix,
			EStructuralFeature feat, EObject obj, String objId) {

		s.append(prefix).append(feat.getName()).append(" -> ");

		EObject value = (EObject) obj.eGet(feat);
		if (value == null) {
			s.append("null");
		}
		else {
			s.append(typeAndId(value, modiff.getMatcher().getIdentifier(value)));
		}
	}

	protected String typeAndId(EObject obj, String objId) {
		return String.format("%s \"%s\"", obj.eClass().getName(), objId);
	}

	/**
	 * Adds the prefix to non-empty strings
	 */
	protected String prefix(String text, String prefix) {
		return preAndPostfix(text, prefix, "");
	}

	/**
	 * Adds prefix and suffix to non-empty strings
	 */
	protected String preAndPostfix(String text, String prefix, String suffix) {
		return "".equals(text) ? "" : prefix + text + suffix;
	}
}
