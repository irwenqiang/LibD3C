package cn.edu.xmu.dm.d3c.sample;

import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Instances;
import cn.edu.xmu.dm.d3c.core.ClusterBasedSelectiveStrategy;
import cn.edu.xmu.dm.d3c.utils.InitClassifiers;
import cn.edu.xmu.dm.d3c.utils.InstanceUtil;

/**
 * desc:D3C集成分类器使用模板
 * <code>Sample</code>
 * @author chenwq (chenwq@stu.xmu.edu.cn)
 * @version 1.0 2012/04/10
 */
public class Sample {

	//public Classifier[] startLibD3C(Instances input,int numfolds)throws Exception{
	public List<String> startLibD3C(Instances input,int numfolds)throws Exception{	
		//
		//
		List<String> nameOfClassifiers=new ArrayList<String>();
		List<String> pathOfClassifiers=new ArrayList<String>();
		List<String> parameterOfCV=new ArrayList<String>();
		
		//
		InstanceUtil myutil = new InstanceUtil();
		//
		myutil.getJarPath(InstanceUtil.class);
		//
		String tempPath= myutil.jarPath+"\\config\\classifiers_qc__.xml";
		//
		System.out.println(tempPath);
		//
		Classifier[] cfsArray = InitClassifiers.init(tempPath,nameOfClassifiers,pathOfClassifiers,parameterOfCV);

		return new ClusterBasedSelectiveStrategy().doClusterBasedSelectiveStrategy(input,cfsArray,numfolds,pathOfClassifiers,parameterOfCV);
	}
	//
	public static void main(String[] argv)throws Exception{
		
		String filename = "data/bupa.arff";
		
		InstanceUtil myutil = new InstanceUtil();// 初始化工具类
		
		Instances input = myutil.getInstances(filename);
		
		input.setClassIndex(input.numAttributes() - 1);
		//
		Sample sample=new Sample();
		
		sample.startLibD3C(input, 5);
	}
}
