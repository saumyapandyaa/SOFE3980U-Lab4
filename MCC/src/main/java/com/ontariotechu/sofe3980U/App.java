package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

public class App {
	public static void main(String[] args) {
		String filePath = "model.csv";
		FileReader filereader;
		List<String[]> allData;

		try {
			filereader = new FileReader(filePath);
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			allData = csvReader.readAll();
		} catch (Exception e) {
			System.out.println("Error reading the CSV file");
			return;
		}

		int numClasses = 5;
		int[][] confusionMatrix = new int[numClasses][numClasses];
		double crossEntropyLoss = 0.0;
		int totalSamples = allData.size();

		for (String[] row : allData) {
			int y_true = Integer.parseInt(row[0]) - 1; // Convert to zero-based index
			double[] probabilities = new double[numClasses];

			int y_pred = 0; // Predicted class
			double maxProb = 0.0;

			for (int i = 0; i < numClasses; i++) {
				probabilities[i] = Double.parseDouble(row[i + 1]);
				if (probabilities[i] > maxProb) {
					maxProb = probabilities[i];
					y_pred = i; // Update predicted class
				}
			}

			// Update confusion matrix
			confusionMatrix[y_pred][y_true]++;

			// Compute cross-entropy loss
			crossEntropyLoss += -Math.log(probabilities[y_true] + 1e-9); // Avoid log(0)
		}

		// Calculate final Cross-Entropy loss
		crossEntropyLoss /= totalSamples;

		// Print results
		System.out.printf("CE = %.7f\n", crossEntropyLoss);
		System.out.println("Confusion matrix");
		System.out.println("\t\ty=1\ty=2\ty=3\ty=4\ty=5");

		for (int i = 0; i < numClasses; i++) {
			System.out.print("y^=" + (i + 1) + "\t");
			for (int j = 0; j < numClasses; j++) {
				System.out.print(confusionMatrix[i][j] + "\t");
			}
			System.out.println();
		}
	}
}
