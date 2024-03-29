package ai.partB.Minimax;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TD Leaf (Lambda) algorithm
 */
public class TDLeafLambda {
	
	private ArrayList<Double> weights;
	private ArrayList<Double> evals_c1;
	private ArrayList<Double> evals_c2;
	private ArrayList<Double> evals_c3;
	private ArrayList<Double> evals_c4;
	
	// trained weights for H
	public static final double H_WEIGHT_1 = 3.9216343435338348;
	public static final double H_WEIGHT_2 = 1.7138357369197565;
	public static final double H_WEIGHT_3 = 0.7683711097108844;
	public static final double H_WEIGHT_4 = 1.2888768116460996;
	
	// trained weights for V
	public static final double V_WEIGHT_1 = 4.6083601998071915;
	public static final double V_WEIGHT_2 = 1.7138357369197565;
	public static final double V_WEIGHT_3 = 2.323526695332643;
	public static final double V_WEIGHT_4 = 1.2166335034280624;
	
	// initial weights
	public static final double WEIGHT_1 = 1.0;
	public static final double WEIGHT_2 = 1.0;
	public static final double WEIGHT_3 = 1.0;
	public static final double WEIGHT_4 = 1.0;
	
	private Scanner reader;
	
	private static final String FILENAME = "learning/weight.txt";
	
	/**
	 * Initialize a TDLeafLambda environment
	 */
	public TDLeafLambda() {
		weights = new ArrayList<Double>();
		evals_c1 = new ArrayList<Double>();
		evals_c2 = new ArrayList<Double>();
		evals_c3 = new ArrayList<Double>();
		evals_c4 = new ArrayList<Double>();
		
		try {
			// read weights
			reader = new Scanner(new File(FILENAME));
			while (reader.hasNextDouble()) {
				weights.add(reader.nextDouble());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get weight at index
	 * @param index
	 * @return weight: at given index
	 */
	public double getWeight(int index) {
		return weights.get(index);
	}
	
	/**
	 * Add evaluated feature 1 to eval_c1 array
	 * @param eval_c1
	 */
	public void addEvalC1(double eval_c1) {
		evals_c1.add(eval_c1);
	}
	
	/**
	 * Add evaluated feature 2 to eval_c2 array
	 * @param eval_c2
	 */
	public void addEvalC2(double eval_c2) {
		evals_c2.add(eval_c2);
	}
	
	/**
	 * Add evaluated feature 3 to eval_c3 array
	 * @param eval_c3
	 */
	public void addEvalC3(double eval_c3) {
		evals_c3.add(eval_c3);
	}
	
	/**
	 * Add evaluated feature 4 to eval_c4 array
	 * @param eval_c4
	 */
	public void addEvalC4(double eval_c4) {
		evals_c4.add(eval_c4);
	}
	
	/**
	 * Finalize a learning
	 */
	public void finalize() {
		// get size
		int n = evals_c1.size();
		
		ArrayList<Double> evals = new ArrayList<Double>();
		ArrayList<Double> rewards = new ArrayList<Double>();
		for (int i = 0; i < n; i++) {
			// calculate eval(s_i^l,w) and store in an array
			evals.add(evals_c1.get(i)*weights.get(0) + evals_c2.get(i)*weights.get(1) + evals_c3.get(i)*weights.get(2) + evals_c4.get(i)*weights.get(3));
			// calculate r(s_i^l,w) and store in an array
			rewards.add(Math.tanh(evals.get(i)));
		}
		
		double alpha = 1.0;
		double lambda = 0.7;
		
		double sum_1 = 0.0;
		double sum_2 = 0.0;
		double sum_3 = 0.0;
		double sum_4 = 0.0;
		for (int i = 0; i < n-1; i++) {
			// tmp_j = sum dr(s_i^l,w)/dw_j * td
			double td = 0.0;
			for (int m = 0; m < n-1; m++) {
				// td = sum lambda * temporal difference
				td += Math.pow(lambda, m-i) * (rewards.get(m+1) - rewards.get(m));
			}
			sum_1 += Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c1.get(i) * td;
			sum_2 += Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c2.get(i) * td;
			sum_3 += Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c3.get(i) * td;
			sum_4 += Math.pow(1.0/(Math.cosh(evals.get(i))), 2) * evals_c4.get(i) * td;
		}
		
		// update weights; new_w_j <- old_w_j + alpha * tmp_j
		double new_w1 = weights.get(0) + alpha * sum_1;
		double new_w2 = weights.get(1) + alpha * sum_2;
		double new_w3 = weights.get(2) + alpha * sum_3;
		double new_w4 = weights.get(3) + alpha * sum_4;
		
		// write new weights to file
		try {
			FileWriter writer = new FileWriter(new File(FILENAME));
			writer.write(new_w1 + "\n" + new_w2 + "\n" + new_w3 + "\n" + new_w4 + "\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}
