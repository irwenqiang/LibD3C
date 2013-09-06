package cn.edu.xmu.dm.d3c.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Instances;

public class ClassifyResultArffLoader {

	public static void writeClassifyResult(Instances input, String filepath,
			List<Integer> available_cfsArray,
			List<List<Integer>> available_classifyRightOrWrong,
			List<List<Integer>> available_classifyErrorNo,
			List<Double> correctRateArray,
			List<List<double[]>> available_classifyDistributeForInstances
			) throws IOException {
		
		// 初始化工具类
		InstanceUtil myutil = new InstanceUtil();
		// 产生分类结果的文件
		myutil.createClassifyResultFile(input.numInstances(),
				available_classifyRightOrWrong);

		String fcfsArray = "data/cfsArray.txt";
		String fclassifyRightOrWrong = "data/classifyRightOrWrong.txt";
		String fclassifyErrorNo = "data/classifyErrorNo.txt";
		String fcorrectRateArray = "data/correctRateArray.txt";
		String fclassifyDistributeForInstances = "data/classifyDistributeForInstances.txt";
		
		//
		System.out.println(fcorrectRateArray);
		//
		BufferedWriter writer = new BufferedWriter(new FileWriter(fcorrectRateArray));

		for (int i = 0; i < correctRateArray.size(); i++) {
			writer.write(correctRateArray.get(i).toString());
			writer.newLine();
		}

		writer.flush();
		writer.close();
		
		BufferedWriter cfsArray = new BufferedWriter(new FileWriter(fcfsArray));
		BufferedWriter classifyRightOrWrongWriter = new BufferedWriter(new FileWriter(fclassifyRightOrWrong));
		BufferedWriter classifyErrorNoWriter = new BufferedWriter(new FileWriter(fclassifyErrorNo));
		BufferedWriter classifyDistributeForInstancesWriter = new BufferedWriter(new FileWriter(fclassifyDistributeForInstances));

		
		for (int i = 0; i < available_classifyRightOrWrong.size(); i++) {
			
			cfsArray.write(available_cfsArray.get(i).toString());
			
			for (int j = 0; j < available_classifyRightOrWrong.get(i).size(); j++) {
				classifyRightOrWrongWriter.write(available_classifyRightOrWrong.get(i).get(j).toString());
				classifyRightOrWrongWriter.write(",");
				
			}
			
			for (int j = 0; j < available_classifyErrorNo.get(i).size(); j++) {	
				classifyErrorNoWriter.write(available_classifyErrorNo.get(i).get(j).toString());
				classifyErrorNoWriter.write(",");
				
			}
			
			for (int j = 0; j < available_classifyDistributeForInstances.get(i).size(); j++) {
				double[] p = available_classifyDistributeForInstances.get(i).get(j);
				classifyDistributeForInstancesWriter.write(String.valueOf(p[0]));
				classifyDistributeForInstancesWriter.write("\t");
				classifyDistributeForInstancesWriter.write(String.valueOf(p[1]));
				classifyDistributeForInstancesWriter.write(",");
				
			}
			
			cfsArray.newLine();
			classifyRightOrWrongWriter.newLine();
			classifyErrorNoWriter.newLine();
			classifyDistributeForInstancesWriter.newLine();
		}
		
		cfsArray.flush();
		cfsArray.close();
		
		classifyRightOrWrongWriter.flush();
		classifyRightOrWrongWriter.close();
		
		classifyErrorNoWriter.flush();
		classifyErrorNoWriter.close();
		
		classifyDistributeForInstancesWriter.flush();
		classifyDistributeForInstancesWriter.close();
	}

	public static Instances loadClassifyResultFromArff(String filepath)
			throws Exception {
		// 初始化工具类
		InstanceUtil myutil = new InstanceUtil();

		myutil.getJarPath(InstanceUtil.class);

		String tempPath ="data/ClassifyResult.arff";
		// 将分类结果读入形成表示各个分类器的实例
		Instances classifyResult = myutil.getInstances(tempPath);
		return classifyResult;
	}

	public static List<Integer> loadCfsArray(String filepath)
			throws Exception {

		filepath = "data/cfsArray.txt";

		List<Integer> cfsArray = new ArrayList<Integer>();

		File file = new File(filepath);
		BufferedReader reader = null;

		try {
		
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				cfsArray.add(Integer.parseInt(tempString));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return cfsArray;
	}
	
	public static List<Double> loadCorrectRate(String filepath)
			throws Exception {

		filepath = "data/correctRateArray.txt";

		List<Double> correctRateArray = new ArrayList<Double>();

		File file = new File(filepath);
		BufferedReader reader = null;

		try {
		
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				correctRateArray.add(Double.parseDouble(tempString));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return correctRateArray;
	}
	
	public static List<List<Integer>> loadClassifyRightOrWrong(String filepath)
			throws Exception {

		filepath = "data/classifyRightOrWrong.txt";

		List<List<Integer>> classifyRightOrWrong = new ArrayList<List<Integer>>();

		File file = new File(filepath);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {				
				String[] tempStrings = tempString.split(",");
				List<Integer> lst = new ArrayList<Integer>();
				for (int i = 0; i < tempStrings.length; i++) {
					lst.add(Integer.parseInt(tempStrings[i]));
				}
				classifyRightOrWrong.add(lst);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return classifyRightOrWrong;
	}
	
	public static List<List<Integer>> loadClassifyRightOrWrong4Diversity(String filepath)
			throws Exception {

		List<List<Integer>> classifyRightOrWrong = new ArrayList<List<Integer>>();

		File file = new File(filepath);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {				
				String[] tempStrings = tempString.split(",");
				List<Integer> lst = new ArrayList<Integer>();
				for (int i = 0; i < tempStrings.length; i++) {
					lst.add(Integer.parseInt(tempStrings[i]));
				}
				classifyRightOrWrong.add(lst);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return classifyRightOrWrong;
	}
	
	public static List<List<double[]>> loadClassifyDistributeForInstances(String filepath)
			throws Exception {

		filepath = "data/classifyDistributeForInstances.txt";

		List<List<double[]>> classifyDistributeForInstances = new ArrayList<List<double[]>>();

		File file = new File(filepath);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {				
				String[] tempStrings = tempString.split(",");
				List<double[]> lst = new ArrayList<double[]>();
				for (int i = 0; i < tempStrings.length; i++) {
					double[] p = new double[2];
					p[0] = Double.parseDouble(tempStrings[i].split("\t")[0]);
					p[1] = Double.parseDouble(tempStrings[i].split("\t")[1]);
					lst.add(p);
				}
				classifyDistributeForInstances.add(lst);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return classifyDistributeForInstances;
	}
	
	public static List<List<Integer>> loadClassifyErrorNo(String filepath)
			throws Exception {

		filepath = "data/classifyErrorNo.txt";

		List<List<Integer>> classifyErrorNo = new ArrayList<List<Integer>>();

		File file = new File(filepath);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String[] tempStrings = tempString.split(",");
				List<Integer> lst = new ArrayList<Integer>();
				for (int i = 0; i < tempStrings.length; i++) {
					lst.add(Integer.parseInt(tempStrings[i]));
				}
				classifyErrorNo.add(lst);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return classifyErrorNo;
	}
	
	public static List<Integer> loadChooseClassifiers(String filepath)
			throws Exception {

		filepath = "data/chooseClassifiers.txt";

		List<Integer> chooseClassifiers = new ArrayList<Integer>();

		File file = new File(filepath);
		BufferedReader reader = null;

		try {
		
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				chooseClassifiers.add(Integer.parseInt(tempString));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return chooseClassifiers;
	}
}
