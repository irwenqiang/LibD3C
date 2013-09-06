package cn.edu.xmu.dm.d3c.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.CVParameterSelection;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.SerializedObject;
import weka.core.Utils;
import cn.edu.xmu.dm.d3c.threads.IndependentTestThread;
import cn.edu.xmu.dm.d3c.threads.IndependentTrainThread;
import cn.edu.xmu.dm.d3c.threads.TestThreadListener;
import cn.edu.xmu.dm.d3c.threads.ThreadListener;

public class SelectiveEnsemble {

	public void IndependentTrain(Instances input, Classifier[] cfsArray,
			int numfolds, List<List<Integer>> classifyRightOrWrong,
			List<List<Integer>> classifyErrorNo, List<Double> correctRateArray,
			List<List<double[]>> classifyDistributeForInstances, Random random,
			List<Integer> qqs, List<String> pathOfClassifiers,
			List<String> parameterOfCV) throws Exception {

		int i, j, k;
		List<Instances> trainCV = new ArrayList<Instances>();
		for (i = 0; i < numfolds; i++) {
			trainCV.add(input.trainCV(numfolds, i, random));
		}

		ThreadListener tl = new ThreadListener();// 监听统计文件线程的监控线程
		for (i = 0; i < cfsArray.length; i++) {
			IndependentTrainThread itt = new IndependentTrainThread(input,
					trainCV, cfsArray[i], numfolds, classifyRightOrWrong,
					classifyErrorNo, correctRateArray,
					classifyDistributeForInstances, i, qqs, pathOfClassifiers,
					parameterOfCV);
			tl.array.add(itt);
			itt.start();
		}

		tl.start();

		while (!ThreadListener.isOver) {

			try {// 每隔一定时间监听一次各个文件统计线程
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("基分类器单独训练完成!");
	}

	public void IndependentTest(Instances input, Classifier[] cfsArray,
			List<List<Integer>> classifyRightOrWrong,
			List<List<Integer>> classifyErrorNo, List<Double> correctRateArray,
			List<List<double[]>> classifyDistributeForInstances, Random random,
			List<Integer> qqs, List<String> pathOfClassifiers) throws Exception {

		int i, j, k;

		TestThreadListener tl = new TestThreadListener();// 监听统计文件线程的监控线程
		for (i = 0; i < cfsArray.length; i++) {
			IndependentTestThread itt = new IndependentTestThread(input,
					cfsArray[i], classifyRightOrWrong, classifyErrorNo,
					correctRateArray, classifyDistributeForInstances, i, qqs,
					pathOfClassifiers);
			tl.array.add(itt);
			itt.start();
		}

		tl.start();

		while (!TestThreadListener.isOver) {

			try {// 每隔一定时间监听一次各个文件统计线程
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("基分类器单独训练完成!");
	}

	// 交叉验证模型用于输出单独训练的概率分布信息
	public void CrossValidationModelForDistribute(Instances input,
			List<Instances> trainCV, Classifier classifier, int numfolds,
			List<List<Integer>> classifyRightOrWrong,
			List<List<Integer>> classifyErrorNo, List<Double> correctRateArray,
			List<List<double[]>> classifyDistributeForInstances, int index,
			List<Integer> qqs, List<String> pathOfClassifiers,
			List<String> parameterOfCV) {

		try {

			int i, j, k;

			input = new Instances(input);

			List<Integer> single_classifyRightOrWrong = new ArrayList<Integer>();
			List<Integer> single_classifyErrorNo = new ArrayList<Integer>();
			double correctRate;
			List<double[]> single_classifyDistributeForInstances = new ArrayList<double[]>();

			Evaluation eval1 = new Evaluation(input);
			Evaluation eval2 = new Evaluation(input);

			CVParameterSelection cvps = new CVParameterSelection();

			if (!parameterOfCV.get(index).equals("")) {

				Classifier copiedClassifier1 = (Classifier) new SerializedObject(
						classifier).getObject();

				Random random = new Random(1000);

				String[] options = new String[2];
				options[0] = "-P";
				options[1] = parameterOfCV.get(index);

				cvps.setOptions(options);
				cvps.setNumFolds(5);
				cvps.setSeed(1);
				cvps.setClassifier(copiedClassifier1);
				cvps.buildClassifier(input);
				eval1.crossValidateModel(cvps, input, 5, random);
			}

			double numOfError = 0;

			if (numfolds != 0) {
				for (i = 0; i < numfolds; i++) {

					Instances train = trainCV.get(i);// 获得训练集
					Instances test = input.testCV(numfolds, i);// 获得测试集

					eval2.setPriors(train);

					Classifier copiedClassifier2;

					if (!parameterOfCV.get(index).equals("")) {
						copiedClassifier2 = ((AbstractClassifier) Utils
								.forName(Classifier.class,
										pathOfClassifiers.get(index),
										cvps.getBestClassifierOptions()));
					} else {
						copiedClassifier2 = (Classifier) new SerializedObject(
								classifier).getObject();
					}

					copiedClassifier2.buildClassifier(train);

					for (j = 0; j < test.numInstances(); j++) {
						// 样本的实际类别
						double real_class = test.instance(j).classValue();
						// 样本的预测类别
						double predict_class = copiedClassifier2
								.classifyInstance(test.instance(j));
						// 添加当前分类器对于一条样本预测的概率分布
						double[] distribute = copiedClassifier2
								.distributionForInstance(test.instance(j));
						single_classifyDistributeForInstances.add(distribute);
						//
						if (real_class != predict_class) {
							//
							numOfError++;
							//
							single_classifyRightOrWrong.add(0);
						} else if (real_class == predict_class) {
							//
							single_classifyRightOrWrong.add(1);
						}
					}
				}

				// 获得当前分类器的样本正确错误的情况
				classifyRightOrWrong.add(single_classifyRightOrWrong);

				// 获得当前分类器的错分样本号
				for (i = 0; i < single_classifyRightOrWrong.size(); i++) {
					if (single_classifyRightOrWrong.get(i) == 0) {
						single_classifyErrorNo.add(i);
					}
				}
				classifyErrorNo.add(single_classifyErrorNo);

				// 获得当前分类器的正确率
				correctRate = ((double) input.numInstances() - numOfError)
						/ (double) input.numInstances();

				correctRateArray.add(correctRate);

				// 获得当前分类器对于每一条样本的概率分布
				classifyDistributeForInstances
						.add(single_classifyDistributeForInstances);

				qqs.add(index);

				System.out.println(pathOfClassifiers.get(index) + "		"
						+ correctRate);
			}
			if (numfolds == 0) {

				Classifier copiedClassifier2;

				if (!parameterOfCV.get(index).equals("")) {
					copiedClassifier2 = ((AbstractClassifier) Utils.forName(
							Classifier.class, pathOfClassifiers.get(index),
							cvps.getBestClassifierOptions()));
				} else {
					copiedClassifier2 = (Classifier) new SerializedObject(
							classifier).getObject();
				}
				copiedClassifier2.buildClassifier(input);
				SerializationHelper.write(
						"models/" + pathOfClassifiers.get(index) + "_" + index
								+ ".model", copiedClassifier2);

				return;
			}
		} catch (Exception e) {
			System.out.println(pathOfClassifiers.get(index) + "		" + "error");
		}
		return;
	}

	public void testModel(Instances input, Classifier classifier,
			List<List<Integer>> classifyRightOrWrong,
			List<List<Integer>> classifyErrorNo, List<Double> correctRateArray,
			List<List<double[]>> classifyDistributeForInstances, int index,
			List<Integer> qqs, List<String> pathOfClassifiers) throws Exception {

		Classifier copiedClassifier2;

		copiedClassifier2 = (Classifier) new SerializedObject(classifier)
				.getObject();

		List<double[]> single_classifyDistributeForInstances = new ArrayList<double[]>();

		List<Integer> single_classifyRightOrWrong = new ArrayList<Integer>();
		List<Integer> single_classifyErrorNo = new ArrayList<Integer>();

		int numOfError = 0;
		for (int j = 0; j < input.numInstances(); j++) {
			// 样本的实际类别
			double real_class = input.instance(j).classValue();
			// 样本的预测类别
			double predict_class = copiedClassifier2.classifyInstance(input
					.instance(j));
			// 添加当前分类器对于一条样本预测的概率分布
			double[] distribute = copiedClassifier2
					.distributionForInstance(input.instance(j));
			single_classifyDistributeForInstances.add(distribute);
			//
			if (real_class != predict_class) {
				//
				numOfError++;
				//
				single_classifyRightOrWrong.add(0);
			} else if (real_class == predict_class) {
				//
				single_classifyRightOrWrong.add(1);
			}
		}

		// 获得当前分类器的样本正确错误的情况
		classifyRightOrWrong.add(single_classifyRightOrWrong);

		// 获得当前分类器的错分样本号
		for (int i = 0; i < single_classifyRightOrWrong.size(); i++) {
			if (single_classifyRightOrWrong.get(i) == 0) {
				single_classifyErrorNo.add(i);
			}
		}
		classifyErrorNo.add(single_classifyErrorNo);

		// 获得当前分类器的正确率
		double correctRate = ((double) input.numInstances() - numOfError)
				/ (double) input.numInstances();

		correctRateArray.add(correctRate);

		// 获得当前分类器对于每一条样本的概率分布
		classifyDistributeForInstances
				.add(single_classifyDistributeForInstances);

		qqs.add(index);

		System.out.println(pathOfClassifiers.get(index) + "		" + correctRate);
	}
}
