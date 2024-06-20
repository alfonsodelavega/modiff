package org.eclipse.epsilon.modiff.output.textual;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.modiff.munidiff.AddedElement;
import org.eclipse.epsilon.modiff.munidiff.ChangedElement;
import org.eclipse.epsilon.modiff.munidiff.Difference;
import org.eclipse.epsilon.modiff.munidiff.Munidiff;
import org.eclipse.epsilon.modiff.munidiff.RemovedElement;
import org.eclipse.epsilon.modiff.output.LabelProvider;
import org.eclipse.epsilon.modiff.output.MunidiffFormatter;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

/**
 * Model differences formatter that mimicks Unified Format
 */
public class UnifiedDiffFormatter extends MunidiffFormatter {

	/* Unified diff constants, not to be customised */

	public static final String HEADER_FROM_FILE = "--- ";
	public static final String HEADER_TO_FILE = "+++ ";
	public static final String HUNK_DELIMITER = "@@";

	public static final String ADD = "+";
	public static final String REMOVE = "-";
	public static final String COMMON = " ";

	public static final String NL = "\n";


	/* Model textual format constants, used through overridable methods */

	private static final String INDENT = "    ";

	private static final String ELEMENT_START = " {";
	private static final String ELEMENT_END = "}";

	private static final String MULTIVALUE_START = " [";
	private static final String MULTIVALUE_END = "]";

	private static final String ATTRIBUTE_DELIMITER = ": ";

	// reference delimiters come from PlantUML class diagram syntax
	private static final String NON_CONTAINMENT_REFERENCE_DELIMITER = " --> ";
	private static final String CONTAINMENT_REFERENCE_DELIMITER = " *--> ";


	protected String getIndent() {
		return INDENT;
	}

	protected String getIndent(int levels) {
		return getIndent().repeat(levels);
	}

	protected String getElementStart() {
		return ELEMENT_START;
	}

	protected String getElementEnd() {
		return ELEMENT_END;
	}

	protected String getMultivalueStart() {
		return MULTIVALUE_START;
	}

	protected String getMultivalueEnd() {
		return MULTIVALUE_END;
	}

	protected String getAttributeDelimiter() {
		return ATTRIBUTE_DELIMITER;
	}

	protected String getNonContainmentReferenceDelimiter() {
		return NON_CONTAINMENT_REFERENCE_DELIMITER;
	}

	protected String getContainmentReferenceDelimiter() {
		return CONTAINMENT_REFERENCE_DELIMITER;
	}

	public UnifiedDiffFormatter(Munidiff munidiff, LabelProvider labelProvider) {
		super(munidiff, labelProvider);
	}

	public String format() {
		StringBuilder s = new StringBuilder();

		List<Difference> differences = munidiff.getDifferences();

		if (!differences.isEmpty()) {
			s.append(HEADER_FROM_FILE).append(munidiff.getFromModelFile()).append(NL);
			s.append(HEADER_TO_FILE).append(munidiff.getToModelFile()).append(NL);
		}

		for (int i = 0; i < differences.size(); i++) {
			Difference d = differences.get(i);
			s.append(getHunkHeader()).append(NL);
			s.append(format(d));
			if (i < differences.size() - 1) {
				s.append(NL);
			}
		}

		return s.toString();
	}

	protected String getHunkHeader() {
		return HUNK_DELIMITER + " " + HUNK_DELIMITER;
	}

	protected String getLabel(EObject obj) {
		return labelProvider.getLabel(obj);
	}

	public String format(Difference d) {
		if (d instanceof AddedElement) {
			return format((AddedElement) d);
		}
		else if (d instanceof RemovedElement) {
			return format((RemovedElement) d);
		}
		else {
			return format((ChangedElement) d);
		}
	}

	public String format(AddedElement addedElement) {
		return featuresMap(addedElement.getElement(), addedElement.getIdentifier(), ADD);
	}

	public String format(RemovedElement removedElement) {
		return featuresMap(removedElement.getElement(), removedElement.getIdentifier(), REMOVE);
	}

