package org.eclipse.epsilon.modiff;

import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.emf.ecore.xmi.XMLDefaultHandler;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.epsilon.modiff.Modiff.DiffSide;
import org.xml.sax.SAXException;

public class ModiffXMLParserPoolImpl extends XMLParserPoolImpl {

	protected Modiff modiff;
	protected DiffSide diffSide;

	public ModiffXMLParserPoolImpl(Modiff modiff, DiffSide diffSide) {
		this.modiff = modiff;
		this.diffSide = diffSide;
	}

	@Override
	public synchronized XMLDefaultHandler getDefaultHandler(XMLResource resource, XMLLoad xmlLoad, XMLHelper helper, Map<?, ?> options) {
		return new ModiffXMIHandler(resource, helper, options, modiff, diffSide);
	}

	protected SAXParser makeParser(Map<String, Boolean> features, Map<String, ?> properties) throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false); // Peacemaker: single line changed to avoid namespace merge problems
		SAXParser parser = factory.newSAXParser();

		// set parser features and properties
		if (features != null) {
			for (Map.Entry<String, Boolean> entry : features.entrySet()) {
				parser.getXMLReader().setFeature(entry.getKey(), entry.getValue());
			}
		}
		if (properties != null) {
			for (Map.Entry<String, ?> entry : properties.entrySet()) {
				parser.getXMLReader().setProperty(entry.getKey(), entry.getValue());
			}
		}
		return parser;
	}
}
