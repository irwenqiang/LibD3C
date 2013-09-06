package cn.edu.xmu.dm.d3c.threads;
import java.util.List;
import java.util.Random;

import cn.edu.xmu.dm.d3c.core.SelectiveEnsemble;

import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 * desc:各个分类器单独训练模型
 * <code>IndependentTrainThread</code>
 * @author chenwq (chenwq@stu.xmu.edu.cn)
 * @version 1.0 2012/04/10
 */
public class IndependentTrainThread extends Thread {
	
	//
	private double value;
	
	private Instances input;

	private List<Instances> trainCV;
	
	private Classifier classifier;
	
	private int numfolds;
	
	private List<List<Integer>> classifyRightOrWrong;
	
	private List<List<Integer>> classifyErrorNo;
	
	private List<Double> correctRateArray;
	
	private List<List<double[]>> classifyDistributeForInstances;
	
	//private Random random;
	
	private int index;
	
	//private List<ClassifierIndex> qqs;
	private List<Integer> qqs;
	
	private List<String> pathOfClassifiers;
	
	private List<String> parameterOfCV;
	
	//private StringBuffer outBuff;
	
	private long executeTime;
	
	private boolean isFinished;
	
	// 构造函数
	public IndependentTrainThread(
			Instances input, 
			List<Instances> trainCV,
			Classifier classifier, 
			int numfolds, 
			List<List<Integer>> classifyRightOrWrong,
			List<List<Integer>> classifyErrorNo,
			List<Double> correctRateArray, 
			List<List<double[]>> classifyDistributeForInstances,
			int index, 
			List<Integer> qqs,
			List<String> pathOfClassifiers,
			List<String> parameterOfCV) {
		//
		this.input = input;
		this.trainCV= trainCV;
		this.classifier = classifier;
		this.numfolds = numfolds;
		this.classifyRightOrWrong=classifyRightOrWrong;
		this.classifyErrorNo = classifyErrorNo;
		this.correctRateArray = correctRateArray;
		this.classifyDistributeForInstances=classifyDistributeForInstances;
		//this.random = random;
		this.index = index;
		this.qqs = qqs;
		this.pathOfClassifiers=pathOfClassifiers;
		this.parameterOfCV=parameterOfCV;
		//this.outBuff=outBuff;
	}

	public void run() {

		SelectiveEnsemble se = new SelectiveEnsemble();
		long nstartTime=System.nanoTime();   //获取开始时间
		long mstartTime=System.currentTimeMillis();   //获取开始时间
	
		se.CrossValidationModelForDistribute(
				input, 
				trainCV,
				classifier, 
				numfolds,
				classifyRightOrWrong,
				classifyErrorNo,
				correctRateArray, 
				classifyDistributeForInstances,
				/*random,*/
				index, 
				qqs,
				pathOfClassifiers,
				parameterOfCV);
		this.isFinished = true;
		
		long mendTime=System.currentTimeMillis(); //获取结束时间
		long nendTime=System.nanoTime(); //获取结束时间
		System.out.println("程序运行时间： "+(nendTime-nstartTime)+"ns"); 
		System.out.println("程序运行时间： "+(mendTime-mstartTime)+"ms");
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public List<List<Integer>> getClassifyRightOrWrong() {
		return classifyRightOrWrong;
	}

	public void setLst(List<List<Integer>> classifyRightOrWrong) {
		this.classifyRightOrWrong = classifyRightOrWrong;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public int getI() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}
}
