package org.eclipse.epsilon.modiff.functions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.matcher.EcoreMatcher;
import org.eclipse.epsilon.modiff.matcher.IdOrNameMatcher;
import org.eclipse.epsilon.modiff.matcher.Matcher;
import org.eclipse.epsilon.modiff.output.LabelProvider;
import org.eclipse.epsilon.modiff.output.MatcherBasedLabelProvider;
import org.eclipse.epsilon.modiff.output.graphical.PlantumlEcoreFormatter;
import org.eclipse.epsilon.modiff.output.graphical.PlantumlFormatter;
import org.eclipse.epsilon.modiff.output.textual.UnifiedDiffFormatter;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.HistogramDiff;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ModiffFunction implements HttpFunction {

	public static void main(String[] args) throws Exception {

		ModiffFunction function = new ModiffFunction();
		
		System.out.println(function.getDiff("a\nb\n", "a\nb\nc"));

		// test function with ecore model
		String fromModelContents = Files.readString(Paths.get("models/ecore/00-from.ecore"));
		String toModelContents = Files.readString(Paths.get("models/ecore/21-deleteSkill.ecore"));

		String modelName = "repairshop.ecore";
		Modiff modiff = function.getModiff(modelName, fromModelContents, toModelContents);

		System.out.println(function.getTextualMunidiff(modiff));
		System.out.println(function.getGraphicalMunidiff(modelName, modiff));

		// test function with custom metamodel
		modelName = "repairshop.model";
		fromModelContents = Files.readString(Paths.get("models/repairshop/00-from.model"));
		toModelContents = Files.readString(Paths.get("models/repairshop/61-moveJob.model"));

		String metamodelContents = Files.readString(Paths.get("models/repairshop/repairshop.ecore"));
		function.registerMetamodel(metamodelContents);

		modiff = function.getModiff(modelName, fromModelContents, toModelContents);
		System.out.println(function.getTextualMunidiff(modiff));
		System.out.println(function.getGraphicalMunidiff(modelName, modiff));
	}

	public ModiffFunction() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl());
	}

	public void registerMetamodel(String metamodelContent) throws IOException {
		ResourceSet ecoreResourceSet = new ResourceSetImpl();

		Resource ecoreResource = ecoreResourceSet.createResource(
				URI.createFileURI("/metamodel.ecore"));

		ecoreResource.load(new ByteArrayInputStream(metamodelContent.getBytes()), Collections.EMPTY_MAP);

		for (EObject o : ecoreResource.getContents()) {
			EPackage ePackage = (EPackage) o;
			EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
		}
	}

	public void service(final HttpRequest request, final HttpResponse response) throws Exception {
		response.appendHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");

		if ("OPTIONS".equals(request.getMethod())) {
			response.appendHeader("Access-Control-Allow-Methods", "GET");
			response.appendHeader("Access-Control-Allow-Headers", "Content-Type");
			response.appendHeader("Access-Control-Max-Age", "3600");
			response.setStatusCode(HttpURLConnection.HTTP_NO_CONTENT);
			return;
		}
		else {
			JsonObject responseJson = new JsonObject();

			try {
				serviceImpl(getJsonObject(request), responseJson);
			}
			catch (Throwable t) {
				responseJson.addProperty("output", t.getMessage());
				responseJson.addProperty("error", t.getMessage());
			}

			response.getWriter().write(responseJson.toString());
		}
	}

	protected void serviceImpl(JsonObject request, JsonObject response) throws Exception {
		String modelName = request.get("modelName").getAsString();

		String fromModel = request.get("fromModel").getAsString() + "\n";
		String toModel = request.get("toModel").getAsString() + "\n";

		if (request.has("metamodel")) {
			registerMetamodel(request.get("metamodel").getAsString());
		}

		response.addProperty("diff", getDiff(fromModel, toModel));

		Modiff modiff = getModiff(modelName, fromModel, toModel);

		response.addProperty("textual-munidiff", getTextualMunidiff(modiff));
		response.addProperty("graphical-munidiff", getGraphicalMunidiff(modelName, modiff));
	}

	protected String getDiff(String fromModel, String toModel) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		RawText fromText = new RawText(fromModel.getBytes());
		RawText toText = new RawText(toModel.getBytes());

		EditList diffList = new EditList();
		diffList.addAll(new HistogramDiff().diff(RawTextComparator.DEFAULT, fromText, toText));

		if (!diffList.isEmpty()) {
			// jgit does not generate unified format headers (done elsewhere)
			// required by diffparser, so we do it by hand
			PrintWriter writer = new PrintWriter(out);
			writer.printf("--- %s\n", "fromModel");
			writer.printf("+++ %s\n", "toModel");
			writer.flush();
			try (DiffFormatter f = new DiffFormatter(out)) {
				f.format(diffList, fromText, toText);
			}
		}

		return out.toString(StandardCharsets.UTF_8);
	}

	protected Modiff getModiff(String modelName, String fromModelContent,
			String toModelContent) throws IOException {

		Modiff modiff = new Modiff(modelName, fromModelContent, toModelContent);

		Matcher matcher = new IdOrNameMatcher();
		if (modelName.endsWith(".ecore")) {
			matcher = new EcoreMatcher();
		}
		modiff.setMatcher(matcher);
		modiff.compare();

		return modiff;
	}

	protected JsonObject getJsonObject(HttpRequest request) throws Exception {
		String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		return getJsonObject(json);
	}

	protected JsonObject getJsonObject(String json) {
		return JsonParser.parseString(json).getAsJsonObject();
	}

	public String getGraphicalMunidiff(String modelName, Modiff modiff) {
		LabelProvider labelProvider = new MatcherBasedLabelProvider(modiff.getMatcher());
		if (modelName.endsWith(".ecore")) {
			return new PlantumlEcoreFormatter(modiff.getMunidiff(), labelProvider).format();
		}
		return new PlantumlFormatter(modiff.getMunidiff(), labelProvider).format();
	}

	public String getTextualMunidiff(Modiff modiff) {
		LabelProvider labelProvider = new MatcherBasedLabelProvider(modiff.getMatcher());
		return new UnifiedDiffFormatter(modiff.getMunidiff(), labelProvider).format();
	}
}
