
/* 
 * TestModel.java 
 *   
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;

/**
 * This program tests the model with 30% files which were not used for training
 * 
 * @author Vinay Vasant More
 * @author Amey Nayak
 * @author Rohit Giyanani
 *
 */
public class TestModel {
	static HashSet<String> stopWords = new HashSet<String>();
	static HashMap<Integer, String> nameMapping = new HashMap<Integer, String>();
	static HashMap[] modelMaps;

	/**
	 * main method
	 * 
	 * @param args
	 *            command line arguments not used
	 *
	 */
	public static void main(String args[]) throws IOException {
		// Building hashmaps from model files that we have created for each
		// newsgroup
		File models = new File("model\\");
		File[] modelfiles = models.listFiles();
		modelMaps = new HashMap[modelfiles.length];
		int counter = 0;
		for (File f : modelfiles) {
			if (f.isFile()) {
				nameMapping.put(counter, f.getName());
				modelMaps[counter] = new HashMap<String, Integer>();
				BufferedReader bring = new BufferedReader(new FileReader(f));
				String str = "";
				int count = 0;
				while ((str = bring.readLine()) != null) {
					if (str.equals("")) {
						continue;
					}
					if (str.contains(":")) {
						String str1[] = str.split(":");
						modelMaps[counter].put(str1[0], Integer.parseInt(str1[1]));
						// modelMaps[counter].put(str1[0], (count + 1));
						// count++;
					}
				}
				counter++;
			}
		}

		// Building hashmap of stopwords
		BufferedReader br1 = new BufferedReader(new FileReader("StopWordsList.txt"));
		String str1 = "";
		while ((str1 = br1.readLine()) != null) {
			stopWords.add(str1.trim().toLowerCase());
		}

		TestModel tm = new TestModel();
		int correctCounter = 0;
		int incorrectCounter = 0;
		File source = new File("");
		// Setting newsgroup directory by manual selection
		JFileChooser select = new JFileChooser();
		select.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		select.showSaveDialog(null);
		if (select.getSelectedFile() == null) {
			System.out.println("Please select correct newsgroup directory");
			System.exit(0);
		} else {
			source = select.getSelectedFile();
		}

		// Run the model on 30% test files which were not used for training
		File[] sourceFolders = source.listFiles();
		for (int i = 0; i < sourceFolders.length; i++) {
			File[] sourceFiles = sourceFolders[i].listFiles();
			for (int j = sourceFiles.length - 1; j > (int) (sourceFiles.length * 0.7); j--) {
				if (sourceFiles[j].isFile()) {
					if (tm.buildWordList(sourceFiles[j])) {
						correctCounter++;
					} else {
						incorrectCounter++;
					}
				} else {
				}
			}
		}
		// Priting results
		System.out.println("Correct: " + correctCounter + " Incorrect: " + incorrectCounter + " Accuracy: "
				+ (correctCounter * 100.0 / (correctCounter + incorrectCounter)));

	}

	/**
	 * buildWordList method builds hashmap of words from test file
	 * 
	 * @param File
	 *            test file for which we want build the hashmap
	 *
	 * @return boolean returns true if predicted newsgroup matches expected
	 *         newsgroup else false
	 *
	 */
	boolean buildWordList(File testFile) throws IOException {
		HashMap<String, Integer> test = new HashMap<String, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(testFile));
		String str = "";
		String result = "";
		// while ((str = br.readLine()) != null) {
		// if (str.equals("")) {
		// continue;
		// }
		// if(str.contains("Newsgroups: ")){
		// result=str;
		// }
		// if (!str.contains(":")) {
		// break;
		// }
		// }

		while ((str = br.readLine()) != null) {
			if (str.contains("Newsgroups: ")) {
				result = str;
			}
			if (str.equals("")) {
				continue;
			}
			str = str.trim().toLowerCase();
			str = str.replaceAll("[^a-zA-Z\\s]", "");
			String temp[] = str.split("\\s+");

			for (int i = 0; i < temp.length; i++) {
				if (!temp[i].equals("")) {
					if (!stopWords.contains(temp[i])) {
						if (test.containsKey(temp[i])) {
							// adding filtered words to the hashmap
							test.put(temp[i], test.get(temp[i]) + 1);
						} else {
							test.put(temp[i], 1);
						}
					}
				}
			}
		}

		double max = 0;
		int identifier = 0;
		for (int i = 0; i < modelMaps.length; i++) {
			double score = 0;
			for (String e : test.keySet()) {
				if (modelMaps[i].containsKey(e)) {
					// score1 function used
					score = score + (test.get(e) * score1(i, e));
					// Uncomment this line and comment above one to use score2
					// function instead of score1
					// score = score + (test.get(e) * score2(i,e));
				}
			}
			// System.out.println(nameMapping.get(i)+":"+score);
			if (score > max) {
				max = score;
				identifier = i;
			}
		}
		// System.out.println(result + "-->" + nameMapping.get(identifier));
		return result.contains(nameMapping.get(identifier));
	}

	/**
	 * score1 method
	 * 
	 * @param Integer,
	 *            String Integer i represents which model file we are accessing
	 *            and e represents the key word.
	 *
	 * @return double returns the score for selected word from test file
	 *
	 */
	double score1(int i, String e) {
		return ((Integer) modelMaps[i].get(e) * 1.0);
	}

	/**
	 * score2 method
	 * 
	 * @param Integer,
	 *            String Integer i represents which model file we are accessing
	 *            and e represents the key word.
	 *
	 * @return double returns the score for selected word from test file
	 *
	 */
	double score2(int i, String e) {
		return ((Integer) modelMaps[i].get(e) * 1.0) / modelMaps[i].size();
	}
}