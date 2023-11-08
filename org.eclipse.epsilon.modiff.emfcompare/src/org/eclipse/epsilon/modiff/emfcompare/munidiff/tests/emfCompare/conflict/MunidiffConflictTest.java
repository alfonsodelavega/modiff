/*******************************************************************************
t * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.emfCompare.conflict;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.differences.Munidiff;
import org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.emfCompare.conflict.data.ConflictInputData;
import org.eclipse.epsilon.modiff.emfcompare.munidiff.transformations.EmfCompare2Munidiff;
import org.junit.BeforeClass;
import org.junit.Test;

public class MunidiffConflictTest {

	protected boolean debug = true;
	protected ConflictInputData input = new ConflictInputData();

	@BeforeClass
	public static void fillEMFRegistries() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl());
		ResourceSet ecoreResourceSet = new ResourceSetImpl();

		String[] ecoreFiles = {
				"models/nodes/nodes.ecore"
		};

		for (String ecoreFile : ecoreFiles) {
			Resource ecoreResource = ecoreResourceSet.createResource(
					URI.createFileURI(new File(ecoreFile).getAbsolutePath()));
			ecoreResource.load(null);

			for (EObject o : ecoreResource.getContents()) {
				EPackage ePackage = (EPackage) o;
				EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
			}
		}
	}

	protected void getReport(String testCase, Resource left, Resource right, Resource origin) {
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		System.out.println(comparison);
		//		Munidiff md = new EmfCompare2Munidiff().transform(comparison);
		//
		//		String report = md.report();
		//
		//		if (debug) {
		//			System.out.println(report);
		//		}
		//
		//		store(testCase, report);
	}

	protected void store(String testCase, String report) {
		File outputFolder = new File("output/req");
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}
		File file = new File(outputFolder, testCase);

		try (FileWriter writer = new FileWriter(file)) {
			writer.write(report);
			System.out.println("Report saved to file: " + file.getAbsolutePath());
		}
		catch (IOException e) {
			System.err.println("Error while saving the string to the file: " + e.getMessage());
		}
	}

	@Test
	public void testA() throws IOException {
		getReport("a01-attribute_munidiff3.diff",
				input.getA1AttributeLeft(), input.getA1AttributeRight(), input.getA1AttributeOrigin());
		getReport("a01-reference_munidiff3.diff",
				input.getA1ReferenceLeft(), input.getA1ReferenceRight(), input.getA1ReferenceOrigin());
		getReport("a02-reference_munidiff3.diff",
				input.getA2ReferenceLeft(), input.getA2ReferenceRight(), input.getA2ReferenceOrigin());
	}

	@Test
	public void testG() throws IOException {
		getReport("g_munidiff3.diff",
				input.getGLeft(), input.getGRight(), input.getGOrigin());
	}
}
