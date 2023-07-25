package org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.emfCompare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class PlainDiffResultsGenerator {

	public static String performDiff(String fromFile, String toFile) throws IOException {

		String fullFromFile = new File(fromFile).getAbsolutePath();
		String fullToFile = new File(toFile).getAbsolutePath();

		// Construct the diff command as an array of strings
		String[] command = { "diff", "-u", fullFromFile, fullToFile };
		for (String s : command) {
			System.out.print(s + " ");
		}
		System.out.println();

		// Execute the diff command and capture the output
		Process process = Runtime.getRuntime().exec(command);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		StringBuilder diffResult = new StringBuilder();
		String line;

		// Capture standard output
		while ((line = stdInput.readLine()) != null) {
			diffResult.append(line).append(System.lineSeparator());
		}

		// Capture error output
		StringBuilder errorOutput = new StringBuilder();
		while ((line = stdError.readLine()) != null) {
			errorOutput.append(line).append(System.lineSeparator());
		}

		// Wait for the process to finish and check the exit status
		try {
			process.waitFor();
		}
		catch (InterruptedException e) {
			throw new IOException("Diff command execution interrupted.", e);
		}

		return diffResult.toString();
	}

	public static void store(String testCase, String report) {
		File outputFolder = new File("output");
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}
		File file = new File(outputFolder, testCase);

		try (FileWriter writer = new FileWriter(file)) {
			writer.write(report);
			System.out.println("Report saved to file: " + file.getAbsolutePath());
		}
		catch (IOException e) {
			System.err.println("Error while saving the string to the file: " + e.getMessage());
		}
	}

	public static void main(String[] args) throws IOException {

		String folder = "src/org/eclipse/epsilon/modiff/emfcompare/munidiff/tests/emfCompare/req/data/";

		String[][] cases = {
				{ "a01-plain.diff", "a1/right.nodes", "a1/left.nodes" },
				{ "a02-plain.diff", "a2/right.nodes", "a2/left.nodes" },
				{ "a03-plain.diff", "a3/right.nodes", "a3/left.nodes" },
				{ "a04-plain.diff", "a4/right.nodes", "a4/left.nodes" },
				{ "a05-plain.diff", "a5/right.nodes", "a5/left.nodes" },
				{ "a06-plain.diff", "a6/right.nodes", "a6/left.nodes" },
				{ "a07-plain.diff", "a7/right.nodes", "a7/left.nodes" },
				{ "a08-plain.diff", "a8/right.nodes", "a8/left.nodes" },
				{ "a09-plain.diff", "a9/right.nodes", "a9/left.nodes" },
				{ "a10-plain.diff", "a10/right.nodes", "a10/left.nodes" },
				{ "a11-plain.diff", "a11/right.nodes", "a11/left.nodes" },

				{ "c1-plain.diff", "c1/right.nodes", "c1/left.nodes" },
				{ "c2-plain.diff", "c2/right.nodes", "c2/left.nodes" },
				{ "c3-plain.diff", "c3/right.nodes", "c3/left.nodes" },
				{ "c4-plain.diff", "c4/right.nodes", "c4/left.nodes" },
				{ "c5-plain.diff", "c5/right.nodes", "c5/left.nodes" }
		};

		for (String[] c : cases) {
			String fromFile = folder + c[1];
			String toFile = folder + c[2];

			String diffResult = performDiff(fromFile, toFile);
			store(c[0], diffResult);
		}
		System.out.println("Done");
	}

}
