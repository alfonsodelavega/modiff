package org.eclipse.epsilon.modiff.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMIResource;

public class PrettyPrint {

	public static String featuresMap(EObject obj) {
		return featuresMap(obj, obj.eClass().getEAllStructuralFeatures(), "");
	}

	public static String featuresMap(EObject obj, String prefix) {
		return featuresMap(obj, obj.eClass().getEAllStructuralFeatures(), prefix);
	}

	public static String featuresMap(EObject obj, List<EStructuralFeature> features, String prefix) {
		StringBuilder s = new StringBuilder();

		EClass eclass = obj.eClass();
		String objId = ((XMIResource) obj.eResource()).getID(obj);

		s.append(eclass.getName()).append(" ").append(objId).append(" {\n\t" + prefix);
		s.append(features.stream()
				.filter(feat -> obj.eIsSet(feat))
				.map(feat -> {
					String result = feat.getName();
					if (feat instanceof EAttribute) {
						result += ": " + obj.eGet(feat);
					}
					else {
						result += " -> ";
						if (!feat.isMany()) {
							result += typeAndId((EObject) obj.eGet(feat));
						}
						else {
							@SuppressWarnings("unchecked")
							List<EObject> values = (List<EObject>) obj.eGet(feat);
							result += "[" + values.stream()
									.map(value -> typeAndId(value))
									.collect(Collectors.joining(", ")) + "]";
						}
					}
					return result;
				})
				.collect(Collectors.joining(",\n\t" + prefix)));
		s.append("\n" + prefix + "}");
		return s.toString();
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
