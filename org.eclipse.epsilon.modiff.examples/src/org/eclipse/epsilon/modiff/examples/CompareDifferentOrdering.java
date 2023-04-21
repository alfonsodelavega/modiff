package org.eclipse.epsilon.modiff.examples;

import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.matcher.IdOrNameMatcher;
import org.eclipse.uml2.uml.UMLPackage;

public class CompareDifferentOrdering {

	public static void main(String[] args) throws IOException {

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();
		resSet.getPackageRegistry().put("http://www.eclipse.org/uml2/5.0.0/UML", UMLPackage.eINSTANCE);

		Modiff modiff = new Modiff(
				"models/uml-differentOrdering/m1-mini.uml",
				"models/uml-differentOrdering/m2-mini.uml");
		modiff.setMatcher(new IdOrNameMatcher());
		modiff.compare();

		System.out.println("Done");
	}
}
