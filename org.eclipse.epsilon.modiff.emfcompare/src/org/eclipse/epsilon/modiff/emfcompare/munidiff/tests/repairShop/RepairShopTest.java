package org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.repairShop;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

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
import org.eclipse.epsilon.modiff.emfcompare.munidiff.transformations.EmfCompare2Munidiff;
import org.junit.BeforeClass;
import org.junit.Test;

public class RepairShopTest {

	protected boolean debug = true;

	protected Munidiff getReport(String leftModel) throws IOException {



		Resource left = getResource(getModelPath(leftModel));
		Resource right = getResource(getModelPath("00-from"));

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		Munidiff md = new EmfCompare2Munidiff().transform(comparison);

		String report = md.report();

		if (debug) {
			System.out.println(leftModel);
			System.out.println(report);
		}

		store(leftModel + "-munidiff.diff", report);

		return md;
	}

	protected Resource getResource(String model) throws IOException {
		final URL fileURL = getClass().getResource(model);
		final InputStream str = fileURL.openStream();
		final URI uri = URI.createURI(fileURL.toString());

		Resource.Factory resourceFactory = Resource.Factory.Registry.INSTANCE.getFactory(uri);
		if (resourceFactory == null) {
			// Most likely a standalone run. Try with a plain XMI resource
			resourceFactory = new XMIResourceFactoryImpl();
		}

		// resourceFactory cannot be null
		Resource res = resourceFactory.createResource(uri);
		res.load(str, Collections.emptyMap());
		str.close();
		return res;
	}

	public String getModelPath(String modelName) {
		return String.format("data/%s.model", modelName);
	}

	protected void store(String testCase, String report) {
		File outputFolder = new File("output/repairShop");
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}
		File file = new File(outputFolder, testCase);

		try (FileWriter writer = new FileWriter(file)) {
			writer.write(report);
		}
		catch (IOException e) {
			System.err.println("Error while saving the string to the file: " + e.getMessage());
		}
	}

	@BeforeClass
	public static void fillEMFRegistries() throws IOException {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl());
		ResourceSet ecoreResourceSet = new ResourceSetImpl();

		String[] ecoreFiles = {
				"models/repairshop/repairshop.ecore"
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

	@Test
	public void test11() throws IOException {
		getReport("11-modifyJobDescription");
	}

	@Test
	public void test21() throws IOException {
		getReport("21-changeMainSkill");
	}

	@Test
	public void test31() throws IOException {
		getReport("31-addStatus");
	}

	@Test
	public void test41() throws IOException {
		getReport("41-addTag");
	}

	@Test
	public void test42() throws IOException {
		getReport("42-removeTag");
	}

	@Test
	public void test51() throws IOException {
		getReport("51-addSecondarySkill");
	}

	@Test
	public void test61() throws IOException {
		getReport("61-moveJob");
	}
}
