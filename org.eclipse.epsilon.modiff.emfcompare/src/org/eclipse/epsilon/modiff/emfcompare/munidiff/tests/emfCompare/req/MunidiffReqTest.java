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
package org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.emfCompare.req;

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
import org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.emfCompare.req.data.ReqInputData;
import org.eclipse.epsilon.modiff.emfcompare.munidiff.transformations.EmfCompare2Munidiff;
import org.eclipse.epsilon.modiff.matcher.IdMatcher;
import org.eclipse.epsilon.modiff.munidiff.Munidiff;
import org.eclipse.epsilon.modiff.output.MatcherBasedLabelProvider;
import org.eclipse.epsilon.modiff.output.UnifiedDiffFormatter;
import org.junit.BeforeClass;
import org.junit.Test;

public class MunidiffReqTest {

	protected boolean debug = true;
	protected ReqInputData input = new ReqInputData();

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

	protected void getReport(String testCase, Resource left, Resource right) {
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		Munidiff md = new EmfCompare2Munidiff(new IdMatcher()).transform(comparison);

		UnifiedDiffFormatter formatter = new UnifiedDiffFormatter(md,
				new MatcherBasedLabelProvider(new IdMatcher()));

		String report = formatter.format();

		if (debug) {
			System.out.println(report);
		}

		store(testCase, report);
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
		getReport("a01-munidiff.diff", input.getA1Left(), input.getA1Right());
		getReport("a02-munidiff.diff", input.getA2Left(), input.getA2Right());
		getReport("a03-munidiff.diff", input.getA3Left(), input.getA3Right());
		getReport("a04-munidiff.diff", input.getA4Left(), input.getA4Right());
		getReport("a05-munidiff.diff", input.getA5Left(), input.getA5Right());
		getReport("a06-munidiff.diff", input.getA6Left(), input.getA6Right());
		getReport("a07-munidiff.diff", input.getA7Left(), input.getA7Right());
		getReport("a08-munidiff.diff", input.getA8Left(), input.getA8Right());
		getReport("a09-munidiff.diff", input.getA9Left(), input.getA9Right());
		getReport("a10-munidiff.diff", input.getA10Left(), input.getA10Right());
		getReport("a11-munidiff.diff", input.getA11Left(), input.getA11Right());
	}

	@Test
	public void testC() throws IOException {
		getReport("c1-munidiff.diff", input.getC1Left(), input.getC1Right());
		getReport("c2-munidiff.diff", input.getC2Left(), input.getC2Right());
		getReport("c3-munidiff.diff", input.getC3Left(), input.getC3Right());
		getReport("c4-munidiff.diff", input.getC4Left(), input.getC4Right());
		getReport("c5-munidiff.diff", input.getC5Left(), input.getC5Right());
	}

}
