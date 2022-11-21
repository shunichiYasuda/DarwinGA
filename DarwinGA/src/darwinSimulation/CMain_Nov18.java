package darwinSimulation;

public class CMain_Nov18 {
	static CIndividual[] pop = new CIndividual[20];

	public static void main(String[] args) {
		for(int i=0;i<pop.length;i++) {
			pop[i] = new CIndividual();
		}
		for (CIndividual p : pop) {
			printRec(p.getMemory());
			System.out.println(":"+p.getAdr());
			printRec(p.getChrom());
			System.out.println();
		}
		//
		System.out.println("--------------------------------------");
		int p1 = 3; 
		int p2 = 10;
		
		System.out.println("p1="+p1+"\t"+"p2="+p2+"\tp1Pay\tp2Pay\tp1Cum\tp2Cum\tp1Ave\tp2Ave");
		for(int i=0;i<10;i++) {
			System.out.print(pop[p1].getChoice()+"\t"+pop[p2].getChoice());
			game(p1,p2);
			System.out.print("\t"+pop[p1].getPayoff()+"\t"+pop[p2].getPayoff());
			System.out.print("\t"+pop[p1].getCumPayoff()+"\t"+pop[p2].getCumPayoff());
			
			System.out.println();
		}
	}

	//
	static void game(int p1, int p2) { // 個体番号 p1,p2 でゲームを行う。
		// それぞれのプレイヤーの「手」
		// 生成の時に memory ができて、そのときに adr も choice も決まっている。
		// ゲームで記憶が更新されるたびに adr も choice も更新されている。
		char select_p1 = pop[p1].getChoice();
		char select_p2 = pop[p2].getChoice();

		// C は 0, Dは 1 いずれchar である。
		if (select_p1 == '0' && select_p2 == '0') {
			pop[p1].setPayoff(3.0);
			pop[p2].setPayoff(3.0);
		}
		if (select_p1 == '0' && select_p2 == '1') {
			pop[p1].setPayoff(0.0);
			pop[p2].setPayoff(5.0);
		}
		if (select_p1 == '1' && select_p2 == '0') {
			pop[p1].setPayoff(5.0);
			pop[p2].setPayoff(0.0);
		}
		if (select_p1 == '1' && select_p2 == '1') {
			pop[p1].setPayoff(1.0);
			pop[p2].setPayoff(1.0);
		}
		pop[p1].reMem(select_p2);
		pop[p2].reMem(select_p1);
		// ゲームカウントはsetPayoff が呼ばれた際にその中でカウントが増える
	}// end of game()

	private static void printRec(char[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i]);
		}
	}

	private static void printRec(double[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i] + "\t");
		}
	}

	private static void printRec(int[] in) {
		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i] + "\t");
		}
	}
} // end of class
