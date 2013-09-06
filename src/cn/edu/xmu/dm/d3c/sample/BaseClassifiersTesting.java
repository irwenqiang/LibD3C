package cn.edu.xmu.dm.d3c.sample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.filechooser.FileFilter;

import weka.classifiers.Classifier;
import weka.core.Instances;
import cn.edu.xmu.dm.d3c.core.WeakClassifiersIndependentTester;
import cn.edu.xmu.dm.d3c.utils.InstanceUtil;

public class BaseClassifiersTesting {

	public void testingBaseClassifiers(Instances input, Classifier[] cfsArray,
			List<String> pathOfClassifiers) throws Exception {
		Instances inputR = new Instances(input);
		Random random = new Random(1);
		inputR.randomize(random);

		WeakClassifiersIndependentTester wcit = new WeakClassifiersIndependentTester();
		wcit.IndependentlyTestWeakClassifiers(
				input, cfsArray,pathOfClassifiers);

	}

	public static void main(String[] args) throws Exception {
		
		if (args.length < 2) {
			System.out.println("Usage:");
			System.out.println("java -jar LibD3CTraing.jar data/bupa_test.arff models");
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
		
		Classifier[] cfsArray = null;
		
		File file = new File(tempPath);
		
		File[] fileNames = file.listFiles();
		
		List<Classifier> lst = new ArrayList<Classifier>();
		
		for (int i = 0; i < fileNames.length; i++) {
			Classifier cls = (Classifier) weka.core.SerializationHelper
				       .read(fileNames[i].getAbsolutePath());
			
			pathOfClassifiers.add(fileNames[i].getName());
			
			lst.add(cls);
		}
			
		cfsArray = lst.toArray(new Classifier[lst.size()]);
		
		BaseClassifiersTesting bct = new BaseClassifiersTesting();
		
		
		long nstartTime=System.nanoTime();   //获取开始时间
		long mstartTime=System.currentTimeMillis();   //获取开始时间
	
		bct.testingBaseClassifiers(input, cfsArray, pathOfClassifiers);
		
		long mendTime=System.currentTimeMillis(); //获取结束时间
		long nendTime=System.nanoTime(); //获取结束时间
		System.out.println("程序运行时间： "+(nendTime-nstartTime)+"ns"); 
		System.out.println("程序运行时间： "+(mendTime-mstartTime)+"ms");
	}
}
