package cn.edu.xmu.dm.d3c.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.core.Instances;
import cn.edu.xmu.dm.d3c.core.CircleCombine;
import cn.edu.xmu.dm.d3c.selectivealgorithms.EBSS;
import cn.edu.xmu.dm.d3c.selectivealgorithms.EFSS;
import cn.edu.xmu.dm.d3c.selectivealgorithms.HCNRR;
import cn.edu.xmu.dm.d3c.selectivealgorithms.HCRR;
import cn.edu.xmu.dm.d3c.utils.ClassifyResultArffLoader;
import cn.edu.xmu.dm.d3c.utils.InstanceUtil;

public class BaseClassifiersEnsemble {

	public double EnsembleClassifiers(Instances input, String sa, String ca) throws Exception {

		String selectiveAlgorithm = sa;
		String CCAlgorithm = ca;
		
		List<Integer> chooseClassifiers = ClassifyResultArffLoader
				.loadChooseClassifiers("");

		List<Integer> cfsArray = ClassifyResultArffLoader.loadCfsArray("");

		List<List<Integer>> classifyRightOrWrong = ClassifyResultArffLoader
				.loadClassifyRightOrWrong("");

		List<List<Integer>> classifyErrorNo = ClassifyResultArffLoader
				.loadClassifyErrorNo("");

		List<Double> correctRateArray = ClassifyResultArffLoader
				.loadCorrectRate("");

		List<List<double[]>> classifyDistributeForInstances = ClassifyResultArffLoader
				.loadClassifyDistributeForInstances("");

		List<Integer> newCfsArray = new ArrayList<Integer>();

		List<List<Integer>> newClassifyRightOrWrong = new ArrayList<List<Integer>>();

		List<List<Integer>> newClassifyErrorNo = new ArrayList<List<Integer>>();

		List<Double> newCorrectRateArray = new ArrayList();

		List<List<double[]>> newClassifyDistributeForInstances = new ArrayList<List<double[]>>();

		// 保存分类信息的子集
		for (int i = 0; i < chooseClassifiers.size(); i++) {
			// 用来存放已经聚类选择的分类器
			newCfsArray.add(cfsArray.get(chooseClassifiers.get(i)));
			// 用来得到通过聚类后选中的分类器的分类结果
			// 取原来available_classifyRightOrWrong的子集，子集的序号为聚类筛选之后的分类器的编号
			newClassifyRightOrWrong.add(classifyRightOrWrong
					.get(chooseClassifiers.get(i)));
			// 用来得到通过聚类后选中的分类器的错分实例编号
			newClassifyErrorNo
					.add(classifyErrorNo.get(chooseClassifiers.get(i)));
			// 用来存放各种算法的正确率
			newCorrectRateArray.add(correctRateArray.get(chooseClassifiers
					.get(i)));
			// 用来存放各种算法的概率分布
			newClassifyDistributeForInstances
					.add(classifyDistributeForInstances.get(chooseClassifiers
							.get(i)));
		}

		double newInitCorrectRate = 1;
		double newInterval = 0.05;

		List<Integer> ClassifierNo = new ArrayList();

		List<Double> currentResult = new ArrayList();

		currentResult.add(Double.MAX_VALUE);
		currentResult.add((double) 0);
		currentResult.add((double) 0);

		double currentCorrectRate = 0;

		Instances inputR = new Instances(input);
		Random random = new Random(1);
		inputR.randomize(random);

		if (selectiveAlgorithm.equals("HCNRR"))
			currentCorrectRate = HCNRR.doHCNRR(inputR, newInitCorrectRate,
					newClassifyRightOrWrong, newClassifyErrorNo,
					newCorrectRateArray, newClassifyDistributeForInstances,
					currentResult, ClassifierNo);
		else if (selectiveAlgorithm.equals("HCRR"))
			currentCorrectRate = HCRR.doHCRR(inputR, newInitCorrectRate,
					newClassifyRightOrWrong, newClassifyErrorNo,
					newCorrectRateArray, newClassifyDistributeForInstances,
					currentResult, ClassifierNo);
		else if (selectiveAlgorithm.equals("EBSS"))
			currentCorrectRate = EBSS.doEBSS(inputR, newInitCorrectRate,
					newClassifyRightOrWrong, newClassifyErrorNo,
					newCorrectRateArray, newClassifyDistributeForInstances,
					currentResult, ClassifierNo);
		else if (selectiveAlgorithm.equals("EFSS"))
			currentCorrectRate = EFSS.doEFSS(inputR, newInitCorrectRate,
					newClassifyRightOrWrong, newClassifyErrorNo,
					newCorrectRateArray, newClassifyDistributeForInstances,
					currentResult, ClassifierNo);
		else if (selectiveAlgorithm.equals("CC"))
			currentCorrectRate = CircleCombine.doCircleCombine(inputR,
					newInitCorrectRate, newInterval, newClassifyRightOrWrong,
					newClassifyErrorNo, newCorrectRateArray,
					newClassifyDistributeForInstances, currentResult,
					ClassifierNo, CCAlgorithm);
		else if (selectiveAlgorithm.equals("DS"))
			currentCorrectRate = CircleCombine.doCircleCombine(inputR,
					newInitCorrectRate, newInterval, newClassifyRightOrWrong,
					newClassifyErrorNo, newCorrectRateArray,
					newClassifyDistributeForInstances, currentResult,
					ClassifierNo, CCAlgorithm);
		else
			throw new Exception("Could not find selective algorithm:"
					+ selectiveAlgorithm);

		System.out.println("选择性集成所得结果：" + currentCorrectRate);
		System.out.println("选择性集成获得的分类器编号：" + ClassifierNo);
		
		return currentCorrectRate;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		if (args.length < 4) {
			System.out.println("Usage:");
			System.out.println("java -jar LibD3CEnsem.jar data/bupa.arff DS EFSS 0.8");
			System.exit(-1);
		}
		
		String filename = args[0];
		String selectiveAlgorithm = args[1];
		String CCAlgorithm = args[2];
		double threadhold = Double.parseDouble(args[3]);

		InstanceUtil myutil = new InstanceUtil();// 初始化工具类

		Instances input = myutil.getInstances(filename);
		
		input.setClassIndex(input.numAttributes() - 1);

		BaseClassifiersEnsemble be = new BaseClassifiersEnsemble();
	
		double currentCorrectRate = 0.0;
		
		while(currentCorrectRate < threadhold) {
			long nstartTime=System.nanoTime();   //获取开始时间
			long mstartTime=System.currentTimeMillis();   //获取开始时间

			currentCorrectRate = be.EnsembleClassifiers(input, selectiveAlgorithm, CCAlgorithm);
			
			long mendTime=System.currentTimeMillis(); //获取结束时间
			long nendTime=System.nanoTime(); //获取结束时间
			System.out.println("程序运行时间： "+(nendTime-nstartTime)+"ns"); 
			System.out.println("程序运行时间： "+(mendTime-mstartTime)+"ms");
		}
	}
}
