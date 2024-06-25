package org.eclipse.epsilon.modiff.output.graphical;

import java.net.URL;

import org.eclipse.epsilon.egl.EglModule;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.modiff.munidiff.Munidiff;
import org.eclipse.epsilon.modiff.output.LabelProvider;
import org.eclipse.epsilon.modiff.output.MunidiffFormatter;

public class PlantumlFormatter extends MunidiffFormatter {
	
	protected boolean hideUnchangedRefsInChangedElems = true;

	public PlantumlFormatter(Munidiff munidiff, LabelProvider labelProvider) {
		super(munidiff, labelProvider);
	}
	
	protected URL getTemplate() {
		return getClass().getResource("munidiff2plantuml.egl");
	}

	public String format() {

		String result = null;
		EglModule module = new EglModule();
		try {
			module.parse(getTemplate());

			module.getContext().getFrameStack().put(Variable.createReadOnlyVariable("munidiff", munidiff));
			module.getContext().getFrameStack().put(Variable.createReadOnlyVariable("labelProvider", labelProvider));

			module.getContext().getFrameStack().put(Variable.createReadOnlyVariable(
					"hideUnchangedRefsInChangedElems", hideUnchangedRefsInChangedElems));

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
