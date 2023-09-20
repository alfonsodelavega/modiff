package org.eclipse.epsilon.modiff.test.repairshop;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.differences.AddedElement;
import org.eclipse.epsilon.modiff.differences.ChangedElement;
import org.eclipse.epsilon.modiff.differences.ModelDifference;
import org.eclipse.epsilon.modiff.matcher.IdMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.test.emfcompare.req.data.ReqInputData;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModiffRepairShopTest {

	protected boolean debug = true;

	protected Modiff modiff;
	protected Matcher matcher;

	protected ReqInputData input = new ReqInputData();

	@SuppressWarnings("unchecked")
	protected List<EObject> getList(Object list) {
		if (!(list instanceof List)) {
			throw new IllegalStateException("Object is not a list");
		}
		return (List<EObject>) list;
	}

	protected EObject getElement(Object list, int index) {
		return getList(list).get(index);
	}

	protected Modiff compare(String fromModel, String toModel) throws IOException {
		modiff = new Modiff(getFullPath(fromModel), getFullPath(toModel));
		matcher = new IdMatcher();
		modiff.setMatcher(matcher);
		modiff.compare();
		return modiff;
	}

	protected ModelDifference getDifferenceForId(List<ModelDifference> differences, String id) {
		for (ModelDifference d : differences) {
			if (d.getIdentifier().equals(id)) {
				return d;
			}
		}
		return null;
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

	@After
	public void reportDifferences() {
		if (debug) {
			System.out.println(modiff.reportDifferences());
		}
	}

	public String getFullPath(String path) {
		return "models/repairshop/" + path + ".model";
	}

	@Test
	public void test01() throws IOException {
		modiff = compare("00-from", "00-from");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 0);
		assert (modiff.reportDifferences().length() == 0);
	}

	@Test
	public void test11() throws IOException {
		modiff = compare("00-from", "11-modifyJobDescription");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 1);

		ModelDifference diff = differences.get(0);
		assert (diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("description"));
	}

	@Test
	public void test21() throws IOException {
		modiff = compare("00-from", "21-changeMainSkill");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 1);

		ModelDifference diff = differences.get(0);
		assert (diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("mainSkill"));
	}

	@Test
	public void test31() throws IOException {
		modiff = compare("00-from", "31-addStatus");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 2);

		ModelDifference added = getDifferenceForId(differences, "st2");
		assert (added != null && added instanceof AddedElement);

		ModelDifference changed = getDifferenceForId(differences, "job2");
		assert (changed != null && changed instanceof ChangedElement);
		assert (((ChangedElement) changed).getChangedFeatures().size() == 1);
		assert (((ChangedElement) changed).getChangedFeatures().get(0).getName().equals("status"));
	}


	@Test
	public void test41() throws IOException {
		modiff = compare("00-from", "41-addTag");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 1);

		ModelDifference diff = differences.get(0);
		assert (diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("tags"));
	}

	@Test
	public void test42() throws IOException {
		modiff = compare("00-from", "42-removeTag");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 1);

		ModelDifference diff = differences.get(0);
		assert (diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("tags"));
	}

	@Test
	public void test51() throws IOException {
		modiff = compare("00-from", "51-addSecondarySkill");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 1);

		ModelDifference diff = differences.get(0);
		assert (diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("secondarySkills"));
	}

	@Test
	public void test61() throws IOException {
		modiff = compare("00-from", "61-moveJob");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 2);

		ModelDifference changed = getDifferenceForId(differences, "Alice");
		assert (changed != null && changed instanceof ChangedElement);
		assert (((ChangedElement) changed).getChangedFeatures().size() == 1);
		assert (((ChangedElement) changed).getChangedFeatures().get(0).getName().equals("queue"));

		changed = getDifferenceForId(differences, "Bob");
		assert (changed != null && changed instanceof ChangedElement);
		assert (((ChangedElement) changed).getChangedFeatures().size() == 1);
		assert (((ChangedElement) changed).getChangedFeatures().get(0).getName().equals("queue"));
	}

	@Test
	public void test62() throws IOException {
		modiff = compare("00-from-noMultiValuedAttributes", "62-moveJob-noMultiValuedAttributes");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 2);

		ModelDifference changed = getDifferenceForId(differences, "Alice");
		assert (changed != null && changed instanceof ChangedElement);
		assert (((ChangedElement) changed).getChangedFeatures().size() == 1);
		assert (((ChangedElement) changed).getChangedFeatures().get(0).getName().equals("queue"));

		changed = getDifferenceForId(differences, "Bob");
		assert (changed != null && changed instanceof ChangedElement);
		assert (((ChangedElement) changed).getChangedFeatures().size() == 1);
		assert (((ChangedElement) changed).getChangedFeatures().get(0).getName().equals("queue"));
	}
}
