package org.eclipse.epsilon.modiff;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.differences.AddedElement;
import org.eclipse.epsilon.modiff.differences.ChangedElement;
import org.eclipse.epsilon.modiff.differences.ModelDifference;
import org.eclipse.epsilon.modiff.differences.RemovedElement;
import org.eclipse.epsilon.modiff.matcher.IdMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.HistogramDiff;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;

import io.reflectoring.diffparser.api.UnifiedDiffParser;
import io.reflectoring.diffparser.api.model.Diff;
import io.reflectoring.diffparser.api.model.Hunk;
import io.reflectoring.diffparser.api.model.Line;

/**
 * Line-based comparator of models. Starting from the command: <br/>
 * 
 * 
 * <br/>
 * 
 * diff -u fromModelFile toModelFile <br/>
 * <br/>
 * 
 * Equivalences: <br/>
 * fromModelFile / toModelFile <br/>
 * old / new <br/>
 * left / right <br/>
 * -lines / +lines <br/>
 * removed / added <br/>
 * 
 * @author alfonsodelavega
 */
public class Modiff {

	public static void main(String[] args) throws IOException {

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl());
		ResourceSet ecoreResourceSet = new ResourceSetImpl();

		String[] ecoreFiles = { "models/comics/comicshop.ecore" };
		for (String ecoreFile : ecoreFiles) {
			Resource ecoreResource = ecoreResourceSet.createResource(
					URI.createFileURI(new File(ecoreFile).getAbsolutePath()));
			ecoreResource.load(null);

			for (EObject o : ecoreResource.getContents()) {
				EPackage ePackage = (EPackage) o;
				EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
			}
		}

		Modiff modiff = new Modiff("models/comics/base.model", "models/comics/left.model");
		modiff.setMatcher(new IdMatcher());
		modiff.compare();
		
