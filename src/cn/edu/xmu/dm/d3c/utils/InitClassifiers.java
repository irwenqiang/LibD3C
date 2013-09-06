package cn.edu.xmu.dm.d3c.utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
//import weka.classifiers.functions.LibSVM;
//import weka.classifiers.functions.LibLINEAR;
import weka.classifiers.lazy.IBk;
import weka.core.Utils;

/**
 * desc:从配置文件中读取分类器的配置，并初始化分类器
 * <code>InitClassifiers</code>
 * @author chenwq (chenwq@stu.xmu.edu.cn)
 * @version 1.0 2012/04/10
 */
public class InitClassifiers {
	//
	public static String[] classifiersName;

	//
	static public Classifier[] init(
			String filePath,
			List<String> nameOfClassifiers, 
			List<String> pathOfClassifiers,
			List<String> parameterOfCV
			) throws Exception {

		// 初始化一个分类器数组
		Classifier[] cfsArray = null;
		//
		File f = new File(filePath);
		// 读取配置文件
		SAXReader reader = new SAXReader();
		//
		Document doc;
		//
		doc = reader.read(f);
		//
		Element root = doc.getRootElement();
		Element foo;
		//
		List<Classifier> lst = new ArrayList<Classifier>();

		//
		for (Iterator iter = root.elementIterator("classifier"); iter.hasNext();) {
			//
			foo = (Element) iter.next();
			//
			String classifierName = foo.attributeValue("name").trim();
			String classifierPath = foo.element("parameter").elementText("class").trim();
			String option = foo.element("parameter").elementText("options").trim();
			//String paraOfCV = foo.element("parameter").elementText("CV").trim();
			String paraOfCV = "";
			//
			String[] options = weka.core.Utils.splitOptions(option);
			//

			if (classifierName.startsWith("IB")) {
				//
				IBk cfs = new IBk();
				//
				cfs.setOptions(options);
				//
				lst.add((Classifier) cfs);
				//
				nameOfClassifiers.add(classifierName);
				//
				pathOfClassifiers.add(classifierPath);
				//
				parameterOfCV.add(paraOfCV);
			} else {
				//
				Classifier cfs = ((AbstractClassifier) Utils.forName(Classifier.class, classifierPath, options));
				//
				lst.add(cfs);
				//
				nameOfClassifiers.add(classifierName);
				//
				pathOfClassifiers.add(classifierPath);
				//
				parameterOfCV.add(paraOfCV);
			}

		}
		//
		cfsArray = lst.toArray(new Classifier[lst.size()]);
		//
		classifiersName = nameOfClassifiers.toArray(new String[nameOfClassifiers.size()]);
		//
		return cfsArray;
	}
	//
	public static void main(String[] args)throws Exception{
		//
		List<String> nameOfClassifiers=new ArrayList<String>();
		List<String> pathOfClassifiers=new ArrayList<String>();
		List<String> parameterOfCV=new ArrayList<String>();
		//
		InitClassifiers.init("config/classifiers_qc_.xml", nameOfClassifiers, pathOfClassifiers, parameterOfCV);
		//
		System.out.println(nameOfClassifiers);
		System.out.println(parameterOfCV);
	}
}
