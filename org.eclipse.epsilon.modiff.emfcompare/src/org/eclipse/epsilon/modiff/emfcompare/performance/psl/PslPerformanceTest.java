package org.eclipse.epsilon.modiff.emfcompare.performance.psl;

import java.io.File;
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
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;

public class PslPerformanceTest {

	/*
	 * requires having the peacemaker-evaluation project in the same folder as this one
	 * https://github.com/alfonsodelavega/peacemaker-evaluation
	 * 
	 * also, models have to be generated (instructions in the other repo)
	 */
	private static final String PSL_MODELS_ROOT =
			"../../peacemaker-evaluation/org.eclipse.epsilon.peacemaker.benchmarks/models/psl-updatedeleteTasks-extraChanges/";

	public static void main(String[] args) throws Exception {
		fillEMFRegistries();

		int warmups = 5;
		int repetitions = 10;

		for (int w = 0; w < warmups; w++) {
			useModiff();
			useEmfCompare();
		}

		long emfCompareStart = System.nanoTime();
		for (int rep = 0; rep < repetitions; rep++) {
			useEmfCompare();
		}
		long emfCompareTotal = System.nanoTime() - emfCompareStart;

		long modiffStart = System.nanoTime();
		for (int rep = 0; rep < repetitions; rep++) {
			useModiff();
		}
		long modiffTotal = System.nanoTime() - modiffStart;

		System.out.println("EMFCompare total (ms): " + emfCompareTotal / 1000000);
		System.out.println("Modiff total (ms): " + modiffTotal / 1000000);

		double speedup = (double) emfCompareTotal / modiffTotal;
		System.out.printf("Modiff is %3.2f%% faster (%1.2f speedup)\n",
				1 - 1 / speedup, speedup);
	}

	public static void useEmfCompare() throws Exception {
		Resource leftResource = getResource(
				getModelPath("1000elems-100percChanges_ancestor.model"));

		Resource rightResource = getResource(
				getModelPath("1000elems-100percChanges_left.model"));

		IComparisonScope scope = new DefaultComparisonScope(leftResource, rightResource, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		comparison.getDifferences();
	}

	public static void useModiff() throws Exception {
		Modiff modiff = new Modiff(
				getModelPath("1000elems-100percChanges_ancestor.model"),
				getModelPath("1000elems-100percChanges_left.model"));

		modiff.compare();
		modiff.getDifferences();
	}

	public static Resource getResource(String resourcePath) throws Exception {

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);

		Resource res = resourceSet.createResource(URI.createFileURI(resourcePath));
		res.load(null);

		return res;
	}

	public static String getModelPath(String model) {
		return PSL_MODELS_ROOT + model;
	}

	public static void fillEMFRegistries() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl());
		ResourceSet ecoreResourceSet = new ResourceSetImpl();

		String[] ecoreFiles = {
				"models/psl/psl.ecore"
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
}
