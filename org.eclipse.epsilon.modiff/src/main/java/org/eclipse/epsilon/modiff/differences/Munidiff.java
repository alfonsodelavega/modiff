package org.eclipse.epsilon.modiff.differences;

import java.util.List;

import org.eclipse.epsilon.modiff.output.LabelProvider;
import org.eclipse.epsilon.modiff.output.UnifiedDiffFormatter;

public class Munidiff {

	protected List<ModelDifference> differences;
	protected LabelProvider labelProvider;

	private String fromModelFile = "";
	private String toModelFile = "";

	public Munidiff(List<ModelDifference> differences, LabelProvider labelProvider) {
		this.differences = differences;
		this.labelProvider = labelProvider;
	}

	public String report() {
		UnifiedDiffFormatter formatter = new UnifiedDiffFormatter(differences, labelProvider);
		formatter.setFromModelFile(getFromModelFile());
		formatter.setToModelFile(getToModelFile());
		return formatter.format();
	}

	public String getFromModelFile() {
		return fromModelFile;
	}

	public void setFromModelFile(String fromModelFile) {
		this.fromModelFile = fromModelFile;
	}

	public String getToModelFile() {
		return toModelFile;
	}

	public void setToModelFile(String toModelFile) {
		this.toModelFile = toModelFile;
	}
}