	public String format(ChangedElement changedElement) {

		StringBuilder s = new StringBuilder();
		
		s.append(COMMON)
				.append(getLabel(changedElement.getFromElement()))
				.append(getElementStart());

		for (EStructuralFeature feat : changedElement.getChangedFeatures()) {
			s.append(NL);
			if (feat.isMany()) {
				appendMultiFeatureDifferences(s, feat, changedElement);
			}
			else {
				appendSingleFeature(s, ADD, feat,
						changedElement.getToElement(), changedElement.getIdentifier());
				s.append(NL);
				appendSingleFeature(s, REMOVE, feat,
						changedElement.getFromElement(), changedElement.getIdentifier());
			}
		}

		s.append(NL).append(COMMON).append(getElementEnd());

		return s.toString();
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

		s.append(prefix)
				.append(getLabel(obj))
				.append(getElementStart()).append(NL);

		for (EStructuralFeature feat : features) {
			if (obj.eIsSet(feat)) {
				appendFeature(s, prefix, feat, obj, objId);
				s.append(NL);
			}
		}

		s.append(prefix).append(getElementEnd());
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

		s.append(prefix).append(getIndent())
				.append(feat.getName()).append(getFeatureSeparator(feat))
				.append(getMultivalueStart())
				.append(NL);
		
		for (String value : values) {
			s.append(prefix).append(getIndent(2)).append(value).append(NL);
		}
		
		if (values.isEmpty()) {
			s.append(NL);
		}
		
		s.append(prefix).append(getIndent()).append(getMultivalueEnd());
	}

	protected String getFeatureSeparator(EStructuralFeature feat) {
		if (feat instanceof EAttribute) {
			return getAttributeDelimiter();
		}
		else {
			return getReferenceDelimiter((EReference) feat);
		}
	}
	protected List<String> getValues(EObject obj, EStructuralFeature feat) {

		if (feat instanceof EAttribute) {
			@SuppressWarnings("unchecked")
			List<Object> values = (List<Object>) obj.eGet(feat);

			return values.stream()
					.map(value -> String.valueOf(value))
					.collect(Collectors.toList());
		}
		else {
			@SuppressWarnings("unchecked")
			List<EObject> values = (List<EObject>) obj.eGet(feat);

			return values.stream()
					.map(value -> getLabel(value))
					.collect(Collectors.toList());
		}
	}

	protected void appendMultiFeatureDifferences(StringBuilder s,
			EStructuralFeature feat, ChangedElement changedElement) {

		List<String> fromValues = getValues(changedElement.getFromElement(), feat);
		List<String> toValues = getValues(changedElement.getToElement(), feat);

		Patch<String> patch = DiffUtils.diff(fromValues, toValues, true);

		s.append(COMMON).append(getIndent()).append(feat.getName()).append(getFeatureSeparator(feat))
				.append(getMultivalueStart()).append(NL);

		List<AbstractDelta<String>> deltas = patch.getDeltas();
		for (int i = 0; i < deltas.size(); i++) {

			AbstractDelta<String> delta = deltas.get(i);
			switch(delta.getType()) {
			case EQUAL:
				s.append(formatChunkLines(delta.getTarget().getLines(), COMMON));
				break;
			case INSERT:
				s.append(formatChunkLines(delta.getTarget().getLines(), ADD));
				break;
			case DELETE:
				s.append(formatChunkLines(delta.getSource().getLines(), REMOVE));
				break;
			case CHANGE:
				s.append(formatChunkLines(delta.getTarget().getLines(), ADD));
				s.append(NL);
				s.append(formatChunkLines(delta.getSource().getLines(), REMOVE));
			}
			
			// add extra new line between chunks (if not the last one)
			if (i < deltas.size() - 1) {
				s.append(NL);
			}
		}

		s.append(NL).append(COMMON).append(getIndent()).append(getMultivalueEnd());

	}

	protected String formatChunkLines(List<String> lines, String prefix) {
		return lines.stream()
				.map(line -> prefix + getIndent(2) + line)
				.collect(Collectors.joining(NL));
	}

	protected void appendSingleFeature(StringBuilder s, String ADD,
			EStructuralFeature feat, EObject obj, String objId) {

		if (feat instanceof EAttribute) {
			appendSingleAttribute(s, ADD, feat, obj);
		}
		else {
			appendSingleReference(s, ADD, (EReference) feat, obj, objId);
		}
	}

	protected void appendSingleAttribute(StringBuilder s, String prefix,
			EStructuralFeature feat, EObject obj) {

		s.append(prefix).append(getIndent())
				.append(feat.getName()).append(getAttributeDelimiter()).append(obj.eGet(feat));
	}

	protected void appendSingleReference(StringBuilder s, String prefix,
			EReference ref, EObject obj, String objId) {

		s.append(prefix).append(getIndent())
				.append(ref.getName()).append(getReferenceDelimiter(ref));

		EObject value = (EObject) obj.eGet(ref);
		if (value == null) {
			s.append("null");
		}
		else {
			s.append(getLabel(value));
		}
	}

	protected String getReferenceDelimiter(EReference ref) {
		if (ref.isContainment()) {
			return getContainmentReferenceDelimiter();
		}
		else {
			return getNonContainmentReferenceDelimiter();
		}
	}

	protected String typeAndId(EObject obj, String objId) {
		return String.format("%s \"%s\"", obj.eClass().getName(), objId);
	}
}
