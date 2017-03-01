
/* 
 * ModelBuild.java 
 *   
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.Map.Entry;
import javax.swing.JFileChooser;

/**
 * This program builds the model with 70% files in each newsgroup directory
 * 
 * @author Vinay Vasant More
 * @author Amey Nayak
 * @author Rohit Giyanani
 *
 */
public class ModelBuild {
	static HashSet<String> stopWords = new HashSet<String>();
	static HashMap<String, Integer> bagOfWords = new HashMap<String, Integer>();

	/**
	 * main method
	 * 
	 * @param args
	 *            command line arguments not used
	 *
	 */
	public static void main(String args[]) throws IOException {
		BufferedReader br1 = new BufferedReader(new FileReader("StopWordsList.txt"));

		// Setting newsgroup directory by manual selection
		JFileChooser select = new JFileChooser();
		select.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		select.showSaveDialog(null);
		File fd = select.getSelectedFile();

		// Building hashmap of stopwords
		String str1 = "";
		while ((str1 = br1.readLine()) != null) {
			stopWords.add(str1.trim().toLowerCase());
		}

		// Printing time taken for model to build
		double start = System.currentTimeMillis();
		extractFromAllFolders(fd);
		double end = System.currentTimeMillis();
		System.out.println("Model built in " + (end - start) + "milliseconds");
	}

	/**
	 * extractFromAllFolders method builds model files by extracting 70% files
	 * from selected folder
	 * 
	 * @param File
	 *            directory path for newsgroups
	 *
	 */
	static void extractFromAllFolders(File fd) throws IOException {
		bagOfWords.clear();
		int counter = 0;
		boolean flag = true;
		File[] files = fd.listFiles();
		int totalFiles = (int) (files.length * 0.7);
		String folderName = fd.getName();
		for (File f : files) {
			if (f.isFile()) {
				counter++;
				if (counter < totalFiles) {
					extractFromFile(f);
				} else {
					break;
				}
			} else {
				flag = false;
				extractFromAllFolders(f);
			}
		}
		if (flag) {
			List list = new LinkedList(bagOfWords.entrySet());
			Object[] objList = bagOfWords.entrySet().toArray();
			Arrays.sort(objList, new Comparator() {
				@Override
				public int compare(Object arg0, Object arg1) {
					// TODO Auto-generated method stub
					return ((Map.Entry<String, Integer>) arg1).getValue()
							.compareTo(((Map.Entry<String, Integer>) arg0).getValue());
				}
			});

			File dir = new File("model");
			dir.mkdirs();

			BufferedWriter bw = new BufferedWriter(new FileWriter("model\\" + folderName));
			for (int j = 0; j < objList.length; j++) {
				bw.write(((Map.Entry<String, Integer>) objList[j]).getKey() + ":"
						+ ((Map.Entry<String, Integer>) objList[j]).getValue() + "\n");
			}
			bw.close();
		}
	}

	/**
	 * extractFromFile method extract words from each file and updates common
	 * hashmap for corresponding newsgroup
	 * 
	 * @param File
	 *            file from which we want to extract words
	 *
	 */
	static void extractFromFile(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String str = "";
		// while ((str = br.readLine()) != null) {
		// if (str.equals("")) {
		// continue;
		// }
		// if (!str.contains(":")) {
		// break;
		// }
		// }

		while ((str = br.readLine()) != null) {
			if (str.equals("")) {
				continue;
			}
			if(str.contains("Newsgroups: ")){
				str=str.substring(12);
			}
			str = str.trim().toLowerCase();
			str = str.replaceAll("[^a-zA-Z\\s]", "");
			String temp[] = str.split("\\s+");

			for (int i = 0; i < temp.length; i++) {
				if (!temp[i].equals("")) {
					if (!stopWords.contains(temp[i])) {
						if (bagOfWords.containsKey(temp[i])) {
							bagOfWords.put(temp[i], bagOfWords.get(temp[i]) + 1);
						} else {
							bagOfWords.put(temp[i], 1);
						}
					}
				}
			}
		}
	}
}