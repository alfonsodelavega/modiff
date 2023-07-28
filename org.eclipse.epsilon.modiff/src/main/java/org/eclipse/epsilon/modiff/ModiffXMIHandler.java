package org.eclipse.epsilon.modiff;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;
import org.eclipse.epsilon.modiff.Modiff.DiffSide;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ModiffXMIHandler extends SAXXMIHandler {

	protected Modiff modiff;

	protected Set<Integer> modifiedLines;
	protected Set<EObject> modifiedElements;

	protected int currentLine;


	public ModiffXMIHandler(XMLResource xmlResource, XMLHelper helper, Map<?, ?> options,
			Modiff modiff, DiffSide diffSide) {

		super(xmlResource, helper, options);

		this.modiff = modiff;

		modifiedLines = modiff.getModifiedLines(diffSide);
		modifiedElements = modiff.getModifiedElements(diffSide);

		currentLine = 1;
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, name, attributes);

		if (isMarkedForReview(getActualPeekObject())) {
			modifiedElements.add(getActualPeekObject());
			return;
		}

		// start and end here mark the first and last lines of the started element's
		//   initial tag (not the whole element)
		int start = currentLine;
		int end = locator.getLineNumber();
		currentLine = end + 1;

		int lineNumber = start;
		EObject peekObject = objects.peekEObject();

		if (peekObject != null) {
			while (lineNumber <= end) {
				if (modifiedLines.contains(lineNumber)) {
					modifiedElements.add(peekObject);
				}
				lineNumber++;
			}
		}
		// a null stack head might be caused by the parsing of a multivalued attribute
		else if (isMultiValuedAttribute(types.peek())) {
			peekObject = getHiddenPeekObject();

			while (lineNumber <= end) {
				if (modifiedLines.contains(lineNumber)) {
					modifiedElements.add(peekObject);
					break;
				}
				lineNumber++;
			}

			// the other multivalued attribute might not have modifications.

			// to avoid wrongly detecting his as added or deleted, when 
			// it is actually a changed elem, we include it for later check.

			// we include it even if we detect no change in case this is the
			//   first model version we are checking (and changes are in the other).
			markForReview(peekObject);
		}
	}

	protected void markForReview(EObject peekObject) {
		if (peekObject == null) {
			throw new IllegalStateException("Peek object to be marked for review must not be null");
		}
		modiff.markForReview(peekObject);
	}

	protected boolean isMarkedForReview(EObject peekObject) {
		return modiff.isMarkedForReview(peekObject);
	}

	protected boolean isMultiValuedAttribute(Object feature) {
		return feature instanceof EAttribute &&
				helper.getFeatureKind((EAttribute) feature) == XMLHelper.DATATYPE_IS_MANY;
	}

	protected EObject getActualPeekObject() {
		EObject peekObject = objects.peekEObject();
		if (peekObject == null) {
			peekObject = getHiddenPeekObject();
		}
		return peekObject;
	}

	protected EObject getHiddenPeekObject() {
		EObject peekObject = objects.peekEObject();
		if (peekObject == null && objects.size() > 1) {
			objects.popEObject(); // remove the "null" at the top
			peekObject = objects.peekEObject();
			objects.push(null); // put it back
		}
		return peekObject;
	}

	@Override
	public void endElement(String uri, String localName, String name) {

		currentLine = locator.getLineNumber() + 1;
		super.endElement(uri, localName, name);
	}

	@Override
	public void endDocument() {
		super.endDocument();

		Iterator<Diagnostic> errorsIterator = xmlResource.getErrors().iterator();
		while (errorsIterator.hasNext()) {
			Diagnostic error = errorsIterator.next();
			
			System.err.println(error);
		}
	}
}
