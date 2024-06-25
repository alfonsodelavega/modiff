package org.eclipse.epsilon.modiff.examples;

import java.io.IOException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.matcher.UmlMatcher;
import org.eclipse.epsilon.modiff.output.MatcherBasedLabelProvider;
import org.eclipse.epsilon.modiff.output.textual.UnifiedDiffFormatter;
import org.eclipse.uml2.uml.UMLPackage;

public class CompareDifferentOrdering {

	public static void main(String[] args) throws IOException {

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();
		resSet.getPackageRegistry().put("http://www.eclipse.org/uml2/5.0.0/UML", UMLPackage.eINSTANCE);

		String[][] examples = {
				{"models/uml-differentOrdering/m1-mini.uml",
				 "models/uml-differentOrdering/m2-mini.uml"},
				
				{"models/uml-differentOrdering/m1.uml",
				 "models/uml-differentOrdering/m2.uml"},
		};
		int example = 0;
		
		Modiff modiff = new Modiff(examples[example][0], examples[example][1]);
		modiff.setMatcher(new UmlMatcher());
		modiff.compare();

		System.out.println(new UnifiedDiffFormatter(modiff.getMunidiff(),
				new MatcherBasedLabelProvider(modiff.getMatcher())).format());

		System.out.println("Done");
	}
}
