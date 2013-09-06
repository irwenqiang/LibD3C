package cn.edu.xmu.dm.d3c.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.core.Instances;
import cn.edu.xmu.dm.d3c.core.WeakClassifiersIndependentTrainer;
import cn.edu.xmu.dm.d3c.utils.InitClassifiers;
import cn.edu.xmu.dm.d3c.utils.InstanceUtil;

public class BaseClassifiersTraining {

	public void trainingBaseClassifiers(Instances input, Classifier[] cfsArray,
			int numfolds, List<String> pathOfClassifiers,
			List<String> parameterOfCV) throws Exception {
		Instances inputR = new Instances(input);
		Random random = new Random(1);
		inputR.randomize(random);

		WeakClassifiersIndependentTrainer wcit = new WeakClassifiersIndependentTrainer();
		wcit.IndependentlyTrainWeakClassifiers(
				input, cfsArray, numfolds, pathOfClassifiers, parameterOfCV);

	}

	public static void main(String[] args) throws Exception {
		
		if (args.length < 2) {
			System.out.println("Usage:");
			System.out.println("java -jar LibD3CTraing.jar data/bupa.arff conf/classifiers.xml");
			System.exit(-1);
		}
		System.out.println(args[0]);
		System.out.println(args[1]);
		String filename = args[0];

		InstanceUtil myutil = new InstanceUtil();// 初始化工具类

		Instances input = myutil.getInstances(filename);

		input.setClassIndex(input.numAttributes() - 1);
		
		List<String> nameOfClassifiers=new ArrayList<String>();
		List<String> pathOfClassifiers=new ArrayList<String>();
		List<String> parameterOfCV=new ArrayList<String>();
		
		myutil.getJarPath(InstanceUtil.class);
		
		//String tempPath= myutil.jarPath+"\\config\\classifiers.xml";
		String tempPath=args[1];
		
		System.out.println(tempPath);
		
		Classifier[] cfsArray = InitClassifiers.init(tempPath,nameOfClassifiers,pathOfClassifiers,parameterOfCV);
		
		BaseClassifiersTraining bct = new BaseClassifiersTraining();
		
		int numfolds = 0;
		
		long nstartTime=System.nanoTime();   //获取开始时间
		long mstartTime=System.currentTimeMillis();   //获取开始时间
	
		bct.trainingBaseClassifiers(input, cfsArray, numfolds, pathOfClassifiers, parameterOfCV);
		
		long mendTime=System.currentTimeMillis(); //获取结束时间
		long nendTime=System.nanoTime(); //获取结束时间
		System.out.println("程序运行时间： "+(nendTime-nstartTime)+"ns"); 
		System.out.println("程序运行时间： "+(mendTime-mstartTime)+"ms");
	}
}
