package org.eclipse.epsilon.modiff.output.graphical;

import org.eclipse.epsilon.egl.EglModule;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.modiff.munidiff.Munidiff;
import org.eclipse.epsilon.modiff.output.LabelProvider;
import org.eclipse.epsilon.modiff.output.MunidiffFormatter;

public class PlantumlFormatter extends MunidiffFormatter {
	
	public PlantumlFormatter(Munidiff munidiff, LabelProvider labelProvider) {
		super(munidiff, labelProvider);
	}
	
	public String format() {

		final String templateName = "munidiff2plantuml.egl";

		String result = null;
		EglModule module = new EglModule();
		try {
			module.parse(getClass().getResource(templateName));

			module.getContext().getFrameStack().put(Variable.createReadOnlyVariable("munidiff", munidiff));
			module.getContext().getFrameStack().put(Variable.createReadOnlyVariable("labelProvider", labelProvider));
			result = (String) module.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
