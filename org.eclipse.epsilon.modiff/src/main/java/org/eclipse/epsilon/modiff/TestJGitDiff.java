package org.eclipse.epsilon.modiff;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.HistogramDiff;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;

import io.reflectoring.diffparser.api.DiffParser;
import io.reflectoring.diffparser.api.UnifiedDiffParser;
import io.reflectoring.diffparser.api.model.Diff;

/**
 * Example diffing files with jgit and then parsing the in-memory result
 */
public class TestJGitDiff {

	public static void main(String[] args) throws IOException {

		try (PipedInputStream in = new PipedInputStream()) {
			// file diff (piped streams allow sharing memory but require threading)
			// https://stackoverflow.com/a/1226031
			new Thread(() -> {
				try (PipedOutputStream out = new PipedOutputStream(in)) {
					getDiff(out, "models/comics/left.model", "models/comics/base.model");
				}
				catch (IOException iox) {
					iox.printStackTrace();
				}
			}).start();

			// parsing the diff
			DiffParser parser = new UnifiedDiffParser();
			List<Diff> diffs = parser.parse(in);

			for (Diff diff : diffs) {
				System.out.println(diff);
			}
		}

		System.out.println("Done");
	}

	/**
	 * https://stackoverflow.com/questions/12987364/how-to-diff-with-two-files-by-jgit-without-creating-repo
	 */
	private static void getDiff(OutputStream out, String fromFile, String toFile) {

		// jgit does not generate unified format headers (done elsewhere)
		// required by diffparser, so we do it by hand
		PrintWriter writer = new PrintWriter(out);
		writer.println(String.format("--- %s", fromFile));
		writer.println(String.format("+++ %s", toFile));
		writer.flush();

		try {
			RawText rt1 = new RawText(new File(fromFile));
			RawText rt2 = new RawText(new File(toFile));
			EditList diffList = new EditList();

			diffList.addAll(new HistogramDiff().diff(RawTextComparator.DEFAULT, rt1, rt2));
			try (DiffFormatter f = new DiffFormatter(out)) {
				f.format(diffList, rt1, rt2);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
