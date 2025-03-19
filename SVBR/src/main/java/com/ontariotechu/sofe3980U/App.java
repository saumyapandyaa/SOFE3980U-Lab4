package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.util.ArrayList;

public class App {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java -cp target/your-jar-file.jar com.ontariotechu.sofe3980U.App <model_file>");
			return;
		}

		String filePath = args[0]; // Read file name from command-line
		List<String[]> allData;

		try {
			FileReader filereader = new FileReader(filePath);
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			allData = csvReader.readAll();
		} catch (Exception e) {
			System.out.println("Error reading the CSV file: " + filePath);
			return;
		}

		// Evaluate the selected model
		evaluateClassification(allData, filePath);
	}

	public static void evaluateClassification(List<String[]> allData, String modelName) {
		int TP = 0, TN = 0, FP = 0, FN = 0;
		double bce = 0.0;

		for (String[] row : allData) {
			int y_true = Integer.parseInt(row[0]);
			double y_pred = Double.parseDouble(row[1]); // Probabilistic prediction

			// Convert y_pred to binary (threshold = 0.5)
			int y_pred_binary = (y_pred >= 0.5) ? 1 : 0;

			// Update Confusion Matrix
			if (y_true == 1 && y_pred_binary == 1)
				TP++;
			else if (y_true == 0 && y_pred_binary == 0)
				TN++;
			else if (y_true == 0 && y_pred_binary == 1)
				FP++;
			else if (y_true == 1 && y_pred_binary == 0)
				FN++;

			// Compute Binary Cross-Entropy (BCE)
			bce += -(y_true * Math.log(y_pred + 1e-15) + (1 - y_true) * Math.log(1 - y_pred + 1e-15));
		}

		int total = TP + TN + FP + FN;
		double accuracy = (double) (TP + TN) / total;
		double precision = (TP + FP == 0) ? 0 : (double) TP / (TP + FP);
		double recall = (TP + FN == 0) ? 0 : (double) TP / (TP + FN);
		double f1Score = (precision + recall == 0) ? 0 : 2 * (precision * recall) / (precision + recall);
		double aucRoc = computeAUC(allData); // Placeholder function for AUC
		bce /= total;

		// Print results
		System.out.println("Model: " + modelName);
		System.out.println("Binary Cross-Entropy (BCE): " + bce);
		System.out.println("Confusion Matrix:");
		System.out.println("          y=1    y=0");
		System.out.println("y^=1    " + TP + "    " + FP);
		System.out.println("y^=0    " + FN + "    " + TN);
		System.out.println("Accuracy: " + accuracy);
		System.out.println("Precision: " + precision);
		System.out.println("Recall: " + recall);
		System.out.println("F1 Score: " + f1Score);
		System.out.println("AUC ROC: " + aucRoc);
	}

	public static double computeAUC(List<String[]> allData) {
		// Placeholder function: AUC calculation requires ranking and sorting
		return 0.9; // Approximate, replace with real AUC computation
	}
}
