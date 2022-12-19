package darwinSimulation;

public class TestApp_Dec19 {
	static CPopulation pop;
	static final int POPSIZE = 20;

	public static void main(String[] args) {
		pop = new CPopulation(POPSIZE);
		int p1 = 0;
		int target = 5;
		// play game in sequence
		while (p1 < POPSIZE - 1) {
			for (int m = (p1 + 1); m < POPSIZE; m++) {
				int p2 = m;
				game(p1, p2);
				if (p1 == target || p2 == target) {
					System.out.print("game(" + p1 + "," + p2 + ")\t");
					double p = pop.member[target].getPayoff();
					System.out.print("payoff=" + p + "\t cumPayoff=" + pop.member[target].getCumPayoff());
					System.out.println("\taveragePayoff="+ pop.member[target].getAvePayoff());
				}
			}
			p1++;
		} // end of gemes
			// calculation some values in population
		pop.calcStat();
		System.out.println("average=" + pop.mAve);
		//
		pop.scaling();
		System.out.println("raw payoff=" + pop.member[target].getAvePayoff());
		System.out.println("scaled payoff=" + pop.member[target].getScaledPayoff());
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

}// end of class
