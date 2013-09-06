package cn.edu.xmu.dm.d3c.diversities;

import java.util.List;

import cn.edu.xmu.dm.d3c.utils.ClassifyResultArffLoader;

/**
 * For statistically independent classifiers, the expectation of Qi,k is 0.
 * Q varies between -1 and 1.
 * Classifiers that tend to recognize the same objects correctly will have positive value of Q
 * ant those which commit errors on different objects will render Q negative.
 * @author chenwq
 */
public class EntropyMeasure {
	
	// 计算多个分类器之间的q度量
	public static double CalculateK(List<Integer>[] classifyResult) {
		int L = classifyResult.length;
		int[] LZ;
		double Qav = 0;
		
		double result = 0;
		if(classifyResult.length > 0) {
			LZ = new int[classifyResult[0].size()];
			for (int i = 0; i < classifyResult[0].size(); i++) {
				for (int j = 0; j < classifyResult.length; j++) {
					if (classifyResult[j].get(i) == 1)
						LZ[i]++;
				}
				
				result += Math.min(LZ[i], L-LZ[i]) / (L-Math.ceil(L/2.0));
			}
			
			return result / classifyResult[0].size();
		}

		return -0.1;
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
		
		EntropyMeasure cd = new EntropyMeasure();
		double k = cd.CalculateK(ls);
		
		System.out.println(k);
	}
}
