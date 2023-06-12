/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.modiff.test.emfcompare.req;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.differences.ChangedElement;
import org.eclipse.epsilon.modiff.differences.ModelDifference;
import org.eclipse.epsilon.modiff.differences.RemovedElement;
import org.eclipse.epsilon.modiff.matcher.IdMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.test.emfcompare.req.data.ReqInputData;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReqComputingTest {

	protected boolean debug = true;
	protected Modiff modiff;
	protected Matcher matcher;
	
	protected ReqInputData input = new ReqInputData();
	

	@Test
	public void testA1UseCase() throws IOException {
		modiff = compare(input.getA1From(), input.getA1To());

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 2);
		assert (differences.get(0) instanceof RemovedElement);
		assert (differences.get(0).getIdentifier().equals("A"));
		assert (differences.get(1) instanceof RemovedElement);
		assert (differences.get(1).getIdentifier().equals("B"));
	}

	@Test
	public void testA2UseCase() throws IOException {
		modiff = compare(input.getA2From(), input.getA2To());

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 2);
		assert (differences.get(0) instanceof RemovedElement);
		assert (differences.get(0).getIdentifier().equals("A"));
		assert (differences.get(1) instanceof RemovedElement);
		assert (differences.get(1).getIdentifier().equals("B"));
	}

	@Test
	public void testA3UseCase() throws IOException {
		modiff = compare(input.getA3From(), input.getA3To());

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 2);
		assert (differences.get(0) instanceof ChangedElement);
		assert (differences.get(0).getIdentifier().equals("A"));
		assert (differences.get(1) instanceof RemovedElement);
		assert (differences.get(1).getIdentifier().equals("B"));
	}

	@Test
	public void testA4UseCase() throws IOException {
		modiff = compare(input.getA4From(), input.getA4To());

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 2);
		assert (differences.get(0) instanceof ChangedElement);
		assert (differences.get(0).getIdentifier().equals("A"));
		
		ChangedElement change = (ChangedElement) differences.get(0);
		EStructuralFeature feature = 
				change.getToElement().eClass().getEStructuralFeature("multiValuedReference");
		
		EObject newA = change.getToElement();
		EObject oldA = change.getFromElement();

		assert (matcher.getIdentifier(getFirstElementOfList(newA.eGet(feature))).equals("C"));
		assert (matcher.getIdentifier(getFirstElementOfList(oldA.eGet(feature))).equals("B"));

		assert (differences.get(1) instanceof RemovedElement);
		assert (differences.get(1).getIdentifier().equals("B"));
	}


	protected EObject getFirstElementOfList(Object list) {
		if (!(list instanceof List)) {
			throw new IllegalStateException("Object is not a list");
		}
		return (EObject) ((List<?>) list).get(0);
	}

	protected Modiff compare(String fromModel, String toModel) throws IOException {
		modiff = new Modiff(fromModel, toModel);
		matcher = new IdMatcher();
		modiff.setMatcher(matcher);
		modiff.compare();
		return modiff;
	}

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
	
	@After
	public void reportDifferences() {
		if (debug) {
			System.out.println(modiff.reportDifferences());
		}
	}
}