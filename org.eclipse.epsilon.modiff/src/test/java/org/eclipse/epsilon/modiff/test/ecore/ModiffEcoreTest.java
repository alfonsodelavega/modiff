package org.eclipse.epsilon.modiff.test.ecore;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.differences.AddedElement;
import org.eclipse.epsilon.modiff.differences.ChangedElement;
import org.eclipse.epsilon.modiff.differences.ModelDifference;
import org.eclipse.epsilon.modiff.differences.RemovedElement;
import org.eclipse.epsilon.modiff.matcher.EcoreMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.test.emfcompare.req.data.ReqInputData;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModiffEcoreTest {

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
		matcher = new EcoreMatcher();
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
	}

	@After
	public void reportDifferences() {
		if (debug) {
			System.out.println(modiff.reportDifferences());
		}
	}

	public String getFullPath(String path) {
		return "models/ecore/" + path + ".ecore";
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
		modiff = compare("00-from", "11-class2abstract");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 1);

		ModelDifference diff = getDifferenceForId(differences, "repairtests.Skill");
		assert (diff != null && diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("abstract"));
	}

	@Test
	public void test12() throws IOException {
		modiff = compare("00-from", "12-className");

		List<ModelDifference> differences = modiff.getDifferences();
		assert (differences.size() == 3);

		ModelDifference diff = getDifferenceForId(differences, "repairtests.RepairShop.workers");
		assert (diff != null && diff instanceof ChangedElement);
		assert (((ChangedElement) diff).getChangedFeatures().size() == 1);
		assert (((ChangedElement) diff).getChangedFeatures().get(0).getName().equals("eType"));

		diff = getDifferenceForId(differences, "repairtests.Worker");
		assert (diff != null && diff instanceof RemovedElement);

		diff = getDifferenceForId(differences, "repairtests.Employee");
		assert (diff != null && diff instanceof AddedElement);
	}
}

