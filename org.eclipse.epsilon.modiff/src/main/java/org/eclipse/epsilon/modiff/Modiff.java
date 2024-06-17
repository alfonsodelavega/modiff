package org.eclipse.epsilon.modiff;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.matcher.IdMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.munidiff.AddedElement;
import org.eclipse.epsilon.modiff.munidiff.ChangedElement;
import org.eclipse.epsilon.modiff.munidiff.Difference;
import org.eclipse.epsilon.modiff.munidiff.MunidiffFactory;
import org.eclipse.epsilon.modiff.munidiff.RemovedElement;
import org.eclipse.epsilon.modiff.output.MatcherBasedLabelProvider;
import org.eclipse.epsilon.modiff.output.UnifiedDiffFormatter;
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

	enum DiffSide {
		FROM, TO
	}

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

	protected Set<String> fromModelIdentifiers = new HashSet<>();
	protected Set<String> toModelIdentifiers = new HashSet<>();

	protected List<String> fromModelDuplicates = new ArrayList<>();
	protected List<String> toModelDuplicates = new ArrayList<>();

	protected Matcher matcher;

	protected MunidiffFactory munidiffFactory = MunidiffFactory.eINSTANCE;
	protected List<Difference> differences = new ArrayList<>();

	/**
	 * Used for proper detection of changes in multi-valued attributes and
	 * element container swaps
	 */
	protected Set<String> markedIdentifiers = new HashSet<>();

	public Modiff(String fromModelFile, String toModelFile) {
		this.fromModelFile = fromModelFile;
		this.toModelFile = toModelFile;

		matcher = new IdMatcher();
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
		return loadModel(fromModelFile, DiffSide.FROM);
	}

	protected Resource loadToModel() throws IOException {
		return loadModel(toModelFile, DiffSide.TO);
	}

	protected Resource loadModel(String modelFile, DiffSide diffSide) throws IOException {

		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createFileURI(modelFile));

		// the pool allows decorating the xml handler to get element lines
		Map<Object, Object> loadOptions = new HashMap<>();
		loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL,
				new ModiffXMLParserPoolImpl(this, diffSide));
		resource.load(loadOptions);

		return resource;
	}

	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}

	public List<Difference> getDifferences() {
		return differences;
	}

	public String getFromModelFile() {
		return fromModelFile;
	}

	public String getToModelFile() {
		return toModelFile;
	}

	public Matcher getMatcher() {
		return matcher;
	}

	protected void identifyDifferences() {

		checkForDuplicates();

		differences = new ArrayList<>();

		for (EObject addedElement : addedElements) {
			boolean matched = false;
			for (EObject removedElement : removedElements) {
				if (matcher.matches(addedElement, removedElement)) {

					registerChangedElement(removedElement, addedElement);

					removedElements.remove(removedElement);
					matched = true;
					break;
				}
			}
			if (!matched) {
				AddedElement added = munidiffFactory.createAddedElement();
				added.setIdentifier(matcher.getIdentifier(addedElement));
				added.setElement(addedElement);
				differences.add(added);
			}
		}

		// any element that remains is a removed one
		for (EObject removedElement : removedElements) {
			RemovedElement removed = munidiffFactory.createRemovedElement();
			removed.setIdentifier(matcher.getIdentifier(removedElement));
			removed.setElement(removedElement);
			differences.add(removed);
		}
	}

	protected ChangedElement createChangedElement(EObject removedElement, EObject addedElement, Matcher matcher) {
		ChangedElement changed = munidiffFactory.createChangedElement();

		changed.setIdentifier(matcher.getIdentifier(removedElement));
		changed.setFromElement(removedElement);
		changed.setToElement(addedElement);
		changed.setMatcher(matcher);

		return changed;
	}

	protected void registerChangedElement(EObject removedElement, EObject addedElement) {

		ChangedElement changedElement = createChangedElement(removedElement, addedElement, matcher);

		// it could be a false positive due to line format changes
		if (changedElement.hasDifferences()) {
			differences.add(changedElement);

			// when an element is moved to a different container,
			//   that new container might not appear in the diff
			for (EReference ref : getContainmentReferences(changedElement.getChangedFeatures())) {
				List<EObject> fromValues = getContainmentReferenceValues(changedElement.getFromElement(), ref);
				List<EObject> toValues = getContainmentReferenceValues(changedElement.getToElement(), ref);

				for (EObject fromValue : fromValues) {
					EObject toValue = findMatch(fromValue, toValues);
					if (toValue == null) {
						EObject removedValue = findMatch(fromValue, removedElements);
						if (removedValue == null || isMarkedForReview(removedValue)) {
							// we have an element container swap, and this is the original place of such element
							// we need to loop the toModel to find its new container to check for differences
							EObject toContainer = findElement(matcher.getIdentifier(fromValue), toModel).eContainer();
							EObject fromContainer = findElement(matcher.getIdentifier(toContainer), fromModel);

							ChangedElement changedContainer = createChangedElement(fromContainer, toContainer, matcher);

							if (changedContainer.hasDifferences()) {
								differences.add(changedContainer);
							}
						}
					}
				}

				for (EObject toValue : toValues) {
					EObject fromValue = findMatch(toValue, fromValues);
					if (fromValue == null) {
						EObject addedValue = findMatch(toValue, addedElements);
						if (addedValue == null || isMarkedForReview(addedValue)) {
							// we have an element container swap, and this is the destination of such element
							// we need to loop the fromModel to find its old container to check for differences
							EObject fromContainer = findElement(matcher.getIdentifier(toValue), fromModel).eContainer();
							EObject toContainer = findElement(matcher.getIdentifier(fromContainer), toModel);

							ChangedElement changedContainer = createChangedElement(fromContainer, toContainer, matcher);

							if (changedContainer.hasDifferences()) {
								differences.add(changedContainer);
							}
						}
					}
				}
			}
		}
	}

	protected EObject findElement(String identifier, Resource model) {
		Iterator<EObject> it = model.getAllContents();
		while (it.hasNext()) {
			EObject candidate = it.next();
			if (identifier.equals(matcher.getIdentifier(candidate))) {
				return candidate;
			}
		}
		return null;
	}

	protected EObject findMatch(EObject value, Collection<EObject> values) {
		for (EObject candidate : values) {
			if (matcher.matches(value, candidate)) {
				return candidate;
			}
		}
		return null;
	}

	protected List<EReference> getContainmentReferences(List<EStructuralFeature> changedFeatures) {
		return changedFeatures.stream()
				.filter(f -> (f instanceof EReference) && (((EReference) f).isContainment()))
				.map(f -> (EReference) f)
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	protected List<EObject> getContainmentReferenceValues(EObject elem, EReference ref) {
		List<EObject> elements = new ArrayList<>();

		Object value = elem.eGet(ref);

		if (ref.isMany()) {
			elements.addAll((List<EObject>) value);
		}
		else if (value != null) {
			elements.add((EObject) value);
		}

		return elements;
	}

	public void registerIfDuplicate(EObject element, DiffSide diffSide) {
		String identifier = matcher.getIdentifier(element);
		if (seenBefore(identifier, diffSide)) {
			switch (diffSide) {
			case FROM:
				fromModelDuplicates.add(identifier);
				break;
			case TO:
				toModelDuplicates.add(identifier);
				break;
			}
		}
	}

	protected boolean seenBefore(String identifier, DiffSide diffSide) {
		boolean seenBefore = false;

		switch (diffSide) {
		case FROM:
			seenBefore = fromModelIdentifiers.contains(identifier);
			if (!seenBefore) {
				fromModelIdentifiers.add(identifier);
			}
			break;
		case TO:
			seenBefore = toModelIdentifiers.contains(identifier);
			if (!seenBefore) {
				toModelIdentifiers.add(identifier);
			}
			break;
		}

		return seenBefore;
	}

	protected void checkForDuplicates() {
		if (!fromModelDuplicates.isEmpty() || !toModelDuplicates.isEmpty()) {
			StringBuilder errorMessage = new StringBuilder();

			errorMessage.append("\n\nID duplicates found in one or both models:\n");
			errorMessage.append(toModelFile)
					.append(": [")
					.append(String.join(", ", fromModelDuplicates))
					.append("]\n");

			errorMessage.append(fromModelFile)
					.append(": [")
					.append(String.join(", ", toModelDuplicates))
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
		UnifiedDiffFormatter formatter =
				new UnifiedDiffFormatter(differences, new MatcherBasedLabelProvider(matcher));

		formatter.setFromModelFile(fromModelFile);
		formatter.setToModelFile(toModelFile);

		return formatter.format();
	}

	public Set<Integer> getModifiedLines(DiffSide diffSide) {
		return diffSide == DiffSide.FROM ? removedLines : addedLines;
	}

	public Set<EObject> getModifiedElements(DiffSide diffSide) {
		return diffSide == DiffSide.FROM ? removedElements : addedElements;
	}

	public void markForReview(EObject element) {
		markedIdentifiers.add(matcher.getIdentifier(element));
	}

	public boolean isMarkedForReview(EObject element) {
		if (element != null && markedIdentifiers.contains(matcher.getIdentifier(element))) {
			return true;
		}
		return false;
	}
}
