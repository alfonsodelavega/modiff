package org.eclipse.epsilon.modiff.output.graphical;

import java.net.URL;

import org.eclipse.epsilon.egl.EglModule;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.modiff.munidiff.Munidiff;
import org.eclipse.epsilon.modiff.munidiff.MunidiffPackage;
import org.eclipse.epsilon.modiff.output.LabelProvider;
import org.eclipse.epsilon.modiff.output.MunidiffFormatter;

public class PlantumlFormatter extends MunidiffFormatter {
	
	protected boolean hideUnchangedRefsInChangedElems = true;

	public PlantumlFormatter(Munidiff munidiff, LabelProvider labelProvider) {
		super(munidiff, labelProvider);
	}
	
	protected URL getTemplate() {
		return getClass().getResource("/output/graphical/munidiff2plantuml.egl");
	}

	protected EglModule getModule() {
		EglModule module = new EglModule();
		module.getContext().getFrameStack().put(Variable.createReadOnlyVariable("munidiff", munidiff));
		module.getContext().getFrameStack().put(Variable.createReadOnlyVariable("labelProvider", labelProvider));

		module.getContext().getFrameStack().put(Variable.createReadOnlyVariable(
				"hideUnchangedRefsInChangedElems", hideUnchangedRefsInChangedElems));

		InMemoryEmfModel model = new InMemoryEmfModel(MunidiffPackage.eINSTANCE.eResource());
		model.setName("munidiffMM");
		module.getContext().getModelRepository().addModel(model);

		return module;
	}

	public String format() {

		String result = null;
		EglModule module = getModule();
		try {
			module.parse(getTemplate());
			result = (String) module.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public PlantumlFormatter hideUnchangedRefsInChangedElems(boolean hide) {
		hideUnchangedRefsInChangedElems = hide;
		return this;
	}
}
