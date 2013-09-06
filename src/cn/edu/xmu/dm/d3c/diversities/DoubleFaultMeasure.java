package cn.edu.xmu.dm.d3c.diversities;

import java.util.List;


import cn.edu.xmu.dm.d3c.utils.ClassifyResultArffLoader;

public class DoubleFaultMeasure {
	// 计算两个分类器之间的不一致度量
	public static double CalculateDis(List<Integer> first, List<Integer> second) {
		int n00 = 0;
		int n01 = 0;
		int n10 = 0;
		int n11 = 0;
		for (int i = 0; i < first.size();i++) {
				if (first.get(i)== second.get(i)) {
					if(first.get(i) == 0)
						n00++;
					else
						n11++;
				} else {
					
					if (first.get(i) == 1) 
						n10++;
					else 
						n01++;
				}
			
		}
		return (n00) / (n11 + n10 + n01 + n00); 
	}
	
	public static double CalculateK(List<Integer>[] classifyResult) {
		int L = classifyResult.length;
		double Qav = 0;
		for (int i = 0; i < L-1; i++) {
			for (int j = i+1; j < L; j++) {
				Qav += CalculateDis(classifyResult[i], classifyResult[j]);
			}
		}
		
		return 2 * Qav / (L*(L-1));
	}
	
public static void main(String[] args) throws Exception {
		
		if (args.length < 1) {
			System.out.println("Usage:");
			System.out.println("java -jar LibD3CDiversity.jar data/classifyRightOrWrong");
			System.exit(-1);
		}
	
		String filepath = args[0];
		
		List<List<Integer>> crw = ClassifyResultArffLoader.loadClassifyRightOrWrong4Diversity(filepath);
		
		List<Integer>[] ls;
		
		ls = new List[crw.size()];
		
		for (int i = 0; i < crw.size(); i++) {
			ls[i] = crw.get(i);
		}
		
		DoubleFaultMeasure cd = new DoubleFaultMeasure();
		double k = cd.CalculateK(ls);
		
		System.out.println(k);
	}
}
