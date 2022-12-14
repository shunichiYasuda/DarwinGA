package darwinSimulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestApp_Jan05 {
	static CPopulation pop;
	static final int POPSIZE = 20;
	static final int GEN = 100;
	static final int LOOP = 1;
	static String dateName;
	static String timeStamp;
	static File aveFile, memFile, statFile;
	static PrintWriter pwAve, pwGType, pwStat;

	public static void main(String[] args) {
		// 記録ファイル類
		makeDate();
		makeFiles();
		// 実験ループ
		int exp = 0;
		while (exp < LOOP) {
			pop = new CPopulation(POPSIZE);
			int gen = 0;
			while (gen < GEN) {
				int p1 = 0;
				while (p1 < POPSIZE - 1) {
					for (int m = (p1 + 1); m < POPSIZE; m++) {
						int p2 = m;
						game(p1, p2);
					}
					p1++;
				}
				pop.calcStat();
				//
				System.out.println(pop.mAve);
				//
				pop.scaling();
				List<Integer> parentsList = new ArrayList<Integer>();
				makeParents(parentsList);
				mutation(parentsList);
				crossover(parentsList);
				// 置き換えられた染色体であらたな個体を作る.
				// pop.member の染色体を上書きするときに、親の個体番号を利用するので、いきなりの
				// 上書きはNG.なので、pop.member から parentsList の長さの数分 chrom配列を作っておいて、
				// そちらに親番号をもつ chromをコピーしておき、一括して置き換える。
				List<String> tmpParentsChrom = new ArrayList<String>();
				for (int m : parentsList) {
					tmpParentsChrom.add(new String(pop.member[m].chrom));
				}
				// 置き換え
				for (int i = 0; i < POPSIZE; i++) {
					char[] tmpChrom = new char[CHeader.LENGTH];
					tmpChrom = tmpParentsChrom.get(i).toCharArray();
					pop.member[i].replace(tmpChrom);
				}
				//
				gen++; //世代を進める
			} // end of while(GEN) 世代ループの終わり
			exp++;
		} // end of while(EXP) 実験ループの終わり

	}// end of main()

	// 記録ファイル作成
	private static void makeFiles() {
		memFile = new File(dateName + "_GType.txt");
		statFile = new File(dateName + "_stat.txt");
		aveFile = new File(dateName + "_ave.txt");
		try {
			FileWriter fw = new FileWriter(memFile);
			FileWriter fw2 = new FileWriter(statFile);
			FileWriter fw3 = new FileWriter(aveFile);
			BufferedWriter bw = new BufferedWriter(fw);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			BufferedWriter bw3 = new BufferedWriter(fw3);
			pwGType = new PrintWriter(bw);
			pwStat = new PrintWriter(bw2);
			pwAve = new PrintWriter(bw3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ファイルのクローズ
	private static void closeFiles() {
		pwGType.close();
		pwStat.close();
		pwAve.close();
	}

// 一点交叉メソッド
	private static void crossover(List<Integer> parentsList) {
		Collections.shuffle(parentsList);
		for (int m = 0; m < parentsList.size() - 1; m += 2) {
			int parent1, parent2;
			parent1 = parentsList.get(m);
			parent2 = parentsList.get(m + 1);
			Random randSeed = new Random();
			if (bingo(CHeader.crossProb)) {
				// check
				//System.out.println("hit:" + parent1 + ":" + parent2);
				int point = randSeed.nextInt(CHeader.LENGTH);
				// まったく入れ替わらない・全部入れ替わるが起きるといやなので
				while (point == 0 || point == CHeader.LENGTH - 1) {
					point = randSeed.nextInt(CHeader.LENGTH);
				}
				for (int index = 0; index < point; index++) {
					char tmp = pop.member[parent1].chrom[index];
					pop.member[parent1].chrom[index] = pop.member[parent2].chrom[index];
					pop.member[parent2].chrom[index] = tmp;
				}
			} // end of if(クロスオーバーがビンゴ
		} // クロスオーバー終わり
	} // end of crossover()

	// mutation
	private static void mutation(List<Integer> parentsList) {
		for (int d : parentsList) {
			for (int index = 0; index < CHeader.LENGTH; index++) {
				if (bingo(CHeader.mutProb)) {
					// System.out.println("hit=" + d);
					if (pop.member[d].chrom[index] == '1') {
						pop.member[d].chrom[index] = '0';
					} else {
						pop.member[d].chrom[index] = '1';
					}
				} //
			}
		}
	}

	//
	private static void makeParents(List<Integer> parentsList) {
		// based on scaled payoff
		double sum = 0.0;
		for (int i = 0; i < POPSIZE; i++) {
			sum += pop.member[i].getScaledPayoff();
		}
		// sum payoff
		double[] roulet = new double[POPSIZE];
		roulet[0] = pop.member[0].getScaledPayoff() / sum;
		for (int m = 1; m < POPSIZE; m++) {
			roulet[m] = roulet[m - 1] + (pop.member[m].getScaledPayoff() / sum);
		}

		// for(int m=0;m<roulet.length;m++){ System.out.println("\t"+roulet[m]); }

		// check hit
		double border;
		int p_index;
		for (int i = 0; i < POPSIZE; i++) {
			p_index = 0;
			border = Math.random();
			while (roulet[p_index] < border)
				p_index++;
			parentsList.add(p_index);
		}
		// System.out.println("");
		// parents is even number
		if (parentsList.size() % 2 == 1) {
			p_index = 0;
			border = Math.random();
			while (roulet[p_index] < border)
				p_index++;
			parentsList.add(p_index);
		}
	}

	// lottery probability prob
	private static boolean bingo(double prob) {
		boolean r = false;
		if (Math.random() < prob)
			r = true;
		return r;
	}

	//
	static void game(int p1, int p2) {
		char select_p1 = pop.member[p1].getChoice();
		char select_p2 = pop.member[p2].getChoice();
		// C �� 0, D�� 1
		if (select_p1 == '0' && select_p2 == '0') {
			pop.member[p1].setPayoff(3.0);
			pop.member[p2].setPayoff(3.0);
		}
		if (select_p1 == '0' && select_p2 == '1') {
			pop.member[p1].setPayoff(0.0);
			pop.member[p2].setPayoff(5.0);
		}
		if (select_p1 == '1' && select_p2 == '0') {
			pop.member[p1].setPayoff(5.0);
			pop.member[p2].setPayoff(0.0);
		}
		if (select_p1 == '1' && select_p2 == '1') {
			pop.member[p1].setPayoff(1.0);
			pop.member[p2].setPayoff(1.0);
		}
		pop.member[p1].reMem(select_p2);
		pop.member[p2].reMem(select_p1);
	}// end of game()
		// 支援メソッド
		// printer

	static void printRec(char[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i]);
		}
	}

	static void printRec(double[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i] + "\t");
		}
	}

	static void printRec(int[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i] + "\t");
		}
	}

	static void makeDate() {
		Calendar cal1 = Calendar.getInstance();
		int year = cal1.get(Calendar.YEAR);
		int month = cal1.get(Calendar.MONTH);
		int day = cal1.get(Calendar.DATE);
		int hour = cal1.get(Calendar.HOUR_OF_DAY);
		int minute = cal1.get(Calendar.MINUTE);
		int second = cal1.get(Calendar.SECOND);
		String[] monthArray = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jly", "Aug", "Sep", "Oct", "Nov", "Dec" };
		dateName = new String(monthArray[month] + day + "_" + year);
		timeStamp = new String(dateName + ":" + hour + ":" + minute + ":" + second);
	}

}
