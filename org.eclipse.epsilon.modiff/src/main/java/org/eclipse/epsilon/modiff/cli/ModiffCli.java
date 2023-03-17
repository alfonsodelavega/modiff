package org.eclipse.epsilon.modiff.cli;
import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.modiff.Modiff;
import org.eclipse.epsilon.modiff.matcher.IDBasedMatcher;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "ModiffApp", mixinStandardHelpOptions = true, version = "1.0",
		description = "CLI-based Java application that reproduces the behavior of the given main program using the Picocli library.")
public class ModiffCli implements Runnable {

	@Option(names = { "-ecore" }, required = true, description = "List of Ecore file paths to load")
	private String[] ecoreFiles;

	@Option(names = { "-base" }, required = true, description = "Base (old) model file path")
	private String baseModelFile;

	@Option(names = { "-left" }, required = true, description = "Left (new) model file path")
	private String leftModelFile;

	public void run() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		ResourceSet ecoreResourceSet = new ResourceSetImpl();

		for (String ecoreFile : ecoreFiles) {
			Resource ecoreResource = ecoreResourceSet.createResource(
					URI.createFileURI(new File(ecoreFile).getAbsolutePath()));
			try {
				ecoreResource.load(null);
			}
			catch (IOException e) {
				System.err.println("Failed to load Ecore file: " + ecoreFile);
				e.printStackTrace();
				return;
			}

			for (EObject o : ecoreResource.getContents()) {
				EPackage ePackage = (EPackage) o;
				EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
			}
		}

		Modiff modiff = new Modiff(baseModelFile, leftModelFile);
		modiff.setMatcher(new IDBasedMatcher());

		try {
			modiff.compare();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Done");
	}

	public static void main(String[] args) {
		int exitCode = new CommandLine(new ModiffCli()).execute(args);
		System.exit(exitCode);
	}
}