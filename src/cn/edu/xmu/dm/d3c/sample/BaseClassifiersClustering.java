package cn.edu.xmu.dm.d3c.sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import weka.core.Instances;

import cn.edu.xmu.dm.d3c.clustering.KMeanz;
import cn.edu.xmu.dm.d3c.core.Parameterz;
import cn.edu.xmu.dm.d3c.utils.ClassifyResultArffLoader;

public class BaseClassifiersClustering {

	public List<Integer> clusterBaseClassifiers(int numclusters) throws Exception {
		
		int numClusters = numclusters;
		
		List<Integer> chooseClassifiers =new ArrayList<Integer>();

		KMeanz km = new KMeanz(numClusters);

		Instances classifyResult = ClassifyResultArffLoader.loadClassifyResultFromArff("");
		
		List<Double> correctRateArray = ClassifyResultArffLoader.loadCorrectRate("");

		km.buildClusterer(classifyResult, chooseClassifiers,
				correctRateArray);
		
		String fchooseClassifiers = "data/chooseClassifiers.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(fchooseClassifiers));

		for (int i = 0; i < chooseClassifiers.size(); i++) {
			writer.write(chooseClassifiers.get(i).toString());
			writer.newLine();
		}

		writer.flush();
		writer.close();
		
		return chooseClassifiers;

	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		BaseClassifiersClustering bcc = new BaseClassifiersClustering();
		
		if (args.length < 1) {
			System.out.println("Usage:");
			System.out.println("java -jar LibD3CClustering.jar 100");
			System.exit(-1);
		}
		
		int numclusters = Integer.parseInt(args[0]);
		List<Integer> chooseClassifiers = bcc.clusterBaseClassifiers(numclusters);
		
		System.out.println("choose classifiers:");
		for (int i = 0; i < chooseClassifiers.size(); i++) {
			System.out.print(chooseClassifiers.get(i));
			System.out.print(" ");
		}
		System.out.println();
	}
}
