package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Evaluate Single Variable Continuous Regression
 */
public class App {
	public static void main(String[] args) {
		// List of model CSV files
		String[] modelFiles = { "model_1.csv", "model_2.csv", "model_3.csv" };

		// Arrays to store evaluation metrics for each model
		double[] mseValues = new double[3];
		double[] maeValues = new double[3];
		double[] mareValues = new double[3];

		// Process each CSV file
		for (int i = 0; i < modelFiles.length; i++) {
			String filePath = modelFiles[i];
			FileReader filereader;
			List<String[]> allData;

			// Read CSV File
			try {
				filereader = new FileReader(filePath);
				CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
				allData = csvReader.readAll();
			} catch (Exception e) {
				System.out.println("Error reading the CSV file: " + filePath);
				return;
			}

			// Lists to store true values and predicted values
			List<Double> trueValues = new ArrayList<>();
			List<Double> predictedValues = new ArrayList<>();

			// Read values from CSV
			for (String[] row : allData) {
				double y_true = Double.parseDouble(row[0]);
				double y_pred = Double.parseDouble(row[1]);

				trueValues.add(y_true);
				predictedValues.add(y_pred);
			}

			// Calculate MSE, MAE, MARE
			mseValues[i] = calculateMSE(trueValues, predictedValues);
			maeValues[i] = calculateMAE(trueValues, predictedValues);
			mareValues[i] = calculateMARE(trueValues, predictedValues);

			// Print results for each model
			System.out.println("\nResults for " + filePath);
			System.out.println("MSE = " + mseValues[i]);
			System.out.println("MAE = " + maeValues[i]);
			System.out.println("MARE = " + mareValues[i]);
		}

		// Call compareModels function with calculated values
		compareModels(mseValues[0], mseValues[1], mseValues[2],
				maeValues[0], maeValues[1], maeValues[2],
				mareValues[0], mareValues[1], mareValues[2]);
	}

	// Method to calculate Mean Squared Error (MSE)
	public static double calculateMSE(List<Double> trueValues, List<Double> predictedValues) {
		double sumSquaredErrors = 0.0;
		int N = trueValues.size();

		for (int i = 0; i < N; i++) {
			double error = trueValues.get(i) - predictedValues.get(i);
			sumSquaredErrors += Math.pow(error, 2);
		}

		return sumSquaredErrors / N;
	}

	public static void compareModels(double mse1, double mse2, double mse3,
			double mae1, double mae2, double mae3,
			double mare1, double mare2, double mare3) {
		System.out.println("\nBest Model Analysis:");

		System.out.println("According to MSE, the best model is: " +
				(mse1 < mse2 && mse1 < mse3 ? "model_1.csv" : mse2 < mse3 ? "model_2.csv" : "model_3.csv"));

		System.out.println("According to MAE, the best model is: " +
				(mae1 < mae2 && mae1 < mae3 ? "model_1.csv" : mae2 < mae3 ? "model_2.csv" : "model_3.csv"));

		System.out.println("According to MARE, the best model is: " +
				(mare1 < mare2 && mare1 < mare3 ? "model_1.csv" : mare2 < mare3 ? "model_2.csv" : "model_3.csv"));
	}

	// Method to calculate Mean Absolute Error (MAE)
	public static double calculateMAE(List<Double> trueValues, List<Double> predictedValues) {
		double sumAbsoluteErrors = 0.0;
		int N = trueValues.size();

		for (int i = 0; i < N; i++) {
			sumAbsoluteErrors += Math.abs(trueValues.get(i) - predictedValues.get(i));
		}

		return sumAbsoluteErrors / N;
	}

	// Method to calculate Mean Absolute Relative Error (MARE)
	public static double calculateMARE(List<Double> trueValues, List<Double> predictedValues) {
		double sumRelativeErrors = 0.0;
		int N = trueValues.size();

		for (int i = 0; i < N; i++) {
			if (trueValues.get(i) != 0) { // Avoid division by zero
				sumRelativeErrors += Math.abs((trueValues.get(i) - predictedValues.get(i)) / trueValues.get(i));
			}
		}

		return sumRelativeErrors / N;
	}
}