		System.out.println(modiff.reportDifferences());
	}

	protected String fromModelFile;
	protected String toModelFile;

	protected Set<Integer> addedLines = new LinkedHashSet<>();
	protected Set<Integer> removedLines = new LinkedHashSet<>();

	protected Set<EObject> addedElements = new LinkedHashSet<>();
	protected Set<EObject> removedElements = new LinkedHashSet<>();

	protected Resource fromModel;
	protected Resource toModel;

	protected Matcher matcher;
	protected List<ModelDifference> differences;

	public Modiff(String fromModelFile, String toModelFile) {
		this.fromModelFile = fromModelFile;
		this.toModelFile = toModelFile;
	}

	/**
	 * Compare two models and show the differences
	 * 
	 * @param fromModelFile Old version of the model
	 * @param toModelFile   New Version of the model
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void compare() throws IOException {

		List<Diff> diffs = getLineDiffs();

		parseLineDiff(diffs);

		fromModel = loadFromModel();
		toModel = loadToModel();

		identifyDifferences();
	}

	protected List<Diff> getLineDiffs() throws IOException {
		List<Diff> diffs = null;

		final PipedOutputStream out = new PipedOutputStream();
		PipedInputStream in = new PipedInputStream(out);
		new Thread(() -> {
			try {
				getLineDiff(out);
				out.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
		

		// parse the diffs to a workable format
		diffs = new UnifiedDiffParser().parse(in);
		in.close();

		return diffs;
	}

	protected void getLineDiff(OutputStream out) throws IOException {

		RawText rt1 = new RawText(new File(fromModelFile));
		RawText rt2 = new RawText(new File(toModelFile));
		EditList diffList = new EditList();

		// FIXME: we would probably want to omit trailing and leading whitespace
		//   (container changes modify indentations)
		diffList.addAll(new HistogramDiff().diff(RawTextComparator.DEFAULT, rt1, rt2));

		if (!diffList.isEmpty()) {
			// jgit does not generate unified format headers (done elsewhere)
			// required by diffparser, so we do it by hand
			PrintWriter writer = new PrintWriter(out);
			writer.printf("--- %s\n", fromModelFile);
			writer.printf("+++ %s\n", toModelFile);
			writer.flush();
			try (DiffFormatter f = new DiffFormatter(out)) {
				f.format(diffList, rt1, rt2);
			}
		}
	}

	protected void parseLineDiff(List<Diff> diffs) {

		for (Diff diff : diffs) {
			for (Hunk hunk : diff.getHunks()) {

				int fromModelLineNumber = hunk.getFromFileRange().getLineStart();
				int toModelLineNumber = hunk.getToFileRange().getLineStart();

				for (Line line : hunk.getLines()) {
					switch (line.getLineType()) {
					case FROM:
						removedLines.add(fromModelLineNumber);
						fromModelLineNumber++;
						break;
					case TO:
						addedLines.add(toModelLineNumber);
						toModelLineNumber++;
						break;
					case NEUTRAL:
						fromModelLineNumber++;
						toModelLineNumber++;
						break;
					}
				}
			}
		}
	}

	protected Resource loadFromModel() throws IOException {
		return loadModel(fromModelFile, removedLines, removedElements);
	}

	protected Resource loadToModel() throws IOException {
		return loadModel(toModelFile, addedLines, addedElements);
	}

	protected Resource loadModel(String modelFile, Set<Integer> modifiedLines,
			Set<EObject> modifiedElements) throws IOException {

		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createFileURI(modelFile));

		// the pool allows decorating the xml handler to get element lines
		Map<Object, Object> loadOptions = new HashMap<>();
		loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL,
				new ModiffXMLParserPoolImpl(modifiedLines, modifiedElements));
		resource.load(loadOptions);

		return resource;
	}

	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}

	public List<ModelDifference> getDifferences() {
		return differences;
	}

	protected void identifyDifferences() {

		checkForDuplicates();

		differences = new ArrayList<>();

		for (EObject addedElement : addedElements) {
			boolean matched = false;
			for (EObject removedElement : removedElements) {
				if (matcher.matches(addedElement, removedElement)) {
					ChangedElement changedElement = new ChangedElement(
							matcher.getIdentifier(addedElement),
							removedElement,
							addedElement);

					// it could be a false positive due to line format changes
					if (changedElement.hasDifferences()) {
						differences.add(changedElement);
					}

					removedElements.remove(removedElement);
					matched = true;
					break;
				}
			}
			if (!matched) {
				differences.add(new AddedElement(
						matcher.getIdentifier(addedElement), addedElement));
			}
		}
		// any element that remains is a removed one
		for (EObject removedElement : removedElements) {
			differences.add(new RemovedElement(
					matcher.getIdentifier(removedElement), removedElement));
		}
	}

	protected void checkForDuplicates() {
		List<String> duplicatesInToModel = findDuplicates(addedElements);
		List<String> duplicatesInFromModel = findDuplicates(removedElements);
		if (!duplicatesInToModel.isEmpty() || !duplicatesInFromModel.isEmpty()) {
			StringBuilder errorMessage = new StringBuilder();

			errorMessage.append("\n\nID duplicates found in one or both models:\n");
			errorMessage.append(toModelFile)
					.append(": [")
					.append(String.join(", ", duplicatesInToModel))
					.append("]\n");

			errorMessage.append(fromModelFile)
					.append(": [")
					.append(String.join(", ", duplicatesInFromModel))
					.append("]\n");

			throw new RuntimeException(errorMessage.toString());
		}
	}

	public List<String> findDuplicates(Set<EObject> elements) {
		List<String> duplicates = new ArrayList<>();

		List<String> ids = elements.stream()
				.map(e -> matcher.getIdentifier(e))
				.collect(Collectors.toList());

		Set<String> uniqueIds = new HashSet<>();

		for (String str : ids) {
			if (!uniqueIds.add(str)) {
				duplicates.add(str);
			}
		}

		return duplicates;
	}

	public String reportDifferences() {
		StringBuilder s = new StringBuilder();

		s.append("--- ").append(fromModelFile).append("\n");
		s.append("+++ ").append(toModelFile).append("\n");

		for (ModelDifference d : differences) {
			s.append("@@").append("\n");
			s.append(d).append("\n");
		}

		return s.toString();
	}
}
