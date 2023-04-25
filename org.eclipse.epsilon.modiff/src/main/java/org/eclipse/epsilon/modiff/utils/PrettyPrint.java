package org.eclipse.epsilon.modiff.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMIResource;

public class PrettyPrint {

	public static String featuresMap(EObject obj, String objId) {
		return featuresMap(obj, objId, "");
	}

	public static String featuresMap(EObject obj, String objId, String prefix) {
		return featuresMap(obj, objId,
				obj.eClass().getEAllStructuralFeatures()
						.stream()
						.filter(feat -> obj.eIsSet(feat))
						.collect(Collectors.toList()),
				prefix);
	}

	public static String featuresMap(EObject obj, String objId,
			List<EStructuralFeature> features, String prefix) {
		StringBuilder s = new StringBuilder();

		EClass eclass = obj.eClass();

		s.append(prefix)
				.append(eclass.getName()).append(" ").append(objId)
				.append(" {\n");

		for (EStructuralFeature feat : features) {
			if (obj.eIsSet(feat)) {
				s.append("\t");
				appendFeature(s, prefix, feat, obj);
				s.append("\n");
			}
		}

		s.append(prefix + "}");
		return s.toString();
	}

	public static String featureDiferences(
			EObject addedObj, String addedObjId, String addedPrefix,
			EObject removedObj, String removedObjId, String removedPrefix,
			List<EStructuralFeature> changedFeatures, String modifiedPrefix) {

		StringBuilder s = new StringBuilder();
		EClass eclass = addedObj.eClass();

		s.append(modifiedPrefix)
				.append(eclass.getName()).append(" ").append(addedObjId)
				.append(" {\n\t");

		for (EStructuralFeature feat : changedFeatures) {
			appendFeature(s, addedPrefix, feat, addedObj);
			s.append("\n\t");
			appendFeature(s, removedPrefix, feat, removedObj);
		}

		s.append("\n").append(modifiedPrefix).append("}");

		return s.toString();
	}

	private static void appendFeature(StringBuilder s, String addedPrefix, EStructuralFeature feat, EObject obj) {
		if (feat instanceof EAttribute) {
			appendAttribute(s, addedPrefix, feat, obj);
		}
		else {
			appendReference(s, addedPrefix, feat, obj);
		}
	}

	private static void appendAttribute(StringBuilder s, String prefix, EStructuralFeature feat, EObject obj) {
		s.append(prefix).append(feat.getName()).append(": ").append(obj.eGet(feat));
	}

	private static void appendReference(StringBuilder s, String prefix, EStructuralFeature feat, EObject obj) {
		s.append(prefix).append(feat.getName()).append(" -> ");

		if (!feat.isMany()) {
			s.append((EObject) obj.eGet(feat));
		}
		else {
			@SuppressWarnings("unchecked")
			List<EObject> values = (List<EObject>) obj.eGet(feat);
			s.append("[")
					.append(values.stream()
							.map(value -> typeAndId(value))
							.collect(Collectors.joining(", ")))
					.append("]");
		}
	}

	public static String typeAndId(EObject obj) {
		return obj.eClass().getName() + " " + ((XMIResource) obj.eResource()).getID(obj);
	}

	/**
	 * Adds the prefix to non-empty strings
	 */
	public static String prefix(String text, String prefix) {
		return format(text, prefix, "");
	}

	/**
	 * Adds prefix and suffix to non-empty strings
	 */
	public static String format(String text, String prefix, String suffix) {
		return "".equals(text) ? "" : prefix + text + suffix;
	}
}
