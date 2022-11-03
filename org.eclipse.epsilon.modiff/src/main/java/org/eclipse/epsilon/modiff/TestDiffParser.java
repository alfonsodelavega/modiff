package org.eclipse.epsilon.modiff;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reflectoring.diffparser.api.DiffParser;
import io.reflectoring.diffparser.api.UnifiedDiffParser;
import io.reflectoring.diffparser.api.model.Diff;

/**
 * Example testing the diff parser from a diff file stored on disk
 */
public class TestDiffParser {

	public static void main(String[] args) throws IOException {
		DiffParser parser = new UnifiedDiffParser();
		List<Diff> diffs = parser.parse(new File("models/comics/left.diff"));

		for (Diff diff : diffs) {
			System.out.println(diff);
		}

		System.out.println("Done");
	}

}
