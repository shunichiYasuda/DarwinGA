package darwinSimulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestApp_Jan12 {
	static CPopulation pop;
	static final int POPSIZE = 20;
	static final int GEN = 100;
	static final int EXP = 1;
	static String dateName;
	static String timeStamp;
	static File aveFile, memFile, statFile;
	static PrintWriter pwAve, pwGType, pwStat;
	// 親集団の染色体プール
	static List<String> parentsChrom;
	// 平均値を記録する2次元配列
	static double[][] aveTable;

	public static void main(String[] args) {
		// 収束判定のため10回分記録をとる
		final int checkTerm = 10;
		char[] Q = new char[checkTerm];
		int checkCount = 6; // 10回の内6回協力を収束と定義
		final double coopValue = 2.76;
		final double defectValue = 1.54;
		// 集団が協力を達成したかどうか状況を示すフラグ：収束なし N ,裏切り D, 協力 C
		char stateFlag = 'N';
		// 集団の状態記録。協力状態、裏切り状態、どちらでもなし
		char[] popState = new char[GEN];
		// 集団の状態記録を EXP分保存するテーブル
		char[][] popStateTable = new char[GEN][EXP];
		//
		// 記憶パターンからみたTFT 個体数の記録
		int[] memBasedTFT = new int[GEN];
		// 記憶パターンからみたTFT個体数を EXP分保存するテーブル
		int[][] memBasedTFTTable = new int[GEN][EXP];
		// 染色体パターンから見たTFT個体数の記録
		int[] gtypeBasedTFT = new int[GEN];
		// 染色体パターンから見たTFT個体数をEXP分保存するテーブル
		int[][] gtypeBasedTFTTable = new int[GEN][EXP];
		// 初期化
		for (int i = 0; i < GEN; i++) {
			memBasedTFT[i] = 0;
			gtypeBasedTFT[i] = 0;
		}
		for (int i = 0; i < GEN; i++) {
			for (int j = 0; j < EXP; j++) {
				memBasedTFTTable[i][j] = 0;
				gtypeBasedTFTTable[i][j] = 0;
			}
		}
		// 集団が協力へ収束したかどうかを判定するフラグ。このフラグが立っている実験を
		// 収束実験と判定してさまざまな状況を記録する。
		boolean convergeFlag = false;
		// すべての実験に関する平均値推移を記録する配列の初期化
		aveTable = new double[GEN][EXP];
		for (int i = 0; i < GEN; i++) {
			for (int j = 0; j < EXP; j++)
				aveTable[i][j] = 0.0;
		}

		// 記録ファイル類
		makeDate();
		makeFiles();
		//
		// 一時的な平均値の記録
		double[] tmpAve = new double[GEN];
		// 実験回数のインデックス
		int exp = 0;
		while (exp < EXP) { // 協力への収束があった実験のみ記録をとる。

		} // end of while()
			// 実験ループの終わり
		closeFiles();

	} //end of main()

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
				// System.out.println("hit:" + parent1 + ":" + parent2);
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
