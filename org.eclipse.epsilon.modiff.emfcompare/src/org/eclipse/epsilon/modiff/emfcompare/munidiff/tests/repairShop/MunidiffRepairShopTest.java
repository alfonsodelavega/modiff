package org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.repairShop;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

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
import org.eclipse.epsilon.modiff.emfcompare.munidiff.transformations.EmfCompare2Munidiff;
import org.eclipse.epsilon.modiff.matcher.IdMatcher;
import org.eclipse.epsilon.modiff.munidiff.AddedElement;
import org.eclipse.epsilon.modiff.munidiff.ChangedElement;
import org.eclipse.epsilon.modiff.munidiff.Difference;
import org.eclipse.epsilon.modiff.munidiff.Munidiff;
import org.eclipse.epsilon.modiff.output.MatcherBasedLabelProvider;
import org.eclipse.epsilon.modiff.output.textual.UnifiedDiffFormatter;
import org.junit.BeforeClass;
import org.junit.Test;

public class MunidiffRepairShopTest {

	protected boolean debug = true;

	protected Munidiff getReport(String leftModel) throws IOException {

		Resource left = getResource(getModelPath(leftModel));
		Resource right = getResource(getModelPath("00-from"));

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		Munidiff md = new EmfCompare2Munidiff(new IdMatcher()).transform(comparison);

		UnifiedDiffFormatter formatter = new UnifiedDiffFormatter(md,
				new MatcherBasedLabelProvider(new IdMatcher()));

		String report = formatter.format();

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

	protected Difference getDifferenceForId(List<Difference> differences, String id) {
		for (Difference d : differences) {
			if (d.getIdentifier().equals(id)) {
				return d;
			}
		}
		return null;
	}

	@Test
	public void test11() throws IOException {
		Munidiff munidiff = getReport("11-modifyJobDescription");

		List<Difference> differences = munidiff.getDifferences();
		assert (differences.size() == 1);

		Difference diff = differences.get(0);
		assert (diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("description"));
	}

	@Test
	public void test21() throws IOException {
		Munidiff munidiff = getReport("21-changeMainSkill");

		List<Difference> differences = munidiff.getDifferences();
		assert (differences.size() == 1);

		Difference diff = differences.get(0);
		assert (diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("mainSkill"));
	}

	@Test
	public void test31() throws IOException {
		Munidiff munidiff = getReport("31-addStatus");

		List<Difference> differences = munidiff.getDifferences();
		assert (differences.size() == 2);

		Difference added = getDifferenceForId(differences, "st2");
		assert (added != null && added instanceof AddedElement);

		Difference changed = getDifferenceForId(differences, "job2");
		assert (changed != null && changed instanceof ChangedElement);
		assert (((ChangedElement) changed).getChangedFeatures().size() == 1);
		assert (((ChangedElement) changed).getChangedFeatures().get(0).getName().equals("status"));
	}

	@Test
	public void test41() throws IOException {
		Munidiff munidiff = getReport("41-addTag");

		List<Difference> differences = munidiff.getDifferences();
		assert (differences.size() == 1);

		Difference diff = differences.get(0);
		assert (diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("tags"));
	}

	@Test
	public void test42() throws IOException {
		Munidiff munidiff = getReport("42-removeTag");

		List<Difference> differences = munidiff.getDifferences();
		assert (differences.size() == 1);

		Difference diff = differences.get(0);
		assert (diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("tags"));
	}

	@Test
	public void test51() throws IOException {
		Munidiff munidiff = getReport("51-addSecondarySkill");

		List<Difference> differences = munidiff.getDifferences();
		assert (differences.size() == 1);

		Difference diff = differences.get(0);
		assert (diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("secondarySkills"));
	}

	@Test
	public void test61() throws IOException {
		Munidiff munidiff = getReport("61-moveJob");

		List<Difference> differences = munidiff.getDifferences();
		assert (differences.size() == 2);

		Difference changed = getDifferenceForId(differences, "Alice");
		assert (changed != null && changed instanceof ChangedElement);
		assert (((ChangedElement) changed).getChangedFeatures().size() == 1);
		assert (((ChangedElement) changed).getChangedFeatures().get(0).getName().equals("queue"));

		changed = getDifferenceForId(differences, "Bob");
		assert (changed != null && changed instanceof ChangedElement);
		assert (((ChangedElement) changed).getChangedFeatures().size() == 1);
		assert (((ChangedElement) changed).getChangedFeatures().get(0).getName().equals("queue"));
	}
}
