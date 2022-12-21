package darwinSimulation;

import java.util.ArrayList;
import java.util.List;

public class TestApp_Dec21 {
	static CPopulation pop;
	static final int POPSIZE = 20;
	public static void main(String[] args) {
		pop = new CPopulation(POPSIZE);
		int p1 = 0;
		while (p1 < POPSIZE - 1) {
			for (int m = (p1 + 1); m < POPSIZE; m++) {
				int p2 = m;
				game(p1, p2);
			}
			p1++;
		}
		pop.calcStat();
		List<Integer> parentsList = new ArrayList<Integer>();
		makeParents(parentsList);

	} // end of main()
	//
	private static void makeParents(List<Integer> parentsList) {
		//based on scaled payoff 
		double sum = 0.0;
		for (int i = 0; i < POPSIZE; i++) {
			sum += pop.member[i].getScaledPayoff();
		}
		//cumlate payoff
		double[] roulet = new double[POPSIZE];
		roulet[0] = pop.member[0].getScaledPayoff() / sum;
		for (int m = 1; m < POPSIZE; m++) {
			roulet[m] = roulet[m - 1] + (pop.member[m].getScaledPayoff() / sum);
		}
		
		  for(int m=0;m<roulet.length;m++){ System.out.println("\t"+roulet[m]); }
		 
		//check hit
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

	
	//lottery probability prob
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

} // end of App
