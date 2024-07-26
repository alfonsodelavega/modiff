package org.eclipse.epsilon.modiff.output.graphical;

import java.net.URL;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.matcher.EcoreMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.munidiff.Munidiff;
import org.eclipse.epsilon.modiff.output.LabelProvider;
import org.eclipse.epsilon.modiff.output.MatcherBasedLabelProvider;
import org.eclipse.epsilon.modiff.output.textual.UnifiedDiffFormatter;

public class PlantumlEcoreFormatter extends PlantumlFormatter {

	public static void main(String[] args) throws Exception {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl());

		Modiff modiff = new Modiff(

				//				"models/ecore/00-from.ecore",
				"models/ecore/01-from-unmodifiedRepairshop.ecore",
				// for 00-from
				//				"models/ecore/11-class2abstract.ecore"
				//				"models/ecore/12-className.ecore"
				//				"models/ecore/22-addDeleteFeature.ecore"
				//				"models/ecore/23-changeAttrs.ecore"
//				"models/ecore/24-reorderAttrs.ecore"
				// for 01-from
				//				"models/ecore/31-deleteSkill.ecore"
				"models/ecore/32-changeSecondarySkill.ecore"
		);

		Matcher matcher = new EcoreMatcher();
		modiff.setMatcher(matcher);

		modiff.compare();

		LabelProvider labelProvider = new MatcherBasedLabelProvider(matcher);

		System.out.println(new UnifiedDiffFormatter(modiff.getMunidiff(), labelProvider).format());
		System.out.println("**************************************************");
		System.out.println(new PlantumlEcoreFormatter(modiff.getMunidiff(), labelProvider).format());
	}

	public PlantumlEcoreFormatter(Munidiff munidiff, LabelProvider labelProvider) {
		super(munidiff, labelProvider);
	}

	@Override
	protected URL getTemplate() {
		return getClass().getResource("/output/graphical/munidiffEcore2plantuml.egl");
	}
}
