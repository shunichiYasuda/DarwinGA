package darwinSimulation;

public class TestPop_Nov25 {

	static CPopulation pop;
	static final int POPSIZE = 20;

	public static void main(String[] args) {
		//
		pop = new CPopulation(POPSIZE);
		// pop のメンバー
		for (CIndividual m : pop.member) {
			printRec(m.getMemory());
			System.out.print("\t");
			System.out.println();
			printRec(m.getChrom());
			System.out.print("\t" + m.getAdr() + "\t" + m.getChoice());
			System.out.println();
		}
	}//end of main()
	
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

}
