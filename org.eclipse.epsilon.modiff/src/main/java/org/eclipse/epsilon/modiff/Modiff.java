package org.eclipse.epsilon.modiff;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.matcher.IDBasedMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.utils.PrettyPrint;
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
		modiff.setMatcher(new IDBasedMatcher());
		modiff.compare();

		System.out.println("Done");
	}

	protected String fromModelFile;
	protected String toModelFile;

	protected Set<Integer> addedLines = new HashSet<>();
	protected Set<Integer> removedLines = new HashSet<>();

	protected Set<EObject> addedElements = new HashSet<>();
	protected Set<EObject> removedElements = new HashSet<>();

	protected Resource fromModel;
	protected Resource toModel;

	protected Matcher matcher;

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
	 */
	public void compare() throws IOException {

		// get line diff of models
		List<Diff> diffs = null;
		try (PipedInputStream in = new PipedInputStream()) {
			// file diff (piped streams allow sharing memory)
			// they usually require threading but here writting finishes before reading
			// https://stackoverflow.com/a/1226031
			try (PipedOutputStream out = new PipedOutputStream(in)) {
				getLineDiff(out);
			}
			catch (IOException iox) {
				iox.printStackTrace();
			}

			// parse the diffs to a workable format
			diffs = new UnifiedDiffParser().parse(in);
		}

		// get added and removed lines (TO and FROM lines respectively) 
		parseLineDiff(diffs);

		// load models and capture diff elements
		fromModel = loadFromModel();
		toModel = loadToModel();

		// match diff elements and identify actual differences
		// report
		identifyDifferences();

	}

	protected void getLineDiff(OutputStream out) throws IOException {

		// jgit does not generate unified format headers (done elsewhere)
		// required by diffparser, so we do it by hand
		PrintWriter writer = new PrintWriter(out);
		writer.printf("--- %s\n", fromModelFile);
		writer.printf("+++ %s\n", toModelFile);
		writer.flush();

		RawText rt1 = new RawText(new File(fromModelFile));
		RawText rt2 = new RawText(new File(toModelFile));
		EditList diffList = new EditList();

		// FIXME: we would probably want to omit trailing and leading whitespace
		//   (container changes modify indentations)
		diffList.addAll(new HistogramDiff().diff(RawTextComparator.DEFAULT, rt1, rt2));
		try (DiffFormatter f = new DiffFormatter(out)) {
			f.format(diffList, rt1, rt2);
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

	protected void identifyDifferences() {
		for (EObject addedElement : addedElements) {
			boolean matched = false;
			for (EObject removedElement : removedElements) {
				if (matcher.matches(addedElement, removedElement)) {
					System.out.println("*************************************");
					System.out.println("Modified element");
					System.out.printf("- %s\n", PrettyPrint.featuresMap(removedElement));
					System.out.printf("+ %s\n", PrettyPrint.featuresMap(addedElement));

					matched = true;
					removedElements.remove(removedElement);
					break;
				}
			}
			if (!matched) {
				System.out.println("*************************************");
				System.out.println("New Element");
				System.out.printf("+ %s\n", PrettyPrint.featuresMap(addedElement));
			}
		}
		for (EObject removedElement : removedElements) {
			System.out.println("*************************************");
			System.out.println("Removed element");
			System.out.printf("- %s\n", PrettyPrint.featuresMap(removedElement));
		}
	}
}
