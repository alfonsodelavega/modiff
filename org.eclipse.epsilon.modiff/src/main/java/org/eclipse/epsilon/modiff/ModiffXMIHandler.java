package org.eclipse.epsilon.modiff;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ModiffXMIHandler extends SAXXMIHandler {

	protected Set<Integer> modifiedLines;
	protected Set<EObject> modifiedElements;

	protected int currentLine;


	public ModiffXMIHandler(XMLResource xmlResource, XMLHelper helper, Map<?, ?> options,
			Set<Integer> modifiedLines, Set<EObject> modifiedElements) {

		super(xmlResource, helper, options);

		this.modifiedLines = modifiedLines;
		this.modifiedElements = modifiedElements;

		currentLine = 1;
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, name, attributes);

		// start and end here mark the first and last lines of the started element's
		//   initial tag (not the whole element)
		int start = currentLine;
		int end = locator.getLineNumber();

		EObject peekObject = objects.peekEObject();
		// if the object's stack head is not null (sometimes null is even pushed
		//   e.g. when parsing multi-valued attributes)
		if (peekObject != null) {
			int lineNumber = start;
			while (lineNumber <= end) {
				if (modifiedLines.contains(lineNumber)) {
					modifiedElements.add(peekObject);
				}
				lineNumber++;
			}
		}

		currentLine = end + 1;
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
