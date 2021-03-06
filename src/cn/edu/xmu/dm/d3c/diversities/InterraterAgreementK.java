package cn.edu.xmu.dm.d3c.diversities;

import java.util.ArrayList;
import java.util.List;

import cn.edu.xmu.dm.d3c.utils.ClassifyResultArffLoader;

public class InterraterAgreementK {
	// 计算两个分类器之间的不一致度量
	public static double CalculateDis(List<Integer> first, List<Integer> second) {
		double Dis = 0;
		int i;
		int diffNum = 0;
		for (i = 0; i < first.size(); i++) {
			if (second.size() > i) {
				if (first.get(i) != second.get(i)) {
					diffNum = diffNum + 1;
				}
			} else {
				break;
			}
		}
		Dis = (double) diffNum / (double) first.size();
		return Dis;
	}

	// 计算多个分类器之间的κ度量
	public static double CalculateK(List<Integer>[] classifyResult) {

		int L = classifyResult.length;
		int N = classifyResult[0].size();
		int i, j;
		int num = 0;
		double Dis = 0;
		double p;
		double k;
		// 计算DISav
		for (i = 0; i < L - 1; i++) {
			for (j = i + 1; j < L; j++) {
				Dis = Dis + CalculateDis(classifyResult[i], classifyResult[j]);
			}
		}
		Dis = (Dis * 2) / (double) (L * (L - 1));
		// 计算平均准确率
		for (i = 0; i < classifyResult.length; i++) {
			//
			for (j = 0; j < classifyResult[i].size(); j++) {
				//
				if (classifyResult[i].get(j) == 1) {
					num = num + 1;
				}
			}
		}
		p = (double) num / (double) (L * N);
		// 计算κ度量
		k = 1 - Dis / (2 * p * (1 - p));
		// System.out.println(k);
		return k;
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
		
		InterraterAgreementK cd = new InterraterAgreementK();
		double k = cd.CalculateK(ls);
		
		System.out.println(k);
		
	}
}
